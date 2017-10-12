package com.codeprinciples.architecturecomponentsproject.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;

import com.codeprinciples.architecturecomponentsproject.BR;
import com.codeprinciples.architecturecomponentsproject.R;
import com.codeprinciples.architecturecomponentsproject.api.ApiManager;
import com.codeprinciples.architecturecomponentsproject.binding.RecyclerViewBindingAdapter;
import com.codeprinciples.architecturecomponentsproject.database.AppDatabase;
import com.codeprinciples.architecturecomponentsproject.models.Movie;
import com.codeprinciples.architecturecomponentsproject.models.MovieSuggestion;

import java.util.ArrayList;
import java.util.List;

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

public class HomeViewModel extends ViewModel {
    private static final String TAG = "HomeViewModel";
    private ObservableArrayList<RecyclerViewBindingAdapter.AdapterDataItem> discoverItems;
    private MutableLiveData<MovieSuggestion> currentMovie;
    public enum ListLayoutType {LIST,GRID}
    private ListLayoutType listLayoutType;

    public MutableLiveData<MovieSuggestion> getCurrentMovie() {
        if(currentMovie==null){
            currentMovie = new MutableLiveData<>();
        }
        return currentMovie;
    }

    public void setCurrentMovie(MovieSuggestion currentMovie) {
        getCurrentMovie().setValue(currentMovie);
    }

    public ObservableArrayList<RecyclerViewBindingAdapter.AdapterDataItem> getSuggestionsObservableList() {
        if(discoverItems==null){
            discoverItems = new ObservableArrayList<>();
        }
        return discoverItems;
    }

    public ListLayoutType getListLayoutType() {
        return listLayoutType;
    }

    public void setListLayoutType(ListLayoutType listLayoutType) {
        this.listLayoutType = listLayoutType;
    }



    public RecyclerView.LayoutManager getLayoutManager(Context context){
        switch (listLayoutType) {
            case LIST:
                return new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
            case GRID:
                return new GridLayoutManager(context,3, LinearLayoutManager.VERTICAL,false);
        }
        return null;
    }
    public void loadSuggestions() {
        ApiManager.getInstance().getMovieSuggestions(
                (obj) -> discoverItems.addAll(convert(obj.results)),
                ((code, msg) -> Log.e(TAG,"Failed loading suggestions:" + msg)));//just first item for now
    }

    private void loadMovieDetails(MovieSuggestion movieSuggestion) {
        ApiManager.getInstance().getMovie(movieSuggestion.id,
                obj -> AppDatabase.executeAsync(
                        () -> {
                            AppDatabase.getInstance().movieDao().insertAll(obj);
                            updateUI(obj);
                        },
                        () -> Log.i(TAG,"Finished writing to database.")),
                (code, msg) -> Log.e(TAG, "Failed loading movie details: " + msg));
    }

    private void updateUI(Movie obj) {
        //todo
    }

    public void onSuggestionClick(MovieSuggestion movieSuggestion){
        setCurrentMovie(movieSuggestion);
    }

    private List<RecyclerViewBindingAdapter.AdapterDataItem> convert(List<MovieSuggestion> suggestions) {
        List<RecyclerViewBindingAdapter.AdapterDataItem> items = new ArrayList<>();
        for (MovieSuggestion item : suggestions) {
            items.add(convert(item));
        }
        return items;
    }

    private RecyclerViewBindingAdapter.AdapterDataItem convert(MovieSuggestion item) {
        return new RecyclerViewBindingAdapter.AdapterDataItem(
                R.layout.layout_discover_movie,
                new Pair<>( BR.homeViewModel, this),
                new Pair<>(BR.movieSuggestion,item));
    }

    public interface OnSuggestionClicked{
        void onClick();
    }
}
