package com.example.activityplanner.services;

import android.os.AsyncTask;
import android.view.View;

import com.example.activityplanner.database.dto.ActivityWithPictures;
import com.example.activityplanner.ui.dashboard.DashboardFragment;

import java.lang.ref.WeakReference;
import java.util.List;

public class RetrieveAllTask extends AsyncTask<Void, Void, List<ActivityWithPictures>> {

    private WeakReference<DashboardFragment> fragmentReference;

    public RetrieveAllTask(DashboardFragment fragment) {
        fragmentReference = new WeakReference<>(fragment);
    }

    @Override
    protected List<ActivityWithPictures> doInBackground(Void... voids) {
        return fragmentReference.get() != null ? fragmentReference.get().getPlannerDatabase().getActivityDAO().getAllActivities() : null;
    }

    @Override
    protected void onPostExecute(List<ActivityWithPictures> activities) {
        fragmentReference.get().getActivities().clear();
        if (activities != null && activities.size() > 0) {
            fragmentReference.get().getActivities().addAll(activities);
            fragmentReference.get().getNoItemsTv().setVisibility(View.GONE);
        } else {
            fragmentReference.get().getNoItemsTv().setVisibility(View.VISIBLE);
        }
        fragmentReference.get().getAdapter().notifyDataSetChanged();
    }
}
