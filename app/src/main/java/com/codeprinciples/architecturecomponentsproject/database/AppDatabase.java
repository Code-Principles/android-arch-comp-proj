package com.codeprinciples.architecturecomponentsproject.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.codeprinciples.architecturecomponentsproject.MyApplication;
import com.codeprinciples.architecturecomponentsproject.models.Genre;
import com.codeprinciples.architecturecomponentsproject.models.Movie;
import com.codeprinciples.architecturecomponentsproject.models.MovieSuggestion;

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

@Database(entities = {Movie.class,  MovieSuggestion.class, Genre.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{
    private static final String TAG = "AppDatabase";
    private static  AppDatabase instance;
    public abstract MovieDao movieDao();
//    public abstract GenreDao genreDao();
//    public abstract MovieSuggestionDao movieSuggestionDao();
    public AppDatabase(){
        Log.w(TAG, "use getInstance() instead!");
    }
    public static AppDatabase getInstance(){
        if(instance==null){
            instance = Room.databaseBuilder(MyApplication.getAppContext(),
                    AppDatabase.class, "app-database").build();
        }
        return instance;
    }
    public static void executeAsync(Runnable action, Runnable completion){
        AppDatabaseTask databaseTask = new AppDatabaseTask();
        databaseTask.setAction(action);
        databaseTask.setCompletion(completion);
        databaseTask.execute();
    }
    public static  void executeAsync(Runnable action){
        executeAsync(action,null);
    }
    private static class AppDatabaseTask extends AsyncTask<Void,Void,Void>{
        private Runnable action, completion;

        private void setAction(Runnable action) {
            this.action = action;
        }

        private void setCompletion(Runnable completion) {
            this.completion = completion;
        }

        @Override
        protected Void doInBackground(Void... params) {
            action.run();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(completion!=null)
                completion.run();
            super.onPostExecute(aVoid);
        }
    }
}
