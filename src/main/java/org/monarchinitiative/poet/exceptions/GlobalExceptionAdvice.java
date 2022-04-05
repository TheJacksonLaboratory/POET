package org.monarchinitiative.poet.exceptions;

import org.monarchinitiative.poet.model.responses.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(DiseaseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorDetails diseaseNotFoundHandler(DiseaseNotFoundException ex) {
        return new ErrorDetails(new Date(), "Data missing error",
            ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(PublicationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorDetails publicationNotFoundHandler(PublicationNotFoundException ex) {
        return new ErrorDetails(new Date(), "Data missing error",
                ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(DuplicateAnnotationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorDetails duplicateAnnotationException(DuplicateAnnotationException ex) {
        return new ErrorDetails(new Date(), "Duplicate data error",
                ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(AnnotationSourceException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ErrorDetails annotationSourceException(AnnotationSourceException ex) {
        return new ErrorDetails(new Date(), "Data missing error",
                ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ErrorDetails authenticationException(AuthenticationException ex) {
        return new ErrorDetails(new Date(), "Unauthorized.",
                ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorDetails handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return new ErrorDetails(new Date(), "Request body validation Failed",
                ex.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ResponseBody
    @ExceptionHandler(ExportException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ErrorDetails handleExportException(ExportException ex){
        return new ErrorDetails(new Date(), "Exporting ontology failed.", ex.getMessage());
    }
}
