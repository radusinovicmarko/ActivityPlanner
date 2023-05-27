package com.example.activityplanner.services;

import android.os.AsyncTask;

import com.example.activityplanner.database.PlannerDatabase;
import com.example.activityplanner.database.dto.ActivityWithPictures;

public class DeleteActivityTask extends AsyncTask<ActivityWithPictures, Void, Void> {

    private PlannerDatabase database;
    private Runnable callback;

    public DeleteActivityTask(PlannerDatabase database, Runnable callback) {
        this.database = database;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(ActivityWithPictures... activityWithPictures) {
        database.getActivityDAO().deleteActivity(activityWithPictures[0].getActivity());
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        callback.run();
    }
}
