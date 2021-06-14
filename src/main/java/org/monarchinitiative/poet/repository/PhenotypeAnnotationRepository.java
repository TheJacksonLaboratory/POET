package org.monarchinitiative.poet.repository;

import org.monarchinitiative.poet.model.entities.AnnotationSource;
import org.monarchinitiative.poet.model.entities.Disease;
import org.monarchinitiative.poet.model.entities.PhenotypeAnnotation;
import org.monarchinitiative.poet.model.enumeration.AnnotationStatus;
import org.monarchinitiative.poet.model.responses.ReviewCount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PhenotypeAnnotationRepository extends CrudRepository<PhenotypeAnnotation, Long> {
    List<PhenotypeAnnotation> findAllByAnnotationSourceDiseaseAndStatusNot(Disease disease, AnnotationStatus status);
    PhenotypeAnnotation findDistinctById(long id);
    boolean existsByAnnotationSourceAndHpoIdAndSexAndEvidenceAndOnsetAndModifier(AnnotationSource source, String hpoId, String sex, String evidence, String onset, String modifier);
    boolean existsByAnnotationSourceAndHpoIdAndSexAndEvidenceAndOnsetAndModifierAndStatusNot(AnnotationSource source, String hpoId, String sex, String evidence, String onset, String modifier, AnnotationStatus status);
    int countAllByAnnotationSourceDiseaseAndStatusNot(Disease disease, AnnotationStatus status);
    int countAllByStatusNot(AnnotationStatus status);
    List<PhenotypeAnnotation> findAllByStatus(AnnotationStatus status);
    @Query("select new org.monarchinitiative.poet.model.responses.ReviewCount(a.annotationSource.disease, count(a), 'phenotype') from PhenotypeAnnotation a where a.status = ?1 group by a.annotationSource.disease")
    List<ReviewCount> getAllByStatus(AnnotationStatus status);

}
