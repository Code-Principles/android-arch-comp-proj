package com.codeprinciples.architecturecomponentsproject.views;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;

import com.codeprinciples.architecturecomponentsproject.R;
import com.codeprinciples.architecturecomponentsproject.databinding.ActivityHomeBinding;
import com.codeprinciples.architecturecomponentsproject.other.Utils;
import com.codeprinciples.architecturecomponentsproject.viewmodels.HomeViewModel;

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
public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private ActivityHomeBinding binding;
    private HomeViewModel homeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.getSuggestionsObservableList();
        homeViewModel.loadSuggestions();

        setFragment(R.id.container, SuggestionListFragment.class);

        if(binding.detailFragment==null) {//phone or tablet portrait
            homeViewModel.getCurrentMovie().observe(this, movieSuggestion ->{
                if(movieSuggestion!=null)
                    setFragment(R.id.container, DetailFragment.class);
                else
                    setFragment(R.id.container, SuggestionListFragment.class);
            });
        }else{//tablet landscape
            setFragment(R.id.detail_fragment, DetailFragment.class);
        }
    }

    private void setFragment(@IdRes int id, Class<?> fragment){
        Utils.setFragmentIfNotExists(getSupportFragmentManager(),
                id,
                fragment,
                false);
    }

    @Override
    public void onBackPressed(){
        if (homeViewModel.getCurrentMovie().getValue()!=null) {
            homeViewModel.setCurrentMovie(null);
        } else {
            super.onBackPressed();
        }
    }
}
