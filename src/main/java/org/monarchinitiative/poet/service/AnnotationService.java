package org.monarchinitiative.poet.service;

import org.monarchinitiative.poet.exceptions.AnnotationSourceException;
import org.monarchinitiative.poet.exceptions.AuthenticationException;
import org.monarchinitiative.poet.exceptions.DuplicateAnnotationException;
import org.monarchinitiative.poet.model.enumeration.AnnotationStatus;
import org.monarchinitiative.poet.model.requests.PhenotypeRequest;
import org.monarchinitiative.poet.model.requests.TreatmentRequest;
import org.monarchinitiative.poet.model.entities.*;
import org.monarchinitiative.poet.model.enumeration.CurationAction;
import org.monarchinitiative.poet.model.enumeration.CurationRole;
import org.monarchinitiative.poet.repository.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    private UserActivityRepository userActivityRespository;
    private PhenotypeAnnotationRepository phenotypeAnnotationRepository;
    private MessageRepository messageRepository;

    public AnnotationService(PublicationRepository publicationRepository,
                             DiseaseRepository diseaseRepository, AnnotationSourceRepository annotationSourceRepository,
                             TreatmentAnnotationRepository treatmentAnnotationRepository,
                             UserActivityRepository userActivityRepository,
                             PhenotypeAnnotationRepository phenotypeAnnotationRepository,
                             MessageRepository messageRepository) {
        this.publicationRepository = publicationRepository;
        this.diseaseRepository = diseaseRepository;
        this.annotationSourceRepository = annotationSourceRepository;
        this.treatmentAnnotationRepository = treatmentAnnotationRepository;
        this.userActivityRespository = userActivityRepository;
        this.phenotypeAnnotationRepository = phenotypeAnnotationRepository;
        this.messageRepository = messageRepository;
    }

    /**
     * A function to get phenotype annotations from the database by just disease.
     *
     * @param user a user entity making the request.
     * @return a collection of phenotype annotations or an empty list.
     * @since 0.5.0
     */
    public List<PhenotypeAnnotation> getPhenotypeAnnotationsByUser(User user, AnnotationStatus status) throws AnnotationSourceException {
        List<PhenotypeAnnotation> annotations;
        if(status != null){
            annotations = this.phenotypeAnnotationRepository.findAllByOwnerAndStatus(user, status);
        } else {
            annotations = this.phenotypeAnnotationRepository.findAllByOwnerAndStatusNot(user, AnnotationStatus.RETIRED);
        }
        return (List<PhenotypeAnnotation>) getLastUpdatedForAnnotation(annotations);
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
    public List<PhenotypeAnnotation> getPhenotypeAnnotationsByDisease(String diseaseId, String sort) throws AnnotationSourceException {
            Disease disease = this.diseaseRepository.findDiseaseByDiseaseId(diseaseId);
            if(disease != null) {
                List<PhenotypeAnnotation> annotations = this.phenotypeAnnotationRepository.findAllByAnnotationSourceDiseaseAndStatusNot(disease, AnnotationStatus.RETIRED);
                return (List<PhenotypeAnnotation>) getLastUpdatedForAnnotation(annotations);
            } else {
                throw new AnnotationSourceException(diseaseId);
            }
    }

    /**
     * Create a phenotype annotation with a pending review status.
     * enforcing business rules with the status of the annotation
     *
     * @param phenotypeRequest a phenotype request body
     * @param user the user making the request
     */
    @Transactional()
    public void createPhenotypeAnnotation(PhenotypeRequest phenotypeRequest, User user) throws DuplicateAnnotationException {
        final AnnotationSource annotationSource = getAnnotationSource(phenotypeRequest.getPublicationId(), phenotypeRequest.getDiseaseId());
        if(annotationSource != null){
            AnnotationStatus status = user.getCurationRole().equals(CurationRole.ELEVATED_CURATOR) ? AnnotationStatus.ACCEPTED : AnnotationStatus.UNDER_REVIEW;
            final PhenotypeAnnotation annotation = new PhenotypeAnnotation(phenotypeRequest, annotationSource,
                    status, user);
            if(phenotypeAnnotationRepository.existsByAnnotationSourceAndHpoIdAndSexAndEvidenceAndOnsetAndModifier(
                    annotation.getAnnotationSource(), annotation.getHpoId(), annotation.getSex(),
                    annotation.getEvidence(), annotation.getOnset(), annotation.getModifier())){
                throw new DuplicateAnnotationException("phenotype", annotation.getAnnotationSource().getDisease().getDiseaseName());
            }
            phenotypeAnnotationRepository.save(annotation);
            updateUserActivity(user, null, CurationAction.CREATE, annotation, null);
        } else {
            throw new AnnotationSourceException(phenotypeRequest.getPublicationId(), phenotypeRequest.getDiseaseId());
        }
    }

    /**
     * Update a phenotype annotation enforcing business rules with the status of the annotation
     *
     * @param phenotypeRequest a phenotype request body
     * @param user the user making the request
     */
    public void updatePhenotypeAnnotation(PhenotypeRequest phenotypeRequest, User user, String review) throws DuplicateAnnotationException {

        PhenotypeAnnotation oldAnnotation = phenotypeAnnotationRepository.findDistinctById(phenotypeRequest.getId());
        User owner = userActivityRespository.getMostRecentDateForAnnotationActivity(oldAnnotation.getId()).getOwner();


        if(isValidReview(review)){
            if(user.getCurationRole().equals(CurationRole.ELEVATED_CURATOR)){
                oldAnnotation.setStatus(reviewToStatus(review));
                if(review.equals("deny")){
                    Message reviewMessage = new Message(phenotypeRequest.getMessage(), user, oldAnnotation);
                    messageRepository.save(reviewMessage);
                    oldAnnotation.newMessage(reviewMessage);
                }
                phenotypeAnnotationRepository.save(oldAnnotation);
                updateUserActivity(owner, user, CurationAction.REVIEW, oldAnnotation, null);
            }else {
                throw new AuthenticationException(user.getNickname());
            }
        } else {
            if(user.equals(owner)){
                if(oldAnnotation.getStatus().equals(AnnotationStatus.NEEDS_WORK)){
                    oldAnnotation.setStatus(AnnotationStatus.UNDER_REVIEW);
                    phenotypeAnnotationRepository.save(oldAnnotation);
                    updateUserActivity(user, null, CurationAction.RESUBMIT, oldAnnotation, null);
                } else {
                    final PhenotypeAnnotation annotation = new PhenotypeAnnotation(phenotypeRequest, oldAnnotation.getAnnotationSource(),
                            oldAnnotation.getStatus(), oldAnnotation.getOwner());

                    // See if we already have an annotation like this.
                    if(phenotypeAnnotationRepository.existsByAnnotationSourceAndHpoIdAndSexAndEvidenceAndOnsetAndModifierAndStatusNot(
                            annotation.getAnnotationSource(), annotation.getHpoId(), annotation.getSex(),
                            annotation.getEvidence(), annotation.getOnset(), annotation.getModifier(), AnnotationStatus.RETIRED)){
                        throw new DuplicateAnnotationException("treatment", annotation.getAnnotationSource().getDisease().getDiseaseName());
                    }
                    oldAnnotation.setStatus(AnnotationStatus.RETIRED);
                    phenotypeAnnotationRepository.save(oldAnnotation);
                    phenotypeAnnotationRepository.save(annotation);
                    updateUserActivity(user, null, CurationAction.UPDATE, annotation, oldAnnotation);
                }

            } else {
                throw new AuthenticationException(user.getNickname());
            }
        }
    }

    /**
     * Delete a phenotype annotation with a pending review status.
     * enforcing business rules with the status of the annotation
     *
     * @param id a phenotype annotation id
     * @param user the user making the request
     * @return a boolean wherther the annotation was created or not.
     */
    public boolean deletePhenotypeAnnotation(Long id, User user) {
        if(id != null){
            PhenotypeAnnotation annotation = phenotypeAnnotationRepository.findDistinctById(id);
            if(annotation != null){
                annotation.setStatus(AnnotationStatus.RETIRED);
                phenotypeAnnotationRepository.save(annotation);
                updateUserActivity(user, null, CurationAction.DELETE, annotation, null);
                return true;
            }
        }
        return false;
    }

    /**
     * A function to get phenotype annotations from the database by just disease.
     *
     * @param user a user entity making the request.
     * @return a collection of phenotype annotations or an empty list.
     * @since 0.5.0
     */
    public List<TreatmentAnnotation> getTreatmentAnnotationByUser(User user, AnnotationStatus status) {
        List<TreatmentAnnotation> annotations;
        if (status != null){
            annotations = this.treatmentAnnotationRepository.findAllByOwnerAndStatus(user, status);
        } else {
            annotations = this.treatmentAnnotationRepository.findAllByOwnerAndStatusNot(user, AnnotationStatus.RETIRED);
        }
        return (List<TreatmentAnnotation>) getLastUpdatedForAnnotation(annotations);
    }

    /**
     * A function to get maxo annotations from the database by either both publication and  disease or just disease.
     *
     * @param diseaseId a OMIM disease id
     * @param sort a string composing of two parts both direction and field. TODO: Implement functionality
     *
     * @return a collection of maxo annotations or an empty list.
     * @since 0.5.0
     */
    public List<TreatmentAnnotation> getTreatmentAnnotationsByDisease(String diseaseId, String sort) throws AnnotationSourceException {
        Disease disease = this.diseaseRepository.findDiseaseByDiseaseId(diseaseId);
        if(disease != null) {
            List<TreatmentAnnotation> annotations = this.treatmentAnnotationRepository.findAllByAnnotationSourceDiseaseAndStatusNot(disease, AnnotationStatus.RETIRED);
            return (List<TreatmentAnnotation>) getLastUpdatedForAnnotation(annotations);
        }
        throw new AnnotationSourceException(diseaseId);
    }


    private List<? extends Annotation> getLastUpdatedForAnnotation(List<? extends Annotation> annotations) {
        if(annotations.size() > 0){
                return annotations.stream().peek(annotation -> {
                    UserActivity activity = userActivityRespository.getMostRecentDateForAnnotationActivity(annotation.getId());
                    annotation.setLastUpdatedDate(activity.getLocalDateTime());
                }).sorted(Comparator.comparing(Annotation::getLastUpdatedDate).reversed()).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Create a MaXo annotation with a pending review status.
     * enforcing business rules with the status of the annotation
     *
     * @param treatmentRequest a maxo request body
     * @param user the user making the request
     *
     */
    @Transactional()
    public void createTreatmentAnnotation(TreatmentRequest treatmentRequest, User user) throws DuplicateAnnotationException {
        // We have a valid publication and a valid disease, do we have an annotation source for them?
        final AnnotationSource annotationSource = getAnnotationSource(treatmentRequest.getPublicationId(), treatmentRequest.getDiseaseId());
        if(annotationSource != null){
            AnnotationStatus status = user.getCurationRole().equals(CurationRole.ELEVATED_CURATOR) ? AnnotationStatus.ACCEPTED : AnnotationStatus.UNDER_REVIEW;
            final TreatmentAnnotation annotation = new TreatmentAnnotation(treatmentRequest, annotationSource, status, user);
            // Check if we have a duplicate annotation, if so throw an error
            // See if we already have an annotation like this.
            if(treatmentAnnotationRepository.existsByAnnotationSourceAndAnnotationTypeAndMaxoIdAndHpoIdAndExtensionIdAndEvidenceAndRelationAndStatusNot(
                    annotation.getAnnotationSource(), "treatment", annotation.getMaxoId(), annotation.getHpoId(), annotation.getExtensionId(), annotation.getEvidence(), annotation.getRelation(), AnnotationStatus.RETIRED)){
                throw new DuplicateAnnotationException("treatment", annotation.getAnnotationSource().getDisease().getDiseaseName());
            }
            treatmentAnnotationRepository.save(annotation);
            updateUserActivity(user, null, CurationAction.CREATE, annotation, null);
        } else {
            throw new AnnotationSourceException(treatmentRequest.getPublicationId(), treatmentRequest.getDiseaseId());
        }
    }

    /**
     * Update a MaXo annotation enforcing business rules with the status of the annotation
     *
     * @param treatmentRequest a maxo request body
     * @param user the user making the request
     * @param review integer 0 = not a review, 1 = approve 2 = deny
     *
     * @return a boolean whether the annotation was created or not.
     */
    public void updateTreatmentAnnotation(TreatmentRequest treatmentRequest, User user, String review) throws DuplicateAnnotationException {

            TreatmentAnnotation oldAnnotation = treatmentAnnotationRepository.findDistinctById(treatmentRequest.getId());
            User owner = userActivityRespository.getMostRecentDateForAnnotationActivity(oldAnnotation.getId()).getOwner();

            if(isValidReview(review)){
                // Review, check that the authentication is a valid user and that they are an elevated curator
                if(user.getCurationRole().equals(CurationRole.ELEVATED_CURATOR)){
                    oldAnnotation.setStatus(reviewToStatus(review));
                    if(review.equals("deny")){
                        Message reviewMessage = new Message(treatmentRequest.getMessage(), user, oldAnnotation);
                        messageRepository.save(reviewMessage);
                        oldAnnotation.newMessage(reviewMessage);
                    }
                    treatmentAnnotationRepository.save(oldAnnotation);
                    updateUserActivity(owner, user, CurationAction.REVIEW, oldAnnotation, null);
                }else {
                    throw new AuthenticationException(user.getNickname());
                }
            } else {
                if(user.equals(owner)){
                    if(oldAnnotation.getStatus().equals(AnnotationStatus.NEEDS_WORK)){
                        oldAnnotation.setStatus(AnnotationStatus.UNDER_REVIEW);
                        treatmentAnnotationRepository.save(oldAnnotation);
                        updateUserActivity(user, null, CurationAction.RESUBMIT, oldAnnotation, null);
                    } else {
                        // Create the new one with updated values
                        final TreatmentAnnotation annotation = new TreatmentAnnotation(oldAnnotation.getAnnotationSource(),
                                oldAnnotation.getStatus(), oldAnnotation.getOwner(), treatmentRequest.getMaxoId(), treatmentRequest.getMaxoName(),
                                treatmentRequest.getHpoName(), treatmentRequest.getHpoId(), treatmentRequest.getEvidence(),
                                treatmentRequest.getComment(), treatmentRequest.getRelation(), treatmentRequest.getExtensionId(), treatmentRequest.getExtensionLabel());

                        // See if we already have an annotation like this.
                        if (treatmentAnnotationRepository.existsByAnnotationSourceAndAnnotationTypeAndMaxoIdAndHpoIdAndExtensionIdAndEvidenceAndRelationAndStatusNot(
                                annotation.getAnnotationSource(), "treatment", annotation.getMaxoId(), annotation.getHpoId(), annotation.getExtensionId(),
                                annotation.getEvidence(), annotation.getRelation(), AnnotationStatus.RETIRED)) {
                            throw new DuplicateAnnotationException("treatment", annotation.getAnnotationSource().getDisease().getDiseaseName());
                        }

                        oldAnnotation.setStatus(AnnotationStatus.RETIRED);
                        treatmentAnnotationRepository.save(oldAnnotation);
                        treatmentAnnotationRepository.save(annotation);
                        updateUserActivity(user, null, CurationAction.UPDATE, annotation, oldAnnotation);
                    }
                } else {
                    throw new AuthenticationException(user.getNickname());
                }
            }
    }

    /**
     * Delete a MaXo annotation with a pending review status.
     * enforcing business rules with the status of the annotation
     *
     * @param id a maxo annotation id
     * @param user the user making the request
     *
     * @return a boolean whether the annotation was created or not.
     */
    public boolean deleteTreatmentAnnotation(Long id, User user) {
        if(id != null){
            TreatmentAnnotation annotation = treatmentAnnotationRepository.findDistinctById(id);
            if(annotation != null){
                annotation.setStatus(AnnotationStatus.RETIRED);
                treatmentAnnotationRepository.save(annotation);
                updateUserActivity(user, null, CurationAction.DELETE, annotation, null);
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
     * @param user a user entity from the database
     * @param curationAction the action that a user is taking.
     * @param annotation the annotation that is being modified.
     *
     * @since 0.5.0
     */
    private void updateUserActivity(User user, User reviewer, CurationAction curationAction,
                                    Annotation annotation, Annotation oldAnnotation){
            UserActivity userActivity;
            if(curationAction.equals(CurationAction.REVIEW)){
              userActivity = new UserActivity(user, reviewer, curationAction, annotation, oldAnnotation);
            } else {
              userActivity = new UserActivity(user, curationAction, annotation, oldAnnotation);
            }
            userActivityRespository.save(userActivity);
    }

    /**
     * A function get the status from a review
     */
    private AnnotationStatus reviewToStatus(String review){
        if(review.equals("approve")){
            return AnnotationStatus.ACCEPTED;
        } else {
            return AnnotationStatus.NEEDS_WORK;
        }
    }

    private boolean isValidReview(String review){
        return review.equals("approve") || review.equals("deny");
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
