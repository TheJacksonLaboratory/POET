package org.monarchinitiative.poet.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.monarchinitiative.poet.model.entities.PhenotypeAnnotation;
import org.monarchinitiative.poet.model.entities.Version;
import org.monarchinitiative.poet.model.enumeration.AnnotationStatus;
import org.monarchinitiative.poet.repository.PhenotypeAnnotationRepository;
import org.monarchinitiative.poet.repository.VersionRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExportService {

    private final AnnotationService annotationService;
    private final VersionRepository versionRepository;
    private final PhenotypeAnnotationRepository phenotypeAnnotationRepository;

    public ExportService(AnnotationService annotationService, VersionRepository versionRepository, PhenotypeAnnotationRepository phenotypeAnnotationRepository) {
        this.annotationService = annotationService;
        this.versionRepository = versionRepository;
        this.phenotypeAnnotationRepository = phenotypeAnnotationRepository;
    }

    public void exportHPOAnnotations(PrintWriter writer, CSVFormat format){
        List<PhenotypeAnnotation> phenotypeAnnotationList = annotationService.getOfficialPhenotypes();
        try(CSVPrinter csvPrinter = new CSVPrinter(writer, format)){
            for (PhenotypeAnnotation annotation : phenotypeAnnotationList) {
                String reference;
                if(annotation.getAnnotationSource().isDiseaseDatabaseSource()){
                     reference = annotation.getAnnotationSource().getDisease().getDiseaseId();
                } else {
                     reference = annotation.getAnnotationSource().getPublication().getPublicationId();
                }
                csvPrinter.printRecord(annotation.getAnnotationSource().getDisease().getDiseaseId(),
                        annotation.getAnnotationSource().getDisease().getDiseaseName(),
                        annotation.getQualifier(), annotation.getHpoId(), reference, annotation.getEvidence(),
                        annotation.getOnset(), annotation.getFrequency(), annotation.getSex(), annotation.getModifier(),
                        annotation.getOwner().getNickname()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void releaseAnnotations(){
        // Create a new release version
        Version version = createNewReleaseVersion();
        List<PhenotypeAnnotation> annotations = phenotypeAnnotationRepository.findAllByStatus(AnnotationStatus.ACCEPTED);
        // For all accepted phenotype annotations we want to check to see if an official one exists already as official
        for(PhenotypeAnnotation annotation: annotations){
            annotation.setStatus(AnnotationStatus.OFFICIAL);
            // If not, change annotation to official status, tag with release
            if(!annotationService.phenotypeAnnotationExists(annotation, null)){
                annotation.setVersion(version);
                phenotypeAnnotationRepository.save(annotation);
            }
        }
    }

    private Version createNewReleaseVersion(){
        return versionRepository.save(new Version(LocalDateTime.now()));
    }
}
