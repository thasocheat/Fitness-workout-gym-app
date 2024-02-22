package com.example.fitnessworkoutgymapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class activity_signup extends AppCompatActivity {

    private EditText etFirstName, etLastName, etEmail, etWeight, eHeight, etAge, etUserName, etPassword;

    private Button btnSubmit;

    private ImageButton btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etFirstName = findViewById(R.id.etfirstname);
        etLastName = findViewById(R.id.etlastname);
        etEmail = findViewById(R.id.etEmailAddress);
        etWeight = findViewById(R.id.etWeight);
        eHeight = findViewById(R.id.etHeight);
        etAge = findViewById(R.id.etAge);
        etUserName = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBack = findViewById(R.id.back);

        // Back button to login screen
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_signup.this, activity_login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        // Inside the onClickListener for the submit button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve user input from EditText fields
                String firstName = etFirstName.getText().toString();
                String lastName = etLastName.getText().toString();
                String email = etEmail.getText().toString();

                // Parse double values
                double weight = 0;
                double height = 0;
                try {
                    weight = Double.parseDouble(etWeight.getText().toString());
                    height = Double.parseDouble(eHeight.getText().toString());
                } catch (NumberFormatException e) {
                    // Handle parsing error
                    Toast.makeText(activity_signup.this, "Please enter valid weight and height", Toast.LENGTH_SHORT).show();
                    return; // Exit method early
                }

                // Parse int value
                int age = 0;
                try {
                    age = Integer.parseInt(etAge.getText().toString());
                } catch (NumberFormatException e) {
                    // Handle parsing error
                    Toast.makeText(activity_signup.this, "Please enter a valid age", Toast.LENGTH_SHORT).show();
                    return; // Exit method early
                }

                String username = etUserName.getText().toString();
                String password = etPassword.getText().toString();

                // Insert user data into the database
                SQLiteDbManager dbManager = new SQLiteDbManager(activity_signup.this, "fitness_db.db", null, 1);
                boolean success = dbManager.addUser(firstName, lastName, email, weight, height, age, username, password);

                if (success) {
                    Toast.makeText(activity_signup.this, "User added successfully", Toast.LENGTH_SHORT).show();
                    // Optionally, navigate to login activity
                    Intent intent = new Intent(activity_signup.this, activity_login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(activity_signup.this, "Failed to add user", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }
}
