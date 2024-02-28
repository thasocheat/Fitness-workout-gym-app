package com.example.fitnessworkoutgymapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.fitnessworkoutgymapp.R;
import com.example.fitnessworkoutgymapp.SQLiteDbManager;
import com.example.fitnessworkoutgymapp.UserModel;

import java.util.Set;

public class ProfileEditFragment extends Fragment {

    EditText etFristname, etLastname, etUsername, etEmail, etPassword, etHeight, etWeight;
    ImageView etProfileImage;
    Button btnUpdate;

    private static final int REQUEST_IMAGE_PICK = 1;

    public ProfileEditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_edit, container, false);

        etProfileImage = view.findViewById(R.id.imageViewProfile);
        etHeight = view.findViewById(R.id.editTextHeight);
        etUsername = view.findViewById(R.id.editTextUsername);
        etFristname = view.findViewById(R.id.editTextName);
        etEmail = view.findViewById(R.id.editTextEmail);
        etPassword = view.findViewById(R.id.editTextPassword);
        etWeight = view.findViewById(R.id.editTextWeight);
        btnUpdate = view.findViewById(R.id.buttonUpdate);

        // Retrive the data passed from ProfileFragment
        Bundle args = getArguments();
        if(args != null) {

            etHeight.setText(args.getString("height"));
            etUsername.setText(args.getString("username"));
            etFristname.setText(args.getString("first_name"));
            etEmail.setText(args.getString("email"));
            etPassword.setText(args.getString("password"));
            etWeight.setText(args.getString("weight"));

            // Check if profile image URL is available
            String profileImageUrl = args.getString("profile_image_url");
            Log.d("SQLiteDbManager", "Image Path: " + profileImageUrl);

            if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                // Load profile image using Glide or any other image loading library
                Glide.with(requireContext()).load(profileImageUrl).into(etProfileImage);
            } else {
                // Load default profile image
                etProfileImage.setImageResource(R.drawable.default_profile_pic);
            }

        }

        // Set OnClickListener for the profile image to let the user select a new image
        etProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement logic to let the user select a new image (e.g., using startActivityForResult)
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        });

        // Set OnClickListener for the update button to save the new profile data
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement logic to save the new profile data to the database
                // Retrieve the new data from EditText fields
                String newHeight = etHeight.getText().toString();
                String newUsername = etUsername.getText().toString();
                String newFirstname = etFristname.getText().toString();
                String newEmail = etEmail.getText().toString();
                String newPassword = etPassword.getText().toString();
                String newWeight = etWeight.getText().toString();

                // Log the new data to verify
                Log.d("ProfileEditFragment", "New Height: " + newHeight);
                Log.d("ProfileEditFragment", "New Username: " + newUsername);
                Log.d("ProfileEditFragment", "New Firstname: " + newFirstname);
                Log.d("ProfileEditFragment", "New Email: " + newEmail);
                Log.d("ProfileEditFragment", "New Password: " + newPassword);
                Log.d("ProfileEditFragment", "New Weight: " + newWeight);

                // Update the user's profile data in the database
                SQLiteDbManager dbManager = new SQLiteDbManager(getActivity(), "fitness_db.db", null, 1);
                UserModel currentUser = dbManager.getCurrentUser();
                if (currentUser != null) {
                    // Update user's data
                    currentUser.setHeight(Double.parseDouble(newHeight));
                    currentUser.setUsername(newUsername);
                    currentUser.setFirstname(newFirstname);
                    currentUser.setEmail(newEmail);
                    currentUser.setPassword(newPassword);
                    currentUser.setWeight(Double.parseDouble(newWeight));

                    // Save the updated data to the database
                    dbManager.updateUser(currentUser.getId(), currentUser);

                    // Notify the user that the update was successful
                    Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                // Handle the selected image here (e.g., display a preview)
                Uri selectedImageUri = data.getData();
                // Log the selected image URI to verify
                Log.d("ProfileEditFragment", "Selected Image URI: " + selectedImageUri.toString());
                // You can display a preview of the selected image in an ImageView
                etProfileImage.setImageURI(selectedImageUri);

                // Store the image path in SQLite
                String imagePath = selectedImageUri.toString();
                SQLiteDbManager dbManager = new SQLiteDbManager(getActivity(), "fitness_db.db", null, 1);
                dbManager.storeImagePath(imagePath);
            }
        }
    }
}
