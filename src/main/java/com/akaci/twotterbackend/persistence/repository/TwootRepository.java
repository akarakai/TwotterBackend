package com.akaci.twotterbackend.persistence.repository;

import com.akaci.twotterbackend.application.dto.response.twoot.TwootMetadata;
import com.akaci.twotterbackend.persistence.entity.TwootEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface TwootRepository extends CrudRepository<TwootEntity, UUID> {

//    @Query("SELECT t FROM TwootJpaEntity t ORDER BY t.postedAt DESC")
//    List<TwootJpaEntity> findAllOrderedByTime();

    List<TwootEntity> findAllByOrderByPostedAtDesc();

    @Query("""
    SELECT COUNT(u)
    FROM TwootEntity t
    JOIN t.likedByUsers u
    WHERE t.id = :twootId
    """)
    int findNumberOfLikes(UUID twootId);


    @Query("""
    SELECT COUNT(c)
    FROM TwootEntity t
    JOIN t.comments c
    WHERE t.id = :twootId
    """)
    int findNumberOfComments(UUID twootId);


//    @Query("""
//        SELECT new com.akaci.twotterbackend.application.dto.response.twoot.TwootResponse(
//            t.id,
//            t.author.username,
//            t.content,
//            SIZE(t.likedByUsers),
//            SIZE(t.comments),
//            t.postedAt, false)
//        FROM TwootJpaEntity t
//        ORDER BY t.postedAt DESC
//        """)
//    List<TwootResponse> findAllTwootsWithCounts();
//
//    @Query("""
//
//            SELECT new com.akaci.twotterbackend.application.dto.response.twoot.TwootResponse(
//            t.id,
//            t.author.username,
//            t.content,
//            SIZE(t.likedByUsers),
//            SIZE(t.comments),
//            t.postedAt,
//            CASE WHEN :userId IN (SELECT u.id FROM t.likedByUsers u) THEN true ELSE false END)
//        FROM TwootJpaEntity t
//        ORDER BY t.postedAt DESC
//        """)
//    List<TwootResponse> findAllTwootsWithCountsAndLikedByUser(UUID userID);

    @Query("""
        SELECT SIZE(t.likedByUsers) FROM TwootEntity t WHERE t.id = :twootId
    """)
    Integer getNumberOfLikes(UUID twootId);

    @Query("""
        SELECT t.id FROM UserEntity u 
        JOIN u.likedTwoots t 
        WHERE u.id = :userId
    """)
    Set<UUID> findLikedTwootsIdByUserId(UUID userId);


    @Query("""
    SELECT t FROM TwootEntity t
    JOIN t.likedByUsers u
    WHERE u.username = :username
    """)
    Set<TwootEntity> findTwootsLikedByUser(String username);

    @Query("""
    SELECT new com.akaci.twotterbackend.application.dto.response.twoot.TwootMetadata(
    SIZE(t.likedByUsers),
    SIZE(t.comments),
    t.postedAt,
    false)
    FROM TwootEntity t
    WHERE t.id = :twootId
    """)
    TwootMetadata findTwootMetadata(UUID twootId);

    @Query("""
    SELECT new com.akaci.twotterbackend.application.dto.response.twoot.TwootMetadata(
    SIZE(t.likedByUsers),
    SIZE(t.comments),
    t.postedAt,
    CASE WHEN :username IN (SELECT u.username FROM t.likedByUsers u) THEN true ELSE false END )
    FROM TwootEntity t
    WHERE t.id = :twootId
    """)
    TwootMetadata findTwootMetadataLikedByUser(UUID twootId, String username);

}
