package com.akaci.twotterbackend.security.authentication.jwt;

import com.akaci.twotterbackend.persistence.entity.enums.Role;
import com.akaci.twotterbackend.security.authentication.GrantedAuthorityImpl;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWEDecryptionKeySelector;
import com.nimbusds.jose.proc.JWEKeySelector;
import com.nimbusds.jose.proc.SimpleSecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class JwtUtilImpl implements JwtUtil {

    private static final String SECRET = "1D37B59349BA15D8CF7D35B35FCA6ABE";
    private static final long EXPIRATION_TIME = 36000000;

    // All JWTs using none as alg should always be rejected at all times.


    @Override
    public String generateJwt(String username, Collection<? extends GrantedAuthority> authorities) throws JOSEException {
        String authority = authorities.stream().map(GrantedAuthority::getAuthority)
                .toList().getFirst().substring(5);
        Date nowDate = new Date();

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .jwtID(UUID.randomUUID().toString())
                .subject(username)
                .claim("auth", authority)
                .issuer("twotter")
                .issueTime(nowDate)
                .expirationTime(new Date(nowDate.getTime() + EXPIRATION_TIME))
                .build();

        Payload payload = new Payload(claims.toJSONObject());
        JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256);

        byte[] secretKeyByte = SECRET.getBytes();
        DirectEncrypter encrypt = new DirectEncrypter(secretKeyByte);
        JWEObject jweObject = new JWEObject(header, payload);
        jweObject.encrypt(encrypt);
        return jweObject.serialize();

    }

    @Override
    public Authentication getAuthenticationFromJwt(String signedJwt) throws ParseException, BadJOSEException, JOSEException {
        ConfigurableJWTProcessor<SimpleSecurityContext> jwtProcessor = new DefaultJWTProcessor<SimpleSecurityContext>();
        JWKSource<SimpleSecurityContext> jwtKeySource = new ImmutableSecret<SimpleSecurityContext>(SECRET.getBytes());
        JWEKeySelector<SimpleSecurityContext> jweKeySelector = new JWEDecryptionKeySelector<SimpleSecurityContext>(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256, jwtKeySource);

        jwtProcessor.setJWEKeySelector(jweKeySelector);

        JWTClaimsSet claimsSet = jwtProcessor.process(signedJwt, new SimpleSecurityContext());

        String username = claimsSet.getSubject();
        String authorities = (String) claimsSet.getClaim("auth");

        return new UsernamePasswordAuthenticationToken(username, null,
                List.of(new GrantedAuthorityImpl(Role.valueOf(authorities))));



    }
}
