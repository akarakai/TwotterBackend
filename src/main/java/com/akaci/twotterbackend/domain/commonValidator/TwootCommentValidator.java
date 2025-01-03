package com.akaci.twotterbackend.domain.commonValidator;

public class TwootCommentValidator {

    private static final int MAX_TWOOTCONTENT_LENGTH = 300;

    public static void validateTwootContent(String content) {
        if (content.length() > MAX_TWOOTCONTENT_LENGTH) {
            throw new IllegalArgumentException("Twoot content is too long");
        }
    }


}
