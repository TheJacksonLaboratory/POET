package org.monarchinitiative.poet.model.entities;

import org.monarchinitiative.poet.model.entities.AnnotationSource;
import javax.persistence.*;
import java.util.List;

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
    @Column(unique = true) private String identifier;
    private String name;

    @OneToMany(mappedBy = "publication")
    private List<AnnotationSource> annotationSources;

    private String date;
    private String firstAuthor;

    protected Publication(){}

    /*
     * Creates new publication.
     */
    public Publication(String id, String name, String date, String firstAuthor){
        this.identifier = id;
        this.name = name;
        this.date = date;
        this.firstAuthor = firstAuthor;
    }

    /**
     * Returns the PubMed Identifier.
     *
     * @return
     */
    public String getPublicationIdentifier() {
        return identifier;
    }

    /**
     * Returns the Publication Name.
     *
     * @return
     */
    public String getPublicationName() {
        return name;
    }

    public String getDate() {
        return date;
    }
    public String getFirstAuthor() {
        return firstAuthor;
    }
}
