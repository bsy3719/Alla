package com.fave.android.alla.network;

import android.content.Context;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by seungyeop on 2017-11-09.
 */

public class RestClient {
    private static final String BASE_URL="http://13.124.217.208/alla/";

    public Retrofit getClient(Context context) {

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request requestOriginal = chain.request();

                Request request = requestOriginal.newBuilder()
                        .header("ex-hader", "sample")
                        .method(requestOriginal.method(), requestOriginal.body())
                        .build();

                return chain.proceed(request);
            }
        })
                .addInterceptor(new AddCookiesInterceptor(context))
                .addInterceptor(new ReceivedCookiesInterceptor(context))
                .build();


        Retrofit client = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                //.client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return client;
    }

    //Response로부터 쿠키정보를 가져와서 Preference에 저장하는 ReceivedCookiesInterceptor클래스를 만들어 줍니다.
    //Request마다 Preference에 저장되어있는 쿠키값을 함께 Header에 넣어주는 AddCookiesInterceptor클래스를 만들어 줍니다.
    public class AddCookiesInterceptor implements Interceptor {

        private SessionSharedPreferences cherryShared;
        public AddCookiesInterceptor(Context context){
            cherryShared = SessionSharedPreferences.getInstanceOf(context);
        }

        @Override public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            HashSet<String> preferences = (HashSet) cherryShared.getHashSet(SessionSharedPreferences.KEY_COOKIE, new HashSet<String>());
            for (String cookie : preferences) {
                builder.addHeader("Cookie", cookie);
            }

            // Web,Android,iOS 구분을 위해 User-Agent세팅
            builder.removeHeader("User-Agent").addHeader("User-Agent", "Android");

            return chain.proceed(builder.build());
        }
    }

    public class ReceivedCookiesInterceptor implements Interceptor {

        private SessionSharedPreferences cherryShared;
        public ReceivedCookiesInterceptor(Context context){
            cherryShared = SessionSharedPreferences.getInstanceOf(context);
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());

            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                HashSet<String> cookies = new HashSet<>();

                for (String header : originalResponse.headers("Set-Cookie")) {
                    cookies.add(header);
                }

                cherryShared.putHashSet(SessionSharedPreferences.KEY_COOKIE, cookies);
            }

            return originalResponse;
        }
    }
}
