package com.example.activityplanner.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.activityplanner.R;
import com.example.activityplanner.database.PlannerDatabase;
import com.example.activityplanner.database.dto.ActivityWithPictures;
import com.example.activityplanner.database.entities.Picture;
import com.example.activityplanner.database.enums.ActivityType;
import com.example.activityplanner.services.DeleteActivityTask;
import com.google.android.material.chip.Chip;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private static final String ACTIVITY_ARG = "Activity";
    private int currentIndex = 0;
    private List<Picture> pictures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ActivityWithPictures activity = (ActivityWithPictures) getIntent().getSerializableExtra(ACTIVITY_ARG);
        pictures = activity.getPictures();

        TextView titleTV = findViewById(R.id.titleTV);
        titleTV.setText(activity.getActivity().getTitle());

        TextView descriptionTV = findViewById(R.id.descriptionTV);
        descriptionTV.setText(activity.getActivity().getDescription());

        TextView dateTV = findViewById(R.id.dateTV);
        dateTV.setText(new SimpleDateFormat("dd. MM. yyyy. HH:mm").format(activity.getActivity().getDate()));

        Chip typeChip = findViewById(R.id.typeChip);
        ActivityType type = activity.getActivity().getType();
        switch (type) {
            case WORK:
                typeChip.setText(R.string.activity_type_work);
                break;
            case FREE_TIME:
                typeChip.setText(R.string.activity_type_free_time);
                break;
            case TRAVEL:
                typeChip.setText(R.string.activity_type_travel);
                break;
        }

        TextView locationTV = findViewById(R.id.locationTV);
        locationTV.setText(getResources().getString(R.string.details_location, activity.getActivity().getLocationName()));

        if (type == ActivityType.TRAVEL) {
            findViewById(R.id.fragment_container_view).setVisibility(View.VISIBLE);
            Bundle bundle = new Bundle();
            bundle.putDouble("lat", activity.getActivity().getLocationLatitude());
            bundle.putDouble("long", activity.getActivity().getLocationLongitude());
            bundle.putString("name", activity.getActivity().getLocationName());

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, MapsFragment.class, bundle)
                    .commit();
        } else if (type == ActivityType.FREE_TIME) {
            findViewById(R.id.carousel).setVisibility(View.VISIBLE);
            loadCarousel();
            findViewById(R.id.leftBtn).setOnClickListener(view -> {
                currentIndex = currentIndex == 0 ? pictures.size() - 1 : currentIndex - 1;
                loadCarousel();
            });
            findViewById(R.id.rightBtn).setOnClickListener(view -> {
                currentIndex = currentIndex == pictures.size() - 1 ? 0 : currentIndex + 1;
                loadCarousel();
            });
        }

        findViewById(R.id.fabDelete).setOnClickListener(view -> new AlertDialog.Builder(this)
                .setTitle(R.string.alert_delete_title)
                .setMessage(R.string.alert_delete_message)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> new DeleteActivityTask(PlannerDatabase.getInstance(this), () -> {
                    Toast.makeText(this, R.string.activity_deleted, Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }).execute(activity))
                .setNegativeButton(android.R.string.no, null).show());
    }

    private void loadCarousel() {
        if (pictures.size() == 0) {
            findViewById(R.id.carousel).setVisibility(View.GONE);
        }
        else {
            findViewById(R.id.carousel).setVisibility(View.VISIBLE);
            ImageView imageView = findViewById(R.id.imageView);
            Picasso.get().load(pictures.get(currentIndex).getUri()).into(imageView);

        }
    }
}