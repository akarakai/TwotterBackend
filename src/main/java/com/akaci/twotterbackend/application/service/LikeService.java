package com.akaci.twotterbackend.application.service;

import com.akaci.twotterbackend.application.dto.response.like.LikeResponse;

import java.util.UUID;

public interface LikeService {

    LikeResponse like(String username, UUID id);

}
