package com.akaci.twotterbackend.application.service.crud;

import com.akaci.twotterbackend.application.dto.response.comment.CommentsOfTwootResponse;
import com.akaci.twotterbackend.domain.model.Comment;

import java.util.UUID;

public interface CommentCrudService {

    Comment postNewComment(String author, String content, UUID twootId);
    CommentsOfTwootResponse getCommentsOfTwoot(UUID twootId);
    CommentsOfTwootResponse getCommentsOfTwoot(UUID twootId, String user);

}
