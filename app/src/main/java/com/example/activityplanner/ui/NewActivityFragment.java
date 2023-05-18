package com.example.activityplanner.ui;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.activityplanner.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewActivityFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View root;

    public NewActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewActivityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewActivityFragment newInstance(String param1, String param2) {
        NewActivityFragment fragment = new NewActivityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(root).navigate(R.id.action_newActivityFragment_to_navigation_home);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_new_activity, container, false);
        MaterialDatePicker<Long> datePicker = MaterialDatePicker
                .Builder
                .datePicker()
                .setTitleText(R.string.select_date)
                .build();

        TextView dateTV = (TextView) root.findViewById(R.id.dateTV);
        TextView timeTV = (TextView) root.findViewById(R.id.timeTV);
        Spinner activityTypeSpinner = (Spinner) root.findViewById(R.id.activityTypeSpinner);

        datePicker.addOnPositiveButtonClickListener(selection -> dateTV.setText(datePicker.getHeaderText()));
        dateTV.setOnClickListener(view -> datePicker.show(getParentFragmentManager(), "DatePicker"));

        MaterialTimePicker timePicker = new MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).setTitleText(R.string.select_time).build();
        timeTV.setOnClickListener(view -> timePicker.show(getParentFragmentManager(), "TimePicker"));
        timePicker.addOnPositiveButtonClickListener(view -> timeTV.setText(timePicker.getHour() + ":" + timePicker.getMinute()));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.activity_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityTypeSpinner.setAdapter(adapter);

        return root;
    }
}