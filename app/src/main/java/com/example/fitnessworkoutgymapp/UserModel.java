package com.example.fitnessworkoutgymapp;


//import com.parse.Parse;
//import com.parse.ParseClassName;
//import com.parse.ParseObject;
//
//import static com.parse.ParseClassName.*;

public class UserModel{
    String firstname;
    String lastname;
    String email;
    double weight;
    double height;
    int age;
    int loggedIn;
    String profileImageUri;
    String username;

    String password;

//    public UserModel(String firstname, String lastname, double weight, double height, int age, String profileImageUri, String username) {
//        this.firstname = firstname;
//        this.lastname = lastname;
//        this.weight = weight;
//        this.height = height;
//        this.age = age;
//        this.objectId = objectId;
//        this.username = username;
//    }

    @Override
    public String toString() {
        return "UserModel{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", weight=" + weight +
                ", height=" + height +
                ", age=" + age +
                ", username='" + username + '\'' +
                ", profileImageUri='" + profileImageUri + '\'' +
                ", loggedIn='" + loggedIn + '\'' +
                '}';
    }


    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getProfileImageUri() {
        return profileImageUri;
    }

    public void setProfileImageUri(String profileImageUri) {
        this.profileImageUri = profileImageUri;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getLoggedIn() {return loggedIn; }

    public void setLoggedIn(int loggedIn) { this.loggedIn = loggedIn; }

    public void setProfileImage(String ImageUrl) {
        this.profileImageUri = ImageUrl;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
