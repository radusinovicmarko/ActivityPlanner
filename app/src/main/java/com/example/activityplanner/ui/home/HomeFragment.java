package com.example.activityplanner.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarDay;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.example.activityplanner.R;
import com.example.activityplanner.adapter.ActivityAdapter;
import com.example.activityplanner.database.PlannerDatabase;
import com.example.activityplanner.database.dto.ActivityWithPictures;
import com.example.activityplanner.database.entities.Activity;
import com.example.activityplanner.databinding.FragmentHomeBinding;
import com.example.activityplanner.services.RetrieveAllByDate;
import com.example.activityplanner.services.RetrieveAllTask;
import com.example.activityplanner.ui.DetailsActivity;
import com.example.activityplanner.ui.NewActivityActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private static final String ACTIVITY_ARG = "Activity";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        PlannerDatabase database = PlannerDatabase.getInstance(getContext());

        CalendarView calendarView = binding.calendarView;
        List<EventDay> events = new ArrayList<>();

        RecyclerView recyclerView = binding.activityList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        new RetrieveAllTask(activities -> {
            for (ActivityWithPictures activityWithPictures : activities) {
                Activity activity = activityWithPictures.getActivity();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(activity.getDate());
                EventDay eventDay = new EventDay(calendar, R.drawable.ic_shortcut_fiber_manual_record);
                events.add(eventDay);
            }
            calendarView.setEvents(events);
        }, database).execute();

        calendarView.setOnDayClickListener(eventDay -> {
            Date date = eventDay.getCalendar().getTime();
            binding.dateSelectedTV.setText(getResources().getString(R.string.date_selected, new SimpleDateFormat("dd. MM. yyyy.").format(date)));
            Date from = new Date(date.getTime());
            from.setHours(0);
            from.setMinutes(0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(from);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            Date to = calendar.getTime();
            new RetrieveAllByDate(database, activities -> {
                if (activities.size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    binding.noItemsTv.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    binding.noItemsTv.setVisibility(View.GONE);
                    ActivityAdapter adapter = new ActivityAdapter(activities, getContext(), (position) -> {
                        Intent intent = new Intent(getContext(), DetailsActivity.class);
                        intent.putExtra(ACTIVITY_ARG, activities.get(position));
                        startActivity(intent);
                    });
                    recyclerView.setAdapter(adapter);
                }
            }).execute(from, to);
        });

        binding.fabAdd.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), NewActivityActivity.class);
            startActivity(i);
        });

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}