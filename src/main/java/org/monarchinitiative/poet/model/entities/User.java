package org.monarchinitiative.poet.model.entities;

import com.fasterxml.jackson.annotation.JsonView;
import org.monarchinitiative.poet.model.enumeration.CurationRole;
import org.monarchinitiative.poet.views.AnnotationViews;
import org.monarchinitiative.poet.views.UserActivityViews;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonView({UserActivityViews.Simple.class, AnnotationViews.Simple.class, AnnotationViews.UserSpecific.class})
    private String nickname;
    @Column(unique = true, nullable = false)
    private String authId;
    private String email;
    @Column(unique = true)
    private String orcid;
    @Enumerated(EnumType.ORDINAL)
    private CurationRole curationRole;

    public User(){}

    public User(String authId, String nickname, String email, CurationRole role) {
        this.authId = authId;
        this.nickname = nickname;
        this.email = email;
        this.curationRole = role;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getUniqueNickname(){
        return String.format("%s#%d", nickname, getId());
    }

    public String getAuthId() {
        return authId;
    }

    public String getEmail() {
        return email;
    }

    public String getOrcid(){
        return orcid;
    }


    public CurationRole getCurationRole() {
        return curationRole;
    }

    public void setCurationRole(CurationRole curationRole) {
        this.curationRole = curationRole == null ? CurationRole.POET_CURATOR : curationRole;
    }

    public void setOrcid(String orcid) {
        this.orcid = orcid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(nickname, user.nickname) &&
                Objects.equals(authId, user.authId) &&
                Objects.equals(email, user.email) &&
                curationRole == user.curationRole;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nickname, authId, email, curationRole);
    }
}
