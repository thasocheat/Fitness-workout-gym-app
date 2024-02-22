package com.example.fitnessworkoutgymapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class activity_login extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Button to signup screen
        btnSignup = findViewById(R.id.btnSignUp);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the signup activity
                startActivity(new Intent(activity_login.this, activity_signup.class));
            }
        });


        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword1);
        btnLogin = findViewById(R.id.btnsubmit);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform user authentication
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (isValidCredentials(username, password)) {
                    // Navigate to the main activity
                    startActivity(new Intent(activity_login.this, MainActivity.class));
                    finish(); // Finish the login activity
                } else {
                    // Display error message
                    Toast.makeText(activity_login.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to validate user credentials (replace this with actual authentication logic)
    private boolean isValidCredentials(String username, String password) {
        // Query the database to check if the provided username and password match a user record
        SQLiteDbManager dbManager = new SQLiteDbManager(activity_login.this, "fitness_db.db", null, 1);
        boolean isValid = dbManager.authenticateUser(username, password);

        return isValid;
    }
}