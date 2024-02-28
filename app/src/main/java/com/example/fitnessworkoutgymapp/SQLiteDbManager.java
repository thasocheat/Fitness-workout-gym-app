package com.example.fitnessworkoutgymapp;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SQLiteDbManager extends SQLiteOpenHelper {
    public SQLiteDbManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create the users table

        String createUserTableQuery = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "first_name TEXT, last_name TEXT, email TEXT, weight REAL, height REAL, age INTEGER, " +
                "username TEXT, password TEXT, profile_image_uri TEXT, loggedIn INTEGER DEFAULT 0);";
        db.execSQL(createUserTableQuery);


        // Create the workout table
        String createWorkoutTableQuery = "CREATE TABLE IF NOT EXISTS workouts (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, start_time TEXT, finish_time TEXT, duration TEXT, workout_type TEXT, calories REAL);";
        db.execSQL(createWorkoutTableQuery);

    }

    // Modify the addUser method to return a boolean indicating success or failure
    public boolean addUser(String firstName, String lastName, String email, double weight, double height,
                           int age, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("first_name", firstName);
        values.put("last_name", lastName);
        values.put("email", email);
        values.put("weight", weight);
        values.put("height", height);
        values.put("age", age);
        values.put("username", username);
        values.put("password", password);

        long result = db.insert("users", null, values);
        db.close();

        // Return true if the result is not -1 (indicating success), otherwise return false
        return result != -1;
    }


    // Add method to insert workout data into the database
    public boolean addWorkout(int userId, String startTime, String finishTime, String duration,
                              String workoutType, double calories) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("start_time", startTime);
        values.put("finish_time", finishTime);
        values.put("duration", duration);
        values.put("workout_type", workoutType);
        values.put("calories", calories);

        long result = db.insert("workouts", null, values);
        db.close();

        // Return true if the result is not -1 (indicating success), otherwise return false
        return result != -1;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean authenticateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", new String[]{username, password});

        boolean isValid = cursor.moveToFirst();

        // Close the cursor and database
        cursor.close();
        db.close();

        return isValid;
    }


    // Get current user
//    @SuppressLint("Range")
    public UserModel getCurrentUser() {
        // Perform a database query to retrieve the current user
        // This is just an example, you need to implement your actual query logic
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
            cursor = db.query("users", null, "loggedIn = ?", new String[]{"1"}, null, null, null);
        UserModel user = null;

        if (cursor != null && cursor.moveToFirst()) {
            // Read user data from cursor and create a User object
            user = new UserModel();
            int idIndex = cursor.getColumnIndex("id");
            int usernameIndex = cursor.getColumnIndex("username");
            int firstNameIndex = cursor.getColumnIndex("first_name");
            int lastNameIndex = cursor.getColumnIndex("last_name");
            int emailIndex = cursor.getColumnIndex("email");
            int weightIndex = cursor.getColumnIndex("weight");
            int heightIndex = cursor.getColumnIndex("height");
            int ageIndex = cursor.getColumnIndex("age");
            int profileImageUriIndex = cursor.getColumnIndex("profile_image_uri");
            int loggedInIndex = cursor.getColumnIndex("loggedIn");


            // It's important to handle cases where getColumnIndex returns -1
            if (idIndex != -1) {
                user.setId(cursor.getInt(idIndex));
            }
            if (usernameIndex != -1) {
                user.setUsername(cursor.getString(usernameIndex));
            }
            if (firstNameIndex != -1) {
                user.setFirstname(cursor.getString(firstNameIndex));
            }
            if (lastNameIndex != -1) {
                user.setLastname(cursor.getString(lastNameIndex));
            }
            if (emailIndex != -1) {
                user.setEmail(cursor.getString(emailIndex));
            }
            if (weightIndex != -1) {
                user.setWeight(cursor.getDouble(weightIndex));
            }
            if (heightIndex != -1) {
                user.setHeight(cursor.getDouble(heightIndex));
            }
            if (ageIndex != -1) {
                user.setAge(cursor.getInt(ageIndex));
            }
            if (profileImageUriIndex != -1) {
                user.setProfileImageUri(cursor.getString(profileImageUriIndex));
            }
            if (loggedInIndex != -1) {
                user.setLoggedIn(cursor.getInt(loggedInIndex));
            }
            cursor.close();
        }
        db.close();
        return user;
    }

    public void updateLoggedInStatus(String username, int i) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("loggedIn", i);
        int rowsAffected = db.update("users", values, "username = ?", new String[]{username});
        db.close();

        if (rowsAffected > 0) {
            // Update successful
            System.out.println("User logged in status updated successfully");
        } else {
            // Update failed
            System.out.println("Failed to update user logged in status");
        }
    }

    public boolean updateUser(int userId, UserModel currentUser) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Retrieve the user ID of the current user
        Log.d("SQLiteDbManager", "User ID: " + userId);


        // Create a ContentValues object to hold the updated values
        values.put("first_name", currentUser.getFirstname());
        values.put("last_name", currentUser.getLastname());
        values.put("email", currentUser.getEmail());
        values.put("weight", currentUser.getWeight());
        values.put("height", currentUser.getHeight());
        values.put("age", currentUser.getAge());
        values.put("password", currentUser.getPassword());
        values.put("profile_image_uri", currentUser.getProfileImageUri());
        values.put("loggedIn", currentUser.getLoggedIn());

        // Update the user entry in the database based on the user ID
        int rowsAffected = db.update("users", values, "id = ?", new String[]{String.valueOf(userId)});
        db.close();

        return rowsAffected > 0;
    }


    // Method to store the image path in the SQLite database
    public void storeImagePath(String imagePath) {

        // Get the directory path where you want to store the images
        String directoryPath = "/storage/emulated/0/Pictures/UsersProfile";

        // Generate a random name for the image file
        String randomImageName = generateRandomImageName();

        // Create the new file path
        String newImagePath = directoryPath + File.separator + randomImageName;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("profile_image_uri", newImagePath);
        long result = db.insert("users", null, values);
        if (result == -1) {
            Log.e("SQLiteDbManager", "Error storing image path in database");
        } else {
            Log.d("SQLiteDbManager", "Image path stored successfully in database");
        }
        db.close();
    }

    private String generateRandomImageName() {
        // Generate a random UUID as the image name
        return UUID.randomUUID().toString() + ".jpg";
    }

    // Add method to retrieve all workouts
    public List<Workout> getAllWorkouts(int userId) {
        List<Workout> workoutList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM workouts WHERE user_id = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int startTimeIndex = cursor.getColumnIndex("start_time");
            int finishTimeIndex = cursor.getColumnIndex("finish_time");
            int durationIndex = cursor.getColumnIndex("duration");
            int workoutTypeIndex = cursor.getColumnIndex("workout_type");
            int caloriesIndex = cursor.getColumnIndex("calories");

            do {
                // Check if the column index is valid
                if (idIndex != -1 && startTimeIndex != -1 && finishTimeIndex != -1
                        && durationIndex != -1 && workoutTypeIndex != -1 && caloriesIndex != -1) {

                    // Retrieve workout data from the cursor and create Workout objects
                    int id = cursor.getInt(idIndex);
                    String startTime = cursor.getString(startTimeIndex);
                    String finishTime = cursor.getString(finishTimeIndex);
                    String duration = cursor.getString(durationIndex);
                    String workoutType = cursor.getString(workoutTypeIndex);
                    double calories = cursor.getDouble(caloriesIndex);

                    Workout workout = new Workout(id, startTime, finishTime, duration, workoutType, calories);
                    workoutList.add(workout);
                } else {
                    // Handle case where column index is invalid
                    Log.e("getAllWorkoutsByUserId", "Column index is invalid");
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return workoutList;
    }

}
