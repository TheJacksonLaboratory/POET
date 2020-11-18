package org.monarchinitiative.poet.model.entities;

import org.monarchinitiative.poet.model.enumeration.CurationAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class UserActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    @OneToOne
    private Annotation annotation;

    @Column(name = "datetime", columnDefinition = "TIMESTAMP")
    private LocalDateTime localDateTime;

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
