package org.monarchinitiative.poet.service;

import org.monarchinitiative.poet.exceptions.AnnotationSourceException;
import org.monarchinitiative.poet.exceptions.DuplicateAnnotationException;
import org.monarchinitiative.poet.model.enumeration.AnnotationStatus;
import org.monarchinitiative.poet.model.requests.PhenotypeRequest;
import org.monarchinitiative.poet.model.requests.TreatmentRequest;
import org.monarchinitiative.poet.model.entities.*;
import org.monarchinitiative.poet.model.enumeration.CurationAction;
import org.monarchinitiative.poet.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * A spring service component created to provide business logic and functionality to manage annotations
 * for poet entities.
 *
 * @author Michael Gargano
 * @since 0.5.0
 */
@Service
public class AnnotationService {

    private PublicationRepository publicationRepository;
    private DiseaseRepository diseaseRepository;
    private AnnotationSourceRepository annotationSourceRepository;
    private TreatmentAnnotationRepository treatmentAnnotationRepository;
    private UserRepository userRepository;
    private UserActivityRepository userActivityRespository;
    private PhenotypeAnnotationRepository phenotypeAnnotationRepository;

    public AnnotationService(PublicationRepository publicationRepository,
                             DiseaseRepository diseaseRepository, AnnotationSourceRepository annotationSourceRepository,
                             TreatmentAnnotationRepository treatmentAnnotationRepository, UserRepository userRepository,
                             UserActivityRepository userActivityRepository,
                             PhenotypeAnnotationRepository phenotypeAnnotationRepository) {
        this.publicationRepository = publicationRepository;
        this.diseaseRepository = diseaseRepository;
        this.annotationSourceRepository = annotationSourceRepository;
        this.treatmentAnnotationRepository = treatmentAnnotationRepository;
        this.userRepository = userRepository;
        this.userActivityRespository = userActivityRepository;
        this.phenotypeAnnotationRepository = phenotypeAnnotationRepository;
    }

    /**
     * A function to get phenotype annotations from the database by just disease.
     *
     * @param diseaseId a OMIM disease id
     * @param sort a string composing of two parts both direction and field. TODO: Implement functionality
     *
     * @return a collection of phenotype annotations or an empty list.
     * @since 0.5.0
     */
    public List<PhenotypeAnnotation> getPhenotypeAnnotations(String diseaseId, String sort) {
            Disease disease = this.diseaseRepository.findDiseaseByDiseaseId(diseaseId);
            if(disease != null) {
                List<PhenotypeAnnotation> annotations = this.phenotypeAnnotationRepository.findAllByAnnotationSourceDiseaseAndStatusNot(disease, AnnotationStatus.RETIRED);
                if(annotations.size() > 0){
                    return annotations.stream().peek(annotation -> {
                        UserActivity activity = userActivityRespository.getMostRecentDateForAnnotationActivity(annotation.getId());
                        annotation.setLastUpdatedDate(activity.getLocalDateTime());
                        annotation.setOwner(activity.getUser().getNickname());
                    }).sorted(Comparator.comparing(PhenotypeAnnotation::getLastUpdatedDate).reversed()).collect(Collectors.toList());
                } else {
                    return Collections.emptyList();
                }
        }
        return null;
    }

    /**
     * Create a phenotype annotation with a pending review status.
     * enforcing business rules with the status of the annotation
     *
     * @param phenotypeRequest a phenotype request body
     * @param authentication a spring authentication object
     */
    @Transactional()
    public void createPhenotypeAnnotation(PhenotypeRequest phenotypeRequest, Authentication authentication) throws DuplicateAnnotationException {
        final AnnotationSource annotationSource = getAnnotationSource(phenotypeRequest.getPublicationId(), phenotypeRequest.getDiseaseId());
        if(annotationSource != null){
            final PhenotypeAnnotation annotation = new PhenotypeAnnotation(phenotypeRequest, annotationSource,
                    AnnotationStatus.UNDER_REVIEW);
            if(phenotypeAnnotationRepository.existsByAnnotationSourceAndHpoIdAndSexAndEvidenceAndOnsetAndModifier(
                    annotation.getAnnotationSource(), annotation.getHpoId(), annotation.getSex(),
                    annotation.getEvidence(), annotation.getOnset(), annotation.getModifier())){
                throw new DuplicateAnnotationException("phenotype", annotation.getAnnotationSource().getDisease().getDiseaseName());
            }
            phenotypeAnnotationRepository.save(annotation);
            updateUserActivity(authentication, CurationAction.CREATE, annotation, null);
        } else {
            throw new AnnotationSourceException(phenotypeRequest.getPublicationId(), phenotypeRequest.getDiseaseId());
        }
    }

    /**
     * Update a phenotype annotation enforcing business rules with the status of the annotation
     *
     * @param phenotypeRequest a phenotype request body
     *
     */
    public void updatePhenotypeAnnotation(PhenotypeRequest phenotypeRequest, Authentication authentication) throws DuplicateAnnotationException {
        PhenotypeAnnotation oldAnnotation = phenotypeAnnotationRepository.findDistinctById(phenotypeRequest.getId());
        final PhenotypeAnnotation annotation = new PhenotypeAnnotation(phenotypeRequest, oldAnnotation.getAnnotationSource(),
                oldAnnotation.getStatus());

        // See if we already have an annotation like this.
        if(phenotypeAnnotationRepository.existsByAnnotationSourceAndHpoIdAndSexAndEvidenceAndOnsetAndModifierAndStatusNot(
                annotation.getAnnotationSource(), annotation.getHpoId(), annotation.getSex(),
                annotation.getEvidence(), annotation.getOnset(), annotation.getModifier(), AnnotationStatus.RETIRED)){
            throw new DuplicateAnnotationException("treatment", annotation.getAnnotationSource().getDisease().getDiseaseName());
        }
        phenotypeAnnotationRepository.save(oldAnnotation);
        oldAnnotation.setStatus(AnnotationStatus.RETIRED);
        phenotypeAnnotationRepository.save(annotation);
        updateUserActivity(authentication, CurationAction.UPDATE, annotation, oldAnnotation);
    }

    /**
     * Delete a phenotype annotation with a pending review status.
     * enforcing business rules with the status of the annotation
     *
     * @param id a phenotype annotation id
     *
     * @return a boolean whether the annotation was created or not.
     */
    public boolean deletePhenotypeAnnotation(Long id, Authentication authentication) {
        if(id != null){
            PhenotypeAnnotation annotation = phenotypeAnnotationRepository.findDistinctById(id);
            if(annotation != null){
                annotation.setStatus(AnnotationStatus.RETIRED);
                updateUserActivity(authentication, CurationAction.DELETE, annotation, null);
                return true;
            }
        }
        return false;
    }

    /**
     * A function to get maxo annotations from the database by either both publication and  disease or just disease.
     *
     * @param diseaseId a OMIM disease id
     * @param publicationId a PubMed id
     * @param sort a string composing of two parts both direction and field. TODO: Implement functionality
     *
     * @return a collection of maxo annotations or an empty list.
     * @since 0.5.0
     */
    public List<TreatmentAnnotation> getTreatmentAnnotations(String diseaseId, String publicationId, String sort) {
            // If publication get source, then annotations for that source
            // Otherwise get all annotationSource
            if(publicationId != null){
                final AnnotationSource annotationSource = getAnnotationSource(publicationId, diseaseId);
                if(annotationSource != null){
                    List<TreatmentAnnotation> annotations = this.treatmentAnnotationRepository.findDistinctByAnnotationSourceAndStatusNot(annotationSource, AnnotationStatus.RETIRED);
                    if(annotations.size() > 0){
                        return annotations.stream().peek(annotation -> {
                            UserActivity activity = userActivityRespository.getMostRecentDateForAnnotationActivity(annotation.getId());
                            annotation.setLastUpdatedDate(activity.getLocalDateTime());
                            annotation.setOwner(activity.getUser().getNickname());
                        }).sorted(Comparator.comparing(TreatmentAnnotation::getLastUpdatedDate).reversed()).collect(Collectors.toList());
                    } else {
                        return Collections.emptyList();
                    }
                }
            } else {
                Disease disease = this.diseaseRepository.findDiseaseByDiseaseId(diseaseId);
                if(disease != null) {
                    List<TreatmentAnnotation> annotations = this.treatmentAnnotationRepository.findAllByAnnotationSourceDiseaseAndStatusNot(disease, AnnotationStatus.RETIRED);
                    if(annotations.size() > 0){
                        return annotations.stream().peek(annotation -> {
                            UserActivity activity = userActivityRespository.getMostRecentDateForAnnotationActivity(annotation.getId());
                            annotation.setLastUpdatedDate(activity.getLocalDateTime());
                            annotation.setOwner(activity.getUser().getNickname());
                        }).sorted(Comparator.comparing(TreatmentAnnotation::getLastUpdatedDate).reversed()).collect(Collectors.toList());
                    } else {
                        return Collections.emptyList();
                    }
                } else {
                    return null;
                }
            }
            return null;
    }

    /**
     * Create a MaXo annotation with a pending review status.
     * enforcing business rules with the status of the annotation
     *
     * @param treatmentRequest a maxo request body
     *
     * @return a boolean whether the annotation was created or not.
     */
    @Transactional()
    public void createTreatmentAnnotation(TreatmentRequest treatmentRequest, Authentication authentication) throws DuplicateAnnotationException {
        // We have a valid publication and a valid disease, do we have an annotation source for them?
        final AnnotationSource annotationSource = getAnnotationSource(treatmentRequest.getPublicationId(), treatmentRequest.getDiseaseId());
        if(annotationSource != null){
            final TreatmentAnnotation annotation = new TreatmentAnnotation(treatmentRequest, annotationSource);
            // Check if we have a duplicate annotation, if so throw an error
            // See if we already have an annotation like this.
            if(treatmentAnnotationRepository.existsByAnnotationSourceAndAnnotationTypeAndMaxoIdAndHpoIdAndExtensionIdAndEvidenceAndRelationAndStatusNot(
                    annotation.getAnnotationSource(), "treatment", annotation.getMaxoId(), annotation.getHpoId(), annotation.getExtensionId(), annotation.getEvidence(), annotation.getRelation(), AnnotationStatus.RETIRED)){
                throw new DuplicateAnnotationException("treatment", annotation.getAnnotationSource().getDisease().getDiseaseName());
            }
            treatmentAnnotationRepository.save(annotation);
            updateUserActivity(authentication, CurationAction.CREATE, annotation, null);
        } else {
            throw new AnnotationSourceException(treatmentRequest.getPublicationId(), treatmentRequest.getDiseaseId());
        }
    }

    /**
     * Update a MaXo annotation enforcing business rules with the status of the annotation
     *
     * @param treatmentRequest a maxo request body
     *
     * @return a boolean whether the annotation was created or not.
     */
    public void updateTreatmentAnnotation(TreatmentRequest treatmentRequest, Authentication authentication) throws DuplicateAnnotationException {
            // Retired the old annotation
            TreatmentAnnotation oldAnnotation = treatmentAnnotationRepository.findDistinctById(treatmentRequest.getId());
            // Create the new one with updated values
            final TreatmentAnnotation annotation = new TreatmentAnnotation(oldAnnotation.getAnnotationSource(),
                    oldAnnotation.getStatus(), treatmentRequest.getMaxoId(), treatmentRequest.getMaxoName(),
                    treatmentRequest.getHpoName(), treatmentRequest.getHpoId(), treatmentRequest.getEvidence(),
                    treatmentRequest.getComment(), treatmentRequest.getRelation(), treatmentRequest.getExtensionId(), treatmentRequest.getExtensionLabel());

            // See if we already have an annotation like this.
            if(treatmentAnnotationRepository.existsByAnnotationSourceAndAnnotationTypeAndMaxoIdAndHpoIdAndExtensionIdAndEvidenceAndRelationAndStatusNot(
                    annotation.getAnnotationSource(), "treatment", annotation.getMaxoId(), annotation.getHpoId(), annotation.getExtensionId(), annotation.getEvidence(), annotation.getRelation(), AnnotationStatus.RETIRED)){
                throw new DuplicateAnnotationException("treatment", annotation.getAnnotationSource().getDisease().getDiseaseName());
            }
            treatmentAnnotationRepository.save(oldAnnotation);
            oldAnnotation.setStatus(AnnotationStatus.RETIRED);
            treatmentAnnotationRepository.save(annotation);
            updateUserActivity(authentication, CurationAction.UPDATE, annotation, oldAnnotation);
    }

    /**
     * Delete a MaXo annotation with a pending review status.
     * enforcing business rules with the status of the annotation
     *
     * @param id a maxo annotation id
     *
     * @return a boolean whether the annotation was created or not.
     */
    public boolean deleteTreatmentAnnotation(Long id, Authentication authentication) {
        if(id != null){
            TreatmentAnnotation annotation = treatmentAnnotationRepository.findDistinctById(id);
            if(annotation != null){
                annotation.setStatus(AnnotationStatus.RETIRED);
                updateUserActivity(authentication, CurationAction.DELETE, annotation, null);
                return true;
            }
        }
        return false;
    }

    /**
     * A function to get an annotation source object.
     *
     * @param publicationId a PubMed id
     * @param diseaseId a OMIM disease id
     *
     * @return an annotation source object  or null
     * @since 0.5.0
     */
    private AnnotationSource getAnnotationSource(String publicationId, String diseaseId){
        final Publication publication = publicationRepository.findByPublicationId(publicationId);
        final Disease disease = diseaseRepository.findDiseaseByDiseaseId(diseaseId);
        if(disease !=null && publication != null) {
            return annotationSourceRepository.findByPublicationAndDisease(publication, disease);
        }
        return null;
    }

    /**
     * A function to track user activity that happens when creating, editing or deleting annotations.
     *
     * @param authentication the spring authentication object.
     * @param curationAction the action that a user is taking.
     * @param annotation the annotation that is being modified.
     *
     * @since 0.5.0
     */
    private void updateUserActivity(Authentication authentication, CurationAction curationAction,
                                    Annotation annotation, Annotation oldAnnotation){
          User user = userRepository.findDistinctByAuthId(authentication.getName());
          if(user != null){
              UserActivity userActivity = new UserActivity(user, curationAction, annotation, oldAnnotation);
              userActivityRespository.save(userActivity);
          } else {
              // We got a sec leak
              System.out.println("Rut Roh!");
          }
    }



    public void insertTestData(){
        Publication publication = new Publication("PMID:31479590", "Encoding Clinical Data with the Human Phenotype Ontology for Computational Differential Diagnostics.", "2019 Sept", "Kohler S");
        Publication publication2 = new Publication("PMID:30476213", "Expansion of the Human Phenotype Ontology (HPO) knowledge base and resources", "2019 Jan", "Kohler S");
        Publication publication3 = new Publication("PMID:30323234", "Mikes future first author paper", "2020 Jan", "Gargano M");
        publicationRepository.save(publication);
        publicationRepository.save(publication2);
        publicationRepository.save(publication3);
        Disease disease = new Disease("OMIM:154700", "Marfan Syndrome");
        Disease disease2 = new Disease("OMIM:300200", "Adrenal Hypoplasia, Congenital");
        diseaseRepository.save(disease);
        diseaseRepository.save(disease2);
        annotationSourceRepository.save(new AnnotationSource(publication3, disease));
        annotationSourceRepository.save(new AnnotationSource(publication, disease));
        annotationSourceRepository.save(new AnnotationSource(publication2, disease));
    }
}
