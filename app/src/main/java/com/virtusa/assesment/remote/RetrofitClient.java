package com.virtusa.assesment.remote;

import com.virtusa.assesment.BaseApplication;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * To send network requests to an API, specify the main URL for the service.
 * Created by Anil on 2/12/2018.
 */
public class RetrofitClient {

    private static Retrofit retrofit = null;


    /**
     * creating retrofit instance through retrofit builder
     *
     * @return : retrofit instance
     */
    public static Retrofit getClient() {
    /*    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        File httpCacheDirectory = new File(BaseApplication.getContextObject().getCacheDir(), "offlineCache");
        //10 MB
        Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(cacheInterceptor())
                .addInterceptor(offlineCacheInterceptor())
                .build();*/

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    //.client(httpClient)
                    .baseUrl(ApiUtils.Main_URL)
                    .addConverterFactory(GsonConverterFactory.create())//directly convert json data to object
                    .build();

        }
        return retrofit;
    }

    private static Interceptor cacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response originalResponse = chain.proceed(chain.request());
                String cacheControl = originalResponse.header("Cache-Control");
                if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                        cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=5000")) {
                    request = request.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, max-age=" + 5000)
                            .build();

                    return chain.proceed(request);

                } else {
                    return originalResponse;
                }
            }
        };

    }

    private static Interceptor offlineCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                try {
                    return chain.proceed(chain.request());
                } catch (Exception e) {
                    Request offlineRequest = chain.request().newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, only-if-cached")
                            .build();
                    return chain.proceed(offlineRequest);
                }
            }
        };
    }
}
