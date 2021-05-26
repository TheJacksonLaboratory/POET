package org.monarchinitiative.poet.service;


import org.monarchinitiative.poet.model.enumeration.AnnotationStatus;
import org.monarchinitiative.poet.model.responses.AnnotationCount;
import org.monarchinitiative.poet.model.responses.Contribution;
import org.monarchinitiative.poet.model.entities.UserActivity;
import org.monarchinitiative.poet.model.responses.ReviewCount;
import org.monarchinitiative.poet.repository.DiseaseRepository;
import org.monarchinitiative.poet.repository.PhenotypeAnnotationRepository;
import org.monarchinitiative.poet.repository.TreatmentAnnotationRepository;
import org.monarchinitiative.poet.repository.UserActivityRepository;
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
    public List<UserActivity> getUserActivity(boolean all, int weeks, Authentication authentication){
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        LocalDateTime compare = new Date(System.currentTimeMillis() - ((7 * weeks) * DAY_IN_MS)).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        if(all){
            if(weeks == 0){
                return (List<UserActivity>) this.userActivityRepository.findAll();
            } else {
                return this.userActivityRepository.findUserActivityByLocalDateTimeAfter(compare);
            }
        } else {
            if(weeks == 0){
                return this.userActivityRepository.findUserActivityByOwnerAuthId(authentication.getName());
            } else {
                return this.userActivityRepository.findUserActivityByLocalDateTimeAfterAndOwnerAuthId(compare, authentication.getName());
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
        final int phenopackets = 0;
        return new Contribution(treatment, phenotype, phenopackets);
    }

    public AnnotationCount summarizeAnnotations(String diseaseId){
        int treatmentCount;
        int phenotypeCount;
        if(diseaseId != null){
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
        List<ReviewCount> phenotypeAnnotations = this.phenotypeAnnotationRepository.getAllByStatus(AnnotationStatus.UNDER_REVIEW);
        List<ReviewCount> treatmentAnnotations = this.treatmentAnnotationRepository.getAllByStatus(AnnotationStatus.UNDER_REVIEW);
        return Stream.concat(phenotypeAnnotations.stream(), treatmentAnnotations.stream()).collect(Collectors.toList());
    }
}
