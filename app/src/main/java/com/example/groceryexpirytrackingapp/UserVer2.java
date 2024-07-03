package com.example.groceryexpirytrackingapp;

public class UserVer2 {
    String username,password,imageUri,phoneNumber,fullname;
    int isAdmin,points;

    public UserVer2() {
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public UserVer2(String username, String phoneNumber, String password, String imageUri, int isAdmin, String fullname, int points) {
        this.username = username;
        this.password = password;
        this.imageUri = imageUri;
        this.isAdmin = isAdmin;
        this.phoneNumber=phoneNumber;
        this.fullname=fullname;
        this.points=points;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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
