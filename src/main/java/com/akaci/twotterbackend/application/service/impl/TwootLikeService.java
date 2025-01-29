package com.akaci.twotterbackend.application.service.impl;

import com.akaci.twotterbackend.application.dto.response.like.LikeResponse;
import com.akaci.twotterbackend.application.dto.response.like.LikeStatus;
import com.akaci.twotterbackend.application.service.LikeService;
import com.akaci.twotterbackend.domain.commonValidator.UsernameValidator;
import com.akaci.twotterbackend.domain.model.Twoot;
import com.akaci.twotterbackend.domain.model.User;
import com.akaci.twotterbackend.domain.service.LikeDomainService;
import com.akaci.twotterbackend.exceptions.response.BadRequestExceptionResponse;
import com.akaci.twotterbackend.persistence.entity.TwootJpaEntity;
import com.akaci.twotterbackend.persistence.entity.UserJpaEntity;
import com.akaci.twotterbackend.persistence.mapper.UserEntityMapper;
import com.akaci.twotterbackend.persistence.repository.TwootRepository;
import com.akaci.twotterbackend.persistence.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Service("twoot-like-service")
public class TwootLikeService implements LikeService {

    private static final String CONTENT_TYPE_TO_LIKE = "twoot";

    private final LikeDomainService likeDomainService;
    private final UserRepository userRepository;
    private final TwootRepository twootRepository;

    public TwootLikeService(UserRepository userRepository, TwootRepository twootRepository,
                            @Qualifier("twoot-like-domain-service") LikeDomainService likeDomainService) {
        this.userRepository = userRepository;
        this.twootRepository = twootRepository;
        this.likeDomainService = likeDomainService;
    }

    @Override
    @Transactional
    public LikeResponse like(String username, UUID twootId) {
        UserJpaEntity userEntity = getUserEntity(username);
        TwootJpaEntity twootEntity = getTwootToLike(twootId);

        User user = mapToUserModel(userEntity);
        Twoot twoot = mapToTwootModel(twootEntity);

//        int initialLikeCount = userEntity.getLikedTwoots().size();
        int initialLikeCount = twoot.getLikedByUsers().size();

        likeDomainService.like(user, twoot);

        // in teoria su should devo controllare twoot, perche e'lui che e'cambiato
        if (shouldUpdateLike(twoot, initialLikeCount)) {
            return addLike(userEntity, twootEntity);
        }

        return removeLike(userEntity, twootEntity);
    }

    private User mapToUserModel(UserJpaEntity userEntity) {

        Set<Twoot> likedTwoots = userEntity.getLikedTwoots().stream()
                .map(t -> Twoot.builder()
                        .id(t.getId())
                        .build())
                .collect(Collectors.toSet());

        return User.builder()
                .id(userEntity.getId())
                .likedTwoots(likedTwoots)
                .build();
    }

    private Twoot mapToTwootModel(TwootJpaEntity twootEntity) {
        return Twoot.builder()
                .id(twootEntity.getId())
                .likedByUsers(UserEntityMapper.setToDomain(twootEntity.getLikedByUsers()))
                .build();
    }

    private boolean shouldUpdateLike(Twoot twoot, int initialLikeCount) {
        return initialLikeCount < twoot.getLikedByUsers().size() || initialLikeCount == 0;
    }

    private LikeResponse addLike(UserJpaEntity userEntity, TwootJpaEntity twootEntity) {
        userEntity.getLikedTwoots().add(twootEntity);
        userRepository.save(userEntity);
        return new LikeResponse(twootEntity.getId().toString(), CONTENT_TYPE_TO_LIKE, LikeStatus.ADDED);
    }

    private LikeResponse removeLike(UserJpaEntity userEntity, TwootJpaEntity twootEntity) {
        userEntity.getLikedTwoots().remove(twootEntity);
        userRepository.save(userEntity);
        return new LikeResponse(twootEntity.getId().toString(), CONTENT_TYPE_TO_LIKE, LikeStatus.REMOVED);
    }

    private UserJpaEntity getUserEntity(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestExceptionResponse("User not found: " + username));
    }

    private TwootJpaEntity getTwootToLike(UUID twootId) {
        return twootRepository.findById(twootId)
                .orElseThrow(() -> new BadRequestExceptionResponse("Twoot not found: " + twootId));
    }
}












//@Service("twoot-like-service")
//public class TwootLikeService implements LikeService  {
//
//    private static final Logger LOGGER = LogManager.getLogger(TwootLikeService.class);
//
//    private static final String CONTENT_TYPE_TO_LIKE = "twoot";
//
//    private final LikeDomainService likeDomainService;
//
//    private final UserRepository userRepository;
//    private final TwootRepository twootRepository;
//
//    public TwootLikeService(UserRepository userRepository, TwootRepository twootRepository,
//                            @Qualifier("twoot-like-domain-service") LikeDomainService likeDomainService) {
//        this.userRepository = userRepository;
//        this.twootRepository = twootRepository;
//        this.likeDomainService = likeDomainService;
//    }
//
//    @Override
//    @Transactional
//    public LikeResponse like(String username, UUID twootId) {
//        UserJpaEntity userEntity = getUserEntity(username);
//        TwootJpaEntity twootEntity = getTwootToLike(twootId);
//
//        Set<UserJpaEntity> likedByUsers = twootEntity.getLikedByUsers();
//        Set<User> likedNyUsersModel = likedByUsers.stream().map(u -> User.builder()
//                .id(u.getId())
//                .build()).collect(Collectors.toSet());
//
//        Twoot twoot = Twoot.builder()
//                .id(twootId)
////                .likedByUsers(likedNyUsersModel) // this have many users, better not to load
//                .build();
//
//        Set<TwootJpaEntity> likedTwootsByUserEntity = userEntity.getLikedTwoots();
//        Set<Twoot> likedTwootsOfUser = likedTwootsByUserEntity.stream().map(t -> Twoot.builder()
//                .id(t.getId())
//                .build()).collect(Collectors.toSet());
//
//
//        User user = User.builder()
//                .id(userEntity.getId())
//                .likedTwoots(likedTwootsOfUser)
//                .build();
//
//        int likedTwootsNumber = user.getLikedTwoots().size();
//        likeDomainService.like(user, twoot);
//
//        if (likedTwootsNumber < userEntity.getLikedTwoots().size() || likedTwootsNumber == 0) {
//            // liked successfully, persist the changes
//
//            // add the user to the liked in the twoot
//            userEntity.getLikedTwoots().add(twootEntity);
//            userRepository.save(userEntity);
//            return new LikeResponse(twootId.toString(), CONTENT_TYPE_TO_LIKE, LikeStatus.ADDED);
//
//        }
//
//        return new LikeResponse(twootId.toString(), CONTENT_TYPE_TO_LIKE, LikeStatus.REMOVED);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//// DA CANCELLARE QUESTO SOTTO
////        // A post (twoot) can be liked by millions of users. But a user can like many less posts
////        // So it makes sense to get the twoots liked by the user, and then if the found twoot is not
////        // inside, I like it, otherwise if its inside, I dislike it
////        Set<TwootJpaEntity> twootsLikedByTheUser = userEntity.getLikedTwoots();
////
////        // Convert to domain models so that we can modify them with business logic
////        // I need only the Id in this case
////        User user = User.builder()
////                .id(userEntity.getId())
////                .likedTwoots(twootsLikedByTheUser.stream().map(t -> Twoot.builder()
////                        .id(twootEntity.getId())
////                                .author(UserEntityMapper.toDomain(t.getAuthor()))
////                        .build())
////                        .collect(Collectors.toSet()))
////                .build();
////        Twoot twoot = TwootEntityMapper.toDomain(twootEntity);
////
////        int numberOfLikedTwootsBefore = twoot.getLikedByUsers().size();
////        // apply business logic to like
////        likeDomainService.like(user, twoot);
////
////        // update database if likes changed.
////        TwootJpaEntity twootJpa = TwootEntityMapper.toJpaEntity(twoot);
////        twootJpa.getLikedByUsers().add(userEntity);
////        twootRepository.save(twootJpa);
////
////        // how to handle if update, not updated?
////        if (numberOfLikedTwootsBefore == twoot.getLikedByUsers().size()) {
////            return new LikeResponse(twootId, CONTENT_TYPE_TO_LIKE, false);
////        }
////
////        twootRepository.save(twootJpa);
////        return new LikeResponse(twootId, CONTENT_TYPE_TO_LIKE, true);
//
//
//    }
//
//    private UserJpaEntity getUserEntity(String username) {
//        try {
//            UsernameValidator.validate(username);
//        } catch (IllegalArgumentException e) {
//            throw new UsernameNotFoundException(e.getMessage());
//        }
//
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
//    }
//
//    private TwootJpaEntity getTwootToLike(UUID twootId) {
//        return twootRepository.findById(twootId)
//                .orElseThrow(() -> new BadRequestExceptionResponse("twoot not found"));
//    }
//}
