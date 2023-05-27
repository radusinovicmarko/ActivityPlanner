package com.example.activityplanner.ui;

import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.activityplanner.BuildConfig;
import com.example.activityplanner.R;
import com.example.activityplanner.database.PlannerDatabase;
import com.example.activityplanner.database.dto.ActivityWithPictures;
import com.example.activityplanner.database.entities.Picture;
import com.example.activityplanner.database.enums.ActivityType;
import com.example.activityplanner.services.InsertActivityTask;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class NewActivityActivity extends AppCompatActivity implements ActivityResultCaller {
    private Place location;
    private Integer hours, minutes;
    private Date date;
    private ActivityType type = ActivityType.WORK;
    private PlannerDatabase plannerDatabase;
    private List<String> pictureUris = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_activity);

        plannerDatabase = PlannerDatabase.getInstance(this);

        // Initialize the SDK
        Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);

        MaterialDatePicker<Long> datePicker = MaterialDatePicker
                .Builder
                .datePicker()
                .setTitleText(R.string.select_date)
                .build();

        TextView dateTV = findViewById(R.id.dateTV);
        TextView timeTV = findViewById(R.id.timeTV);
        Spinner activityTypeSpinner = findViewById(R.id.activityTypeSpinner);
        datePicker.addOnPositiveButtonClickListener(selection -> {
            date = new Date(selection);
            dateTV.setText(datePicker.getHeaderText());
        });
        dateTV.setOnClickListener(view -> datePicker.show(getSupportFragmentManager(), "DatePicker"));

        MaterialTimePicker timePicker = new MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).setTitleText(R.string.select_time).build();
        timeTV.setOnClickListener(view -> timePicker.show(getSupportFragmentManager(), "TimePicker"));
        timePicker.addOnPositiveButtonClickListener(view -> {
            hours = timePicker.getHour();
            minutes = timePicker.getMinute();
            timeTV.setText(String.format("%02d:%02d", hours, minutes));
        });

        findViewById(R.id.locationTV).setOnClickListener(view -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                    .build(this);
            startAutocomplete.launch(intent);
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.activity_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityTypeSpinner.setAdapter(adapter);
        activityTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = ActivityType.values()[i];
                if (type == ActivityType.FREE_TIME) {
                    findViewById(R.id.addPicturesBtn).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.addPicturesBtn).setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        findViewById(R.id.nextBtn).setOnClickListener(this::nextClick);

        findViewById(R.id.addPicturesBtn).setOnClickListener(view -> {
            Intent intent = new Intent(this, AddPicturesActivity.class);
            intent.putExtra("pictures", (Serializable) pictureUris);
            startAddPictures.launch(intent);
        });
    }

    private void nextClick(View view) {
        String title = String.valueOf(((EditText) findViewById(R.id.titleET)).getText());
        String description = String.valueOf(((EditText) findViewById(R.id.descriptionET)).getText());
        if (date == null || hours == null || minutes == null || location == null || title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, R.string.create_activity_error, Toast.LENGTH_SHORT).show();
        } else {
            //TODO: fix deprecated
            date.setHours(hours);
            date.setMinutes(minutes);
            com.example.activityplanner.database.entities.Activity activity =
                    new com.example.activityplanner.database.entities.Activity(title, description, date, location.getName(), location.getLatLng().latitude, location.getLatLng().longitude, type);
            new InsertActivityTask(() -> {
                Toast.makeText(this, R.string.activity_created, Toast.LENGTH_SHORT).show();
                finish();
            }, plannerDatabase).execute(new ActivityWithPictures(activity, pictureUris.stream().map(Picture::new).collect(Collectors.toList())));
        }
    }

    private final ActivityResultLauncher<Intent> startAutocomplete = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        location = Autocomplete.getPlaceFromIntent(intent);
                        TextView locationTV = findViewById(R.id.locationTV);
                        locationTV.setText(location.getName());
                    }
                }
            });

    private final ActivityResultLauncher<Intent> startAddPictures = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        //TODO: fix hardcoded names
                        pictureUris = (List<String>) intent.getSerializableExtra("pictures");
                    }
                }
            });
}