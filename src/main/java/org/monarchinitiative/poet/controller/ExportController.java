package org.monarchinitiative.poet.controller;

import org.monarchinitiative.poet.model.entities.Publication;
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
    public void getPublicationById(HttpServletResponse httpServletResponse,
                                          @PathVariable(value="ontology") String ontology) throws IOException {
        if(ontology.equals("hpo")){
            httpServletResponse.setContentType("text/csv");
            httpServletResponse.addHeader("Content-Disposition","attachment; filename=\"hpoa.csv\"");
            exportService.exportHPOAnnotations(httpServletResponse.getWriter());
        } else if(ontology.equals("maxo")){
            httpServletResponse.setContentType("text/csv");
            httpServletResponse.addHeader("Content-Disposition","attachment; filename=\"maxo.csv\"");
            exportService.exportHPOAnnotations(httpServletResponse.getWriter());
        }
    }

}
