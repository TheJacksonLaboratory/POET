package org.monarchinitiative.poet.controller;

import org.monarchinitiative.poet.model.search.SearchResponse;
import org.monarchinitiative.poet.service.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/search") // /annotation/<type>/<action>
public class SearchController {

    private final SearchService searchService;

    public SearchController(final SearchService searchService) {
        this.searchService = searchService;
    }

    /**
     *
     * @param query - a string to find a disease annotation by id.
     * @return the rare disease annotation or nothing
     */
    @GetMapping(value = "", headers = "Accept=application/json")
    public SearchResponse searchPublicationsAndDiseases(@RequestParam String query) {
        return searchService.searchPublicationAndDisease(query.trim());
    }
}
