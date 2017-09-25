package com.codeprinciples.architecturecomponentsproject.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.codeprinciples.architecturecomponentsproject.R;
import com.codeprinciples.architecturecomponentsproject.api.ApiManager;
import com.codeprinciples.architecturecomponentsproject.database.AppDatabase;
import com.codeprinciples.architecturecomponentsproject.models.Movie;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        loadSuggestions();
    }

    private void loadSuggestions() {
        ApiManager.getInstance().getMovieSuggestions(
                (obj) -> loadMovieDetails(obj.results.get(0).id),
                ((code, msg) -> Log.e(TAG,"Failed loading suggestions:" + msg)));//just first item for now
    }

    private void loadMovieDetails(int id) {
        ApiManager.getInstance().getMovie(id,
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
}
