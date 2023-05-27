package com.example.activityplanner.services;

import android.os.AsyncTask;
import android.view.View;

import com.example.activityplanner.database.PlannerDatabase;
import com.example.activityplanner.database.dto.ActivityWithPictures;
import com.example.activityplanner.ui.ListFragment;
import com.example.activityplanner.ui.dashboard.DashboardFragment;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.function.Consumer;

public class RetrieveAllTask extends AsyncTask<Void, Void, List<ActivityWithPictures>> {

    private WeakReference<ListFragment> fragmentReference;
    private PlannerDatabase database;

    private Consumer<List<ActivityWithPictures>> callback;

    public RetrieveAllTask(ListFragment fragment) {
        fragmentReference = new WeakReference<>(fragment);
    }

    public RetrieveAllTask(Consumer<List<ActivityWithPictures>> callback, PlannerDatabase database) {
        this.callback = callback;
        this.database = database;
    }

    @Override
    protected List<ActivityWithPictures> doInBackground(Void... voids) {
        if (callback != null) {
            return database.getActivityDAO().getAllActivities();
        } else
            return fragmentReference.get() != null ? fragmentReference.get().getPlannerDatabase().getActivityDAO().getAllActivities() : null;
    }

    @Override
    protected void onPostExecute(List<ActivityWithPictures> activities) {
        if (callback != null) {
            callback.accept(activities);
        } else {
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
}
