package org.monarchinitiative.poet.repository;

import org.monarchinitiative.poet.model.entities.User;
import org.monarchinitiative.poet.model.entities.UserActivity;
import org.springframework.data.repository.CrudRepository;

public interface UserActivityRepository extends CrudRepository<UserActivity, Long> {
    User findUserActivityByUserAuthId(String authId);
}
