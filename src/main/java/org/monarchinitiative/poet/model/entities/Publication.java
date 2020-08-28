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
    @Column(unique = true) private String publicationIdentifier;
    private String publicationName;

    @OneToMany(mappedBy = "publication")
    private List<AnnotationSource> amnotationSource;

    protected Publication(){}

    /*
     * Creates new publication.
     */
    public Publication(String id, String name){
        this.setPublicationIdentifier(id);
        this.setPublicationName(name);
    }

    /**
     * Returns the PubMed Identifier.
     *
     * @return
     */
    public String getPublicationIdentifier() {
        return publicationIdentifier;
    }

    public void setPublicationIdentifier(String identifier) {
        this.publicationIdentifier = identifier;
    }

    /**
     * Returns the Publication Name.
     *
     * @return
     */
    public String getPublicationName() {
        return publicationName;
    }

    public void setPublicationName(String publicationName) {
        this.publicationName = publicationName;
    }
}
