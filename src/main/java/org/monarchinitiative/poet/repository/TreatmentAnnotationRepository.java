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
    TreatmentAnnotation findDistinctById(long id);
    int countAllByAnnotationSourceDiseaseAndStatusNot(Disease disease, AnnotationStatus status);
    int countAllByStatusNot(AnnotationStatus status);
    boolean existsByAnnotationSourceAndAnnotationTypeAndMaxoIdAndHpoIdAndExtensionIdAndEvidenceAndRelationAndStatusNot(
            AnnotationSource annotationSource, String annotationType, String maxoId, String hpoId,
            String extensionId, String evidence, String relation, AnnotationStatus status);
    List<TreatmentAnnotation> findAllByStatus(AnnotationStatus status);
}
