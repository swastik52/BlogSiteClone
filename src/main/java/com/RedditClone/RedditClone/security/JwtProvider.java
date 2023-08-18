package com.RedditClone.RedditClone.security;


import com.RedditClone.RedditClone.exception.SpringRedditException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

@Service
public class JwtProvider {

    private KeyStore keyStore;

    @PostConstruct
    public void init(){
        try{
            keyStore=KeyStore.getInstance("JKS");
            InputStream resourseAsStream=getClass().getResourceAsStream("/springblog.jks");
            keyStore.load(resourseAsStream,"secret".toCharArray());
        } catch (KeyStoreException |CertificateException| IOException|NoSuchAlgorithmException e) {
            throw new SpringRedditException("Exception occure while loading keystore..");
        }
    }

    public  String generateToken(Authentication authentication){
        User pricipal= (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(pricipal.getUsername())
                .signWith(getPrivateKey())
                .compact();
    }

    private Key getPrivateKey() {
        try{
            return (PrivateKey) keyStore.getKey("springblog", "secret".toCharArray());
        }catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new SpringRedditException("Exception occure while retrieving public key form keystore..");
        }
    }
}