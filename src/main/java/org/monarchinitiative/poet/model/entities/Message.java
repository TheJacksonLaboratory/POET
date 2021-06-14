package org.monarchinitiative.poet.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Message {

    @Id
    @GeneratedValue
    private long id;

    private String value;

    @ManyToOne
    private Annotation annotation;

    public Message(){

    }

    public Message(String value, Annotation annotation) {
        this.annotation = annotation;
        this.value = value;
    }
}
