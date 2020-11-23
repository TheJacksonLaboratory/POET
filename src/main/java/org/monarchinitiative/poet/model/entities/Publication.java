package org.monarchinitiative.poet.model.entities;

import com.fasterxml.jackson.annotation.JsonView;
import org.monarchinitiative.poet.model.entities.AnnotationSource;
import org.monarchinitiative.poet.views.DiseaseViews;
import org.monarchinitiative.poet.views.PublicationViews;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

/***
 * Publication Entity
 *
 * @author Michael Gargano
 */
@Entity
public class Publication {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JsonView(PublicationViews.Simple.class)
    @Column(unique = true) private String publicationId;

    @JsonView(PublicationViews.Simple.class)
    private String publicationName;

    @OneToMany(mappedBy = "publication")
    private List<AnnotationSource> annotationSources;

    @JsonView(PublicationViews.Simple.class)
    private String date;

    @JsonView(PublicationViews.Simple.class)
    private String firstAuthor;

    protected Publication(){}

    /*
     * Creates new publication.
     */
    public Publication(String publicationId, String publicationName, String date, String firstAuthor){
        this.publicationId = publicationId;
        this.publicationName = publicationName;
        this.date = date;
        this.firstAuthor = firstAuthor;
    }

    /**
     * Returns the PubMed Identifier.
     *
     * @return
     */
    public String getPublicationIdentifier() {
        return publicationId;
    }

    /**
     * Returns the Publication Name.
     *
     * @return
     */
    public String getPublicationName() {
        return publicationName;
    }

    public String getDate() {
        return date;
    }

    public String getFirstAuthor() {
        return firstAuthor;
    }

    public List<Disease> getDiseases(){
        return this.annotationSources.stream().map(AnnotationSource::getDisease).collect(Collectors.toList());
    }
}