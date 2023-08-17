package com.RedditClone.RedditClone.controller;


import com.RedditClone.RedditClone.dto.ResisterRequest;
import com.RedditClone.RedditClone.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody ResisterRequest resisterRequest){
            authService.signup(resisterRequest);

            return new ResponseEntity<>("User Registration Successfull", HttpStatus.OK);
    }

}
