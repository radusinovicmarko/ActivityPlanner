package org.unibl.etf.mr.activityplanner.services;

import android.os.AsyncTask;

import org.unibl.etf.mr.activityplanner.database.PlannerDatabase;
import org.unibl.etf.mr.activityplanner.database.dto.ActivityWithPictures;

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
        if (dates.length > 1) {
            return database.getActivityDAO().getAllByDate(dates[0], dates[1]);
        } else {
            return database.getActivityDAO().getAllByDate(dates[0]);
        }
    }

    @Override
    protected void onPostExecute(List<ActivityWithPictures> activities) {
        super.onPostExecute(activities);
        onPostExecuteCallback.accept(activities);
    }
}
