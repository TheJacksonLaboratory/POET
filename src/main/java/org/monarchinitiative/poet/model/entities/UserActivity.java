package org.monarchinitiative.poet.model.entities;

import com.fasterxml.jackson.annotation.JsonView;
import org.monarchinitiative.poet.model.enumeration.CurationAction;
import org.monarchinitiative.poet.views.UserActivityViews;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @JsonView(UserActivityViews.Simple.class)
    @Column(name = "datetime", columnDefinition = "TIMESTAMP")
    private LocalDateTime localDateTime;

    @JsonView(UserActivityViews.Simple.class)
    @Enumerated(EnumType.ORDINAL)
    private CurationAction curationAction;

    protected UserActivity() {
    }

    public UserActivity(User user, CurationAction curationAction, Annotation annotation) {
        this.user = user;
        this.annotation = annotation;
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
}
