package com.akaci.twotterbackend.application.service.crud.impl;

import com.akaci.twotterbackend.application.dto.response.UserResponse;
import com.akaci.twotterbackend.application.dto.response.twoot.TwootAllResponse;
import com.akaci.twotterbackend.application.dto.response.twoot.TwootResponse;
import com.akaci.twotterbackend.application.service.crud.TwootCrudService;
import com.akaci.twotterbackend.application.service.crud.UserCrudService;
import com.akaci.twotterbackend.domain.commonValidator.TwootCommentValidator;
import com.akaci.twotterbackend.domain.model.Twoot;
import com.akaci.twotterbackend.exceptions.response.BadRequestExceptionResponse;
import com.akaci.twotterbackend.persistence.entity.TwootEntity;
import com.akaci.twotterbackend.persistence.entity.UserEntity;
import com.akaci.twotterbackend.persistence.mapper.TwootEntityMapper;
import com.akaci.twotterbackend.persistence.repository.TwootRepository;
import com.akaci.twotterbackend.persistence.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;


// replace repos with services.
@Service
public class TwootCrudServiceImpl implements TwootCrudService {

    private static final Logger LOGGER = LogManager.getLogger(TwootCrudServiceImpl.class);

    private final TwootRepository twootRepository;
    private final UserRepository userRepository;
    private final UserCrudService userCrudService;

    public TwootCrudServiceImpl(TwootRepository twootRepository, UserRepository userRepository, UserCrudService userCrudService) {
        this.twootRepository = twootRepository;
        this.userRepository = userRepository;
        this.userCrudService = userCrudService;
    }


    @Override
    @Transactional
    public Twoot postNewTwoot(String username, String content) {
        validateContent(content);
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));

        TwootEntity twootEntity = TwootEntity.builder()
                .content(content)
                .postedAt(LocalDateTime.now())
                .author(userEntity)
                .build();

        TwootEntity savedTwoot = twootRepository.save(twootEntity);
        return TwootEntityMapper.toDomain(savedTwoot);
    }

    // TODO MAYBE IT IS HEAVY
    @Override
    public TwootAllResponse getAllTwoots() {
        List<TwootEntity> allTwoots = twootRepository.findAllByOrderByPostedAtDesc();
//        List<TwootResponse> twoots = allTwoots.stream()
//                .map(twJpa -> new TwootResponse(
//                       twJpa.getId(),
//                       new UserResponse(twJpa.getAuthor().getId(), twJpa.getAuthor().getUsername(), false),
//                       twJpa.getContent(),
//                       twJpa.getLikedByUsers().size(),
//                       twJpa.getComments().size(),
//                       twJpa.getPostedAt(),
//                       false
//                )).toList();
        return null;

    }

    @Override
    public TwootResponse getTwoot(UUID id, String username) {
        Optional<TwootEntity> twootEntity = twootRepository.findById(id);
        Set<UserEntity> followed = userRepository.findFollowed(username);
        if (twootEntity.isEmpty()) throw new BadRequestExceptionResponse("Twoot not found");
        TwootEntity twootJpaEntity = twootEntity.get();
        Set<UserEntity> usersWhoLikedTwoot = twootJpaEntity.getLikedByUsers();
        boolean isLikedByUser = usersWhoLikedTwoot.stream().anyMatch(usr -> usr.getUsername().equals(username));
//        return new TwootResponse(
//                twootJpaEntity.getId(),
//                new UserResponse(twootJpaEntity.getAuthor().getId(), twootJpaEntity.getAuthor().getUsername(), isAuthorFollowed(twootJpaEntity.getAuthor().getId(), followed)),
//                twootJpaEntity.getContent(),
//                twootJpaEntity.getLikedByUsers().size(),
//                twootJpaEntity.getComments().size(),
//                twootJpaEntity.getPostedAt(),
//                isLikedByUser
//        );
        return null;

    }

    @Override
    public TwootResponse getTwoot(UUID id) {
        Optional<TwootEntity> twootEntity = twootRepository.findById(id);
        if (twootEntity.isEmpty()) throw new BadRequestExceptionResponse("TWOOT not found");
        TwootEntity twootJpaEntity = twootEntity.get();
//        return new TwootResponse(
//            twootJpaEntity.getId(),
//            new UserResponse(twootJpaEntity.getAuthor().getId(), twootJpaEntity.getAuthor().getUsername(), false),
//            twootJpaEntity.getContent(),
//            twootJpaEntity.getLikedByUsers().size(),
//            twootJpaEntity.getComments().size(),
//            twootJpaEntity.getPostedAt(),
//            false
//        );
        return null;
    }

    @Override
    public TwootAllResponse getAllTwoots(String username) {
        UUID userId = userCrudService.findByUsername(username).getId();
        Set<UserEntity> followed = userRepository.findFollowed(username);
        List<TwootEntity> allTwoots = twootRepository.findAllByOrderByPostedAtDesc();
        Set<UUID> likedByUser = twootRepository.findLikedTwootsIdByUserId(userId);
//        List<TwootResponse> allTwootsWithLiked = allTwoots.stream()
//                .map(twoot -> new TwootResponse(
//                        twoot.getId(),
//                        new UserResponse(twoot.getAuthor().getId(), twoot.getAuthor().getUsername(), isAuthorFollowed(twoot.getAuthor().getId(), followed)),
//                        twoot.getContent(),
//                        twoot.getLikedByUsers().size(),
//                        twoot.getComments().size(),
//                        twoot.getPostedAt(),
//                        likedByUser.contains(twoot.getId())
//                ))
//                .toList();

//        return new TwootAllResponse(
//                allTwootsWithLiked.size(),
//                allTwoots.getFirst().getPostedAt(),
//                allTwootsWithLiked
//        );
        return null;
    }

    private boolean isAuthorFollowed(UUID authorId, Set<UserEntity> followed) {
        return followed.stream().anyMatch(usr -> usr.getId().equals(authorId));

    }

    private void validateContent(String content) {
        try {
            TwootCommentValidator.validateTwootContent(content);
        } catch (IllegalArgumentException e) {
            throw new BadRequestExceptionResponse(e.getMessage());
        }
    }



}
