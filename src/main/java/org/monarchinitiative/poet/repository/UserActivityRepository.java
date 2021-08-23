package org.monarchinitiative.poet.repository;

import org.monarchinitiative.poet.model.entities.UserActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;


import java.time.LocalDateTime;

public interface UserActivityRepository extends PagingAndSortingRepository<UserActivity, Long>, CrudRepository<UserActivity, Long> {
    Page<UserActivity> findUserActivityByOwnerAuthId(String authId, Pageable pageable);
    Integer countAllByAnnotation_AnnotationTypeAndOwnerAuthId(String annotation_type, String authId);
    Page<UserActivity> findUserActivityByLocalDateTimeAfter(LocalDateTime dateTime, Pageable pageable);
    Page<UserActivity> findUserActivityByLocalDateTimeAfterAndOwnerAuthId(LocalDateTime dateTime, String authId, Pageable pageable);
    @Query(nativeQuery = true, value = "SELECT *, max(a.datetime) FROM user_activity  a where a.annotation_id = ? group by a.id ORDER BY a.datetime desc LIMIT 1")
    UserActivity getMostRecentDateForAnnotationActivity(@Param("id") Long id);
}
