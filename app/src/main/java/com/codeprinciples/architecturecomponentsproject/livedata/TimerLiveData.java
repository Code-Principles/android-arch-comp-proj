package com.codeprinciples.architecturecomponentsproject.livedata;

import android.arch.lifecycle.LiveData;
import android.os.Handler;

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
