package com.mkayad.codechallenge.services;

import com.mkayad.codechallenge.services.marvel.MarvelService;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static final String MARVEL_BASE_URL = "https://gateway.marvel.com:443/v1/public/";
    public static final String GOOGLE_BASE_URL = "https://translation.googleapis.com/language/translate/v2/";

    private static Retrofit.Builder marvelBuilder
            = new Retrofit.Builder()
            .baseUrl(MARVEL_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

    private static Retrofit.Builder googleBuilder
            = new Retrofit.Builder()
            .baseUrl(GOOGLE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create());


    private static Retrofit marvelRetrofit = marvelBuilder.build();
    private static Retrofit googleRetrofit = googleBuilder.build();

    public static <S> S createService(Class<S> serviceClass) {
        return serviceClass.equals(MarvelService.class)?marvelRetrofit.create(serviceClass):googleRetrofit.create(serviceClass);
    }

}