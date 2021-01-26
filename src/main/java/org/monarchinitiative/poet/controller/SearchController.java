package org.monarchinitiative.poet.controller;

import org.monarchinitiative.poet.model.SearchResponse;
import org.monarchinitiative.poet.service.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * This class is an implementation of Spring's Rest Controller. It provides RESTful API's to search the main
 * entities of poet.
 *
 * @author Michael Gargano
 * @since 0.5.0
 */
@RestController
@RequestMapping(value = "${api.version}/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(final SearchService searchService) {
        this.searchService = searchService;
    }

    /**
     * The endpoint to retrieve a disease annotation by disease id.
     *
     * @param query a query string to search for publication or diseases
     * @return a list of search response objects containing ids and names.
     * @since 0.5.0
     */
    @GetMapping(value = "", headers = "Accept=application/json")
    public List<SearchResponse> searchPublicationsAndDiseases(@RequestParam String query) {
        return searchService.searchDisease(query.trim());
    }
}
