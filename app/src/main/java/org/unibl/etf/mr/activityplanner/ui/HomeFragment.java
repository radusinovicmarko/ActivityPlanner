package org.unibl.etf.mr.activityplanner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import org.unibl.etf.mr.activityplanner.R;
import org.unibl.etf.mr.activityplanner.adapter.ActivityAdapter;
import org.unibl.etf.mr.activityplanner.database.PlannerDatabase;
import org.unibl.etf.mr.activityplanner.database.dto.ActivityWithPictures;
import org.unibl.etf.mr.activityplanner.database.entities.Activity;
import org.unibl.etf.mr.activityplanner.databinding.FragmentHomeBinding;
import org.unibl.etf.mr.activityplanner.services.RetrieveAllByDate;
import org.unibl.etf.mr.activityplanner.services.RetrieveAllTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private PlannerDatabase database;
    private CalendarView calendarView;
    private static final String ACTIVITY_ARG = "Activity";
    private Date from;
    private Date to;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        database = PlannerDatabase.getInstance(getContext());

        calendarView = binding.calendarView;

        new RetrieveAllTask(this::listEvents, database).execute();

        recyclerView = binding.activityList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        calendarView.setOnDayClickListener(eventDay -> {
            Date date = eventDay.getCalendar().getTime();
            binding.dateSelectedTV.setVisibility(View.VISIBLE);
            binding.dateSelectedTV.setText(getResources().getString(R.string.date_selected, new SimpleDateFormat("dd. MM. yyyy.").format(date)));
            from = new Date(date.getTime());
            from.setHours(0);
            from.setMinutes(0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(from);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            to = calendar.getTime();
            new RetrieveAllByDate(database, this::listActivities).execute(from, to);
        });

        return root;
    }

    private void listEvents(List<ActivityWithPictures> activities) {
        List<EventDay> events = new ArrayList<>();
        for (ActivityWithPictures activityWithPictures : activities) {
            Activity activity = activityWithPictures.getActivity();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(activity.getDate());
            TypedValue typedValue = new TypedValue();
            requireContext().getTheme().resolveAttribute(R.attr.circle_icon, typedValue, true);
            EventDay eventDay = new EventDay(calendar, Objects.requireNonNull(ContextCompat.getDrawable(requireContext(), typedValue.resourceId)));
            events.add(eventDay);
        }
        calendarView.setEvents(events);
    }

    private void listActivities(List<ActivityWithPictures> activities) {
        if (activities.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            binding.noItemsTv.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            binding.noItemsTv.setVisibility(View.GONE);
            ActivityAdapter adapter = new ActivityAdapter(activities, getContext(), (position) -> {
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                intent.putExtra(getResources().getString(R.string.intent_extra_activity), activities.get(position));
                startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.activityList.setVisibility(View.GONE);
        new RetrieveAllTask(this::listEvents, database).execute();
        if (from != null && to != null) {
            new RetrieveAllByDate(database, this::listActivities).execute(from, to);
        } else {
            binding.dateSelectedTV.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}