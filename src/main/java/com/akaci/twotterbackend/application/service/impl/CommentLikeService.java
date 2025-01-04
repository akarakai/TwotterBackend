package com.akaci.twotterbackend.application.service.impl;

import com.akaci.twotterbackend.application.dto.response.like.LikeResponse;
import com.akaci.twotterbackend.application.service.LikeService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("comment-like-service")
public class CommentLikeService implements LikeService {

    @Override
    public LikeResponse like(String username, UUID commentId) {
        return null;
    }
}
