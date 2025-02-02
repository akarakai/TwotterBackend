package com.akaci.twotterbackend.persistence.mapper;

import com.akaci.twotterbackend.domain.model.Comment;
import com.akaci.twotterbackend.domain.model.Twoot;
import com.akaci.twotterbackend.domain.model.User;
import com.akaci.twotterbackend.persistence.entity.CommentEntity;
import com.akaci.twotterbackend.persistence.entity.TwootEntity;
import com.akaci.twotterbackend.persistence.entity.UserEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

public class CommentEntityMapper {

    private static final Logger LOGGER = LogManager.getLogger(CommentEntityMapper.class);

    public static Comment toDomain(CommentEntity commentJpa) {
        Set<User> likedByUsers = UserEntityMapper.setToDomain(commentJpa.getLikedByUsers());
        User author = UserEntityMapper.toDomain(commentJpa.getAuthor());
        Twoot twoot = TwootEntityMapper.toDomain(commentJpa.getTwoot());
        return Comment.builder()
                .id(commentJpa.getId())
                .content(commentJpa.getContent())
                .author(author)
                .likedByUsers(likedByUsers)
                .twoot(twoot)
                .build();
    }

    public static CommentEntity toJpaEntity(Comment comment) {
        Set<UserEntity> likedByUsers = UserEntityMapper.setToJpa(comment.getLikedByUsers());
        UserEntity user = UserEntityMapper.toJpaEntity(comment.getAuthor());
        TwootEntity twoot = TwootEntityMapper.toJpaEntity(comment.getTwoot());
        return CommentEntity.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .likedByUsers(likedByUsers)
                .twoot(twoot)
                .author(user)
                .build();
    }
}
