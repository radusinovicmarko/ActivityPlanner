package com.example.activityplanner.services;

import android.os.AsyncTask;

import com.example.activityplanner.database.PlannerDatabase;
import com.example.activityplanner.database.dto.ActivityWithPictures;

import java.util.List;
import java.util.function.Consumer;

public class RetrieveAllTask extends AsyncTask<Void, Void, List<ActivityWithPictures>> {
    private final PlannerDatabase database;
    private final Consumer<List<ActivityWithPictures>> onPostExecuteCallback;

    public RetrieveAllTask(Consumer<List<ActivityWithPictures>> onPostExecuteCallback, PlannerDatabase database) {
        this.onPostExecuteCallback = onPostExecuteCallback;
        this.database = database;
    }

    @Override
    protected List<ActivityWithPictures> doInBackground(Void... voids) {
        return database.getActivityDAO().getAllActivities();
    }

    @Override
    protected void onPostExecute(List<ActivityWithPictures> activities) {
        super.onPostExecute(activities);
        onPostExecuteCallback.accept(activities);
    }
}
