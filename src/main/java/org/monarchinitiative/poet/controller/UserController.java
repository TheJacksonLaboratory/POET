package org.monarchinitiative.poet.controller;

import org.monarchinitiative.poet.model.enumeration.CurationRole;
import org.monarchinitiative.poet.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value="/user")
public class UserController {

    private UserService userService;
    private final String nickname_claim = "https://poet.jax.org/nickname";
    private final String email_claim = "https://poet.jax.org/email";
    private final String role_claim = "https://poet.jax.org/role";
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/check", headers = "Accept=application/json")
    public ResponseEntity<?> checkUser(Authentication authentication) {
        final Jwt credentials = (Jwt) authentication.getCredentials();
        if(userService.saveNewUser(authentication.getName(),
                credentials.getClaim(nickname_claim),
                credentials.getClaim(email_claim), "", CurationRole.valueOf(credentials.getClaim(role_claim)))){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
