package org.monarchinitiative.poet.repository;

import org.monarchinitiative.poet.model.entities.AnnotationSource;
import org.monarchinitiative.poet.model.entities.Disease;
import org.monarchinitiative.poet.model.entities.TreatmentAnnotation;
import org.monarchinitiative.poet.model.enumeration.AnnotationStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface TreatmentAnnotationRepository extends CrudRepository<TreatmentAnnotation, Long> {

    List<TreatmentAnnotation> findDistinctByAnnotationSourceAndStatusNot(AnnotationSource source, AnnotationStatus status);
    List<TreatmentAnnotation> findAllByAnnotationSourceDiseaseAndStatusNot(Disease disease, AnnotationStatus status);
   /* @Query("SELECT a, u.datetime FROM ANNOTATION a INNER JOIN ANNOTATION_SOURCE s  ON s.id = a.annotation_source_id INNER " +
            "JOIN DISEASE d ON d.id = s.disease_id AND d.disease_id = :diseaseId INNER JOIN  USER_ACTIVITY u " +
            "ON a.id = u.annotation_id  AND u.curation_action = 0 OR u.curation_action = 1 WHERE a.status != 'RETIRED'")
    List<TreatmentAnnotation> findAllByDiseaseWithStatusNot(@Param("diseaseId") String diseaseId);*/
    TreatmentAnnotation findDistinctById(long id);
    int countAllByAnnotationSourceDiseaseAndStatusNot(Disease disease, AnnotationStatus status);
    int countAllByStatusNot(AnnotationStatus status);
    boolean existsByAnnotationSourceAndAnnotationTypeAndMaxoIdAndHpoIdAndExtensionIdAndEvidenceTypeAndRelationAndStatusNot(
            AnnotationSource annotationSource, String annotationType, String maxoId, String hpoId,
            String extensionId, String evidenceType, String relation, AnnotationStatus status);
}
