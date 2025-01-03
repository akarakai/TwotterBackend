package com.akaci.twotterbackend.security.authentication.jwt;

import com.akaci.twotterbackend.persistence.entity.enums.Role;
import com.akaci.twotterbackend.security.authentication.GrantedAuthorityImpl;
import com.nimbusds.jose.proc.BadJWEException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilImplTest {

    private static final Logger LOGGER = LogManager.getLogger(JwtUtilImplTest.class);

    private final JwtUtil jwtUtil = new JwtUtilImpl();

    private static final String USERNAME = "goodUsername1998";
    private static final String ROLE = "ROLE_USER";
    private static final Collection<GrantedAuthority> USER_AUTHORITY = Collections.singletonList(new GrantedAuthorityImpl(Role.USER));
    private static final Collection<GrantedAuthority> ADMIN_AUTHORITY = Collections.singletonList(new GrantedAuthorityImpl(Role.ADMIN));


    @Test
    void goodJwt_success() throws Exception {
        // generate jwt
        String jwt = jwtUtil.generateJwt(USERNAME, USER_AUTHORITY);
        assertNotNull(jwt);

        Authentication authentication = jwtUtil.getAuthenticationFromJwt(jwt);

        String authorityFromJWT = authentication.getAuthorities().iterator().next().getAuthority();

        assertEquals(ROLE, authorityFromJWT);
        assertEquals(USERNAME, authentication.getPrincipal());
        assertTrue(authentication.isAuthenticated());
    }

    @Test
    void badJwt_throwsException() throws Exception {
        // generate jwt
        String jwt = jwtUtil.generateJwt(USERNAME, USER_AUTHORITY);
        assertNotNull(jwt);

        // corrupt jwt
        String badJwt = jwt.replace("A", "B");

        assertThrows(BadJWEException.class, () -> jwtUtil.getAuthenticationFromJwt(badJwt));
    }

}