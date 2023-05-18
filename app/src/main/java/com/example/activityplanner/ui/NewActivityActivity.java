package com.example.activityplanner.ui;

import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.activityplanner.BuildConfig;
import com.example.activityplanner.R;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewActivityActivity extends AppCompatActivity implements ActivityResultCaller {
    private Place location;
    private Integer hours, minutes;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_activity);

        // Initialize the SDK
        Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);

        MaterialDatePicker<Long> datePicker = MaterialDatePicker
                .Builder
                .datePicker()
                .setTitleText(R.string.select_date)
                .build();

        TextView dateTV = (TextView) findViewById(R.id.dateTV);
        TextView timeTV = (TextView) findViewById(R.id.timeTV);
        Spinner activityTypeSpinner = (Spinner) findViewById(R.id.activityTypeSpinner);
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

        findViewById(R.id.nextBtn).setOnClickListener(view -> {
            date.setHours(hours);
            date.setMinutes(minutes);
            Toast.makeText(this, date.toString() + " " + location.getLatLng() + " " + location.getName(), Toast.LENGTH_SHORT).show();
        });
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
}