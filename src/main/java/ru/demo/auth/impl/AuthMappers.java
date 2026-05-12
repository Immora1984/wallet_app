package ru.demo.auth.impl;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import ru.demo.auth.AuthMapper;
import ru.demo.auth.model.AuthToken;
import ru.demo.auth.impl.jpa.Auth;
import ru.demo.user.impl.jpa.User;

@Slf4j
@Component
public class AuthMappers implements AuthMapper {

    @Override
    public Auth fromUser(User user) {
        var auth = new Auth();
        auth.setJti(UUID.randomUUID().toString());
        auth.setUser(user);
        return auth;
    }

    @Override
    public AuthToken toAuthToken(Auth auth, long expires, Supplier<String> accessToken) {
        var token = new AuthToken();
        token.setRoles(auth.getUser().getAuthorities());
        token.setRefreshToken(auth.getId().toString());
        token.setAccessToken(accessToken.get());
        token.setTokenType("Bearer");
        token.setExpiresIn(expires);
        return token;
    }

    JWTClaimsSet toClaims(Auth auth, Date expires) {
        return (new JWTClaimsSet.Builder()).audience(auth.getUser().getAuthorities()
                        .stream().map(Enum::name).toList())
                .subject(auth.getUser().getId().toString()).expirationTime(expires).jwtID(auth.getJti())
                .build();
    }

    public SignedJWT toSignedJWT(Auth auth, Date expires) {
        return new SignedJWT(new JWSHeader(JWSAlgorithm.RS512), this.toClaims(auth, expires));
    }

    public Authentication toAuthentication(BiFunction<String, UUID, Optional<Auth>> function, Supplier<JWTClaimsSet> c) {
        return this.toAuthentication(function, c.get());
    }

    Authentication toAuthentication(BiFunction<String, UUID, Optional<Auth>> function, JWTClaimsSet c) {
        return this.toAuthentication(function, UUID.fromString(c.getSubject()), c.getJWTID(), c.getAudience());
    }

    Authentication toAuthentication(BiFunction<String, UUID, Optional<Auth>> authProvider,
                                    UUID subject,
                                    String jwtId,
                                    List<String> audience) {
        var auth = new UsernamePasswordAuthenticationToken(
                subject,
                jwtId,
                AuthorityUtils.createAuthorityList(audience)
        );
        auth.setDetails((Supplier<Auth>) () -> {
            try {
                return authProvider.apply(jwtId, subject)
                        .orElseThrow(() -> new DisabledException("Ошибка авторизации"));
            } catch (Exception e) {
                throw new AuthenticationServiceException("Ошибка получения данных аутентификации", e);
            }
        });
        return auth;
    }
}
