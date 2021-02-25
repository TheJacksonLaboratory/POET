package org.monarchinitiative.poet.model.entities;

import com.fasterxml.jackson.annotation.JsonView;
import org.monarchinitiative.poet.model.enumeration.CurationAction;
import org.monarchinitiative.poet.views.UserActivityViews;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class UserActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonView(UserActivityViews.Simple.class)
    @ManyToOne
    private User user;

    @JsonView(UserActivityViews.Simple.class)
    @OneToOne
    private Annotation annotation;

    @OneToOne
    private Annotation oldAnnotation;

    @JsonView(UserActivityViews.Simple.class)
    @Column(name = "datetime", columnDefinition = "TIMESTAMP")
    private LocalDateTime localDateTime;

    @JsonView(UserActivityViews.Simple.class)
    @Enumerated(EnumType.ORDINAL)
    private CurationAction curationAction;

    protected UserActivity() {
    }

    public UserActivity(User user, CurationAction curationAction, Annotation annotation, Annotation oldAnnotation) {
        this.user = user;
        this.annotation = annotation;
        this.oldAnnotation = oldAnnotation;
        this.curationAction = curationAction;
        this.localDateTime = LocalDateTime.now();
    }

    public User getUser() {
        return user;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserActivity that = (UserActivity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(user, that.user) &&
                Objects.equals(annotation, that.annotation) &&
                Objects.equals(localDateTime, that.localDateTime) &&
                curationAction == that.curationAction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, annotation, localDateTime, curationAction);
    }
}
