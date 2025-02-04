package com.akaci.twotterbackend.application.service;

import com.akaci.twotterbackend.application.dto.mapper.TwootDtoMapper;
import com.akaci.twotterbackend.application.dto.response.twoot.TwootMetadata;
import com.akaci.twotterbackend.application.dto.mapper.UserDtoMapper;
import com.akaci.twotterbackend.application.dto.request.TwootRequest;
import com.akaci.twotterbackend.application.dto.response.UserResponse;
import com.akaci.twotterbackend.application.dto.response.twoot.TwootAllResponse;
import com.akaci.twotterbackend.application.dto.response.twoot.TwootResponse;
import com.akaci.twotterbackend.exceptions.response.BadRequestExceptionResponse;
import com.akaci.twotterbackend.persistence.entity.TwootEntity;
import com.akaci.twotterbackend.persistence.entity.UserEntity;
import com.akaci.twotterbackend.persistence.repository.TwootRepository;
import com.akaci.twotterbackend.persistence.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class TwootService {

    private final TwootRepository twootRepo;
    private final UserRepository userRepo;

    private final UserDtoMapper userDtoMapper;
    private final TwootDtoMapper twootDtoMapper;

    public TwootService(TwootRepository twootRepo, UserDtoMapper userDtoMapper, TwootDtoMapper twootDtoMapper,
                        UserRepository userRepo) {
        this.twootRepo = twootRepo;
        this.userRepo = userRepo;
        this.userDtoMapper = userDtoMapper;
        this.twootDtoMapper = twootDtoMapper;
    }


    public TwootResponse getTwoot(UUID twootId) {
        TwootEntity twootEntity = twootRepo.findById(twootId)
                .orElseThrow(() -> new BadRequestExceptionResponse("Twoot does not exist"));
        UserEntity authorEntity = twootEntity.getAuthor();

        UserResponse response = userDtoMapper.toDto(authorEntity, false);
        TwootMetadata metadata = generateMetadata(twootEntity);

        log.info("Twoot metadata: {}", metadata);

        return twootDtoMapper.toDto(twootEntity, response, metadata);
    }

    public TwootResponse getTwoot(UUID twootId, String username) {
        TwootEntity twootEntity = twootRepo.findById(twootId)
                .orElseThrow(() -> new BadRequestExceptionResponse("Twoot does not exist"));
        UserEntity authorEntity = twootEntity.getAuthor();

        UserResponse response = userDtoMapper.toDto(authorEntity, isAuthorFollowedByUser(authorEntity, username));
        TwootMetadata metadata = generateMetadata(twootEntity, username);

        log.info("Twoot metadata: {}", metadata);

        return twootDtoMapper.toDto(twootEntity, response, metadata);
    }

    public TwootResponse postTwoot(TwootRequest twootRequest, String username) {
        validateRequest(twootRequest);
        UserEntity userEntity = userRepo.findByUsername(username)
                .orElseThrow(() -> new BadRequestExceptionResponse("User does not exist"));
        TwootEntity newTwoot = TwootEntity.builder()
                .content(twootRequest.content())
                .author(userEntity)
                .build();
        twootRepo.save(newTwoot);
        UserResponse response = userDtoMapper.toDto(userEntity, false);
        TwootMetadata metadata = TwootMetadata.builder()
                .likes(0)
                .likedByUser(false)
                .commentNumber(0)
                .postedAt(LocalDateTime.now())
                .build();
        return twootDtoMapper.toDto(newTwoot, response, metadata);

    }

    public TwootAllResponse getAllTwoots() {
        List<TwootEntity> allTwoots = (List<TwootEntity>) twootRepo.findAll();
        List<TwootResponse> allTwootsRes = allTwoots.stream()
                .map(twoot -> {
                    TwootMetadata metadata = generateMetadata(twoot);
                    UserEntity author = twoot.getAuthor();
                    UserResponse authorResponse = userDtoMapper.toDto(author, false);
                    return twootDtoMapper.toDto(twoot, authorResponse, metadata);
                }).toList();
        return new TwootAllResponse(
                allTwootsRes.size(),
                allTwootsRes.getFirst().getMetadata().getPostedAt(),
                allTwootsRes
        );
    }

    public TwootAllResponse getAllTwootsAuth(String username) {
        return null;
    }

    private boolean isAuthorFollowedByUser(UserEntity authorEntity, String username) {
        Set<UserEntity> followedByUser = userRepo.findFollowed(username);
        return followedByUser.contains(authorEntity);

    }


    private void validateRequest(TwootRequest twootRequest) {
        if (twootRequest.content().length() > 300) {
            throw new BadRequestExceptionResponse("Twoot content is too long");
        }

    }

    private TwootMetadata generateMetadata(TwootEntity twootEntity, String username) {
        return twootRepo.findTwootMetadataLikedByUser(twootEntity.getId(), username);
    }

    private TwootMetadata generateMetadata(TwootEntity twootEntity) {
        return twootRepo.findTwootMetadata(twootEntity.getId());

    }
}
