package com.example.activityplanner.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.activityplanner.R;
import com.example.activityplanner.adapter.ActivityAdapter;
import com.example.activityplanner.database.dto.ActivityWithPictures;

import java.util.List;

public class UpcomingActivity extends AppCompatActivity {
    private static final String ACTIVITY_ARG = "Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);

        RecyclerView recyclerView = findViewById(R.id.activityList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<ActivityWithPictures> activities = (List<ActivityWithPictures>) getIntent().getSerializableExtra("activities");
        ActivityAdapter adapter = new ActivityAdapter(activities, this, (position) -> {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(ACTIVITY_ARG, activities.get(position));
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }
}