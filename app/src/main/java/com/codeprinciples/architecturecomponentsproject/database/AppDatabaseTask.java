package com.codeprinciples.architecturecomponentsproject.database;

import android.os.AsyncTask;

/**
 * Created by Oleksiy on 9/24/2017.
 */

class AppDatabaseTask extends AsyncTask<Void,Void,Void> {
    private Runnable action, completion;

    public void setAction(Runnable action) {
        this.action = action;
    }

    public void setCompletion(Runnable completion) {
        this.completion = completion;
    }

    @Override
    protected Void doInBackground(Void... params) {
        action.run();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if(completion!=null)
            completion.run();
        super.onPostExecute(aVoid);
    }
}
