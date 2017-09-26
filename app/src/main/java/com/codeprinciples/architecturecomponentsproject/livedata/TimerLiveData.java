package com.codeprinciples.architecturecomponentsproject.livedata;

import android.arch.lifecycle.LiveData;
import android.os.Handler;

/**
 * Created by Oleksiy on 9/25/2017.
 */

public class TimerLiveData extends LiveData<Long> {
    private static final long SECOND_AS_MILLI = 1000;
    private Handler handler;
    private Runnable runnable;
    private long secondsCount;
    private long secondsAtDeactivation;

    public TimerLiveData() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                setValue(++secondsCount);
                handler.postDelayed(this,SECOND_AS_MILLI);
            }
        };
    }

    @Override
    protected void onActive() {
        super.onActive();
        if(secondsAtDeactivation>0) //counts the time passed while inactive
            secondsCount += (System.currentTimeMillis()/ SECOND_AS_MILLI - secondsAtDeactivation);
        handler.post(runnable );
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        handler.removeCallbacks(runnable);
        secondsAtDeactivation = System.currentTimeMillis()/ SECOND_AS_MILLI;
    }
}
