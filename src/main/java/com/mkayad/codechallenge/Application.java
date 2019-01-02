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
        if(args.length >=3){
            marvelPublicKey =args[0].split("=")[1];
            marvelPrivateKey =args[1].split("=")[1];
            googleAPIKey =args[2].split("=")[1];
            SpringApplication.run(Application.class, args);
        }else {
            System.out.println("The application requires the Marvel and Google Translation API keys passed in arguments \n as  shown below :\n" +
                    "mvn spring-boot:run -Dspring-boot.run.arguments=--mv-pub-key=XXXXX,--mv-priv-key=YYYYY,--gg-api-key=ZZZZZ\n" +
                    "where XXXXX=marvel public key, YYYYY=Marvel private key and ZZZZZ=Google Translate API key");
            System.exit(0);
        }
    }


    @Bean
    CommandLineRunner initData(){
        return args -> {
            System.out.println("Initialization step == load the 100 results and load into memory");

            int ts= LocalDateTime.now().getNano();
            Cache cache=cacheConfiguration.ehCacheCacheManager().getCacheManager().getCache("charactersCache");
            Observable<MarvelResponseWrapper> characters= ServiceGenerator.createService(MarvelService.class).getCharacters(ts, marvelPublicKey,getHash(ts),100);
            characters
                    .subscribe(response ->{
                        if(response.getCode()==200)
                            response.getData().getResults().stream().forEach(d -> cache.put(new Element(d.getId(),d.getId())));
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

