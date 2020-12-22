package org.monarchinitiative.poet.service;

import org.monarchinitiative.poet.model.Contribution;
import org.monarchinitiative.poet.model.entities.UserActivity;
import org.monarchinitiative.poet.repository.UserActivityRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * A spring service component created to provide business logic and functionality to get user activity
 *
 * @author Michael Gargano
 * @since 0.5.0
 */
@Service
public class StatisticsService {

    private UserActivityRepository userActivityRepository;

    public StatisticsService(UserActivityRepository userActivityRepository){
        this.userActivityRepository = userActivityRepository;
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
        final Integer maxo = userActivityRepository.countAllByAnnotation_AnnotationTypeAndUserAuthId("maxo", authentication.getName());
        final Integer hpo = 0;
        final Integer phenopackets = 0;
        return new Contribution(maxo, hpo, phenopackets);
    }
}
