package org.monarchinitiative.poet.repository;

import org.monarchinitiative.poet.model.entities.UserActivity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserActivityRepository extends CrudRepository<UserActivity, Long> {
    List<UserActivity> findUserActivityByUserAuthId(String authId);
    Integer countAllByAnnotation_AnnotationTypeAndUserAuthId(String annotation_type, String authId);
    @Query(value = "SELECT a, max(a.localDateTime) FROM UserActivity a where a.annotation.id = :id")
    UserActivity getMostRecentDateForAnnotationActivity(@Param("id") Long id);

}