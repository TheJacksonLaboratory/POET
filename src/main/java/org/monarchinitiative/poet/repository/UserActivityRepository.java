package org.monarchinitiative.poet.repository;

import org.monarchinitiative.poet.model.entities.UserActivity;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface UserActivityRepository extends CrudRepository<UserActivity, Long> {
    List<UserActivity> findUserActivityByUserAuthId(String authId);
    Integer countAllByAnnotation_AnnotationTypeAndUserAuthId(String annotation_type, String authId);
}
