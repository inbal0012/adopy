package com.example.adopy.Utilities.Models;

public class User {
    private String id;
    private String email;
    private String username;
    private String imageUri;
    private String age;
    private String gender;
    private String device_token;

    public User(String id, String email, String username, String imageUri, String age, String gender, String device_token) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.imageUri = imageUri;
        this.age = age;
        this.gender = gender;
        this.device_token = device_token;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
