package com.akaci.twotterbackend.application.service.crud.impl;

import com.akaci.twotterbackend.application.dto.response.twoot.TwootAllResponse;
import com.akaci.twotterbackend.application.dto.response.twoot.TwootResponse;
import com.akaci.twotterbackend.application.service.crud.TwootCrudService;
import com.akaci.twotterbackend.domain.commonValidator.TwootCommentValidator;
import com.akaci.twotterbackend.domain.model.Twoot;
import com.akaci.twotterbackend.domain.model.User;
import com.akaci.twotterbackend.exceptions.response.BadRequestExceptionResponse;
import com.akaci.twotterbackend.persistence.entity.TwootJpaEntity;
import com.akaci.twotterbackend.persistence.entity.UserJpaEntity;
import com.akaci.twotterbackend.persistence.mapper.TwootEntityMapper;
import com.akaci.twotterbackend.persistence.repository.TwootRepository;
import com.akaci.twotterbackend.persistence.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TwootCrudServiceImpl implements TwootCrudService {

    private static final Logger LOGGER = LogManager.getLogger(TwootCrudServiceImpl.class);

    private final TwootRepository twootRepository;
    private final UserRepository userRepository;

    public TwootCrudServiceImpl(TwootRepository twootRepository, UserRepository userRepository) {
        this.twootRepository = twootRepository;
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public Twoot postNewTwoot(String username, String content) {
        validateContent(content);
        UserJpaEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));

        TwootJpaEntity twootEntity = TwootJpaEntity.builder()
                .content(content)
                .postedAt(LocalDateTime.now())
                .author(userEntity)
                .build();

        TwootJpaEntity savedTwoot = twootRepository.save(twootEntity);
        return TwootEntityMapper.toDomain(savedTwoot);
    }

    // TODO MAYBE IT IS HEAVY
    @Override
    public TwootAllResponse getAllTwoots() {
        List<TwootResponse> allTwoots = twootRepository.findAllTwootsWithCounts();
        return new TwootAllResponse(
                allTwoots.size(),
                allTwoots.getFirst().postedAt(),
                allTwoots
        );


        // TODO TRY THIS CODE TO SEE HOW MUCH IT DESTROYS THE DATABASE
//        List<TwootJpaEntity> allTwoots = twootRepository.findAllOrderedByTime();
//        int totalTwoots = allTwoots.size();
//        LocalDateTime firstTwootPostedAt = allTwoots.getFirst().getPostedAt();
//
//        return new TwootAllResponse(
//                totalTwoots,
//                firstTwootPostedAt,
//                allTwoots.stream().map(t -> new TwootResponse(
//                        t.getId(),
//                        t.getAuthor().getUsername(),
//                        t.getContent(),
//                        t.getLikedByUsers().size(),
//                        t.getComments().size(),
//                        t.getPostedAt()
//                )).toList()
//        );

    }

    private void validateContent(String content) {
        try {
            TwootCommentValidator.validateTwootContent(content);
        } catch (IllegalArgumentException e) {
            throw new BadRequestExceptionResponse(e.getMessage());
        }
    }



}
