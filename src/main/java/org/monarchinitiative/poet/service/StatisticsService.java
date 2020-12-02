package org.monarchinitiative.poet.service;

import org.monarchinitiative.poet.model.Contribution;
import org.monarchinitiative.poet.model.entities.UserActivity;
import org.monarchinitiative.poet.repository.UserActivityRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class StatisticsService {

    private UserActivityRepository userActivityRepository;

    public StatisticsService(UserActivityRepository userActivityRepository){
        this.userActivityRepository = userActivityRepository;
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
