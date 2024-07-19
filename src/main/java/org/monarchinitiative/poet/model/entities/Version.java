package org.monarchinitiative.poet.model.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
public class Version {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition = "TIMESTAMP")
    private final LocalDateTime version;

    public Version() {
        this.version = null;
    }

    public Version(LocalDateTime version) {
        this.version = version;
    }

    public LocalDateTime getVersion() {
        return version;
    }
}
