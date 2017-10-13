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

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import com.codeprinciples.architecturecomponentsproject.models.Configuration;
import com.codeprinciples.architecturecomponentsproject.models.Resource;
import com.codeprinciples.architecturecomponentsproject.repo.Repository;

public class LauncherViewModel extends ViewModel implements LoadRetryErrorViewModel {
    private MutableLiveData<Resource<Configuration>> configuration;
    private ObservableField<Resource.Error> error;


    public MutableLiveData<Resource<Configuration>> getConfiguration() {
        setErrorState(null);
        if(configuration==null)
            configuration = new MutableLiveData<>();
        if(configuration.getValue()==null || configuration.getValue().getData()==null)
            Repository.getInstance().getConfig(configuration);
        return configuration;
    }

    @Override
    public ObservableField<Resource.Error> getError() {
        if(error==null)
            error = new ObservableField<>();
        return error;
    }

    @Override
    public void onRetryClick() {
        getConfiguration();
    }

    public void setErrorState(Resource.Error err){
        getError().set(err);
    }
}
