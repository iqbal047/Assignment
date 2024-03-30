package com.assingment.spectrumapplication.service;

import com.assingment.spectrumapplication.constants.Constants;
import com.assingment.spectrumapplication.constants.enums.OperationStatus;
import com.assingment.spectrumapplication.dao.UserRepository;
import com.assingment.spectrumapplication.dto.auth.LoginResponse;
import com.assingment.spectrumapplication.model.Role;
import com.assingment.spectrumapplication.model.User;
import com.assingment.spectrumapplication.utils.auth.CookieUtil;
import com.assingment.spectrumapplication.utils.auth.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final JwtUtil jwtUtil;

    @Override
    @Cacheable(value = "loggedInUsers", key = "#username")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository
                .findByUsernameAndActive(username, true)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));
    }

    @Override
    public String getUserameByRandomStringAndPublicKeyAndHash(String randomStr, String publicKey, String hash) {
        return repository.retrieveUsernameByPublicKeyAndPrivateKey(randomStr, publicKey, hash);
    }

    @Override
    public ResponseEntity<LoginResponse> getLoginResponse(User user) {
        HttpHeaders responseHeaders = new HttpHeaders();
        String accessToken = jwtUtil.createAccessToken(user);
        String refreshToken = jwtUtil.createRefreshToken(user.getUsername());
        CookieUtil.addCookieToHttpHeader(responseHeaders, Constants.ACCESS_TOKEN_NAME, accessToken, Constants.ACCESS_TOKEN_DURATION_MILLISECONDS);
        CookieUtil.addCookieToHttpHeader(responseHeaders, Constants.REFRESH_TOKEN_NAME, refreshToken, Constants.REFRESH_TOKEN_DURATION_MILLISECONDS);
        List<Role> roles = user.getRoles();
        List<String> authorities = new LinkedList<>();
        List<String> permissions = new LinkedList<>();
        LoginResponse loginResponse = new LoginResponse(
                user.getUsername(),
                authorities,
                permissions,
                accessToken,
                OperationStatus.SUCCESS.toString(),
                "Login successful.");
        return ResponseEntity.ok().headers(responseHeaders).body(loginResponse);
    }

    @Override
    public ResponseEntity<LoginResponse> refreshAuthTokens(String refreshToken) {
        Claims claimsSet = jwtUtil.verifyToken(refreshToken);
        User user = (User) loadUserByUsername(claimsSet.getSubject());
        return getLoginResponse(user);
    }

}
