package com.example.activityplanner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.example.activityplanner.database.PlannerDatabase;
import com.example.activityplanner.database.enums.NotificationOption;
import com.example.activityplanner.databinding.ActivityMainBinding;
import com.example.activityplanner.services.RetrieveAllByDate;
import com.example.activityplanner.ui.NewActivityActivity;
import com.example.activityplanner.ui.UpcomingActivity;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.activityplanner.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_list, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
            if (navDestination.getId() == R.id.navigation_settings) {
                findViewById(R.id.fabAdd).setVisibility(View.GONE);
            } else {
                findViewById(R.id.fabAdd).setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.fabAdd).setOnClickListener(view -> {
            Intent i = new Intent(this, NewActivityActivity.class);
            startActivity(i);
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String optionString = sharedPreferences.getString(getResources().getString(R.string.notifications_preference_key), getResources().getString(R.string.notifications_preference_default));
        NotificationOption option = NotificationOption.values()[Integer.parseInt(optionString)];
        if (option != NotificationOption.OFF) {
            sendNotification(option);
        }
        setLocale(getBaseContext());
        recreate();
    }

    private void sendNotification(NotificationOption option) {
        Calendar calendar = Calendar.getInstance();
        Date from = calendar.getTime();
        Date to;
        if (option == NotificationOption.HOUR) {
            calendar.add(Calendar.HOUR, 1);
        } else if (option == NotificationOption.DAY) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        } else {
            calendar.add(Calendar.DAY_OF_YEAR, 7);
        }
        to = calendar.getTime();
        new RetrieveAllByDate(PlannerDatabase.getInstance(this), activities -> {
            TypedValue typedValue = new TypedValue();
            int primaryColor;
            int colorOnPrimary;
            try (TypedArray a = obtainStyledAttributes(typedValue.data, new int[] { com.google.android.material.R.attr.colorPrimary, com.google.android.material.R.attr.colorOnPrimary })) {
                primaryColor = a.getColor(0, 0);
                colorOnPrimary = a.getColor(1, 0);
            }
            Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator), getResources().getString(R.string.notifications_text, activities.size()), Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(primaryColor)
                    .setActionTextColor(colorOnPrimary);
            if (activities.size() > 0) {
                snackbar.setAction(R.string.snackbar_btn, view -> {
                    Intent intent = new Intent(this, UpcomingActivity.class);
                    intent.putExtra(getResources().getString(R.string.intent_extra_activities), (Serializable) activities);
                    intent.putExtra(getResources().getString(R.string.intent_extra_from), from);
                    intent.putExtra(getResources().getString(R.string.intent_extra_to), to);
                    startActivity(intent);
                });
            }
            snackbar.show();
        }).execute(from, to);
    }

    private void setLocale(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String language = sharedPreferences.getString("language", context.getResources().getString(R.string.language_preference_default));
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = context.getResources().getConfiguration();
        config.setLocale(locale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    @Override
    protected void attachBaseContext(Context base) {
        setLocale(base);
        super.attachBaseContext(base);
    }

}