package com.codeprinciples.architecturecomponentsproject.api;

/**
 * Created by Oleksiy on 9/24/2017.
 */

public interface CallbackSuccess<T> {
    void onSuccess(T obj);
}
