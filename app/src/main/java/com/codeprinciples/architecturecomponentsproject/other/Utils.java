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

package com.codeprinciples.architecturecomponentsproject.other;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class Utils {
    public static void setFragmentIfNotExists(FragmentManager fragmentManager, @IdRes int id, Class<?> cls, boolean addToBackStack) {
        if (fragmentManager.findFragmentById(id) == null || !fragmentManager.findFragmentById(id).getClass().equals(cls)) {
            try {
                    FragmentTransaction ft =fragmentManager.beginTransaction();
                        ft.replace(id, (Fragment) cls.getConstructors()[0].newInstance());
                        if(addToBackStack)
                            ft.addToBackStack(null);
                        ft.commit();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public static void setFragmentIfNotExists(FragmentManager fragmentManager, @IdRes int id, Fragment fragment, boolean addToBackStack){
        if (fragmentManager.findFragmentById(id) == null || fragmentManager.findFragmentById(id)!= fragment) {
            try {
                FragmentTransaction ft =fragmentManager.beginTransaction();
                ft.replace(id, fragment);
                if(addToBackStack)
                    ft.addToBackStack(null);
                ft.commit();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
