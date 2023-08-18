package com.RedditClone.RedditClone.service;

import com.RedditClone.RedditClone.dto.AuthenticationResponse;
import com.RedditClone.RedditClone.dto.LoginRequest;
import com.RedditClone.RedditClone.dto.RegisterRequest;
import com.RedditClone.RedditClone.exception.SpringRedditException;
import com.RedditClone.RedditClone.model.NotificationEmail;
import com.RedditClone.RedditClone.model.User;
import com.RedditClone.RedditClone.model.VerificationToken;
import com.RedditClone.RedditClone.repository.UserRepository;
import com.RedditClone.RedditClone.repository.VerificationTokenRepository;
import com.RedditClone.RedditClone.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {


    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Transactional
    public void signup(RegisterRequest resisterRequest){
        User user=new User();
        user.setUsername(resisterRequest.getUsername());
        user.setEmail(resisterRequest.getEmail());
        user.setPassword(encoder.encode(resisterRequest.getPassword()));
        user.setEnabled(false);
        userRepository.save(user);

        String token=generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please Acti" +
                "vate your Account",
                user.getEmail(), "Thank you for signing up to Spring Reddit, " +
                "please click on the below url to activate your account : " +
                "http://localhost:8080/api/auth/accountVerification/" + token));
    }

    private String generateVerificationToken(User user) {

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken=new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        tokenRepository.save(verificationToken);

        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken= tokenRepository.findByToken(token);
        verificationToken.orElseThrow(()->new SpringRedditException("Invalid Token"));
        fetchUserAndEnable(verificationToken.get());
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username=verificationToken.getUser().getUsername();
        User user=userRepository.findByUsername(username).orElseThrow(()->new SpringRedditException("Usernot Found with name - "+username));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token= jwtProvider.generateToken(authenticate);
        return new AuthenticationResponse(token,loginRequest.getUsername());
    }
}
