package com.mkayad.yapilycodechallenge;

import com.mkayad.yapilycodechallenge.config.CacheConfiguration;
import com.mkayad.yapilycodechallenge.domain.Hero;
import com.mkayad.yapilycodechallenge.services.MarvelService;
import com.mkayad.yapilycodechallenge.services.MarvelServiceGenerator;
import io.reactivex.Observable;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import java.time.LocalDateTime;


@SpringBootApplication
public class YapilycodechallengeApplication {
    @Value("${marvel.api.url}")
    private static String apiURL;
    private static String apiKey;
    private static String privateKey;
    @Autowired
    CacheConfiguration cacheConfiguration;

//    public static Retrofit getRetrofit(){
//        return new Retrofit.Builder()
//                .baseUrl(apiURL)
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//    }


    public static void main(String[] args) {
        if(args.length >=2){
            System.out.println("API KEY ="+args[0].split("=")[1]);
            apiKey=args[0].split("=")[1];
            privateKey=args[1].split("=")[1];
            System.out.println(System.getProperty("apiKey"));
            System.out.println(System.getProperty("privKey"));

                //System.out.println(System.getProperties().toString());
                SpringApplication.run(YapilycodechallengeApplication.class, args);
        }else {
            System.out.println("The application requires the API KEY passed in arguments \n run as below :\n" +
                    "mvn spring-boot:run -Dspring-boot.run.arguments=--apiKey=XXXXX&privKey=YYYYY");
            System.exit(0);
        }
    }
    public static String getHash(int ts){
        return DigestUtils.md5Hex(ts+privateKey+apiKey);
    }

    @Bean
    CommandLineRunner initData(){
        return args -> {
            System.out.println("Initialization step == load the 100 characters and load into memory");
            int ts= LocalDateTime.now().getNano();
            Cache cache=cacheConfiguration.ehCacheCacheManager().getCacheManager().getCache("charactersCache");
            Observable<Hero> characters= MarvelServiceGenerator.createService(MarvelService.class).getCharacters(ts,apiKey,getHash(ts),100);
            characters.subscribe(e ->{
                e.getData().getResults().stream().forEach(d -> cache.put(new Element(d.getId(),d.getId())));
               // System.out.println(e.toString());

            });

        };
    }


}

