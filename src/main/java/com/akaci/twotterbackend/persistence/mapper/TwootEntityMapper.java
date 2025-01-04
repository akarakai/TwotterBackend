package com.akaci.twotterbackend.persistence.mapper;

import com.akaci.twotterbackend.domain.model.Twoot;
import com.akaci.twotterbackend.domain.model.User;
import com.akaci.twotterbackend.persistence.entity.TwootJpaEntity;
import com.akaci.twotterbackend.persistence.entity.UserJpaEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

public class TwootEntityMapper {

    private static final Logger LOGGER = LogManager.getLogger(TwootEntityMapper.class);

    public static Twoot toDomain(TwootJpaEntity twootJpa) {
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

    public static TwootJpaEntity toJpaEntity(Twoot twoot) {
        Set<UserJpaEntity> likedByUserJpa = UserEntityMapper.setToJpa(twoot.getLikedByUsers());
        UserJpaEntity authorJpa = UserEntityMapper.toJpaEntity(twoot.getAuthor());

        return TwootJpaEntity.builder()
                .id(twoot.getId())
                .content(twoot.getContent())
                .likedByUsers(likedByUserJpa)
                .author(authorJpa)
                .postedAt(twoot.getPostedAt())
                .build();
    }






}
