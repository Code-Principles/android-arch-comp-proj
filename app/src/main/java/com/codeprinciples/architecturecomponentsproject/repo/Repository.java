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
import com.codeprinciples.architecturecomponentsproject.database.AppDatabase;
import com.codeprinciples.architecturecomponentsproject.models.Configuration;
import com.codeprinciples.architecturecomponentsproject.models.Resource;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    private static final Repository ourInstance = new Repository();

    public static Repository getInstance() {
        return ourInstance;
    }

    private Repository() {
    }
    public void getConfig(MutableLiveData<Resource<Configuration>> configurationMutableLiveData){
        final List<Configuration> configurationList = new ArrayList<>();
        AppDatabase.executeAsync(() -> {
            configurationList.addAll(AppDatabase.getInstance().configurationDao().getAll());
            if(configurationList.size()==0){
                loadConfigFromNetwork(configurationMutableLiveData);
            }else{
                if(configurationList.get(0).canUseCashed())
                    configurationMutableLiveData.postValue(new Resource<>(configurationList.get(0)));
                else {
                    AppDatabase.getInstance().configurationDao().delete((Configuration[]) configurationList.toArray());
                    loadConfigFromNetwork(configurationMutableLiveData);
                }
            }
        });

    }

    private void loadConfigFromNetwork(MutableLiveData<Resource<Configuration>> configurationMutableLiveData) {
        ApiManager.getInstance().getConfiguration(obj -> {
            AppDatabase.executeAsync(() -> AppDatabase.getInstance().configurationDao().insertAll(obj));
            configurationMutableLiveData.setValue(new Resource<>(obj));
        }, (code, msg) -> configurationMutableLiveData.setValue(new Resource<>(new Resource.Error(code,msg))));
    }
}
