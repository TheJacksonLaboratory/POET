package org.monarchinitiative.poet.repository;

import org.monarchinitiative.poet.model.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findDistinctByAuthId(String authId);
}
