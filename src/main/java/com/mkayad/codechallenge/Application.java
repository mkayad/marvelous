package com.mkayad.codechallenge;

import com.mkayad.codechallenge.config.CacheConfiguration;
import com.mkayad.codechallenge.domain.marvel.MarvelResponseWrapper;
import com.mkayad.codechallenge.services.ServiceGenerator;
import com.mkayad.codechallenge.services.marvel.MarvelService;
import io.reactivex.Observable;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDateTime;


@SpringBootApplication
public class Application {

    public  static String marvelPublicKey;
    public  static String marvelPrivateKey;
    public  static String googleAPIKey;

    @Autowired
    CacheConfiguration cacheConfiguration;

    public static void main(String[] args) {
            SpringApplication.run(Application.class, args);
    }


    @Bean
    CommandLineRunner initData(){
        return args -> {
            System.out.println("Cache initialization with 100 character ids");
            if(marvelPublicKey==null)
                marvelPublicKey=System.getProperty("mv-pub-key");

            if(marvelPrivateKey==null)
                marvelPrivateKey=System.getProperty("mv-priv-key");

            if(googleAPIKey==null)
                googleAPIKey=System.getProperty("gg-api-key");

            int ts= LocalDateTime.now().getNano();
            Cache cache=cacheConfiguration.ehCacheCacheManager().getCacheManager().getCache("charactersCache");
            Observable<MarvelResponseWrapper> characters= ServiceGenerator.createService(MarvelService.class).getCharacters(ts, marvelPublicKey,getHash(ts),100);
            characters
                    .subscribe(response ->{
                        if(response.getCode()==200) {
                            response.getData().getResults().stream().forEach(d -> cache.put(new Element(d.getId(), d.getId())));
                            System.out.println("The Cache has been initialized.");
                        }
                        else {
                            System.out.println("Error occurred while retrieving the data");
                        }
                        },error -> error.printStackTrace()
                    );
            };
    }
    public static String getHash(int ts){
        return DigestUtils.md5Hex(ts+ marvelPrivateKey + marvelPublicKey);
    }

}

