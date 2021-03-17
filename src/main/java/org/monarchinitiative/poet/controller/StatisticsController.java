package org.monarchinitiative.poet.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.monarchinitiative.poet.model.response.AnnotationCount;
import org.monarchinitiative.poet.model.response.Contribution;
import org.monarchinitiative.poet.model.entities.UserActivity;
import org.monarchinitiative.poet.service.StatisticsService;
import org.monarchinitiative.poet.views.UserActivityViews;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
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
                                              Authentication authentication){
        return statisticsService.getUserActivity(all, weeks, authentication);
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
}
