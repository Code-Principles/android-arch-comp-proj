/*
 *  MIT License
 *  <p>
 *  Copyright (c) 2017. Oleksiy
 *  <p>
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *  <p>
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *  <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.codeprinciples.architecturecomponentsproject.repo;

import android.arch.lifecycle.MutableLiveData;

import com.codeprinciples.architecturecomponentsproject.api.ApiManager;
import com.codeprinciples.architecturecomponentsproject.api.CallbackFailure;
import com.codeprinciples.architecturecomponentsproject.api.CallbackSuccess;
import com.codeprinciples.architecturecomponentsproject.database.AppDatabase;
import com.codeprinciples.architecturecomponentsproject.models.Configuration;
import com.codeprinciples.architecturecomponentsproject.models.DiscoverMoviesRequest;
import com.codeprinciples.architecturecomponentsproject.models.Movie;
import com.codeprinciples.architecturecomponentsproject.models.Resource;

public class Repository {
    private static final Repository ourInstance = new Repository();

    public static Repository getInstance() {
        return ourInstance;
    }

    private Repository() {
    }

    public void getConfig(MutableLiveData<Resource<Configuration>> configurationMutableLiveData){
        AppDatabase.executeAsync(() -> {
            Configuration config= AppDatabase.getInstance().configurationDao().getSingle();
            if(config == null){
                loadConfigFromNetwork(configurationMutableLiveData);
            }else{
                if(config.canUseCashed())
                    configurationMutableLiveData.postValue(new Resource<>(config));
                else {
                    AppDatabase.getInstance().configurationDao().deleteSingle(config);
                    loadConfigFromNetwork(configurationMutableLiveData);
                }
            }
        });

    }

    private void loadConfigFromNetwork(MutableLiveData<Resource<Configuration>> configurationMutableLiveData) {
        ApiManager.getInstance().getConfiguration(obj -> {
            AppDatabase.executeAsync(() -> AppDatabase.getInstance().configurationDao().setSingle(obj));
            configurationMutableLiveData.setValue(new Resource<>(obj));
        }, (code, msg) -> configurationMutableLiveData.setValue(new Resource<>(new Resource.Error(code,msg))));
    }

    public void getMovie(MutableLiveData<Resource<Movie>> movieMutableLiveData, int id){
        AppDatabase.executeAsync(() -> {
            Movie movie= AppDatabase.getInstance().movieDao().getWithId( id);
            if(movie == null){
                loadMovieFromNetwork(movieMutableLiveData,id);
            }else{
                if(movie.canUseCashed())
                    movieMutableLiveData.postValue(new Resource<>(movie));
                else {
                    AppDatabase.getInstance().movieDao().deleteSingle(movie);
                    loadMovieFromNetwork(movieMutableLiveData, id);
                }
            }
        });
    }

    private void loadMovieFromNetwork(MutableLiveData<Resource<Movie>> movieMutableLiveData, int id) {
        ApiManager.getInstance().getMovie(id, obj -> {
            AppDatabase.executeAsync(() -> AppDatabase.getInstance().movieDao().setSingle(obj));
            movieMutableLiveData.setValue(new Resource<>(obj));
        }, (code, msg) -> movieMutableLiveData.setValue(new Resource<>(new Resource.Error(code,msg))));
    }

    public void getMovieSuggestions(CallbackSuccess<DiscoverMoviesRequest> success, CallbackFailure failure){
        ApiManager.getInstance().getMovieSuggestions(success, failure);
    }

    public void getMovieSuggestions(String query, CallbackSuccess<DiscoverMoviesRequest> success, CallbackFailure failure){
        ApiManager.getInstance().getMovieSuggestions(query, success, failure);
    }
}
