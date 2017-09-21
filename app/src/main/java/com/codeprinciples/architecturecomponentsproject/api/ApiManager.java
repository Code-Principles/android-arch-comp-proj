package com.codeprinciples.architecturecomponentsproject.api;

import com.codeprinciples.architecturecomponentsproject.BuildConfig;
import com.codeprinciples.architecturecomponentsproject.models.DiscoverMoviesRequest;
import com.codeprinciples.architecturecomponentsproject.models.Movie;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * MIT License
 * <p>
 * Copyright (c) 2017 Oleksiy
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

public class ApiManager {
    private static final String TAG = "ApiManager";
    private static final ApiManager ourInstance = new ApiManager();
    private final MovieApiEndoitInterfcae apiService;

    public static ApiManager getInstance() {
        return ourInstance;
    }

    private ApiManager() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder().addQueryParameter("api_key",BuildConfig.API_KEY).build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        })
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(logging)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        apiService =
                retrofit.create(MovieApiEndoitInterfcae.class);
    }

    public void getMovieSuggestions(com.codeprinciples.architecturecomponentsproject.api.Callback<DiscoverMoviesRequest> callback){
        apiService.getMovieSuggestions()
                .enqueue(new RetroCallback<>(callback));
    }

    public void getMovie(int movieId, Callback<Movie> callback){
        apiService.getMovie(movieId)
                .enqueue(new RetroCallback<>(callback));
    }

    private class RetroCallback<T> implements retrofit2.Callback<T>{
        private WeakReference<Callback<T>> callbackWeakReference;
        private int statusCode;
        public RetroCallback(Callback<T> callback) {
            this.callbackWeakReference = new WeakReference<>(callback);
        }

        @Override
        public void onResponse(Call<T> call, retrofit2.Response<T> response) {
            statusCode = response.code();
            if(callbackWeakReference.get()!=null){
                callbackWeakReference.get().onSuccess(response.body());
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            if(callbackWeakReference.get()!=null){
                callbackWeakReference.get().onFailure(statusCode,t.getMessage());
            }
        }
    }
}
