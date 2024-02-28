package com.example.fitnessworkoutgymapp.fragments;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
//import com.parse.GetDataCallback;
//import com.parse.ParseException;
//import com.parse.ParseFile;
//import com.parse.ParseUser;
import com.bumptech.glide.Glide;
import com.example.fitnessworkoutgymapp.SQLiteDbManager;
import com.example.fitnessworkoutgymapp.UserModel;
import com.example.fitnessworkoutgymapp.activity_login;
//import com.example.fitnessworkoutgymapp.ProfileEdit;
import com.example.fitnessworkoutgymapp.R;

public class ProfileFragment extends Fragment {
    TextView tvName;
    TextView tvUsername;
    TextView tvUserHeight;
    TextView tvUserWeight;
    RecyclerView rvWorkouts;
    ImageView ivProfileImage;
    ImageButton btnLogout;
    ImageButton btnEdit;
    String TAG = "ProfileFragment";

    // Request code for runtime permission
    private static final int REQUEST_PERMISSION_CODE = 1001;

    public ProfileFragment() {
        // Required empty public constructor
    }
    // The onCreateView method is called when Fragment should create its View object hierarchy
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
    // This event is triggered soon after onCreateView().
    // Any view setup should occur here. E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvName = view.findViewById(R.id.tvUsernameProf);
        tvUsername = view.findViewById(R.id.tvName);
        tvUserHeight = view.findViewById(R.id.tvUserHeight);
        tvUserWeight = view.findViewById(R.id.tvUserWeight);
        rvWorkouts = view.findViewById(R.id.rvWorkouts);
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnEdit = view.findViewById(R.id.btnEdit);

//        requestStoragePermission();
        // Logout button
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Update the logged in status to 0, it mean the user is not logged in
                SQLiteDbManager dbManager = new SQLiteDbManager(getActivity(), "fitness_db.db", null, 1);
                UserModel currentUser = dbManager.getCurrentUser();

                if (currentUser != null) {
                    // Update the loggedIn to 0
                    currentUser.setLoggedIn(0);
                    dbManager.updateLoggedInStatus(currentUser.getUsername(), currentUser.getLoggedIn());

                    // Redirect to login screen
                    Intent i = new Intent(getContext(), activity_login.class);
                    startActivity(i);
                    getActivity().finish();

                    // Show the message when logout
                    Toast.makeText(getContext(), "Logout Successful", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "Logout Failed", Toast.LENGTH_SHORT).show();
                }



            }
        });
        // Edit Profile
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetch user data from the database
                SQLiteDbManager dbManager = new SQLiteDbManager(getActivity(), "fitness_db.db", null, 1);
                UserModel currentUser = dbManager.getCurrentUser();

                if (currentUser != null) {
                    // Create a new instance of ProfileEditFragment
                    ProfileEditFragment profileEditFragment = new ProfileEditFragment();

                    // Pass any necessary data to ProfileEditFragment using a Bundle
                    Bundle args = new Bundle();
                    args.putString("email", currentUser.getEmail());
                    args.putString("password", currentUser.getPassword());
                    args.putString("first_name", currentUser.getFirstname());
                    args.putString("username", currentUser.getUsername());
                    args.putString("height", String.valueOf(currentUser.getHeight()));
                    args.putString("weight", String.valueOf(currentUser.getWeight()));
                    profileEditFragment.setArguments(args);

                    // Replace the current fragment with ProfileEditFragment
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.flContainer, profileEditFragment)
                            .addToBackStack(null)
                            .commit();
                } else {
                    // Handle case when user data is not available
                    Toast.makeText(getContext(), "User data not available", Toast.LENGTH_SHORT).show();
                }



            }
        });

        // show current user
        getCurrentUser();

    }
    public UserModel getCurrentUser() {
        // After login, Parse will cache it on disk, so
        // we don't need to login every time we open this
        // application

        SQLiteDbManager dbManager = new SQLiteDbManager(getActivity(), "fitness_db.db", null, 1);
        UserModel user = dbManager.getCurrentUser(); // Assuming getCurrentUser() returns a User object

        if (user != null) {
            tvName.setText("@" + user.getUsername());
            tvUsername.setText(user.getFirstname() + " " + user.getLastname());
            tvUserHeight.setText(String.valueOf(user.getHeight()));
            tvUserWeight.setText(String.valueOf(user.getWeight()));

            // Load profile image if available
            // Assuming you have stored profile image URI in the User object
            String profileImageUri = user.getProfileImageUri();
            Log.d("SQLiteDbManager", "Image Path: " + profileImageUri);

            if (profileImageUri != null && !profileImageUri.isEmpty()) {
                // Load the image using a Bitmap
                Bitmap bitmap = BitmapFactory.decodeFile(profileImageUri);
                if(bitmap != null){
                    ivProfileImage.setImageBitmap(bitmap);
                }else{
                    // If bitmap is null, handle the error or set a default image
                    ivProfileImage.setImageResource(R.drawable.default_profile_pic);
                }
            }else {

                // If bitmap is null, handle the error or set a default image
                ivProfileImage.setImageResource(R.drawable.default_profile_pic);
            }
        } else {
            // User not found or logged out, handle accordingly
            Toast.makeText(getContext(), "User not found or logged out", Toast.LENGTH_SHORT).show();
        }


        return user;
    }

    // Method to request storage permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Request the permission
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_CODE);
        } else {
            // Permission has already been granted
            // You can proceed with accessing the image file
        }
    }

    // Override onRequestPermissionsResult to handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_CODE) {
            // Check if the permission request is granted
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                // You can proceed with accessing the image file
                loadProfileImage();
            } else {
                // Permission is denied
                // Handle the denial or inform the user
                Toast.makeText(requireContext(), "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    // Load profile image if permission is granted
    private void loadProfileImage() {
        // Load profile image if available
        // Assuming you have stored profile image URI in the User object
        SQLiteDbManager dbManager = new SQLiteDbManager(getActivity(), "fitness_db.db", null, 1);
        UserModel user = dbManager.getCurrentUser();

        String profileImageUri = user.getProfileImageUri();
        Log.d("SQLiteDbManager", "Image Path: " + profileImageUri);

        if (profileImageUri != null && !profileImageUri.isEmpty()) {
            // Load the image using a Bitmap
            Bitmap bitmap = BitmapFactory.decodeFile(profileImageUri);
            if (bitmap != null) {
                ivProfileImage.setImageBitmap(bitmap);
            } else {
                // If bitmap is null, handle the error or set a default image
                ivProfileImage.setImageResource(R.drawable.default_profile_pic);
            }
        } else {
            // If bitmap is null, handle the error or set a default image
            ivProfileImage.setImageResource(R.drawable.default_profile_pic);
        }
    }

}