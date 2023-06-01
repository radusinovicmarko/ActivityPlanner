package com.example.activityplanner.services;

import android.os.AsyncTask;

import com.example.activityplanner.database.PlannerDatabase;
import com.example.activityplanner.database.dto.ActivityWithPictures;

import java.util.List;
import java.util.function.Consumer;

public class RetrieveAllByTitleTask extends AsyncTask<String, Void, List<ActivityWithPictures>> {
    private final Consumer<List<ActivityWithPictures>> onPostExecuteCallback;
    private final PlannerDatabase database;

    public RetrieveAllByTitleTask(Consumer<List<ActivityWithPictures>> onPostExecuteCallback, PlannerDatabase database) {
        this.onPostExecuteCallback = onPostExecuteCallback;
        this.database = database;
    }

    @Override
    protected List<ActivityWithPictures> doInBackground(String... strings) {
        return database.getActivityDAO().getAllByTitle(strings[0]);
    }

    @Override
    protected void onPostExecute(List<ActivityWithPictures> activities) {
        super.onPostExecute(activities);
        onPostExecuteCallback.accept(activities);
    }
}
