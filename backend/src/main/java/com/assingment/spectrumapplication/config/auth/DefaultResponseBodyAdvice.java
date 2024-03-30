package com.assingment.spectrumapplication.config.auth;

import com.assingment.spectrumapplication.constants.Constants;
import com.assingment.spectrumapplication.filter.auth.AuthFilter;
import com.assingment.spectrumapplication.utils.auth.CookieUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Map;

@ControllerAdvice
public class DefaultResponseBodyAdvice<T> implements ResponseBodyAdvice<T> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public T beforeBodyWrite(T body, MethodParameter returnType, MediaType selectedContentType,
                             Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

            HttpHeaders requestHeaders = request.getHeaders();
            HttpHeaders responseHeaders = response.getHeaders();
            String authToken = String.join("", requestHeaders.getOrEmpty(HttpHeaders.AUTHORIZATION));
            if(AuthFilter.REFRESH_STATUS_MAP.containsKey(authToken)){
                Map<String, String> tokenMap = AuthFilter.REFRESH_STATUS_MAP.remove(authToken);
                String accessTokenCookie = CookieUtil.createCookie(Constants.ACCESS_TOKEN_NAME, tokenMap.get(Constants.ACCESS_TOKEN_NAME), Constants.ACCESS_TOKEN_DURATION_MILLISECONDS);
                String refreshTokenCookie = CookieUtil.createCookie(Constants.REFRESH_TOKEN_NAME, tokenMap.get(Constants.REFRESH_TOKEN_NAME), Constants.REFRESH_TOKEN_DURATION_MILLISECONDS);
                responseHeaders.add(HttpHeaders.SET_COOKIE, accessTokenCookie);
                responseHeaders.add(HttpHeaders.SET_COOKIE, refreshTokenCookie);
                responseHeaders.add(Constants.REFRESHED_ACCESS_TOKEN, accessTokenCookie.substring(Constants.ACCESS_TOKEN_NAME.length() + 1, accessTokenCookie.indexOf(";")));
            }
        return body;
    }

}
