package org.monarchinitiative.poet.controller;

import org.apache.commons.csv.CSVFormat;
import org.monarchinitiative.poet.service.ExportService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping(value = "${api.version}/export/")
public class ExportController {
    private final ExportService exportService;

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
    public void getPublicationById(HttpServletResponse httpServletResponse, @PathVariable String ontology, @RequestParam(name="delim", defaultValue = "tsv") String delim) throws IOException {
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
            exportService.exportHPOAnnotations(httpServletResponse.getWriter(), format);
        } else if(ontology.equals("maxo")){
            httpServletResponse.setContentType("text/csv");
            httpServletResponse.addHeader("Content-Disposition",String.format("attachment; filename=\"maxo%s\"", fileExtension));
            exportService.exportHPOAnnotations(httpServletResponse.getWriter(), format);
        }
    }

}
