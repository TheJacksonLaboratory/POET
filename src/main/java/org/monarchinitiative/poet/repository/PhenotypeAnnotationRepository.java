package org.monarchinitiative.poet.repository;

import org.monarchinitiative.poet.model.entities.*;
import org.monarchinitiative.poet.model.enumeration.AnnotationStatus;
import org.monarchinitiative.poet.model.responses.ReviewCount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PhenotypeAnnotationRepository extends CrudRepository<PhenotypeAnnotation, Long> {
    List<PhenotypeAnnotation> findAllByAnnotationSourceDiseaseAndStatusNotIn(Disease disease, List<AnnotationStatus> statuses);
    List<PhenotypeAnnotation> findAllByOwnerAndStatus(User owner, AnnotationStatus status);
    List<PhenotypeAnnotation> findAllByStatus(AnnotationStatus status);
    List<PhenotypeAnnotation> findAllByStatusIn(List<AnnotationStatus> statuses);
    PhenotypeAnnotation findDistinctById(long id);
    boolean existsByAnnotationSourceAndHpoIdAndSexAndEvidenceAndOnsetAndFrequencyAndModifierAndQualifierAndStatusNotAndIdNot(AnnotationSource source, String hpoId, String sex, String evidence, String onset, String frequency, String modifier, String qualifier, AnnotationStatus status, long id);
    boolean existsByAnnotationSourceAndHpoIdAndSexAndEvidenceAndOnsetAndFrequencyAndModifierAndQualifierAndStatusNotIn(AnnotationSource source, String hpoId, String sex, String evidence, String onset, String frequency, String modifier, String qualifier, List<AnnotationStatus> statuses);
    boolean existsByAnnotationSourceAndHpoIdAndSexAndEvidenceAndOnsetAndFrequencyAndModifierAndQualifierAndStatus(AnnotationSource source, String hpoId, String sex, String evidence, String onset, String frequency, String modifier, String qualifier, AnnotationStatus status);
    int countAllByAnnotationSourceDiseaseAndStatusNot(Disease disease, AnnotationStatus status);
    int countAllByStatusNot(AnnotationStatus status);
    @Query("select new org.monarchinitiative.poet.model.responses.ReviewCount(a.annotationSource.disease, count(a), 'phenotype') from PhenotypeAnnotation a where a.status = ?1 group by a.annotationSource.disease")
    List<ReviewCount> countAllByStatus(AnnotationStatus status);
    @Query("select new org.monarchinitiative.poet.model.responses.ReviewCount(a.annotationSource.disease, count(a), 'phenotype') from PhenotypeAnnotation a where a.status = ?1 and a.owner = ?2 group by a.annotationSource.disease")
    List<ReviewCount> countAllByStatusAndUser(AnnotationStatus status, User user);

}
