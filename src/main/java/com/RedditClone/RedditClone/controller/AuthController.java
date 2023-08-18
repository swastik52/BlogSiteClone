package com.RedditClone.RedditClone.controller;


import com.RedditClone.RedditClone.dto.AuthenticationResponse;
import com.RedditClone.RedditClone.dto.LoginRequest;
import com.RedditClone.RedditClone.dto.RegisterRequest;
import com.RedditClone.RedditClone.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest resisterRequest){
            authService.signup(resisterRequest);

            return new ResponseEntity<>("User Registration Successfull", HttpStatus.OK);
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token){
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account ACtivated Successfully",HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }
}
