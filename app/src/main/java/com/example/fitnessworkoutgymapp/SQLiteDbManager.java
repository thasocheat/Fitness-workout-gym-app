package com.example.fitnessworkoutgymapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteDbManager extends SQLiteOpenHelper {
    public SQLiteDbManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create the users table

        String createUserTableQuery = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "first_name TEXT, last_name TEXT, email TEXT, weight REAL, height REAL, age INTEGER, " +
                "username TEXT, password TEXT)";
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
}
