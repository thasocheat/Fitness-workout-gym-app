package com.example.fitnessworkoutgymapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessworkoutgymapp.DateSummary;
import com.example.fitnessworkoutgymapp.HomeWorkoutListAdapter;
import com.example.fitnessworkoutgymapp.R;
import com.example.fitnessworkoutgymapp.SQLiteDbManager;
import com.example.fitnessworkoutgymapp.UserModel;
import com.example.fitnessworkoutgymapp.Workout;
import com.example.fitnessworkoutgymapp.activity_signup;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    TextView tvUserName;
    TextView tvDate;
    TextView tvActivity;
    TextView tvCalories;
    RecyclerView rvWorkouts;
    private String TAG = "HomeFragment";

    List<String> workout;   //workout name
    List<String> listTime;  //time for each workout
    List<String> indivCalo; //indivial calories per workout
    HomeWorkoutListAdapter adapter;
    private Double totalCal;
    private int totalTime;
    private static DecimalFormat df2 = new DecimalFormat("#.##");



    public HomeFragment() {
        // Required empty public constructor
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_home, container, false);



        tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        tvDate = (TextView) view.findViewById(R.id.tvDate);
        tvCalories = (TextView) view.findViewById(R.id.tvCalories);
        tvActivity = (TextView) view.findViewById(R.id.tvActivity);
        getCurrentUser();
        workout = new ArrayList<>();
        listTime = new ArrayList<>();
        indivCalo = new ArrayList<>();
        totalCal = 0.0;

        rvWorkouts = (RecyclerView) view.findViewById(R.id.rvWorkouts);
        rvWorkouts.setLayoutManager(new LinearLayoutManager(getContext()));

// Initialize the adapter
        adapter = new HomeWorkoutListAdapter(workout, listTime, indivCalo, this);
        rvWorkouts.setAdapter(adapter);

        populateWorkout();



        tvCalories.setText(String.valueOf(df2.format(totalCal)));
        Log.e(TAG, String.valueOf(workout.size()));

        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here. E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void getCurrentUser() {
        // After login, Parse will cache it on disk, so
        // we don't need to login every time we open this
        // application

        SQLiteDbManager dbManager = new SQLiteDbManager(getContext(), "fitness_db.db", null, 1);

        // Retrieve the current user from the database
        UserModel currentUser = dbManager.getCurrentUser();

        // Check if the current user exists
        if (currentUser != null) {
            // If the current user exists, set the username in tvUserName TextView
            // and set the current date in tvDate TextView
            tvUserName.setText("Hello " + currentUser.getUsername());
            tvDate.setText(DateSummary.getDate()); // Assuming DateSummary is a class providing date-related functionality
        } else {
            // If the current user doesn't exist, show the signup or login screen
            Toast.makeText(getContext(), "Please login first", Toast.LENGTH_LONG).show();
        }


    }

    public void populateWorkout() {
        SQLiteDbManager dbManager = new SQLiteDbManager(getContext(), "fitness_db.db", null, 1);
        UserModel currentUser = dbManager.getCurrentUser();

        int userId = currentUser.getId();

        // Retrieve workout data from SQLite database
        List<Workout> workoutList = dbManager.getAllWorkouts(userId); // Implement this method in your SQLiteDbManager class

        // Clear existing data
        workout.clear();
        listTime.clear();
        indivCalo.clear();
        totalCal = 0.0;
        totalTime = 0;

        // Populate lists and calculate total calories and total time
        for (Workout w : workoutList) {
            workout.add(w.getWorkoutType());
            listTime.add(w.getDuration());
            indivCalo.add(String.valueOf(w.getCalories()));
            totalCal += w.getCalories();

            String duration = w.getDuration();
            if (!duration.isEmpty()) {
                String[] timeParts = duration.split(":");
                if (timeParts.length == 2) {
                    int minutes = Integer.parseInt(timeParts[0]);
                    int seconds = Integer.parseInt(timeParts[1]);
                    totalTime += (minutes * 60) + seconds;
                } else {
                    Log.e(TAG, "Invalid duration format: " + duration);
                }
            } else {
                Log.e(TAG, "Duration is empty");
            }

        }

        // Update UI
        tvCalories.setText(String.valueOf(df2.format(totalCal)));
        String finalSec = "";
        if ((totalTime % 60) < 10) {
            finalSec = "0" + String.valueOf(totalTime % 60);
        } else {
            finalSec = String.valueOf(totalTime % 60);
        }
        String finalTime = totalTime / 60 + ":" + finalSec;
        tvActivity.setText(finalTime);

        // Notify adapter
        adapter.notifyDataSetChanged();

    }

}