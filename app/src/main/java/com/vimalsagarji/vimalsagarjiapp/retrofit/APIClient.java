package com.vimalsagarji.vimalsagarjiapp.retrofit;

import com.vimalsagarji.vimalsagarjiapp.common.CommonUrl;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static final String BASE_URL = CommonUrl.Main_url;
    private static Retrofit retrofit = null;

    private static OkHttpClient getRequestHeader() {

        return new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
//                    .client(getRequestHeader())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
