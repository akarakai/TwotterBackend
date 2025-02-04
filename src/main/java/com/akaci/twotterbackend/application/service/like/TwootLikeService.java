package com.akaci.twotterbackend.application.service.like;

import com.akaci.twotterbackend.application.dto.response.like.LikeResponse;
import com.akaci.twotterbackend.application.dto.response.like.LikeStatus;
import com.akaci.twotterbackend.exceptions.response.BadRequestExceptionResponse;
import com.akaci.twotterbackend.persistence.entity.TwootEntity;
import com.akaci.twotterbackend.persistence.entity.UserEntity;
import com.akaci.twotterbackend.persistence.repository.TwootRepository;
import com.akaci.twotterbackend.persistence.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Slf4j
@Service("twoot")
public class TwootLikeService implements LikeContentService {

    private final static String LIKE_TYPE = "twoot";

    private final TwootRepository twootRepo;
    private final UserRepository userRepo;

    public TwootLikeService(TwootRepository twootRepo, UserRepository userRepo) {
        this.twootRepo = twootRepo;
        this.userRepo = userRepo;
    }


    @Override
    @Transactional
    public LikeResponse addLike(UUID twootId, String username) {
        UserEntity userEntity = getUserAlongWithLikedTwoots(username);
        TwootEntity twootEntity = getTwoot(twootId);

        Set<TwootEntity> likedTwoots = userEntity.getLikedTwoots();

        if (likedTwoots.contains(twootEntity)) throw new BadRequestExceptionResponse("Twoot already liked");

        likedTwoots.add(twootEntity);
        userEntity.setLikedTwoots(likedTwoots);

        return new LikeResponse(twootId, LIKE_TYPE, LikeStatus.ADDED);
    }

    @Override
    @Transactional
    public LikeResponse removeLike(UUID twootId, String username) {
        UserEntity userEntity = getUserAlongWithLikedTwoots(username);
        TwootEntity twootEntity = getTwoot(twootId);

        Set<TwootEntity> likedTwoots = userEntity.getLikedTwoots();

        if (!likedTwoots.contains(twootEntity)) throw new BadRequestExceptionResponse("Twoot was not already liked");

        likedTwoots.remove(twootEntity);
        userEntity.setLikedTwoots(likedTwoots);

        return new LikeResponse(twootId, LIKE_TYPE, LikeStatus.REMOVED);
    }

    private UserEntity getUserAlongWithLikedTwoots(String username) {
        return userRepo.findUserWithTwootLiked(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private TwootEntity getTwoot(UUID twootId) {
        return twootRepo.findById(twootId)
                .orElseThrow(() -> new RuntimeException("Twoot not found"));
    }


}
