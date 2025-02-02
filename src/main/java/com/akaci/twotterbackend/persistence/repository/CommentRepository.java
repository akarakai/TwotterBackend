package com.akaci.twotterbackend.persistence.repository;

import com.akaci.twotterbackend.persistence.entity.CommentEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface CommentRepository extends CrudRepository<CommentEntity, UUID> {

//    @Query("""
//    SELECT new com.akaci.twotterbackend.application.dto.response.comment.CommentResponse(
//        c.twoot.id,
//        c.id,
//        c.author.username,
//        c.content,
//        SIZE(c.likedByUsers),
//        c.postedAt,
//        false
//    )
//    FROM CommentJpaEntity c
//        WHERE c.twoot.id = :twootId
//    ORDER BY c.postedAt DESC
//    """)
//    List<CommentResponse> findAllCommentsOfTwoot(UUID twootId);


//    @Query("""
//    SELECT new com.akaci.twotterbackend.application.dto.response.comment.CommentResponse(
//        c.twoot.id,
//        c.id,
//        c.author.username,
//        c.content,
//        SIZE(c.likedByUsers),
//        c.postedAt,
//        CASE WHEN :username IN (SELECT u.username FROM c.likedByUsers u) THEN true ELSE false END
//    )
//    FROM CommentJpaEntity c
//        WHERE c.twoot.id = :twootId
//    ORDER BY c.postedAt DESC
//    """)
//    List<CommentResponse> findAllCommentsOfTwoot(UUID twootId, String username);

    @Query("""
    SELECT c FROM CommentEntity c
    WHERE c.twoot.id = :twootId
""")
    Set<CommentEntity> findByTwoot(UUID twootId);

    @Query("""
       SELECT c FROM CommentEntity c
       WHERE c.author.username = :username
    """)

    Set<CommentEntity> findByUser(String username);


    @Query("""
    SELECT c FROM CommentEntity c
    JOIN c.likedByUsers u
    WHERE u.username = :username
    """)
    Set<CommentEntity> findLikedByUser(String username);
}
