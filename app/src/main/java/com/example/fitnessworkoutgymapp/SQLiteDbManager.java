package com.example.fitnessworkoutgymapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    public boolean updateUser(UserModel currentUser) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("first_name", currentUser.getFirstname());
        values.put("last_name", currentUser.getLastname());
        values.put("email", currentUser.getEmail());
        values.put("weight", currentUser.getWeight());
        values.put("height", currentUser.getHeight());
        values.put("age", currentUser.getAge());
        values.put("username", currentUser.getUsername());
        values.put("password", currentUser.getPassword());
        values.put("profile_image_uri", currentUser.getProfileImageUri());
        values.put("loggedIn", currentUser.getLoggedIn());
        int rowsAffected = db.update("users", values, "username = ?", new String[]{currentUser.getUsername()});
        db.close();

        return rowsAffected > 0;
    }
}
