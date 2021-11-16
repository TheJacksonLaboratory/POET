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
    private EntityService entityService;

    public AnnotationService(PublicationRepository publicationRepository,
                             DiseaseRepository diseaseRepository, AnnotationSourceRepository annotationSourceRepository,
                             TreatmentAnnotationRepository treatmentAnnotationRepository,
                             UserActivityRepository userActivityRepository,
                             PhenotypeAnnotationRepository phenotypeAnnotationRepository,
                             MessageRepository messageRepository, EntityService entityService) {
        this.publicationRepository = publicationRepository;
        this.diseaseRepository = diseaseRepository;
        this.annotationSourceRepository = annotationSourceRepository;
        this.treatmentAnnotationRepository = treatmentAnnotationRepository;
        this.userActivityRespository = userActivityRepository;
        this.phenotypeAnnotationRepository = phenotypeAnnotationRepository;
        this.messageRepository = messageRepository;
        this.entityService = entityService;
    }

    /**
     * A function to get phenotype annotations from the database by just disease.
     *
     * @return a collection of official phenotype annotations
     * @since 0.5.0
     */
    public List<PhenotypeAnnotation> getOfficialPhenotypes() {
        return this.phenotypeAnnotationRepository.findAllByStatus(AnnotationStatus.OFFICIAL);
    }

    /**
     * A function to get phenotype annotations from the database by just disease.
     *
     * @param diseaseId a OMIM disease id
     *
     * @return a collection of phenotype annotations or an empty list.
     * @since 0.5.0
     */
    public List<PhenotypeAnnotation> getPhenotypeAnnotationsByDisease(String diseaseId) throws AnnotationSourceException {
            Disease disease = this.diseaseRepository.findDiseaseByDiseaseId(diseaseId);
            if(disease != null) {
                List<PhenotypeAnnotation> annotations = this.phenotypeAnnotationRepository.findAllByAnnotationSourceDiseaseAndStatusNotAndStatusNot(disease, AnnotationStatus.RETIRED, AnnotationStatus.RETIRED_PENDING);
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
    public void createPhenotypeAnnotation(PhenotypeRequest phenotypeRequest, User user, boolean initialize) throws DuplicateAnnotationException {
        final AnnotationSource annotationSource = entityService.getAnnotationSource(phenotypeRequest.getPublicationId(), phenotypeRequest.getDiseaseId());
        if(annotationSource != null){
            AnnotationStatus status = user.getCurationRole().equals(CurationRole.POET_ADMIN) ? AnnotationStatus.ACCEPTED : AnnotationStatus.UNDER_REVIEW;
            if(initialize){
                status = AnnotationStatus.OFFICIAL;
            }
            final PhenotypeAnnotation annotation = new PhenotypeAnnotation(phenotypeRequest, annotationSource,
                    status, user);
            if(phenotypeAnnotationRepository.existsByAnnotationSourceAndHpoIdAndSexAndEvidenceAndOnsetAndFrequencyAndModifierAndQualifierAndStatusNot(
                    annotation.getAnnotationSource(), annotation.getHpoId(), annotation.getSex(),
                    annotation.getEvidence(), annotation.getOnset(), annotation.getFrequency(), annotation.getQualifier(),
                    annotation.getModifier(), AnnotationStatus.RETIRED)){
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
        if(user.getCurationRole().equals(CurationRole.POET_ADMIN)){
            // An elevated curator reviewing an annotation.
            if(isValidReview(review)){
                oldAnnotation.setStatus(reviewToStatus(review));
                if(review.equals("deny")){
                    Message reviewMessage = new Message(phenotypeRequest.getMessage(), user, oldAnnotation);
                    messageRepository.save(reviewMessage);
                    oldAnnotation.newMessage(reviewMessage);
                }
                phenotypeAnnotationRepository.save(oldAnnotation);
                updateUserActivity(owner, user, CurationAction.REVIEW, oldAnnotation, null);
            }

            // Allow official annotations to be updated by elevated users
            if(oldAnnotation.getStatus().equals(AnnotationStatus.OFFICIAL)){
                final PhenotypeAnnotation annotation = new PhenotypeAnnotation(phenotypeRequest, oldAnnotation.getAnnotationSource(),
                        AnnotationStatus.ACCEPTED, oldAnnotation.getOwner());
                oldAnnotation.setStatus(AnnotationStatus.RETIRED_PENDING);
                phenotypeAnnotationRepository.save(oldAnnotation);
                phenotypeAnnotationRepository.save(annotation);
                updateUserActivity(oldAnnotation.getOwner(), user, CurationAction.OVERRIDE, annotation, oldAnnotation);
            // Allow accepted annotations to be updated by elevated users
            } else if(oldAnnotation.getStatus().equals(AnnotationStatus.ACCEPTED)){
                final PhenotypeAnnotation annotation = new PhenotypeAnnotation(phenotypeRequest, oldAnnotation.getAnnotationSource(),
                        AnnotationStatus.ACCEPTED, oldAnnotation.getOwner());
                oldAnnotation.setStatus(AnnotationStatus.RETIRED);
                phenotypeAnnotationRepository.save(oldAnnotation);
                phenotypeAnnotationRepository.save(annotation);
                updateUserActivity(oldAnnotation.getOwner(), user, CurationAction.OVERRIDE, annotation, oldAnnotation);
            // An elevated curators annotations can never be under review, must be someone elses
            } else if(oldAnnotation.getStatus().equals(AnnotationStatus.UNDER_REVIEW)){
                // Most likely editing someones annotation
                // Build the new annotation, do a check for duplicated data 
                final PhenotypeAnnotation annotation = new PhenotypeAnnotation(phenotypeRequest, oldAnnotation.getAnnotationSource(),
                AnnotationStatus.ACCEPTED, oldAnnotation.getOwner());
                if(phenotypeAnnotationRepository.existsByAnnotationSourceAndHpoIdAndSexAndEvidenceAndOnsetAndFrequencyAndModifierAndQualifierAndStatus(
                    annotation.getAnnotationSource(), annotation.getHpoId(), annotation.getSex(), annotation.getEvidence(), 
                    annotation.getOnset(), annotation.getFrequency(), annotation.getModifier(), annotation.getQualifier(), annotation.getStatus())){
                        throw new DuplicateAnnotationException("phenotype", annotation.getAnnotationSource().getDisease().getDiseaseId());
                    }
                // Save the new annotation as accepted, retire the current under review one
                phenotypeAnnotationRepository.save(annotation);
                oldAnnotation.setStatus(AnnotationStatus.RETIRED);
                phenotypeAnnotationRepository.save(oldAnnotation);
                updateUserActivity(oldAnnotation.getOwner(), user, CurationAction.OVERRIDE, annotation, oldAnnotation);
            }
        } else if(user.equals(owner)){
            // Resubmitting after needs work
            if(oldAnnotation.getStatus().equals(AnnotationStatus.NEEDS_WORK) || oldAnnotation.getStatus().equals(AnnotationStatus.UNDER_REVIEW)){
                oldAnnotation.setStatus(AnnotationStatus.UNDER_REVIEW);
                oldAnnotation.updateAnnotation(phenotypeRequest);
                phenotypeAnnotationRepository.save(oldAnnotation);
                updateUserActivity(user, null, CurationAction.RESUBMIT, oldAnnotation, null);
            // Updating their own annotations that are accepted or under review
            } else if(oldAnnotation.getStatus().equals(AnnotationStatus.ACCEPTED)) {
                final PhenotypeAnnotation annotation = new PhenotypeAnnotation(phenotypeRequest, oldAnnotation.getAnnotationSource(),
                        AnnotationStatus.UNDER_REVIEW, oldAnnotation.getOwner());
                if(phenotypeAnnotationRepository.existsByAnnotationSourceAndHpoIdAndSexAndEvidenceAndOnsetAndFrequencyAndModifierAndQualifierAndStatusNotAndIdNot(
                        annotation.getAnnotationSource(), annotation.getHpoId(), annotation.getSex(),
                        annotation.getEvidence(), annotation.getOnset(), annotation.getFrequency(), annotation.getModifier(), annotation.getQualifier(), AnnotationStatus.RETIRED,  oldAnnotation.getId())){
                    throw new DuplicateAnnotationException("phenotype", annotation.getAnnotationSource().getDisease().getDiseaseName());
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
     * A function to get maxo annotations from the database by either both publication and  disease or just disease.
     *
     * @param diseaseId a OMIM disease id
     *
     * @return a collection of maxo annotations or an empty list.
     * @since 0.5.0
     */
    public List<TreatmentAnnotation> getTreatmentAnnotationsByDisease(String diseaseId) throws AnnotationSourceException {
        Disease disease = this.diseaseRepository.findDiseaseByDiseaseId(diseaseId);
        if(disease != null) {
            List<TreatmentAnnotation> annotations = this.treatmentAnnotationRepository.findAllByAnnotationSourceDiseaseAndStatusNotAndStatusNot(disease, AnnotationStatus.RETIRED, AnnotationStatus.RETIRED_PENDING);
            return (List<TreatmentAnnotation>) getLastUpdatedForAnnotation(annotations);
        }
        throw new AnnotationSourceException(diseaseId);
    }


    private List<? extends Annotation> getLastUpdatedForAnnotation(List<? extends Annotation> annotations) {
        if(annotations.size() > 0){
                return annotations.stream().peek(annotation -> {
                    UserActivity activity = userActivityRespository.getMostRecentDateForAnnotationActivity(annotation.getId());
                    annotation.setLastUpdatedDate(activity.getDateTime());
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
    public void createTreatmentAnnotation(TreatmentRequest treatmentRequest, User user) throws DuplicateAnnotationException {
        // We have a valid publication and a valid disease, do we have an annotation source for them?
        final AnnotationSource annotationSource = entityService.getAnnotationSource(treatmentRequest.getPublicationId(), treatmentRequest.getDiseaseId());
        if(annotationSource != null){
            AnnotationStatus status = user.getCurationRole().equals(CurationRole.POET_ADMIN) ? AnnotationStatus.ACCEPTED : AnnotationStatus.UNDER_REVIEW;
            final TreatmentAnnotation annotation = new TreatmentAnnotation(treatmentRequest, annotationSource, status, user);
            // Check if we have a duplicate annotation, if so throw an error
            // See if we already have an annotation like this.
            if(treatmentAnnotationExists(annotation, AnnotationStatus.RETIRED)){
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

            if(user.getCurationRole().equals(CurationRole.POET_ADMIN)) {
                if(isValidReview(review)){
                    // Review, check that the authentication is a valid user and that they are an elevated curator
                        oldAnnotation.setStatus(reviewToStatus(review));
                        if(review.equals("deny")){
                            Message reviewMessage = new Message(treatmentRequest.getMessage(), user, oldAnnotation);
                            messageRepository.save(reviewMessage);
                            oldAnnotation.newMessage(reviewMessage);
                        }
                        treatmentAnnotationRepository.save(oldAnnotation);
                        updateUserActivity(owner, user, CurationAction.REVIEW, oldAnnotation, null);
                }
                // Allow official annotations to be updated by elevated users
                if(oldAnnotation.getStatus().equals(AnnotationStatus.OFFICIAL)) {
                    final TreatmentAnnotation annotation = new TreatmentAnnotation(oldAnnotation.getAnnotationSource(),
                            AnnotationStatus.ACCEPTED, oldAnnotation.getOwner(), treatmentRequest.getMaxoId(), treatmentRequest.getMaxoName(),
                            treatmentRequest.getHpoName(), treatmentRequest.getHpoId(), treatmentRequest.getEvidence(),
                            treatmentRequest.getComment(), treatmentRequest.getRelation(), treatmentRequest.getExtensionId(), treatmentRequest.getExtensionLabel());
                    oldAnnotation.setStatus(AnnotationStatus.RETIRED_PENDING);
                    treatmentAnnotationRepository.save(oldAnnotation);
                    treatmentAnnotationRepository.save(annotation);
                    updateUserActivity(oldAnnotation.getOwner(), user, CurationAction.OVERRIDE, annotation, oldAnnotation);
                // Allow accepted annotations to be updated by elevated users
                } else if(oldAnnotation.getStatus().equals(AnnotationStatus.ACCEPTED)) {
                    final TreatmentAnnotation annotation = new TreatmentAnnotation(oldAnnotation.getAnnotationSource(),
                            AnnotationStatus.ACCEPTED, oldAnnotation.getOwner(), treatmentRequest.getMaxoId(), treatmentRequest.getMaxoName(),
                            treatmentRequest.getHpoName(), treatmentRequest.getHpoId(), treatmentRequest.getEvidence(),
                            treatmentRequest.getComment(), treatmentRequest.getRelation(), treatmentRequest.getExtensionId(), treatmentRequest.getExtensionLabel());
                    oldAnnotation.setStatus(AnnotationStatus.RETIRED);
                    treatmentAnnotationRepository.save(oldAnnotation);
                    treatmentAnnotationRepository.save(annotation);
                    updateUserActivity(oldAnnotation.getOwner(), user, CurationAction.OVERRIDE, annotation, oldAnnotation);
                 // An elevated curators annotations can never be under review, must be someone elses
                } else if(oldAnnotation.getStatus().equals(AnnotationStatus.UNDER_REVIEW)){
                    // Most likely editing someones annotation
                    // Build the new annotation, do a check for duplicated data 
                    final TreatmentAnnotation annotation = new TreatmentAnnotation(treatmentRequest, oldAnnotation.getAnnotationSource(),
                    AnnotationStatus.ACCEPTED, oldAnnotation.getOwner());
                    if(treatmentAnnotationExists(annotation, null)){
                            throw new DuplicateAnnotationException("treatment", annotation.getAnnotationSource().getDisease().getDiseaseId());
                    }
                    // Save the new annotation as accepted, retire the current under review one
                    oldAnnotation.setStatus(AnnotationStatus.RETIRED);
                    treatmentAnnotationRepository.save(oldAnnotation);
                    treatmentAnnotationRepository.save(annotation);
                    updateUserActivity(oldAnnotation.getOwner(), user, CurationAction.OVERRIDE, annotation, oldAnnotation);
                }
            } else if(user.equals(owner)){
                // Resubmitting after needs work
                if(oldAnnotation.getStatus().equals(AnnotationStatus.NEEDS_WORK) || oldAnnotation.getStatus().equals(AnnotationStatus.UNDER_REVIEW)){
                    oldAnnotation.setStatus(AnnotationStatus.UNDER_REVIEW);
                    oldAnnotation.updateAnnotation(treatmentRequest);
                    treatmentAnnotationRepository.save(oldAnnotation);
                    updateUserActivity(user,null, CurationAction.RESUBMIT, oldAnnotation, null);
                // Updating their own annotations that are accepted or under review
                } else if(oldAnnotation.getStatus().equals(AnnotationStatus.ACCEPTED)){
                    final TreatmentAnnotation annotation = new TreatmentAnnotation(oldAnnotation.getAnnotationSource(),
                            AnnotationStatus.UNDER_REVIEW, oldAnnotation.getOwner(), treatmentRequest.getMaxoId(), treatmentRequest.getMaxoName(),
                            treatmentRequest.getHpoName(), treatmentRequest.getHpoId(), treatmentRequest.getEvidence(),
                            treatmentRequest.getComment(), treatmentRequest.getRelation(), treatmentRequest.getExtensionId(), treatmentRequest.getExtensionLabel());

                    // See if we already have an annotation that exists from the one we are trying to update to.
                    if (treatmentAnnotationExists(annotation, AnnotationStatus.RETIRED)) {
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

    public boolean phenotypeAnnotationExists(PhenotypeAnnotation phenotypeAnnotation, AnnotationStatus status){
        return false;
    }

    public boolean treatmentAnnotationExists(TreatmentAnnotation treatmentAnnotation, AnnotationStatus status){
        if(status != null){
            // Negative test against a specific status
            return treatmentAnnotationRepository.existsByAnnotationSourceAndMaxoIdAndHpoIdAndExtensionIdAndEvidenceAndRelationAndStatusNot(
                    treatmentAnnotation.getAnnotationSource(), treatmentAnnotation.getMaxoId(), treatmentAnnotation.getHpoId(), treatmentAnnotation.getExtensionId(),
                    treatmentAnnotation.getEvidence(), treatmentAnnotation.getRelation(), status);
        } else {
            // Positive test with the treatments status
            return treatmentAnnotationRepository.existsByAnnotationSourceAndMaxoIdAndHpoIdAndExtensionIdAndEvidenceAndRelationAndStatus(
                    treatmentAnnotation.getAnnotationSource(), treatmentAnnotation.getMaxoId(), treatmentAnnotation.getHpoId(), treatmentAnnotation.getExtensionId(),
                    treatmentAnnotation.getEvidence(), treatmentAnnotation.getRelation(), treatmentAnnotation.getStatus());
        }
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
            if(curationAction.equals(CurationAction.REVIEW) || curationAction.equals(CurationAction.OVERRIDE)){
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
}
