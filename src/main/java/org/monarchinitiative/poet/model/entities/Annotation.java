package org.monarchinitiative.poet.model.entities;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import org.monarchinitiative.poet.model.enumeration.AnnotationStatus;
import org.monarchinitiative.poet.views.AnnotationViews;
import org.monarchinitiative.poet.views.UserActivityViews;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="annotation_type",
        discriminatorType = DiscriminatorType.STRING)
public class Annotation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView({AnnotationViews.Simple.class, UserActivityViews.Simple.class, AnnotationViews.UserSpecific.class})
    private long id;

    @ManyToOne
    @JsonView({AnnotationViews.Simple.class, UserActivityViews.Simple.class, AnnotationViews.UserSpecific.class})
    private AnnotationSource annotationSource;

    @JsonView({AnnotationViews.Simple.class, AnnotationViews.UserSpecific.class})
    @Enumerated(EnumType.STRING)
    private AnnotationStatus status;

    @JsonView(UserActivityViews.Simple.class)
    @Column(name="annotation_type", insertable = false, updatable = false)
    protected String annotationType;

    @ManyToOne
    private Version version;

    @OneToMany
    @JsonView(AnnotationViews.Simple.class)
    private List<Message> reviewMessages;

    @Transient
    @JsonInclude()
    @JsonView({AnnotationViews.Simple.class, AnnotationViews.UserSpecific.class})
    private LocalDateTime lastUpdatedDate;

    @OneToOne
    @JsonView({AnnotationViews.Simple.class, AnnotationViews.UserSpecific.class})
    private User owner;

    public Annotation(){}

    public Annotation(AnnotationSource annotationSource, AnnotationStatus status) {
        this.annotationSource = annotationSource;
        this.status = status;
    }

    public Annotation(AnnotationSource annotationSource, AnnotationStatus status, User user) {
        this.annotationSource = annotationSource;
        this.status = status;
        this.owner = user;
    }

    public AnnotationSource getAnnotationSource() {
        return annotationSource;
    }

    public void setAnnotationSource(AnnotationSource annotationSource) {
        this.annotationSource = annotationSource;
    }

    public AnnotationStatus getStatus() {
        return status;
    }

    public void setStatus(AnnotationStatus status) {
        this.status = status;
    }

    public void setVersion(Version version){this.version = version;}

    public String getAnnotationType() {
        return annotationType;
    }

    public long getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public List<Message> getReviewMessages() {
        return reviewMessages;
    }

    public LocalDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public void newMessage(Message message){
        this.reviewMessages.add(message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Annotation that = (Annotation) o;
        return id == that.id &&
                Objects.equals(annotationSource, that.annotationSource) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, annotationSource, status);
    }

    @Override
    public String toString() {
        return "Annotation{" +
                "id=" + id +
                ", annotationSource=" + annotationSource +
                ", status=" + status +
                '}';
    }
}

