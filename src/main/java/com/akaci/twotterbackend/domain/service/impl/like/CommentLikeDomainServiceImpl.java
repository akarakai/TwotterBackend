package com.akaci.twotterbackend.domain.service.impl.like;

import com.akaci.twotterbackend.domain.model.Likable;
import com.akaci.twotterbackend.domain.model.User;
import com.akaci.twotterbackend.domain.service.LikeDomainService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service("comment-like-domain-service")
public class CommentLikeDomainServiceImpl implements LikeDomainService {

    private static final Logger LOGGER = LogManager.getLogger(CommentLikeDomainServiceImpl.class);


    @Override
    public void like(User user, Likable likableComment) {
        if (user.getLikedComments().contains(likableComment)) {
            user.removeLike(likableComment);
            return;
        }

        user.like(likableComment);
    }
}
