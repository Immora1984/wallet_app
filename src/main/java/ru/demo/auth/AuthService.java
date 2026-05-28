package ru.demo.auth;

import java.io.IOException;

import jakarta.mail.MessagingException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import ru.demo.auth.impl.jpa.Auth;
import ru.demo.auth.model.AuthRefresh;
import ru.demo.auth.model.AuthToken;

public interface AuthService {
    void jwtFilter(HttpServletRequest rq, HttpServletResponse rp, FilterChain chain) throws ServletException, IOException;


    void jwtFilter(ServletRequest rq, ServletResponse rp, FilterChain chain) throws ServletException, IOException;

    void authenticate(HttpServletRequest rq, HttpServletResponse rp, Authentication auth);

    void failure(HttpServletRequest rq, HttpServletResponse rp, RuntimeException except);

    void logout(HttpServletRequest rq, HttpServletResponse rp, Authentication auth);

    AuthToken updateAccessToken(AuthRefresh refresh);

    AuthToken createToken(Auth auth);
}