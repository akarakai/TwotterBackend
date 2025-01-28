package com.akaci.twotterbackend.persistence.repository;

import com.akaci.twotterbackend.application.dto.response.twoot.TwootResponse;
import com.akaci.twotterbackend.persistence.entity.TwootJpaEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface TwootRepository extends CrudRepository<TwootJpaEntity, UUID> {

//    @Query("SELECT t FROM TwootJpaEntity t ORDER BY t.postedAt DESC")
//    List<TwootJpaEntity> findAllOrderedByTime();

    @Query("""
    SELECT COUNT(u)
    FROM TwootJpaEntity t
    JOIN t.likedByUsers u
    WHERE t.id = :twootId
    """)
    int findNumberOfLikes(UUID twootId);


    @Query("""
    SELECT COUNT(c)
    FROM TwootJpaEntity t
    JOIN t.comments c
    WHERE t.id = :twootId
    """)
    int findNumberOfComments(UUID twootId);

    // IL PROBLEMA E'CHE CI SERVE IL
    // LIKED BY USER< QUINDI QUESTO TOCCA TOGLIERE
    @Query("""
        SELECT new com.akaci.twotterbackend.application.dto.response.twoot.TwootResponse(
            t.id, 
            t.author.username,
            t.content,
            SIZE(t.likedByUsers),
            SIZE(t.comments),
            t.postedAt)
        FROM TwootJpaEntity t
        ORDER BY t.postedAt DESC
        """)
    List<TwootResponse> findAllTwootsWithCounts();

    @Query("""
        SELECT SIZE(t.likedByUsers) FROM TwootJpaEntity t WHERE t.id = :twootId
    """)
    Integer getNumberOfLikes(UUID twootId);

}
