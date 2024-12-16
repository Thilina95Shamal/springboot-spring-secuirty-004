package com.example.proj.service.jwtfilter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public interface JWTService {

    String generateAccessToken(String username, Authentication authentication);

    String extractUsername(String token);

    boolean validateToken(String token, UserDetails userDetails);

    String generateRefreshToken(String username, Authentication authentication);

    String generateNewAccessToken(String username, Collection<? extends GrantedAuthority> authorities);

    String generateNewRefreshToken(String username, Collection<? extends GrantedAuthority> authorities);
}
