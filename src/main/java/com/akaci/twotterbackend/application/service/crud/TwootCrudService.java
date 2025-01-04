package com.akaci.twotterbackend.application.service.crud;

import com.akaci.twotterbackend.application.dto.response.twoot.TwootAllResponse;
import com.akaci.twotterbackend.domain.model.Twoot;

public interface TwootCrudService {

    Twoot postNewTwoot(String username, String content);
    TwootAllResponse getAllTwoots();

}
