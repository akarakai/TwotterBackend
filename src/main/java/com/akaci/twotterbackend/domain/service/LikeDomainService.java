package com.akaci.twotterbackend.domain.service;

import com.akaci.twotterbackend.domain.model.Likable;
import com.akaci.twotterbackend.domain.model.Twoot;
import com.akaci.twotterbackend.domain.model.User;

public interface LikeDomainService {

    void like(User user, Likable likable);

}
