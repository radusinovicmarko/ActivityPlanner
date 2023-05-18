package com.example.activityplanner.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activityplanner.MainActivity;
import com.example.activityplanner.R;
import com.example.activityplanner.adapter.ActivityAdapter;
import com.example.activityplanner.database.PlannerDatabase;
import com.example.activityplanner.database.dto.ActivityWithPictures;
import com.example.activityplanner.database.entities.Activity;
import com.example.activityplanner.databinding.FragmentDashboardBinding;
import com.example.activityplanner.services.RetrieveAllByTitleTask;
import com.example.activityplanner.services.RetrieveAllTask;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private PlannerDatabase plannerDatabase;
    private List<ActivityWithPictures> activities;
    private ActivityAdapter adapter;
    private int position;
    private TextView noItemsTv;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.activityList;
        noItemsTv = binding.noItemsTv;
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        activities = new ArrayList<>();
        adapter = new ActivityAdapter(activities, getContext(), (position) -> Toast.makeText(getContext(), "Click", Toast.LENGTH_LONG));
        recyclerView.setAdapter(adapter);
        plannerDatabase = PlannerDatabase.getInstance(getContext());
        new RetrieveAllTask(this).execute();

        SearchView searchView = binding.activitySearchView;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                new RetrieveAllByTitleTask(DashboardFragment.this).execute(newText);
                return false;
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public PlannerDatabase getPlannerDatabase() {
        return plannerDatabase;
    }

    public List<ActivityWithPictures> getActivities() {
        return activities;
    }

    public ActivityAdapter getAdapter() {
        return adapter;
    }

    public TextView getNoItemsTv() {
        return noItemsTv;
    }
}