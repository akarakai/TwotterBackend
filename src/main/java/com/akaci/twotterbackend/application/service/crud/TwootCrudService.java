package com.akaci.twotterbackend.application.service.crud;

import com.akaci.twotterbackend.application.dto.response.twoot.TwootAllResponse;
import com.akaci.twotterbackend.domain.model.Twoot;

import java.util.UUID;

public interface TwootCrudService {

    Twoot postNewTwoot(String username, String content);
    TwootAllResponse getAllTwoots(String username);
    TwootAllResponse getAllTwoots();

}
