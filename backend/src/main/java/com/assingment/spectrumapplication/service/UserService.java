package com.assingment.spectrumapplication.service;

import com.assingment.spectrumapplication.dto.auth.LoginResponse;
import com.assingment.spectrumapplication.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {

   UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    String getUserameByRandomStringAndPublicKeyAndHash(String randomStr, String publicKey, String hash);

    ResponseEntity<LoginResponse> getLoginResponse(User user);

    ResponseEntity<LoginResponse> refreshAuthTokens(String refreshToken);
}
