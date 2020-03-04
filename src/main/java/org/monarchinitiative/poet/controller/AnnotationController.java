package org.monarchinitiative.poet.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.monarchinitiative.poet.model.Greeting;
import org.monarchinitiative.poet.security.AnnotationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnnotationController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    private final AnnotationService annotationService;

    public AnnotationController(final AnnotationService annotationService){
     this.annotationService = annotationService;
    }

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
}
