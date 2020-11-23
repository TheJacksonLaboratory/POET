package org.monarchinitiative.poet.service;

import org.monarchinitiative.poet.model.Contribution;
import org.monarchinitiative.poet.model.entities.User;
import org.monarchinitiative.poet.model.entities.UserActivity;
import org.monarchinitiative.poet.repository.MaxoAnnotationRepository;
import org.monarchinitiative.poet.repository.UserActivityRepository;
import org.monarchinitiative.poet.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class StatisticsService {
    private UserActivityRepository userActivityRepository;
    private MaxoAnnotationRepository maxoAnnotationRepository;
    private UserRepository userRepository;

    public StatisticsService(UserActivityRepository userActivityRepository,
                             MaxoAnnotationRepository maxoAnnotationRepository,
                             UserRepository userRepository) {
        this.userActivityRepository = userActivityRepository;
        this.maxoAnnotationRepository = maxoAnnotationRepository;
        this.userRepository = userRepository;
    }

    public List<UserActivity> getUserActivity(String who, Authentication authentication){
        if(who.equals("all")){
            return (List<UserActivity>) this.userActivityRepository.findAll();
        } else if(who.equals("current")){
            return this.userActivityRepository.findUserActivityByUserAuthId(authentication.getName());
        }
        return Collections.emptyList();
    }

    public Contribution summarizeUserContributions(Authentication authentication){
        final Integer maxo = userActivityRepository.countAllByAnnotation_AnnotationTypeAndUserAuthId("maxo", authentication.getName());
        final Integer hpo = 0;
        final Integer phenopackets = 0;
        return new Contribution(maxo, hpo, phenopackets);
    }
}
