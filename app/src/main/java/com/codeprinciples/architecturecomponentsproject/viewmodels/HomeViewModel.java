package com.codeprinciples.architecturecomponentsproject.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.adapters.SearchViewBindingAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.codeprinciples.architecturecomponentsproject.BR;
import com.codeprinciples.architecturecomponentsproject.R;
import com.codeprinciples.architecturecomponentsproject.api.ApiManager;
import com.codeprinciples.architecturecomponentsproject.binding.RecyclerViewBindingAdapter;
import com.codeprinciples.architecturecomponentsproject.models.Movie;
import com.codeprinciples.architecturecomponentsproject.models.MovieSuggestion;
import com.codeprinciples.architecturecomponentsproject.models.Resource;
import com.codeprinciples.architecturecomponentsproject.repo.Repository;

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

public class HomeViewModel extends ViewModel implements SearchViewBindingAdapter.OnQueryTextSubmit {
    private static final String TAG = "HomeViewModel";
    private ObservableArrayList<RecyclerViewBindingAdapter.AdapterDataItem> discoverItems;
    private MutableLiveData<MovieSuggestion> selectedMovieSuggestion;
    private MutableLiveData<Resource<Movie>> movieDetails;

    public enum ListLayoutType {LIST,GRID}
    private ListLayoutType listLayoutType;
    private RecyclerViewBindingAdapter.AdapterDataItem loadingRowItem, searchInfoRowItem;

    public MutableLiveData<MovieSuggestion> getSelectedMovieSuggestion() {
        if(selectedMovieSuggestion ==null){
            selectedMovieSuggestion = new MutableLiveData<>();
            loadSuggestions();
        }
        return selectedMovieSuggestion;
    }

    public void setSelectedMovieSuggestion(MovieSuggestion selectedMovieSuggestion) {
        if(movieDetails!=null)
            movieDetails.setValue(null);//always reset details as they depend on selected suggestion
        getSelectedMovieSuggestion().setValue(selectedMovieSuggestion);
    }

    public ObservableArrayList<RecyclerViewBindingAdapter.AdapterDataItem> getSuggestionsObservableList() {
        if(discoverItems==null){
            discoverItems = new ObservableArrayList<>();
        }
        return discoverItems;
    }

    public void setListLayoutType(ListLayoutType listLayoutType) {
        this.listLayoutType = listLayoutType;
    }

    public RecyclerView.LayoutManager getLayoutManager(Context context){
        switch (listLayoutType) {
            case LIST:
                return new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
            case GRID:
                GridLayoutManager gridLayoutManager = new GridLayoutManager(context,3, LinearLayoutManager.VERTICAL,false);
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return lookUpRowSpan(position);
                    }
                });
                return gridLayoutManager;
        }
        return null;
    }

    private int lookUpRowSpan(int position) {
        RecyclerViewBindingAdapter.AdapterDataItem item = discoverItems.get(position);
        if(item.equals(getLoadingRowItem()) || item.equals(getSearchInfoRowItem())){
            return 3;
        }
        return 1;
    }

    public MutableLiveData<Resource<Movie>> getMovieDetails(int id) {
        //setErrorState(null);
        if(movieDetails==null)
            movieDetails = new MutableLiveData<>();
        if(movieDetails.getValue()==null || movieDetails.getValue().getData()==null)
            Repository.getInstance().getMovie(movieDetails, id);
        return movieDetails;
    }

    private RecyclerViewBindingAdapter.AdapterDataItem getLoadingRowItem(){
        if(loadingRowItem==null)
            loadingRowItem = new RecyclerViewBindingAdapter.AdapterDataItem(R.layout.layout_loading_item);
        return loadingRowItem;
    }

    private RecyclerViewBindingAdapter.AdapterDataItem getSearchInfoRowItem(){
        if(searchInfoRowItem==null)
            searchInfoRowItem = new RecyclerViewBindingAdapter.AdapterDataItem(R.layout.layout_search_info_item);
        return searchInfoRowItem;
    }

    public void loadSuggestions() {
        discoverItems.add(getLoadingRowItem());
        ApiManager.getInstance().getMovieSuggestions(
                (obj) -> {
                    discoverItems.remove(getLoadingRowItem());
                    discoverItems.addAll(convert(obj.results));
                    },
                ((code, msg) -> Log.e(TAG,"Failed loading suggestions:" + msg)));//just first item for now
    }

    public void onSuggestionClick(MovieSuggestion movieSuggestion){
        setSelectedMovieSuggestion(movieSuggestion);
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        discoverItems.remove(getSearchInfoRowItem());
        discoverItems.add(getLoadingRowItem());
        if(!TextUtils.isEmpty(query))
            ApiManager.getInstance().getMovieSuggestions(query,
                    (obj) -> {
                        discoverItems.remove(getLoadingRowItem());
                        discoverItems.addAll(convert(obj.results));
                    },
                ((code, msg) -> Log.e(TAG,"Failed loading suggestions:" + msg)));//just first item for now
        else
            loadSuggestions();
        return false;
    }

    public boolean onSearchClear(){
        setSearchMode(true);
        return true;
    }

    public void setSearchMode(boolean isSearch){
        if(isSearch){
            discoverItems.clear();
            discoverItems.add(getSearchInfoRowItem());
        }else{
            discoverItems.clear();
            loadSuggestions();
        }
    }


}
