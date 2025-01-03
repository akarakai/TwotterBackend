package com.akaci.twotterbackend.application.service.crud;

import com.akaci.twotterbackend.domain.model.Twoot;
import com.akaci.twotterbackend.domain.model.User;

public interface TwootCrudService {

    Twoot createTwoot(String username, String content);

}
