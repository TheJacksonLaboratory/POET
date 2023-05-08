package org.monarchinitiative.poet.controller;

import org.apache.commons.csv.CSVFormat;
import org.monarchinitiative.poet.service.ExportService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping(value = "${api.version}/export/")
public class ExportController {
    private final ExportService exportService;

    @Value("${releasekey}")
    private String releasekey;

    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    /**
     * The endpoint to retrieve a publication annotation by publication id.
     *
     * @param ontology - the ontology to export
     * @return a publication object with fields annotated with PublicationViews in the model.
     * @since 0.5.0
     */
    @GetMapping(value = "/{ontology}")
    public void exportOntology(HttpServletResponse httpServletResponse, @PathVariable String ontology,
                               @RequestParam(name="delim", defaultValue = "tsv") String delim,
                               @RequestParam(name="unstable", defaultValue = "true") boolean unstable)
            throws IOException {

        String contentType = "text/tab-separated-values";
        String fileExtension = ".tsv";
        CSVFormat format = CSVFormat.MONGODB_TSV; 
        if(delim.equals("csv")){
            contentType = "text/csv";
            fileExtension = ".csv";
            format = CSVFormat.EXCEL;
        }

        if(ontology.equals("hpo")){
            httpServletResponse.setContentType(contentType);
            httpServletResponse.addHeader("Content-Disposition", String.format("attachment; filename=\"hpoa%s\"", fileExtension));
            exportService.exportHPOAnnotations(httpServletResponse.getWriter(), format, unstable);
        } else if(ontology.equals("maxo")){
            httpServletResponse.setContentType("text/csv");
            httpServletResponse.addHeader("Content-Disposition",String.format("attachment; filename=\"maxo%s\"", fileExtension));
            exportService.exportMAXOAnnotations(httpServletResponse.getWriter(), format, unstable);
        }
    }

    @GetMapping(value = "/release")
    public ResponseEntity<?> releaseAnnotations(@RequestParam(name="key") String key){
        if(key.equals(releasekey)){
            try {
                exportService.releaseAnnotations();
                return ResponseEntity.status(HttpStatus.OK).build();
            } catch(Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
