package com.mkayad.yapilycodechallenge.services;

import com.mkayad.yapilycodechallenge.domain.Hero;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Query;


import java.util.List;
import retrofit2.http.GET;

public interface MarvelService {
    @GET("characters")
    Observable<Hero> getCharacters(@Query("ts") int ts,@Query("apikey") String apiKey,@Query("hash") String hash,@Query("limit") int limit);
}
