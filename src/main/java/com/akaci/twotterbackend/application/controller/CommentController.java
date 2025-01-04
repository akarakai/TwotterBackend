package com.akaci.twotterbackend.application.controller;

import com.akaci.twotterbackend.application.service.LikeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class CommentController {

    private static final Logger logger = LogManager.getLogger(CommentController.class);

    private final LikeService commentLikeService;

    public CommentController(@Qualifier("comment-like-service") LikeService likeService) {
        this.commentLikeService = likeService;
    }

}
