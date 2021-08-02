package org.monarchinitiative.poet.model.entities;

import com.fasterxml.jackson.annotation.JsonView;
import org.monarchinitiative.poet.views.AnnotationViews;

import javax.persistence.*;

@Entity
public class Message {

    @Id
    @GeneratedValue
    private long id;

    @JsonView(AnnotationViews.Simple.class)
    private String value;

    @OneToOne
    @JsonView(AnnotationViews.Simple.class)
    private User reviewer;

    @ManyToOne
    private Annotation annotation;

    public Message(){

    }

    public Message(String value, User reviewer, Annotation annotation) {
        this.value = value;
        this.annotation = annotation;
        this.reviewer = reviewer;
    }

    public User getReviewer() {
        return reviewer;
    }

    public String getValue() {
        return value;
    }

    public User getUser() {
        return reviewer;
    }
}
