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

package com.codeprinciples.architecturecomponentsproject.views;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.codeprinciples.architecturecomponentsproject.R;
import com.codeprinciples.architecturecomponentsproject.databinding.ActivityLauncherBinding;
import com.codeprinciples.architecturecomponentsproject.viewmodels.LauncherViewModel;

public class LauncherActivity extends AppCompatActivity {

    private LauncherViewModel launcherViewModel;
    private ActivityLauncherBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_launcher);
        launcherViewModel = ViewModelProviders.of(this).get(LauncherViewModel.class);
        launcherViewModel.getConfiguration().observe(this, configuration -> {
            if(configuration!=null)
                binding.getRoot().postDelayed(() -> {
                    startActivity(new Intent(LauncherActivity.this,HomeActivity.class));
                    finish();
                },2000);
        });
    }
}
