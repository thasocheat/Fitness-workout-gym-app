package com.example.fitnessworkoutgymapp;

public class Workout {

    private int id;
    private String startTime;
    private String finishTime;
    private String duration;
    private String workoutType;
    private double calories;

    // Constructor
    public Workout(int id, String startTime, String finishTime, String duration, String workoutType, double calories) {
        this.id = id;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.duration = duration;
        this.workoutType = workoutType;
        this.calories = calories;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getWorkoutType() {
        return workoutType;
    }

    public void setWorkoutType(String workoutType) {
        this.workoutType = workoutType;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }
}
