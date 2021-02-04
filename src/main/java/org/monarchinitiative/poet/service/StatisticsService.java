package org.monarchinitiative.poet.service;

import org.monarchinitiative.poet.model.enumeration.AnnotationStatus;
import org.monarchinitiative.poet.model.response.AnnotationCount;
import org.monarchinitiative.poet.model.response.Contribution;
import org.monarchinitiative.poet.model.entities.Disease;
import org.monarchinitiative.poet.model.entities.UserActivity;
import org.monarchinitiative.poet.repository.DiseaseRepository;
import org.monarchinitiative.poet.repository.TreatmentAnnotationRepository;
import org.monarchinitiative.poet.repository.UserActivityRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

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
    private DiseaseRepository diseaseRepository;

    public StatisticsService(UserActivityRepository userActivityRepository,
                             TreatmentAnnotationRepository treatmentAnnotationRepository,
                             DiseaseRepository diseaseRepository){
        this.userActivityRepository = userActivityRepository;
        this.treatmentAnnotationRepository = treatmentAnnotationRepository;
        this.diseaseRepository = diseaseRepository;
    }

    /**
     * A function to search the disease and publication repositories for any records.
     *
     * @param all a string query to search for.
     * @param authentication a spring security authentication object that represents the user making the request
     * @return a list of search response objects or an empty list
     * @since 0.5.0
     */
    public List<UserActivity> getUserActivity(boolean all, Authentication authentication){
        if(all){
            return (List<UserActivity>) this.userActivityRepository.findAll();
        } else {
            return this.userActivityRepository.findUserActivityByUserAuthId(authentication.getName());
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
        final int maxo = userActivityRepository.countAllByAnnotation_AnnotationTypeAndUserAuthId("maxo", authentication.getName());
        final int hpo = 0;
        final int phenopackets = 0;
        return new Contribution(maxo, hpo, phenopackets);
    }

    public AnnotationCount summarizeAnnotations(String diseaseId){
        int treatmentCount = 0;
        int phenotypeCount = 0;
        if(diseaseId != null){
            treatmentCount = this.treatmentAnnotationRepository.countAllByAnnotationSourceDiseaseAndStatusNot(
                    this.diseaseRepository.findDiseaseByDiseaseId(diseaseId), AnnotationStatus.RETIRED
            );
        } else {
            treatmentCount = (int) this.treatmentAnnotationRepository.countAllByStatusNot(AnnotationStatus.RETIRED);
        }

        return new AnnotationCount(0, treatmentCount);
    }
}
