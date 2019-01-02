package com.mkayad.codechallenge.services.marvel;

import com.mkayad.codechallenge.domain.marvel.MarvelResponseWrapper;
import io.reactivex.Observable;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.GET;

public interface MarvelService {
    @GET("characters")
    Observable<MarvelResponseWrapper> getCharacters(@Query("ts") int ts, @Query("apikey") String apiKey, @Query("hash") String hash, @Query("limit") int limit);

    @GET("characters/{characterId}")
    Observable<MarvelResponseWrapper> getCharacterDetail(@Path("characterId") int characterId, @Query("ts") int ts, @Query("apikey") String apiKey, @Query("hash") String hash);

}
