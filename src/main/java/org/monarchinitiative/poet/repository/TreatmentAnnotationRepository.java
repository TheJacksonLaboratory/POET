package org.monarchinitiative.poet.repository;

import org.monarchinitiative.poet.model.entities.AnnotationSource;
import org.monarchinitiative.poet.model.entities.Disease;
import org.monarchinitiative.poet.model.entities.TreatmentAnnotation;
import org.monarchinitiative.poet.model.entities.User;
import org.monarchinitiative.poet.model.enumeration.AnnotationStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.monarchinitiative.poet.model.responses.ReviewCount;

import java.util.List;

public interface TreatmentAnnotationRepository extends CrudRepository<TreatmentAnnotation, Long> {

    List<TreatmentAnnotation> findAllByAnnotationSourceDiseaseAndStatusNotAndStatusNot(Disease disease, AnnotationStatus status, AnnotationStatus status_two);
    List<TreatmentAnnotation> findAllByAnnotationSourceDiseaseAndStatusNotAndStatus(Disease disease, AnnotationStatus statusNot, AnnotationStatus status);
    TreatmentAnnotation findDistinctById(long id);
    int countAllByAnnotationSourceDiseaseAndStatusNot(Disease disease, AnnotationStatus status);
    int countAllByStatusNot(AnnotationStatus status);
    boolean existsByAnnotationSourceAndMaxoIdAndHpoIdAndExtensionIdAndEvidenceAndRelationAndStatusNot(
            AnnotationSource annotationSource, String maxoId, String hpoId,
            String extensionId, String evidence, String relation, AnnotationStatus status);
    boolean existsByAnnotationSourceAndMaxoIdAndHpoIdAndExtensionIdAndEvidenceAndRelationAndStatus(
            AnnotationSource annotationSource, String maxoId, String hpoId,
            String extensionId, String evidence, String relation, AnnotationStatus status);
    boolean existsByAnnotationSourceAndMaxoIdAndHpoIdAndExtensionIdAndEvidenceAndRelationAndStatusNotAndStatusNot(AnnotationSource annotationSource, String maxoId, String hpoId, String extensionId, String evidence, String relation, AnnotationStatus status, AnnotationStatus status2);
    List<TreatmentAnnotation> findAllByStatus(AnnotationStatus status);
    @Query("select new org.monarchinitiative.poet.model.responses.ReviewCount(a.annotationSource.disease, count(a), 'treatment') from TreatmentAnnotation a where a.status = ?1 group by a.annotationSource.disease")
    List<ReviewCount> getAllByStatus(AnnotationStatus status);
    @Query("select new org.monarchinitiative.poet.model.responses.ReviewCount(a.annotationSource.disease, count(a), 'treatment') from TreatmentAnnotation a where a.status = ?1 and a.owner = ?2 group by a.annotationSource.disease")
    List<ReviewCount> getAllByStatusAndUser(AnnotationStatus status, User user);

    List<TreatmentAnnotation> findAllByOwnerAndStatusNot(User user, AnnotationStatus status);
    List<TreatmentAnnotation> findAllByOwnerAndStatus(User user, AnnotationStatus status);
}
