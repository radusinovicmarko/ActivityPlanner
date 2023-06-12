package org.unibl.etf.mr.activityplanner.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.mr.activityplanner.R;
import org.unibl.etf.mr.activityplanner.adapter.ActivityAdapter;
import org.unibl.etf.mr.activityplanner.database.PlannerDatabase;
import org.unibl.etf.mr.activityplanner.database.dto.ActivityWithPictures;
import org.unibl.etf.mr.activityplanner.services.RetrieveAllByDate;

import java.util.Date;
import java.util.List;

public class UpcomingActivity extends AppCompatActivity {
    private Date from;
    private Date to;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);

        recyclerView = findViewById(R.id.activityList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<ActivityWithPictures> activities = (List<ActivityWithPictures>) getIntent().getSerializableExtra(getResources().getString(R.string.intent_extra_activities));
        from = (Date) getIntent().getSerializableExtra(getResources().getString(R.string.intent_extra_from));
        to = (Date) getIntent().getSerializableExtra(getResources().getString(R.string.intent_extra_to));
        ActivityAdapter adapter = new ActivityAdapter(activities, this, (position) -> {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(getResources().getString(R.string.intent_extra_activity), activities.get(position));
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new RetrieveAllByDate(PlannerDatabase.getInstance(this), activities -> {
            ActivityAdapter adapter = new ActivityAdapter(activities, this, (position) -> {
                Intent intent = new Intent(this, DetailsActivity.class);
                intent.putExtra(getResources().getString(R.string.intent_extra_activity), activities.get(position));
                startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
        }).execute(from, to);
    }
}