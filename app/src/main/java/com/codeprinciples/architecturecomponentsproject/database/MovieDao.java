package com.codeprinciples.architecturecomponentsproject.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.codeprinciples.architecturecomponentsproject.models.Movie;

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
@Dao
public interface MovieDao extends BaseDao<Movie>{
    @Override
    @Query("SELECT * FROM movie LIMIT 1")
    Movie getSingle();

    @Override
    @Query("SELECT * FROM movie")
    List<Movie> get();

    @Override
    @Query("SELECT * FROM movie WHERE id = :idValue")
    Movie getWithId( int idValue);

    @Override
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void setSingle(Movie movie);

    @Override
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void set(List<Movie> items);

    @Override
    @Delete
    void deleteSingle(Movie movie);

    @Override
    @Delete
    void delete(List<Movie> items);
}
