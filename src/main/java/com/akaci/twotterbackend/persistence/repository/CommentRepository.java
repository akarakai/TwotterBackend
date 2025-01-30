package com.akaci.twotterbackend.persistence.repository;

import com.akaci.twotterbackend.application.dto.response.comment.CommentResponse;
import com.akaci.twotterbackend.domain.model.Comment;
import com.akaci.twotterbackend.persistence.entity.CommentJpaEntity;
import com.akaci.twotterbackend.persistence.entity.TwootJpaEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface CommentRepository extends CrudRepository<CommentJpaEntity, UUID> {

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
    SELECT c FROM CommentJpaEntity c
    WHERE c.twoot.id = :twootId
""")
    Set<CommentJpaEntity> findByTwoot(UUID twootId);

    @Query("""
       SELECT c FROM CommentJpaEntity c
       WHERE c.author.username = :username
    """)

    Set<CommentJpaEntity> findByUser(String username);


    @Query("""
    SELECT c FROM CommentJpaEntity c
    JOIN c.likedByUsers u
    WHERE u.username = :username
    """)
    Set<CommentJpaEntity> findLikedByUser(String username);
}
