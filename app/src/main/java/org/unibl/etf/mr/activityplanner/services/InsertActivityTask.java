package org.unibl.etf.mr.activityplanner.services;

import android.os.AsyncTask;

import org.unibl.etf.mr.activityplanner.database.PlannerDatabase;
import org.unibl.etf.mr.activityplanner.database.dto.ActivityWithPictures;
import org.unibl.etf.mr.activityplanner.database.entities.Picture;

import java.util.List;

public class InsertActivityTask extends AsyncTask<ActivityWithPictures, Void, Void> {

    private final Runnable onPostExecuteCallback;
    private final PlannerDatabase database;

    public InsertActivityTask(Runnable onPostExecuteCallback, PlannerDatabase database) {
        this.onPostExecuteCallback = onPostExecuteCallback;
        this.database = database;
    }

    @Override
    protected Void doInBackground(ActivityWithPictures... activityWithPictures) {
        long id = database.getActivityDAO().insertActivity(activityWithPictures[0].getActivity());
        List<Picture> pictures = activityWithPictures[0].getPictures();
        for (Picture picture : pictures) {
            picture.setActivityId(id);
            database.getActivityDAO().insertPicture(picture);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        onPostExecuteCallback.run();
    }
}
