package org.monarchinitiative.poet.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.monarchinitiative.poet.model.entities.*;
import org.monarchinitiative.poet.model.enumeration.CurationAction;
import org.monarchinitiative.poet.model.responses.AnnotationCount;
import org.monarchinitiative.poet.model.responses.Contribution;
import org.monarchinitiative.poet.model.responses.DiseaseCount;
import org.monarchinitiative.poet.model.responses.ReviewCount;
import org.monarchinitiative.poet.service.StatisticsService;
import org.monarchinitiative.poet.service.UserService;
import org.monarchinitiative.poet.views.UserActivityViews;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is an implementation of Spring's Rest Controller. It provides RESTful API's to get useful
 * statistics about users activities.
 *
 * @author Michael Gargano
 * @since 0.5.0
 */
@RestController
@RequestMapping(value="${api.version}/statistics")
public class StatisticsController {

    private StatisticsService statisticsService;
    private UserService userService;

    public StatisticsController(StatisticsService statisticsService, UserService userService) {
        this.statisticsService = statisticsService;
        this.userService = userService;
    }

    /**
     * The endpoint to get user activity
     *
     * @param all a request parameter boolean whether to return all activity or just the current user
     * @return a list of user activity objects
     * @since 0.5.0
     */
    @JsonView(UserActivityViews.Simple.class)
    @GetMapping(value = "/activity", headers = "Accept=application/json")
    public List<UserActivity> getUserActivity(@RequestParam(value = "all", defaultValue = "true") boolean all,
                                              @RequestParam(value = "weeks", defaultValue = "0") int weeks,
                                              @RequestParam(value = "offset", defaultValue = "0") int offset,
                                              @RequestParam(value = "limit", defaultValue = "1000") int limit,
                                              Authentication authentication){
        Pageable pageable = PageRequest.of(offset,limit, Sort.by("dateTime").descending());
        return statisticsService.getUserActivity(all, weeks, pageable, authentication).stream().peek(activity -> {
            if(activity.getCurationAction().equals(CurationAction.REVIEW) ||
                    activity.getCurationAction().equals(CurationAction.OVERRIDE)){
                activity.ownerSwap();
            }
        }).collect(Collectors.toList());
    }

    /**
     * Group annotations by "activity" count
     * 1. get list of user activity for last 5 weeks.
     */
    @GetMapping(value ="/activity/disease", headers = "Accept=application/json")
    public List<DiseaseCount> getDiseaseActivity(@RequestParam(value = "all", defaultValue = "false") boolean all,
                                                    @RequestParam(value = "weeks", defaultValue = "4") int weeks,
                                                    @RequestParam(value = "offset", defaultValue = "0") int offset,
                                                    @RequestParam(value = "limit", defaultValue = "250") int limit,
                                                    Authentication authentication){
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("dateTime").descending());
        return statisticsService.getUserActivity(true, weeks, pageable, authentication).stream().
                map(UserActivity::getAnnotation).map(Annotation::getAnnotationSource)
                .collect(Collectors.groupingByConcurrent(AnnotationSource::getDisease, Collectors.counting()))
                .entrySet().stream()
                .map(entry -> {
                    return new DiseaseCount(entry.getKey().getDiseaseId(), entry.getKey().getDiseaseName(),
                            entry.getValue());

                }).collect(Collectors.toList());
    }

    /**
     * The endpoint to get user contributions stratified by annotation types
     *
     * @return a list of contributions of the user making the request
     * @since 0.5.0
     */
    @GetMapping(value = "/contributions", headers = "Accept=application/json")
    public Contribution getCurrentUserContributions(Authentication authentication){
        return statisticsService.summarizeUserContributions(authentication);
    }

    /**
     * The endpoint to get annotation summary statistics by type.
     * @param diseaseId a disease id
     * @return
     */
    @GetMapping(value = "/annotation/{diseaseId}")
    public AnnotationCount getAnnotationStatistics(@PathVariable(value = "diseaseId", required = false)  String diseaseId){
        return this.statisticsService.summarizeAnnotations(diseaseId);
    }

    /**
     * The endpoint to get annotation summary statistics by type.
     * @return
     */
    @GetMapping(value = "/annotation/review")
    public List<ReviewCount> getAnnotationsNeedingReview(){
        return this.statisticsService.summarizeAnnotationNeedReview();
    }

    /**
     * The endpoint to get annotation summary statistics by type.
     * @return
     */
    @GetMapping(value = "/annotation/work")
    public List<ReviewCount> getAnnotationsNeedWorkByUser(Authentication authentication){
        final User user = userService.getExistingUser(authentication);
        return this.statisticsService.summarizeAnnotationNeedWork(user);
    }
}
