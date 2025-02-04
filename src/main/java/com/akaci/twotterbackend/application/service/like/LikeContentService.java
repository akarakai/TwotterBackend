package com.akaci.twotterbackend.application.service.like;

import com.akaci.twotterbackend.application.dto.response.like.LikeResponse;
import java.util.UUID;


public interface LikeContentService {

    LikeResponse addLike(UUID contentId, String username);
    LikeResponse removeLike(UUID contentId, String username);

}
