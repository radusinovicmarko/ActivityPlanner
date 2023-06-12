package org.unibl.etf.mr.activityplanner.services;

import android.os.AsyncTask;

import org.unibl.etf.mr.activityplanner.database.PlannerDatabase;
import org.unibl.etf.mr.activityplanner.database.dto.ActivityWithPictures;

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
