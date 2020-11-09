package org.monarchinitiative.poet.controller;

import org.monarchinitiative.poet.exceptions.DiseaseNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AnnotationExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(DiseaseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String diseaseNotFoundHandler(DiseaseNotFoundException ex) {
        return ex.getMessage();
    }
}
