package com.RedditClone.RedditClone.service;

import com.RedditClone.RedditClone.dto.ResisterRequest;
import com.RedditClone.RedditClone.model.NotificationEmail;
import com.RedditClone.RedditClone.model.User;
import com.RedditClone.RedditClone.model.VerificationToken;
import com.RedditClone.RedditClone.repository.UserRepository;
import com.RedditClone.RedditClone.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {


    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final MailService mailService;


    @Transactional
    public void signup(ResisterRequest resisterRequest){
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
}
