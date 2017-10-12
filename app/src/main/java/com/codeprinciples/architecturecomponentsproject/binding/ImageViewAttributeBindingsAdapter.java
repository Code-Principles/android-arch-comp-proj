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

package com.codeprinciples.architecturecomponentsproject.binding;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.codeprinciples.architecturecomponentsproject.database.AppDatabase;
import com.codeprinciples.architecturecomponentsproject.models.Configuration;
import com.squareup.picasso.Picasso;

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

public class ImageViewAttributeBindingsAdapter {
    private static String POSTER_BASE_URL;
    @BindingAdapter({"android:posterUrl", "android:error","android:placeholder", "android:posterSize"})
    public static void loadPosterImage(ImageView view, String url, Drawable error,Drawable placeHolder, ApiAssetSize posterSize) {
        if(POSTER_BASE_URL==null)
        {
            AppDatabase.executeAsync(() -> {
                Configuration config = AppDatabase.getInstance().configurationDao().get();
                if(config!=null&&config.images!=null&&config.images.baseUrl!=null) {
                    POSTER_BASE_URL = config.images.baseUrl + getSizeValue(config.images.posterSizes, posterSize);
                }
            }, () -> Picasso.with(view.getContext()).load(POSTER_BASE_URL + url).error(error).placeholder(placeHolder).into(view));
        }else {
            Picasso.with(view.getContext()).load(POSTER_BASE_URL + url).error(error).placeholder(placeHolder).into(view);
        }
    }

    private static String getSizeValue(List<String> posterSizes, ApiAssetSize size) {
        if(posterSizes!=null){
            switch (size) {
                case SMALL:
                    posterSizes.get(0);
                    break;
                case MED:
                    posterSizes.get(posterSizes.size()/2);
                    break;
                case LARGE:
                    posterSizes.get(posterSizes.size()-1);
                    break;
            }
        }
        return "w500";
    }

    @BindingAdapter({"android:srcUrl", "android:error","android:placeholder"})
    public static void loadImage(ImageView view, String url,Drawable placeHolder, Drawable error) {
        Picasso.with(view.getContext()).load(url).error(error).placeholder(placeHolder).into(view);
    }
}
