package com.assingment.spectrumapplication.controller.auth;

import com.assingment.spectrumapplication.constants.Constants;
import com.assingment.spectrumapplication.dto.auth.LoginResponse;
import com.assingment.spectrumapplication.payloads.auth.LoginRequest;
import com.assingment.spectrumapplication.utils.auth.CookieUtil;
import com.assingment.spectrumapplication.model.User;
import com.assingment.spectrumapplication.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping(value = "login", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        return userService.getLoginResponse(user);
    }

    @PostMapping(value = "refresh", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> refreshToken(@CookieValue(name = Constants.REFRESH_TOKEN_NAME, required = false) String refreshToken) throws Exception {
        return userService.refreshAuthTokens(refreshToken);
    }

    @GetMapping(value = "logout/{username}")
    @CacheEvict(value = "loggedInUsers", key = "#username")
    public void logout(@PathVariable String username, HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.deleteAllCookie(request, response);
    }

    @GetMapping(value = "accessDenied")
    public void accessDenied(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        try {
            response.getWriter().write("Unauthorized request");
        } catch (IOException e) {
            log.warn("unauthorized request made");
        }
    }
}
