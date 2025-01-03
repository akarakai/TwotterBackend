package com.akaci.twotterbackend.application.controller;

import com.akaci.twotterbackend.application.dto.request.TwootRequest;
import com.akaci.twotterbackend.application.dto.response.TwootResponse;
import com.akaci.twotterbackend.application.service.crud.TwootCrudService;
import com.akaci.twotterbackend.domain.model.Twoot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class TwootController {

    private static final Logger LOGGER = LogManager.getLogger(TwootController.class);

    private final TwootCrudService twootCrudService;

    public TwootController(TwootCrudService twootCrudService) {
        this.twootCrudService = twootCrudService;
    }

    @PostMapping("twoot/new")
    public ResponseEntity<TwootResponse> postNewTwoot(@RequestBody TwootRequest twootRequest) {
        String username = getAccountUsername();
        String content = twootRequest.content();
        Twoot newTwoot = twootCrudService.createTwoot(username, content);
        TwootResponse response = new TwootResponse(
                newTwoot.getId(),
                newTwoot.getAuthor().getUsername(),
                newTwoot.getContent(),
                0,
                newTwoot.getPostedAt()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }


    private String getAccountUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new RuntimeException("could not get authentication");
        }
        return auth.getName();
    }
}
