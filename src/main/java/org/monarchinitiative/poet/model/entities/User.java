package org.monarchinitiative.poet.model.entities;

import org.monarchinitiative.poet.model.enumeration.CurationRole;

import javax.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nickname;
    @Column(unique = true, nullable = false)
    private String authId;
    private String email;
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
