package org.monarchinitiative.poet.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.monarchinitiative.poet.model.Contribution;
import org.monarchinitiative.poet.model.entities.UserActivity;
import org.monarchinitiative.poet.service.StatisticsService;
import org.monarchinitiative.poet.views.UserActivityViews;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/statistics")
public class StatisticsController {

    private StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @JsonView(UserActivityViews.Simple.class)
    @GetMapping(value = "/user-activity", headers = "Accept=application/json")
    public List<UserActivity> getUserActivity(@RequestParam(value = "w", defaultValue = "all") String who,
                                              Authentication authentication){
        return statisticsService.getUserActivity(who, authentication);
    }

    @GetMapping(value = "/contributions", headers = "Accept=application/json")
    public Contribution getCurrentUserContributions(Authentication authentication){
        return statisticsService.summarizeUserContributions(authentication);
    }
}
