package com.example.proj.controller.login;

import com.example.proj.dto.user.UserDTO;
import com.example.proj.service.jwtfilter.JWTService;
import com.example.proj.service.userdetail.CustomUserDetailService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtFilterService;
    private final CustomUserDetailService customUserDetailService;

    public LoginController(AuthenticationManager authenticationManager, JWTService jwtFilterService, CustomUserDetailService customUserDetailService) {
        this.authenticationManager = authenticationManager;
        this.jwtFilterService = jwtFilterService;
        this.customUserDetailService = customUserDetailService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody UserDTO userDTO, HttpServletResponse response) {
        //Verify user
        //String verify = userService.verify(user);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));
        if (authentication.isAuthenticated()) {
            // Create Access,Refresh Tokens
            // Todo: use a vault or db to store the refresh token (hashed)(a good practice)
            String accessToken = jwtFilterService.generateAccessToken(userDTO.getUsername(), authentication);
            String refreshToken = jwtFilterService.generateRefreshToken(userDTO.getUsername(), authentication);

            // Access Token should be saved in HTTP headers BUT refresh tokens should be stored in HTTP-only Cookies (more Secure)
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", "Bearer " + accessToken);

            // Refresh token can be updated in database for now (But rather it should be saved in a Vault)
            Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setPath("/");
            response.addCookie(refreshTokenCookie);

            return new ResponseEntity<>("ok", httpHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("not ok", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<?> generateNewAccessToken(@CookieValue("refreshToken") String refreshToken, HttpServletResponse response) {
        //Todo : Verify if refresh token is Expired NEED TO VERIFY BEFORE EXTRACTING
        String username = jwtFilterService.extractUsername(refreshToken);
        if(username!=null){
            Collection<? extends GrantedAuthority> authorities =
                    customUserDetailService.loadUserByUsername(username).getAuthorities();

            // Todo: use a vault or db to store the refresh token (hashed)(a good practice)
            String newAccessToken = jwtFilterService.generateNewAccessToken(username, authorities);
            String newRefreshToken = jwtFilterService.generateNewRefreshToken(username, authorities);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization", "Bearer " + newAccessToken);

            // Set new refresh token in HTTP-only cookie (automatically overwrites the old one)
            Cookie refreshTokenCookie = new Cookie("refreshToken", newRefreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setPath("/");
            response.addCookie(refreshTokenCookie);


            return new ResponseEntity<>("ok", httpHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid refresh token", HttpStatus.UNAUTHORIZED);
        }

    }

}
