package com.vasiu_catalina.beauty_salon.security.service;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.vasiu_catalina.beauty_salon.security.SecurityConstants;
import com.vasiu_catalina.beauty_salon.security.exception.BadCredentialsException;
import com.vasiu_catalina.beauty_salon.service.impl.ClientServiceImpl;
import com.vasiu_catalina.beauty_salon.service.impl.EmployeeServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class SecurityService {

    @Autowired
    private ClientServiceImpl clientService;

    @Autowired
    private EmployeeServiceImpl employeeService;

    private SecurityConstants securityConstants;

    private String CLIENT_REGISTER_PATH;
    private String CLIENT_LOGIN_PATH;
    private String EMPLOYEE_REGISTER_PATH;
    private String EMPLOYEE_LOGIN_PATH;
    private String LOGOUT_PATH;

    private String JWT_SECRET;
    private String JWT_AUTHORIZATION;
    private String JWT_BEARER;
    private Long JWT_TOKEN_EXPIRATION;

    private String COOKIE_NAME;
    private boolean IS_COOKIE_HTTP_ONLY;
    private boolean IS_COOKIE_SECURE;
    private String COOKIE_PATH;
    private int COOKIE_EXPIRATION;

    public SecurityService(SecurityConstants securityConstants) {
        this.securityConstants = securityConstants;

        String path = "/api/v1";

        CLIENT_REGISTER_PATH = path + securityConstants.getPath().getClient_register();
        CLIENT_LOGIN_PATH = path + securityConstants.getPath().getClient_login();
        EMPLOYEE_REGISTER_PATH = path + securityConstants.getPath().getEmployee_register();
        EMPLOYEE_LOGIN_PATH = path + securityConstants.getPath().getEmployee_login();
        LOGOUT_PATH = path + securityConstants.getPath().getLogout();

        JWT_SECRET = securityConstants.getJwt().getSecret();
        JWT_AUTHORIZATION = securityConstants.getJwt().getAuthorization();
        JWT_BEARER = securityConstants.getJwt().getBearer() + " ";
        JWT_TOKEN_EXPIRATION = securityConstants.getJwt().getToken_expiration_days() * 24 * 3600 * 1000L;

        COOKIE_NAME = securityConstants.getCookie().getName();
        COOKIE_PATH = securityConstants.getCookie().getPath();
        IS_COOKIE_HTTP_ONLY = securityConstants.getCookie().isHttponly();
        IS_COOKIE_SECURE = securityConstants.getCookie().isSecure();
        COOKIE_EXPIRATION = securityConstants.getCookie().getExpiration_days() * 24 * 3600;
    }

    // Authorization paths

    public String getClientRegisterPath() {
        return CLIENT_REGISTER_PATH;
    }

    public String getClientLoginPath() {
        return CLIENT_LOGIN_PATH;
    }

    public String getEmployeeRegisterPath() {
        return EMPLOYEE_REGISTER_PATH;
    }

    public String getEmployeeLoginPath() {
        return EMPLOYEE_LOGIN_PATH;
    }

    public String getLogoutPath() {
        return LOGOUT_PATH;
    }

    public void authenticateUser(String header) {

        String token = header.replace(JWT_BEARER, "");
        DecodedJWT decodedJWT = getDecodedJWT(token);

        String email = decodedJWT.getSubject();
        String role = decodedJWT.getClaim("role").asString();
        Long userId = getUserId(email, role);

        var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);
        ((UsernamePasswordAuthenticationToken) authentication).setDetails(userId);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void successfulAuthentication(HttpServletResponse response, String email, String role)
            throws IOException, ServletException {
        String token = createJwtToken(email, role);
        Cookie cookie = createCookie(token);
        response.addCookie(cookie);
    }

    public void unsuccessfulAuthentication(HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(failed.getMessage());
        response.getWriter().flush();
    }

    public void logout(HttpServletResponse response) {
        Cookie cookie = getLogoutCookie();
        response.addCookie(cookie);
    }

    public boolean isLogoutRequest(HttpServletRequest request) {
        return request.getRequestURI().equals(getLogoutPath()) &&
                request.getMethod().equalsIgnoreCase("POST");
    }

    public String getHeader(HttpServletRequest request) {
        return request.getHeader(JWT_AUTHORIZATION);
    }

    public boolean isBearerHeader(String header) {
        return header != null && header.startsWith(JWT_BEARER);
    }

    private Long getUserId(String email, String role) {
        if (role.equals("CLIENT")) {
            return clientService.getClientByEmail(email).getId();
        }
        if (role.equals("EMPLOYEE")) {
            return employeeService.getEmployeeByEmail(email).getId();
        }
        throw new BadCredentialsException("Invalid role provided");
    }

    private DecodedJWT getDecodedJWT(String token) {
        return JWT.require(Algorithm.HMAC512(securityConstants.getJwt().getSecret()))
                .build()
                .verify(token);
    }

    private String createJwtToken(String email, String role) {
        return JWT.create()
                .withSubject(email)
                .withClaim("role", role)
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_TOKEN_EXPIRATION))
                .sign(Algorithm.HMAC512(JWT_SECRET));
    }

    private Cookie createCookie(String token) {
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setHttpOnly(IS_COOKIE_HTTP_ONLY);
        cookie.setSecure(IS_COOKIE_SECURE);
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge(COOKIE_EXPIRATION);
        return cookie;
    }

    private Cookie getLogoutCookie() {
        Cookie cookie = new Cookie(securityConstants.getCookie().getName(), "");
        cookie.setHttpOnly(securityConstants.getCookie().isHttponly());
        cookie.setSecure(securityConstants.getCookie().isSecure());
        cookie.setPath(securityConstants.getCookie().getPath());
        cookie.setMaxAge(0);
        return cookie;
    }

}
