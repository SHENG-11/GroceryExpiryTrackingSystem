package com.example.groceryexpirytrackingapp;

public class User {
    String username,password,imageUri,phoneNumber;
    int isAdmin;

    public User() {
    }

    public User(String username, String phoneNumber,String password, String imageUri, int isAdmin) {
        this.username = username;
        this.password = password;
        this.imageUri = imageUri;
        this.isAdmin = isAdmin;
        this.phoneNumber=phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }
}
