package com.assingment.spectrumapplication.utils.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.util.Optional;

public class CookieUtil {

    /**
     * Prevent initialization of class as all the class members should be static
     */
    private CookieUtil(){}

    public static void addCookieToHttpHeader(HttpHeaders httpHeaders, String cookieName, String cookieValue, int duration) {
        httpHeaders.add(HttpHeaders.SET_COOKIE, createCookie(cookieName, cookieValue, duration));
    }

    public static String createCookie(String cookieName, String cookieValue, int duration) {
        HttpCookie httpCookie = ResponseCookie.from(cookieName, cookieValue).maxAge(duration)
                .httpOnly(true).secure(true)
                .sameSite("Strict")
                .path("/").build();
        return httpCookie.toString();
    }

    public static void deleteCookie(String cookieName, HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public static void deleteAllCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]);
        for (Cookie cookie : cookies) {
            deleteCookie(cookie.getName(), response);
        }
    }

    public static Cookie getCookieByName(String cookieName, HttpServletRequest request) {
        Cookie[] cookies = Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]);
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase(cookieName)) return cookie;
        }
        return new Cookie(Cookie.class.getSimpleName(), Strings.EMPTY);
    }
}
