package com.akaci.twotterbackend.domain.service.impl.like;

import com.akaci.twotterbackend.domain.model.Likable;
import com.akaci.twotterbackend.domain.model.Twoot;
import com.akaci.twotterbackend.domain.model.User;
import com.akaci.twotterbackend.domain.service.LikeDomainService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service("twoot-like-domain-service")
public class TwootLikeDomainServiceImpl implements LikeDomainService {

    private static final Logger LOGGER = LogManager.getLogger(TwootLikeDomainServiceImpl.class);

    @Override
    public void like(User user, Likable likeableTwoot) {
        if (user.getLikedTwoots().contains(likeableTwoot)) {
            // twoot is already liked
            user.removeLike(likeableTwoot);
            return;
        }

        user.like(likeableTwoot);
    }
}
