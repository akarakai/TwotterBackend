package com.akaci.twotterbackend.persistence.mapper;

import com.akaci.twotterbackend.domain.model.Twoot;
import com.akaci.twotterbackend.domain.model.User;
import com.akaci.twotterbackend.persistence.entity.TwootEntity;
import com.akaci.twotterbackend.persistence.entity.UserEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

public class TwootEntityMapper {

    private static final Logger LOGGER = LogManager.getLogger(TwootEntityMapper.class);

    public static Twoot toDomain(TwootEntity twootJpa) {
        Set<User> likedByUsers = UserEntityMapper.setToDomain(twootJpa.getLikedByUsers());
        User author = UserEntityMapper.toDomain(twootJpa.getAuthor());

        return Twoot.builder()
                .id(twootJpa.getId())
                .content(twootJpa.getContent())
                .author(author)
                .likedByUsers(likedByUsers)
                .postedAt(twootJpa.getPostedAt())
                .build();

    }

    public static TwootEntity toJpaEntity(Twoot twoot) {
        Set<UserEntity> likedByUserJpa = UserEntityMapper.setToJpa(twoot.getLikedByUsers());
        UserEntity authorJpa = UserEntityMapper.toJpaEntity(twoot.getAuthor());

        return TwootEntity.builder()
                .id(twoot.getId())
                .content(twoot.getContent())
                .likedByUsers(likedByUserJpa)
                .author(authorJpa)
                .postedAt(twoot.getPostedAt())
                .build();
    }






}
