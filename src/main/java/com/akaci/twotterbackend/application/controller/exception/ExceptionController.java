package com.akaci.twotterbackend.application.controller.exception;

import com.akaci.twotterbackend.application.dto.response.ErrorResponse;
import com.akaci.twotterbackend.exceptions.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionController {

    private static final Logger LOGGER = LogManager.getLogger(ExceptionController.class);

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> usernameAlreadyExists(UsernameAlreadyExistsException usernameAlreadyExistsException) {
        ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT.value(), usernameAlreadyExistsException.getMessage(), LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(UsernameNotValidException.class)
    public ResponseEntity<ErrorResponse> usernameNotValid(UsernameNotValidException usernameNotValidException) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), usernameNotValidException.getMessage(), LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(PasswordRefusedException.class)
    public ResponseEntity<ErrorResponse> passwordRefused(PasswordRefusedException passwordRefusedException) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), passwordRefusedException.getMessage(), LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> illegalArgument(IllegalArgumentException illegalArgumentException) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), illegalArgumentException.getMessage(), LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);    }

    @ExceptionHandler(UserAlreadyFollowedException.class)
    public ResponseEntity<ErrorResponse> userAlreadyFollowed(UserAlreadyFollowedException userAlreadyFollowedException) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), userAlreadyFollowedException.getMessage(), LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(UserToFollowNotFoundException.class)
    public ResponseEntity<ErrorResponse> userToFollowNotFound(UserToFollowNotFoundException userToFollowNotFoundException) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), userToFollowNotFoundException.getMessage(), LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
}
