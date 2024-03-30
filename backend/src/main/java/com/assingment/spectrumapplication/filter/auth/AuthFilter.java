package com.assingment.spectrumapplication.filter.auth;

import com.assingment.spectrumapplication.constants.Constants;
import com.assingment.spectrumapplication.utils.auth.CookieUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.assingment.spectrumapplication.config.auth.SecurityConfig;
import com.assingment.spectrumapplication.service.UserService;
import com.assingment.spectrumapplication.utils.auth.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.StringUtils.hasText;

@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    public final UserService userService;
    public static final Set<String> AUTH_IGNORE_PATTERN_LIST = new HashSet<>(Arrays.asList(SecurityConfig.authIgnoreList));
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();
    public static final Map<String, Map<String, String>> REFRESH_STATUS_MAP = new ConcurrentHashMap<>();


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (AUTH_IGNORE_PATTERN_LIST.stream().noneMatch(pattern -> ANT_PATH_MATCHER.match(pattern, request.getServletPath()))) {
            try {
                String authorizationHeader = request.getHeader(AUTHORIZATION);
                if (hasText(authorizationHeader) && authorizationHeader.startsWith(Constants.TOKEN_BEARER_PREFIX)) {
                    // Authorize by authorization header
                    String token = authorizationHeader.substring(Constants.TOKEN_BEARER_PREFIX.length());
                    Claims claimsSet = getClaimSetFromJwtToken(token, request, response);
                    if (claimsSet != null && claimsSet.get(Constants.AUTHORITIES_KEY) != null) {
                        doAuthorizeUserByUsername(claimsSet.getSubject(), request);
                    }
                } else {
                    // Authorize by API key
                    String randomStr = request.getParameter("ts");
                    String publicKey = request.getParameter("apiKey");
                    String hash = request.getParameter("hash");
                    if (hasText(randomStr) && hasText(publicKey) && hasText(hash)) {
                        String username = userService.getUserameByRandomStringAndPublicKeyAndHash(randomStr, publicKey, hash);
                        if (hasText(username)) {
                            doAuthorizeUserByUsername(username, request);
                        }
                    }
                }
                filterChain.doFilter(request, response);
            } catch (Exception ex) {
                ex.printStackTrace();
                logErrors(ex, response);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private void doAuthorizeUserByUsername(String username, HttpServletRequest request) {
        UserDetails user = userService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Claims getClaimSetFromJwtToken(String token, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Claims claimsSet = null;
        try {
            claimsSet = jwtUtil.verifyToken(token);
        } catch (ExpiredJwtException e) {
            try {
                claimsSet = refreshToken(request, response, token);
            } catch (Exception ex) {
                logErrors(ex, response);
            }
        }
        return claimsSet;
    }

    private Claims refreshToken(HttpServletRequest request, HttpServletResponse response, String expiredAccessToken) throws Exception {
        try {
            Cookie refreshTokenCookie = CookieUtil.getCookieByName(Constants.REFRESH_TOKEN_NAME, request);
            Claims claimsSet = jwtUtil.verifyToken(refreshTokenCookie.getValue());
            UserDetails user = userService.loadUserByUsername(claimsSet.getSubject());
            String accessToken = jwtUtil.createAccessToken(user);
            String refreshToken = jwtUtil.createRefreshToken(user.getUsername());
            Map<String, String> tokenMap = new HashMap<>();
            tokenMap.put(Constants.ACCESS_TOKEN_NAME, accessToken);
            tokenMap.put(Constants.REFRESH_TOKEN_NAME, refreshToken);
            REFRESH_STATUS_MAP.put(Constants.TOKEN_BEARER_PREFIX.concat(expiredAccessToken), tokenMap);
            return jwtUtil.verifyToken(accessToken);
        } catch (Exception ex) {
            CookieUtil.deleteAllCookie(request, response);
            logErrors(ex.getMessage(), HttpServletResponse.SC_RESET_CONTENT, response);
        }
        return null;
    }

    private void logErrors(Exception ex, HttpServletResponse response) throws IOException {
        logger.error(ex.getLocalizedMessage());
        response.setHeader("error", ex.getMessage());
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), ex.getMessage());
    }

    private void logErrors(String message, int httpStatus, HttpServletResponse response) throws IOException {
        logger.error(message);
        response.setHeader("error", message);
        response.setStatus(httpStatus);
        response.setContentType(APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), message);
    }

}
