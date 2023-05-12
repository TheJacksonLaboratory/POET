package org.monarchinitiative.poet.service;


import org.monarchinitiative.poet.model.entities.User;
import org.monarchinitiative.poet.model.enumeration.AnnotationStatus;
import org.monarchinitiative.poet.model.responses.AnnotationCount;
import org.monarchinitiative.poet.model.responses.Contribution;
import org.monarchinitiative.poet.model.entities.UserActivity;
import org.monarchinitiative.poet.model.responses.ReviewCount;
import org.monarchinitiative.poet.repository.DiseaseRepository;
import org.monarchinitiative.poet.repository.PhenotypeAnnotationRepository;
import org.monarchinitiative.poet.repository.TreatmentAnnotationRepository;
import org.monarchinitiative.poet.repository.UserActivityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A spring service component created to provide business logic and functionality to get user activity
 *
 * @author Michael Gargano
 * @since 0.5.0
 */
@Service
public class StatisticsService {

    private UserActivityRepository userActivityRepository;
    private TreatmentAnnotationRepository treatmentAnnotationRepository;
    private PhenotypeAnnotationRepository phenotypeAnnotationRepository;
    private DiseaseRepository diseaseRepository;

    public StatisticsService(UserActivityRepository userActivityRepository,
                             TreatmentAnnotationRepository treatmentAnnotationRepository,
                             DiseaseRepository diseaseRepository, PhenotypeAnnotationRepository phenotypeAnnotationRepository ){
        this.userActivityRepository = userActivityRepository;
        this.treatmentAnnotationRepository = treatmentAnnotationRepository;
        this.diseaseRepository = diseaseRepository;
        this.phenotypeAnnotationRepository = phenotypeAnnotationRepository;
    }

    /**
     * A function to search the disease and publication repositories for any records.
     *
     * @param all a string query to search for.
     * @param authentication a spring security authentication object that represents the user making the request
     * @return a list of search response objects or an empty list
     * @since 0.5.0
     */
    public Page<UserActivity> getUserActivity(boolean all, int weeks, Pageable pageable, Authentication authentication){
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        LocalDateTime compare = new Date(System.currentTimeMillis() - ((7 * weeks) * DAY_IN_MS)).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        if(all){
            if(weeks == 0){
                return this.userActivityRepository.findAll(pageable);
            } else {
                return this.userActivityRepository.findUserActivityByDateTimeAfter(compare, pageable);
            }
        } else {
            if(weeks == 0){
                return this.userActivityRepository.findUserActivityByOwnerAuthId(authentication.getName(), pageable);
            } else {
                return this.userActivityRepository.findUserActivityByDateTimeAfterAndOwnerAuthId(compare, authentication.getName(), pageable);
            }
        }
    }

    /**
     * A function to retrieve user contributions by annotation type.
     *
     * @param authentication a spring security authentication object that represents the user making the request
     * @return a contribution object with counts
     * @since 0.5.0
     */
    public Contribution summarizeUserContributions(Authentication authentication){
        final int treatment = userActivityRepository.countAllByAnnotation_AnnotationTypeAndOwnerAuthId("maxo", authentication.getName());
        final int phenotype = 0;
        return new Contribution(treatment, phenotype);
    }

    public AnnotationCount summarizeAnnotations(String diseaseId){
        int treatmentCount;
        int phenotypeCount;
        if(diseaseId.isBlank()){
            treatmentCount = this.treatmentAnnotationRepository.countAllByAnnotationSourceDiseaseAndStatusNot(
                    this.diseaseRepository.findDiseaseByDiseaseId(diseaseId), AnnotationStatus.RETIRED
            );
            phenotypeCount = this.phenotypeAnnotationRepository.countAllByAnnotationSourceDiseaseAndStatusNot(
                    this.diseaseRepository.findDiseaseByDiseaseId(diseaseId), AnnotationStatus.RETIRED
            );
        } else {
            treatmentCount = this.treatmentAnnotationRepository.countAllByStatusNot(AnnotationStatus.RETIRED);
            phenotypeCount = this.phenotypeAnnotationRepository.countAllByStatusNot(AnnotationStatus.RETIRED);
        }

        return new AnnotationCount(phenotypeCount, treatmentCount);
    }

    public List<ReviewCount> summarizeAnnotationNeedReview(){
        List<ReviewCount> phenotypeAnnotations = this.phenotypeAnnotationRepository.countAllByStatus(AnnotationStatus.UNDER_REVIEW);
        List<ReviewCount> treatmentAnnotations = this.treatmentAnnotationRepository.countAllByStatus(AnnotationStatus.UNDER_REVIEW);
        return Stream.concat(phenotypeAnnotations.stream(), treatmentAnnotations.stream()).collect(Collectors.toList());
    }

    public List<ReviewCount> summarizeAnnotationNeedWork(User user){
        List<ReviewCount> phenotypeAnnotations = this.phenotypeAnnotationRepository.countAllByStatusAndUser(AnnotationStatus.NEEDS_WORK, user);
        List<ReviewCount> treatmentAnnotations = this.treatmentAnnotationRepository.countAllByStatusAndUser(AnnotationStatus.NEEDS_WORK, user);
        return Stream.concat(phenotypeAnnotations.stream(), treatmentAnnotations.stream()).collect(Collectors.toList());
    }
}
