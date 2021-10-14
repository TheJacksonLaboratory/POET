package org.monarchinitiative.poet.repository;

import org.monarchinitiative.poet.model.entities.Version;
import org.springframework.data.repository.CrudRepository;

public interface VersionRepository extends CrudRepository<Version, Long> {

}
