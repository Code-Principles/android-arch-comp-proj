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

package com.codeprinciples.architecturecomponentsproject.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.codeprinciples.architecturecomponentsproject.models.Configuration;

import java.util.List;

@Dao
public interface  ConfigurationDao extends BaseDao<Configuration>{
    @Override
    @Query("SELECT * FROM configuration LIMIT 1")
    Configuration getSingle();

    @Override
    @Query("SELECT * FROM configuration")
    List<Configuration> get();

    @Override
    @Query("SELECT * FROM configuration WHERE id = :idValue")
    Configuration getWithId(int idValue);

    @Override
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void setSingle(Configuration configuration);

    @Override
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void set(List<Configuration> items);

    @Override
    @Delete
    void deleteSingle(Configuration configuration);

    @Override
    @Delete
    void delete(List<Configuration> items);
}
