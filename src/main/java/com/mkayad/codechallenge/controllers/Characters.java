package com.mkayad.codechallenge.controllers;

import com.mkayad.codechallenge.config.CacheConfiguration;
import com.mkayad.codechallenge.domain.marvel.Character;
import com.mkayad.codechallenge.domain.marvel.SimpleCharacter;
import com.mkayad.codechallenge.Application;
import com.mkayad.codechallenge.domain.DataResponse;
import com.mkayad.codechallenge.domain.marvel.MarvelResponseWrapper;
import com.mkayad.codechallenge.domain.marvel.URL;
import com.mkayad.codechallenge.services.google.GoogleTranslateService;
import com.mkayad.codechallenge.services.marvel.MarvelService;
import com.mkayad.codechallenge.services.ServiceGenerator;
import io.reactivex.Observable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "Marvel comic characters ",description = "The service allows retrieving the ")
@RestController
public class Characters {

    @Value("${marvel.api.url}")
    private String apiURL;

    /* Google recognized language codes*/
    private final String languages[]= {"af","am","ar","az","be","bg","bn","bs","ca","ceb","co","cs","cy","da","de","el",
            "en","eo","es","et","eu","fa","fi","fr","fy","ga","gd","gl","gu","ha","haw","hi","hmn","hr","ht","hu","hy",
            "id","ig","is","it","iw","ja","jw","ka","kk","km","kn","ko","ku","ky","la","lb","lo","lt","lv","mg","mi","mk",
            "ml","mn","mr","ms","mt","my","ne","nl","no","ny","pa","pl","ps","pt","ro","ru","sd","si","sk","sl","sm","sn",
            "so","sq","sr","st","su","sv","sw","ta","te","tg","th","tl","tr","uk","ur","uz","vi","xh","yi","yo","zh","zh-TW","zu"};


    @Autowired
    private CacheConfiguration cacheConfiguration;

    @ApiOperation(value = "Retrieve the first 100 characters from the cache if not from the marvel api")
    @GetMapping("characters")
    public DeferredResult<String> getCharacters(){
        DeferredResult<String> deferredResult=new DeferredResult<>();

        if(getCache().getKeys().size()!=0)
            deferredResult.setResult(getCache().getKeys().toString());
        else {
            final int ts=LocalDateTime.now().getNano();
            Observable<MarvelResponseWrapper> characters= ServiceGenerator.createService(MarvelService.class).getCharacters(ts, Application.marvelPublicKey,Application.getHash(ts),100);

            characters
                    .subscribe(response ->{
                        if(response.getCode()==200) {
                            response.getData().getResults().stream().forEach(d -> getCache().put(new Element(d.getId(), d.getId())));
                            deferredResult.setResult(getCache().getKeys().toString());
                        }
                        else {
                            deferredResult.setErrorResult("Error occurred while retrieving the data");
                            System.out.println("Error occurred while retrieving the data");
                        }
                    },error ->deferredResult.setErrorResult(error.getMessage()));
        }

        return deferredResult;

    }




    @ApiOperation(value = "Retrieve a specific character detail")
    @RequestMapping(method = RequestMethod.GET,value="/characters/{characterId}",produces = "application/json")
    public @ResponseBody DeferredResult<SimpleCharacter> getCharacterDetail(@PathVariable("characterId") int characterId) {
        DeferredResult<SimpleCharacter> characters=new DeferredResult<>();
        fetchCharacterDetail(characterId).subscribe(response-> {
                    System.out.println(response);
            if (response.getCode() == 200 && response.getData().getResults()!=null && response.getData().getResults().size()>0) {
                Character character=response.getData().getResults().get(0);
                SimpleCharacter simpleCharacter=new SimpleCharacter(character.getId(),character.getName(),character.getDescription(),character.getThumbnail());
                characters.setResult(simpleCharacter);
            }else {
                characters.setErrorResult("Error, Character can't be found "+response.getStatus());
            }

        },error -> {
                characters.setErrorResult("Error, Character can't be found due to this error:"+error.getMessage());
            }
        );
       return characters;
    }


    @RequestMapping(method=RequestMethod.GET,value="/characters/{characterId}/powers",produces = "application/json")
    public @ResponseBody DeferredResult<DataResponse> getCharacterPowers(@PathVariable("characterId") int characterId, @RequestParam(value = "language",required = false) String languageCode)  {
        DeferredResult<DataResponse> powers=new DeferredResult<>();
        fetchCharacterDetail(characterId).subscribe(response->{

                if(response.getCode()==200  && response.getData().getResults()!=null && response.getData().getResults().size()>0){
                        Character character=response.getData().getResults().get(0);
                        List<URL> wikiUrls=
                                character.getUrls().stream().filter( wiki ->wiki.getType().equals("wiki")).collect(Collectors.toList());

                        if(wikiUrls!=null && wikiUrls.size()>0) {
                            final Document doc = Jsoup.connect(wikiUrls.get(0).getUrl()).get();
                            final org.jsoup.select.Elements content = doc.getElementsByClass("power-circle__label");
                            DataResponse retrievedPowers = new DataResponse(content.stream().map(e -> e.text()).collect(Collectors.toList()));

                            if(languageCode ==null) {
                                powers.setResult(retrievedPowers);
                            }else {
                                List<String> targetLanguage=Arrays.asList(languages).stream().filter(lang ->lang.equals(languageCode)).collect(Collectors.toList());
                                if(targetLanguage != null  && targetLanguage.size() != 1)
                                    powers.setErrorResult("Unknown language");
                                //Call the translation API
                                String url=ServiceGenerator.GOOGLE_BASE_URL.substring(0,ServiceGenerator.GOOGLE_BASE_URL.length()-1)+"?key="+Application.googleAPIKey+"&target="+languageCode+"&q="+retrievedPowers;
                                ServiceGenerator.createService(GoogleTranslateService.class).translate(url)
                                        .subscribe(
                                                googleResponse -> {
                                                    String translatedTexts=googleResponse
                                                            .getData()
                                                            .getTranslations()
                                                            .stream()
                                                            .map(t->t.getTranslatedText())
                                                            .collect(Collectors.toList())
                                                            .toString()
                                                            .replaceAll("\\[\\[","")
                                                            .replaceAll("]]","");
                                                    powers.setResult(new DataResponse(Arrays.asList(translatedTexts.split(","))));
                                                    }
                                                ,error ->powers.setErrorResult(error.getMessage()));

                            }
                        }else {
                            powers.setErrorResult("No wiki URL available for this character");
                        }
                    }

                },
                error-> {
                    powers.setErrorResult(error.getMessage());
                }
        );
    return powers;
    }

    private Observable<MarvelResponseWrapper> fetchCharacterDetail(int characterId){
        final int ts=LocalDateTime.now().getNano();
        return ServiceGenerator.createService(MarvelService.class).getCharacterDetail(characterId,ts, Application.marvelPublicKey,Application.getHash(ts));
    }

    private Cache getCache(){
        return cacheConfiguration.ehCacheCacheManager().getCacheManager().getCache("charactersCache");
    }

}
