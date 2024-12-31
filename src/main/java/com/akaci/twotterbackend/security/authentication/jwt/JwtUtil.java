package com.akaci.twotterbackend.security.authentication.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.text.ParseException;
import java.util.Collection;

public interface JwtUtil {

    String generateJwt(String username, Collection<? extends GrantedAuthority> authorities) throws JOSEException;
    Authentication getAuthenticationFromJwt(String signedJwt) throws ParseException, BadJOSEException, JOSEException;

}
