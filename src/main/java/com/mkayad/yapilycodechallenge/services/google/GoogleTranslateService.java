package com.mkayad.yapilycodechallenge.services.google;


import com.mkayad.yapilycodechallenge.domain.google.GoogleResponseWrapper;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface GoogleTranslateService {

    @GET()
    Observable<GoogleResponseWrapper> translate(@Url String url);//@Query("key") String key, @Query("target") String languageCode, @Query("q") String query);
}
