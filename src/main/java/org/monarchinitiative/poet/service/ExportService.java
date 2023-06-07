package org.monarchinitiative.poet.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.monarchinitiative.poet.exceptions.ExportException;
import org.monarchinitiative.poet.model.entities.PhenotypeAnnotation;
import org.monarchinitiative.poet.model.entities.TreatmentAnnotation;
import org.monarchinitiative.poet.model.entities.Version;
import org.monarchinitiative.poet.model.enumeration.AnnotationStatus;
import org.monarchinitiative.poet.repository.PhenotypeAnnotationRepository;
import org.monarchinitiative.poet.repository.TreatmentAnnotationRepository;
import org.monarchinitiative.poet.repository.VersionRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class ExportService {

    private final AnnotationService annotationService;
    private final VersionRepository versionRepository;
    private final PhenotypeAnnotationRepository phenotypeAnnotationRepository;
    private final TreatmentAnnotationRepository treatmentAnnotationRepository;

    public ExportService(AnnotationService annotationService, VersionRepository versionRepository,
                         PhenotypeAnnotationRepository phenotypeAnnotationRepository,
                         TreatmentAnnotationRepository treatmentAnnotationRepository) {
        this.annotationService = annotationService;
        this.versionRepository = versionRepository;
        this.phenotypeAnnotationRepository = phenotypeAnnotationRepository;
        this.treatmentAnnotationRepository = treatmentAnnotationRepository;
    }

    public void exportHPOAnnotations(PrintWriter writer, CSVFormat format, boolean unstable){
        List<PhenotypeAnnotation> phenotypeAnnotationList;
        if(unstable){
            phenotypeAnnotationList = annotationService.getOfficialAndApprovedPhenotypes();
        } else {
            phenotypeAnnotationList = annotationService.getOfficialPhenotypes();
        }
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
                        annotation.getOwner().getExportName()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ExportException("HPO");
        }
    }

    public void exportMAXOAnnotations(PrintWriter writer, CSVFormat format, boolean unstable){
        List<TreatmentAnnotation> treatmentAnnotationList;
        if(unstable){
            treatmentAnnotationList = annotationService.getOfficialAndApprovedTreatments();
        } else {
            treatmentAnnotationList = annotationService.getOfficialTreatments();
        }
        try(CSVPrinter csvPrinter = format.print(writer)){
            csvPrinter.printRecord("disease_id", "disease_name", "source_id", "maxo_id", "maxo_name", "hpo_id",
                    "relation", "evidence", "extension_id", "extension_name", "comment", "other", "author", "last_updated", "created");
            for (TreatmentAnnotation annotation : treatmentAnnotationList) {
                String reference;
                if(annotation.getAnnotationSource().isDiseaseDatabaseSource()){
                    reference = annotation.getAnnotationSource().getDisease().getEquivalentId();
                } else {
                    reference = annotation.getAnnotationSource().getPublication().getPublicationId();
                }

                annotationService.getLastUpdatedForAnnotation(annotation);
                annotationService.getCreatedDateForAnnotation(annotation);
                csvPrinter.printRecord(annotation.getAnnotationSource().getDisease().getExportDiseaseId(),
                        annotation.getAnnotationSource().getDisease().getDiseaseName(), reference,
                        annotation.getMaxoId(), annotation.getMaxoName(), annotation.getHpoId(), annotation.getRelation(),
                        annotation.getEvidence(), annotation.getExtensionId(), annotation.getExtensionLabel(),
                        annotation.getComment(), "", annotation.getOwner().getExportName(), annotation.getExportLastUpdatedDate(), annotation.getCreatedDate()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ExportException("MAXO");
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void releaseAnnotations(){
        // Create a new release version
        Version version = createNewReleaseVersion();
        List<PhenotypeAnnotation> phenotypeAnnotations = phenotypeAnnotationRepository.findAllByStatus(AnnotationStatus.ACCEPTED);
        List<TreatmentAnnotation> treatmentAnnotations = treatmentAnnotationRepository.findAllByStatus(AnnotationStatus.ACCEPTED);
        // For all accepted phenotype annotations we want to check to see if an official one exists already as official
        for(PhenotypeAnnotation annotation: phenotypeAnnotations){
            // If not, change annotation to official status, tag with release
            if(!annotationService.phenotypeAnnotationExists(annotation, AnnotationStatus.OFFICIAL)){
                annotation.setStatus(AnnotationStatus.OFFICIAL);
                annotation.setVersion(version);
                phenotypeAnnotationRepository.save(annotation);
                // TODO: Add useractivity update
            } else {
                // we have data integrity issues
                // throw poet error about data integrity with this annotation information
            }
        }

        for(TreatmentAnnotation annotation: treatmentAnnotations) {
            if (!annotationService.treatmentAnnotationExistsByStatus(annotation, AnnotationStatus.OFFICIAL)) {
                annotation.setStatus(AnnotationStatus.OFFICIAL);
                annotation.setVersion(version);
                treatmentAnnotationRepository.save(annotation);
                // TODO: Add useractivity update
            } else {
                // we have data integrity issues
                // throw poet error about data integrity with this annotation information
            }
        }
    }

    Version createNewReleaseVersion(){
        return versionRepository.save(new Version(LocalDateTime.now()));
    }
}
