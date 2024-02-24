package com.example.fitnessworkoutgymapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.parse.ParseUser;

import com.example.fitnessworkoutgymapp.fragments.HomeFragment;
import com.example.fitnessworkoutgymapp.fragments.LearnFragment;
import com.example.fitnessworkoutgymapp.fragments.ProfileFragment;
import com.example.fitnessworkoutgymapp.fragments.TrackFragment;

public class MainActivity extends AppCompatActivity {

    private final FragmentManager mFragmentManager = getSupportFragmentManager();
    private BottomNavigationView mBottomNavigationView;

    MenuItem home_item, learn_item, track_item, profile_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationView = findViewById(R.id.bottomNavigation);

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Fragment fragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.action_home) {
                    fragment = new HomeFragment();
                } else if (itemId == R.id.action_learn) {
                    fragment = new LearnFragment();
                } else if (itemId == R.id.action_track) {
                    fragment = new TrackFragment();
                } else{
                    fragment = new ProfileFragment();
                }

                // If fragment is not null, replace the current fragment
                if (fragment != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.flContainer, fragment)
                            .commit();
                    return true;

                }else {
                    return false;
                }
            }
        });
        mBottomNavigationView.setSelectedItemId(R.id.action_home);
    }
}