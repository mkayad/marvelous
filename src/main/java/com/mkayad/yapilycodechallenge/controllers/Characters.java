package com.mkayad.yapilycodechallenge.controllers;

import com.mkayad.yapilycodechallenge.config.CacheConfiguration;
import io.reactivex.Observable;

import net.sf.ehcache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.digest.DigestUtils;

@RestController
public class Characters {

    @Value("${marvel.api.url}")
    private String apiURL;

    @Autowired
    CacheConfiguration cacheConfiguration;


    @GetMapping("characters")
    public String getCharacters(){
        //call the api and process the result
        String hash="";
        System.out.println("Cache retrieved  element "+getCache().get(1009178));
        String input= "Moussa";//ts+privateKey+apiKey;
       hash=DigestUtils.md5Hex(input);
        String result;
        Observable<String> observer = Observable.just("Hello"); // provides datea
       // observer.subscribe(s -> result=s); // Callable as subscriber
        //assertTrue(result.equals("Hello"));
        return "hello World "+hash;
    }
    private Cache getCache(){
        return cacheConfiguration.ehCacheCacheManager().getCacheManager().getCache("charactersCache");
    }

}
