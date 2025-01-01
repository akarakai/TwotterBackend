package com.akaci.twotterbackend.security.authentication.jwt;


import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import com.akaci.twotterbackend.persistence.entity.enums.Role;
import com.akaci.twotterbackend.security.authentication.GrantedAuthorityImpl;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jose.proc.*;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtUtilImpl implements JwtUtil {

    private static final String SECRET = "1D37B59349BA15D8CF7D35B35FCA6ABE";
    private static final long EXPIRATION_TIME = 36000000; // 10 hours
    private static final String ISSUER = "twotter";

    @Override
    public String generateJwt(String username, Collection<? extends GrantedAuthority> authorities) throws JOSEException {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (authorities == null || authorities.isEmpty()) {
            throw new IllegalArgumentException("Authorities cannot be null or empty");
        }

        String authority = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(auth -> auth.startsWith("ROLE_"))
                .findFirst()
                .map(auth -> auth.substring("ROLE_".length())) // Safely remove prefix
                .orElseThrow(() -> new IllegalStateException("No valid authorities found"));

        Date now = new Date();
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .jwtID(UUID.randomUUID().toString())
                .subject(username)
                .claim("auth", authority)
                .issuer(ISSUER)
                .issueTime(now)
                .expirationTime(new Date(now.getTime() + EXPIRATION_TIME))
                .build();

        JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256);
        JWEObject jweObject = new JWEObject(header, new Payload(claims.toJSONObject()));

        DirectEncrypter encrypter = new DirectEncrypter(SECRET.getBytes());
        jweObject.encrypt(encrypter);

        // Clear sensitive data if necessary
        clearSensitiveData(encrypter.getKey().getEncoded());

        return jweObject.serialize();
    }

    @Override
    public Authentication getAuthenticationFromJwt(String signedJwt) throws ParseException, BadJOSEException, JOSEException {
        ConfigurableJWTProcessor<SimpleSecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        JWKSource<SimpleSecurityContext> keySource = new ImmutableSecret<>(SECRET.getBytes());

        jwtProcessor.setJWEKeySelector(new JWEDecryptionKeySelector<>(
                JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256, keySource));

        JWTClaimsSet claimsSet = jwtProcessor.process(signedJwt, null);

        // Validate mandatory claims
        validateClaims(claimsSet);

        String username = claimsSet.getSubject();
        String authority = claimsSet.getStringClaim("auth");

        return new UsernamePasswordAuthenticationToken(
                username, null, List.of(new GrantedAuthorityImpl(Role.valueOf(authority))));
    }


    private void validateClaims(JWTClaimsSet claimsSet) throws ParseException {
        if (!ISSUER.equals(claimsSet.getIssuer())) {
            throw new SecurityException("Invalid issuer");
        }
        if (claimsSet.getExpirationTime().before(new Date())) {
            throw new SecurityException("JWT has expired");
        }
        if (claimsSet.getSubject() == null || claimsSet.getSubject().isEmpty()) {
            throw new SecurityException("Subject claim is missing or empty");
        }
        if (claimsSet.getStringClaim("auth") == null) {
            throw new SecurityException("Authority claim is missing");
        }
    }

    private void clearSensitiveData(byte[] key) {
        if (key != null) {
            Arrays.fill(key, (byte) 0);
        }
    }
}

