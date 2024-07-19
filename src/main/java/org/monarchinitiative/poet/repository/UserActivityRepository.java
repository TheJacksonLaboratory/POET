package org.monarchinitiative.poet.repository;

import org.monarchinitiative.poet.model.entities.Annotation;
import org.monarchinitiative.poet.model.entities.UserActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserActivityRepository extends CrudRepository<UserActivity, Long>, PagingAndSortingRepository<UserActivity, Long> {
    Page<UserActivity> findUserActivityByOwnerAuthId(String authId, Pageable pageable);
    Integer countAllByAnnotation_AnnotationTypeAndOwnerAuthId(String annotation_type, String authId);
    Page<UserActivity> findUserActivityByDateTimeAfter(LocalDateTime dateTime, Pageable pageable);
    Page<UserActivity> findUserActivityByDateTimeAfterAndOwnerAuthId(LocalDateTime dateTime, String authId, Pageable pageable);

    Optional<UserActivity> findUserActivityByAnnotation(Annotation annotation);

    @Query(nativeQuery = true, value = "SELECT *, max(a.datetime) FROM user_activity  a where a.annotation_id = ? group by a.id ORDER BY a.datetime desc LIMIT 1")
    UserActivity getMostRecentDateForAnnotationActivity(@Param("id") Long id);
    @Query(nativeQuery = true, value = "SELECT *, max(a.datetime) FROM user_activity  a where a.annotation_id = ? and a.curation_action = 0 group by a.id ORDER BY a.datetime asc LIMIT 1")
    UserActivity getCreateDateForAnnotationActivity(@Param("id") Long id);
}
