package org.monarchinitiative.poet.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.monarchinitiative.poet.model.entities.PhenotypeAnnotation;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Service
public class ExportService {

    private final AnnotationService annotationService;
    public ExportService(AnnotationService annotationService) {
        this.annotationService = annotationService;
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
}
