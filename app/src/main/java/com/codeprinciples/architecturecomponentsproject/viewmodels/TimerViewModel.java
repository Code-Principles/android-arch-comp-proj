package com.codeprinciples.architecturecomponentsproject.viewmodels;

import android.arch.lifecycle.ViewModel;

import com.codeprinciples.architecturecomponentsproject.livedata.TimerLiveData;

/**
 * Created by Oleksiy on 9/25/2017.
 */

public class TimerViewModel extends ViewModel {
    private TimerLiveData timerLiveData;
    public TimerLiveData getTimer(){
        if(timerLiveData == null)
            timerLiveData = new TimerLiveData();
        return timerLiveData;
    }
}
