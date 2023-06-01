package com.example.activityplanner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activityplanner.adapter.ActivityAdapter;
import com.example.activityplanner.database.PlannerDatabase;
import com.example.activityplanner.database.dto.ActivityWithPictures;
import com.example.activityplanner.services.RetrieveAllByTitleTask;
import com.example.activityplanner.services.RetrieveAllTask;
import com.example.activityplanner.databinding.FragmentListBinding;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    private FragmentListBinding binding;
    private PlannerDatabase plannerDatabase;
    private List<ActivityWithPictures> activities;
    private TextView noItemsTv;
    private static final String ACTIVITY_ARG = "Activity";
    private String searchPattern;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.activityList;
        noItemsTv = binding.noItemsTv;
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        plannerDatabase = PlannerDatabase.getInstance(getContext());
        new RetrieveAllTask(this::getActivities, plannerDatabase).execute();

        SearchView searchView = binding.activitySearchView;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchPattern = newText;
                new RetrieveAllByTitleTask(ListFragment.this::getActivities, plannerDatabase).execute(newText);
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

    private void getActivities(List<ActivityWithPictures> activities) {
        if (activities != null && activities.size() > 0) {
            noItemsTv.setVisibility(View.GONE);
        } else {
            noItemsTv.setVisibility(View.VISIBLE);
        }
        ActivityAdapter adapter = new ActivityAdapter(activities, getContext(), (position) -> {
            Intent intent = new Intent(getContext(), DetailsActivity.class);
            intent.putExtra(ACTIVITY_ARG, activities.get(position));
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (searchPattern != null) {
            new RetrieveAllByTitleTask(this::getActivities, plannerDatabase).execute(searchPattern);
        } else {
            new RetrieveAllTask(this::getActivities, plannerDatabase).execute();
        }
    }
}