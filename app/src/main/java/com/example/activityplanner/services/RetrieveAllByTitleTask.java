package com.example.activityplanner.services;

import android.os.AsyncTask;
import android.view.View;

import com.example.activityplanner.database.dto.ActivityWithPictures;
import com.example.activityplanner.ui.ListFragment;
import com.example.activityplanner.ui.dashboard.DashboardFragment;

import java.lang.ref.WeakReference;
import java.util.List;

public class RetrieveAllByTitleTask extends AsyncTask<String, Void, List<ActivityWithPictures>> {
    private WeakReference<ListFragment> fragmentReference;

    public RetrieveAllByTitleTask(ListFragment fragment) {
        fragmentReference = new WeakReference<>(fragment);
    }

    @Override
    protected List<ActivityWithPictures> doInBackground(String... strings) {
        return fragmentReference.get() != null ? fragmentReference.get().getPlannerDatabase().getActivityDAO().getAllByTitle(strings[0]) : null;
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
