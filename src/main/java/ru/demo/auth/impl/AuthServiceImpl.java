package ru.demo.auth.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.function.Consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.demo.auth.impl.jpa.Auth;
import ru.demo.auth.model.AuthException;
import ru.demo.auth.model.AuthRefresh;
import ru.demo.auth.model.AuthToken;
import ru.demo.user.UserRepository;
import ru.demo.user.UserService;
import ru.demo.user.impl.jpa.User;
import ru.demo.auth.AuthRepository;
import ru.demo.auth.AuthService;
import ru.demo.user.model.Role;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RSASSAVerifier rsassaVerifier;
    private final AuthRepository authRepository;
    private final ObjectMapper objectMapper;
    private final AuthMappers authMapper;
    private final UserService userService;
    private final JWSSigner jwssigner;

    @Value("${spring.security.jwt.expires}")
    private Duration expires;

    public void jwtFilter(HttpServletRequest rq, HttpServletResponse rp, FilterChain chain) throws ServletException, IOException {
        authorizationBearer(rq, (accessToken) -> {
            var auth = createAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(auth);
        });
        chain.doFilter(rq, rp);
    }

    @Override
    public void jwtFilter(ServletRequest rq, ServletResponse rp, FilterChain chain) throws ServletException, IOException {
        jwtFilter((HttpServletRequest) rq, (HttpServletResponse) rp, chain);
    }

    @Override
    public void authenticate(HttpServletRequest rq, HttpServletResponse rp, Authentication auth) throws IOException {
        if (auth instanceof UsernamePasswordAuthenticationToken token)
            authenticateUsernamePasswordToken(rq, rp, token);

        if (auth instanceof OAuth2AuthenticationToken oauthToken) {
            authenticateOAuth2Token(rp, oauthToken);
        }
    }

    void authenticateOAuth2Token(HttpServletResponse rp, OAuth2AuthenticationToken oauthToken) throws IOException {
        try {
            var user = userService.importOAuth2(oauthToken.getPrincipal());
            var token = createToken(authRepository.save(authMapper.fromUser(user)));
            var cookieHeader = String.format("Authorization=%s; Path=/; HttpOnly; SameSite=Lax; Max-Age=604800",
                    token.getRefreshToken());

            rp.addHeader(HttpHeaders.SET_COOKIE, cookieHeader);
            rp.sendRedirect("http://localhost:5173/login?token=" + token.getAccessToken());
        } catch (Exception e) {
            rp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Server error");
        }
    }

    public void failure(HttpServletRequest rq, HttpServletResponse rp, RuntimeException except) {
        failureHandle(rp, except);
    }

    public void logout(HttpServletRequest rq, HttpServletResponse rp, Authentication authentication) {
        authorizationBearer(rq, (accessToken) -> {
            var jti = (String) createAuthentication(accessToken).getCredentials();
            authRepository.removeByJti(jti);
        });
    }

    @Override
    public AuthToken updateAccessToken(AuthRefresh refresh) {
        var auth = authRepository.findById(refresh.getRefreshToken()).orElseThrow(AuthException.Failed::new);

        if (!auth.getUser().isEnabled())
            throw new DisabledException("Пользователь деактивирован");
        authRepository.save(auth);
        return createToken(auth);
    }

    void authenticateUsernamePasswordToken(HttpServletRequest rq, HttpServletResponse rp, UsernamePasswordAuthenticationToken token) {
        var user = (User) token.getPrincipal();
        if (user != null && !user.isEnabled()) throw new DisabledException("Пользователь деактивирован");

        authenticateUser(rp, user);

    }


    void authenticateUser(HttpServletResponse rp, User user) {
        authenticate(rp, authMapper.fromUser(user));
    }


    void authenticate(HttpServletResponse rp, Auth auth) {
        successHandle(rp, authRepository.save(auth));
    }

    void failureHandle(HttpServletResponse rp, RuntimeException except) {
        handle(rp, ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, except.getLocalizedMessage()));
    }

    void successHandle(HttpServletResponse rp, Auth auth) {
        handle(rp, createToken(auth));
    }

    void handle(HttpServletResponse rp, Object payload) {
        rp.setContentType("application/json;charset=UTF-8");
        if (payload instanceof ProblemDetail pd) {
            rp.setStatus(pd.getStatus());
        }

        try {
            try (ServletOutputStream sos = rp.getOutputStream()) {
                sos.write(objectMapper.writeValueAsBytes(payload));
            }

        } catch (IOException e) {
            throw new AccessDeniedException(e.getLocalizedMessage());
        }
    }

    Authentication createAuthentication(String bearerToken) {
        return authMapper.toAuthentication(authRepository::findByJtiAndUserId, () -> {
            try {
                var signed = SignedJWT.parse(bearerToken);
                if (!signed.verify(rsassaVerifier)) {
                    throw new BadCredentialsException("Ошибка авторизации");
                } else {
                    return signed.getJWTClaimsSet();
                }
            } catch (ParseException | JOSEException var3) {
                throw new BadCredentialsException("Ошибка авторизации");
            }
        });
    }

    @Override
    public AuthToken createToken(Auth auth) {
        var current = new Date(Instant.now().plus(expires).toEpochMilli());
        return authMapper.toAuthToken(auth, current.getTime(), () -> {
            var jwt = authMapper.toSignedJWT(auth, current);

            try {
                jwt.sign(jwssigner);
            } catch (JOSEException e) {
                throw new AccessDeniedException("Ошибка при создании сессии", e);
            }
            return jwt.serialize();
        });
    }

    void authorizationBearer(HttpServletRequest rq, Consumer<String> consumer) {
        var header = rq.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            consumer.accept(header.substring(7));
        }
    }
}
