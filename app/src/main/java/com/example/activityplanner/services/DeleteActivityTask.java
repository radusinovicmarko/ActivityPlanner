package com.example.activityplanner.services;

import android.os.AsyncTask;

import com.example.activityplanner.database.PlannerDatabase;
import com.example.activityplanner.database.dto.ActivityWithPictures;

public class DeleteActivityTask extends AsyncTask<ActivityWithPictures, Void, Void> {

    private final PlannerDatabase database;
    private final Runnable onPostExecuteCallback;

    public DeleteActivityTask(PlannerDatabase database, Runnable onPostExecuteCallback) {
        this.database = database;
        this.onPostExecuteCallback = onPostExecuteCallback;
    }

    @Override
    protected Void doInBackground(ActivityWithPictures... activityWithPictures) {
        database.getActivityDAO().deleteActivity(activityWithPictures[0].getActivity());
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        onPostExecuteCallback.run();
    }
}
