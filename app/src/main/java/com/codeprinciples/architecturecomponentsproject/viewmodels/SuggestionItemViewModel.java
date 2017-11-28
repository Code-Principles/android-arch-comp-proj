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

package com.codeprinciples.architecturecomponentsproject.viewmodels;

import com.codeprinciples.architecturecomponentsproject.models.MovieSuggestion;
import com.codeprinciples.architecturecomponentsproject.other.OnItemClickListener;

public class SuggestionItemViewModel {
    private MovieSuggestion movieSuggestion;
    private OnItemClickListener<SuggestionItemViewModel> itemClickListener;

    public SuggestionItemViewModel(MovieSuggestion movieSuggestion, OnItemClickListener<SuggestionItemViewModel> onItemClickListener) {
        this.movieSuggestion = movieSuggestion;
        this.itemClickListener = onItemClickListener;
    }

    public void onClick(){
        if(itemClickListener!=null)
            itemClickListener.onItemClick(this);
    }

    public MovieSuggestion getMovieSuggestion() {
        return movieSuggestion;
    }

    public void setMovieSuggestion(MovieSuggestion movieSuggestion) {
        this.movieSuggestion = movieSuggestion;
    }

    public OnItemClickListener<SuggestionItemViewModel> getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(OnItemClickListener<SuggestionItemViewModel> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
