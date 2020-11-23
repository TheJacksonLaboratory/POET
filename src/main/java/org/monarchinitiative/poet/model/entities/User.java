package org.monarchinitiative.poet.model.entities;

import com.fasterxml.jackson.annotation.JsonView;
import org.monarchinitiative.poet.model.enumeration.CurationRole;
import org.monarchinitiative.poet.views.UserActivityViews;

import javax.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonView(UserActivityViews.Simple.class)
    @Column(unique = true, nullable = false)
    private String nickname;
    @Column(unique = true, nullable = false)
    private String authId;
    private String email;
    @JsonView(UserActivityViews.Simple.class)
    @Column(unique = true)
    private String orcId;

    @Enumerated(EnumType.ORDINAL)
    private CurationRole curationRole;

    public User(){}

    public User(String authId, String nickname, String email, String orcId, CurationRole role) {
        this.authId = authId;
        this.nickname = nickname;
        this.email = email;
        this.orcId = orcId;
        this.curationRole = role;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAuthId() {
        return authId;
    }

    public String getEmail() {
        return email;
    }

    public String getOrcId() {
        return orcId;
    }
}
