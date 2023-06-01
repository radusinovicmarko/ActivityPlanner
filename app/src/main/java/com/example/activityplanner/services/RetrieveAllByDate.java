package com.example.activityplanner.services;

import android.os.AsyncTask;

import com.example.activityplanner.database.PlannerDatabase;
import com.example.activityplanner.database.dto.ActivityWithPictures;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

public class RetrieveAllByDate extends AsyncTask<Date, Void, List<ActivityWithPictures>> {
    private final PlannerDatabase database;
    private final Consumer<List<ActivityWithPictures>> onPostExecuteCallback;

    public RetrieveAllByDate(PlannerDatabase database, Consumer<List<ActivityWithPictures>> onPostExecuteCallback) {
        this.database = database;
        this.onPostExecuteCallback = onPostExecuteCallback;
    }

    @Override
    protected List<ActivityWithPictures> doInBackground(Date... dates) {
        return database.getActivityDAO().getAllByDate(dates[0], dates[1]);
    }

    @Override
    protected void onPostExecute(List<ActivityWithPictures> activities) {
        super.onPostExecute(activities);
        onPostExecuteCallback.accept(activities);
    }
}
