package org.monarchinitiative.poet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.WordUtils;
import org.monarchinitiative.poet.exceptions.DuplicateAnnotationException;
import org.monarchinitiative.poet.model.utility.HpoAnnotationLine;
import org.monarchinitiative.phenol.base.PhenolException;
import org.monarchinitiative.phenol.io.OntologyLoader;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.monarchinitiative.phenol.ontology.data.Term;
import org.monarchinitiative.phenol.ontology.data.TermId;
import org.monarchinitiative.poet.model.entities.*;
import org.monarchinitiative.poet.model.requests.PhenotypeRequest;
import org.monarchinitiative.poet.model.requests.PublicationRequest;
import org.monarchinitiative.poet.utility.ContainsPubmed;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Service
public class InitDatabaseService {

    private final EntityService entityService;
    private final AnnotationService annotationService;
    private final UserService userService;

    InitDatabaseService(AnnotationService annotationService,
                 EntityService entityService, UserService userService){
        this.entityService = entityService;
        this.annotationService = annotationService;
        this.userService = userService;
    }

    private boolean shouldLoad = true;

    @Value("classpath:phenotype.hpoa")
    private Resource hpoaResource;

    @Value("classpath:hp.obo")
    private Resource oboResource;

    //Run this method when application started
    @EventListener(ApplicationReadyEvent.class)
    public void loadData() throws IOException, PhenolException {
        // if property has load data flag
        if(shouldLoad) {
            System.out.println("Starting Loading Database!!");
            User user = userService.getHumanPhenotypeOntologyUser();
            Ontology ontology = OntologyLoader.loadOntology(new File(oboResource.getURI()));
            Map<TermId, Term> ontologyMap = ontology.getTermMap();
            Map<TermId, List<HpoAnnotationLine>> diseaseAnnotationMap = new HashMap<>();
            try (BufferedReader br = new BufferedReader(new FileReader(new File(hpoaResource.getURI())))) {
                String line = br.readLine();
                while (line.startsWith("#")) {
                    line = br.readLine();
                } // this skips the comments (including the definition of the header)
                while ((line = br.readLine()) != null) {
                    HpoAnnotationLine annotationLine = HpoAnnotationLine.constructFromString(line);
                    if (annotationLine.hasValidNumberOfFields()) {
                        TermId diseaseId = annotationLine.getDiseaseTermId();
                        diseaseAnnotationMap.putIfAbsent(diseaseId, new ArrayList<>());
                        diseaseAnnotationMap.get(diseaseId).add(annotationLine);
                    }
                }
            } catch (IOException | PhenolException e) {
                throw new PhenolException(String.format("Could not read annotation file: %s", e.getMessage()));
            }
            // For each entry we load?
            diseaseAnnotationMap.entrySet().forEach(entry -> {
                Optional<String> diseaseNameOptional = entry.getValue().stream().map(
                        line -> line.getDbObjectName().replaceAll("\\d{6}|^#\\d{6}|^\\d{6}|^\\+\\d{6}", "").trim().split(";")[0]).min(Comparator.comparingInt(String::length));
                final String diseaseId = entry.getKey().toString();
                final Disease disease;
                if(diseaseNameOptional.isPresent()){
                    if(diseaseId.contains("OMIM")){
                        final String diseaseName = WordUtils.capitalizeFully(diseaseNameOptional.get());
                        disease = new Disease(diseaseId.toString(), diseaseName);
                        entityService.saveNewDisease(disease);
                    } else {
                        System.out.println("IGNORED: " + entry.getKey().toString());
                        return;
                    }

                } else {
                    return;
                }

                entry.getValue().forEach(phenotype -> {
                    Term term = ontologyMap.get(phenotype.getPhenotypeId());
                    ContainsPubmed<String> cpm = new ContainsPubmed<>();
                    List<String> publications = phenotype.getPublications();
                    AnnotationSource annotationSource = new AnnotationSource();
                    // Use the first Publication Identifier for now until we scale out the model
                    Optional<String> publicationCitation = publications.stream().filter(citation -> citation.contains("PMID")).findFirst();
                    Optional<String> diseaseCitation = publications.stream().filter(citation -> citation.contains("OMIM")).findFirst();
                    Optional<String> isbnCitation = publications.stream().filter(citation -> citation.contains("ISBN")).findFirst();
                    if(publicationCitation.isPresent()){
                        // Phenotype has PMID create source if not exist
                        Publication publication = new Publication(publicationCitation.get(), "", "", "");
                        annotationSource = entityService.getAnnotationSource(publication.getPublicationId(), disease.getDiseaseId());
                        if(annotationSource == null){
                            annotationSource = entityService.createAnnotationSource(new PublicationRequest(publication, disease));
                        }
                    } else if(diseaseCitation.isPresent()){
                        // Create the disease identifier source.
                        annotationSource = entityService.createOrGetDiseaseDatabaseSource(disease);
                    } else if(isbnCitation.isPresent() || publications.size() == 1){
                        final String publicationId = isbnCitation.orElseGet(() -> publications.get(0));
                        Publication publication = new Publication(publicationId, "", "", "");
                        annotationSource = entityService.getAnnotationSource(publication.getPublicationId(), disease.getDiseaseId());
                        if(annotationSource == null){
                            annotationSource = entityService.createAnnotationSource(new PublicationRequest(publication, disease));
                        }
                    }

                    String publicationId = null;
                    String publicationName = null;

                    if(annotationSource.getPublication() != null){
                        publicationId = annotationSource.getPublication().getPublicationId();
                        publicationName = annotationSource.getPublication().getPublicationName();
                    }


                    PhenotypeRequest request = new PhenotypeRequest(null, term.getName(), term.getId().toString(),
                            phenotype.getEvidence(), "", phenotype.getOnsetId(),
                            publicationId,
                            publicationName,
                            annotationSource.getDisease().getDiseaseId(),
                            annotationSource.getDisease().getDiseaseName(),
                            phenotype.getModifierList(),
                            phenotype. getFrequency(), phenotype.isNOT() ? "NOT": "", phenotype.getSex(), null);
                    try{
                        annotationService.createPhenotypeAnnotation(request, user);
                    } catch (DuplicateAnnotationException ex){
                        System.out.println(ex.getMessage());
                    }
                });
            });
            System.out.println("Finished Loading Database!!");
        }
    }

   /** private Map getMostRecentDiseaseName(String diseaseId) throws IOException {
        final String urlString = "https://api.monarchinitiative.org/api/search/entity/autocomplete/" + diseaseId +"?" +
                "category=disease&include_eqs=false&rows=1&start=0&exclude_groups=false&minimal_tokenizer=false";
        URL url = new URL(urlString);
        // Convert to a JSON object to print data
        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(url, Map.class);
        List<LinkedHashMap> docs = (List<LinkedHashMap>) map.get("docs");
        if(docs.size() > 0){
            return docs.get(0).get("")
        }

        return map;
    }**/
}

