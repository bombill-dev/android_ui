package com.app.bombill.network;

import android.util.Base64;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by amolmhatre on 7/9/20
 */

public class RetrofitClient {
    private static final String AUTH = "Basic " + Base64.encodeToString(("bombill:123456").getBytes(), Base64.NO_WRAP);

//    private static final String BASE_URL = "http://nirvaacls.com/bombill_pages/";
    private static final String BASE_URL = "https://bombill.com/bombill_pages/";

    private static RetrofitClient mInstance;
    private Retrofit retrofit;


//    private RetrofitClient() {
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(
//                        new Interceptor() {
//                            @Override
//                            public Response intercept(Chain chain) throws IOException {
//                                Request original = chain.request();
//
//                                Request.Builder requestBuilder = original.newBuilder()
//                                        .addHeader("Authorization", AUTH)
//                                        .method(original.method(), original.body());
//
//                                Request request = requestBuilder.build();
//                                return chain.proceed(request);
//                            }
//                        }
//                ).build();
//
//        retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(okHttpClient)
//                .build();
//    }

    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public API getApi() {
        return retrofit.create(API.class);
    }
}