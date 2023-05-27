package com.example.activityplanner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    private ActivityAdapter adapter;
    private int position;
    private TextView noItemsTv;
    private static final String ACTIVITY_ARG = "Activity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.activityList;
        noItemsTv = binding.noItemsTv;
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        activities = new ArrayList<>();
        //TODO: refresh on result
        adapter = new ActivityAdapter(activities, getContext(), (position) -> {
            Intent intent = new Intent(getContext(), DetailsActivity.class);
            intent.putExtra(ACTIVITY_ARG, activities.get(position));
            startActivity(intent);
        });
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
                new RetrieveAllByTitleTask(ListFragment.this).execute(newText);
                return false;
            }
        });

        binding.fabAdd.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), NewActivityActivity.class);
            startActivity(i);
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