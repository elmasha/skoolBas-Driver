package com.app.driver.Models;

import java.util.ArrayList;
import java.util.List;

public class Users {

    private String user;
    private List<String> followers;
    private List<String> following;

    public Users() {
        // Default constructor required for Firestore
    }

    public Users(String username) {
        this.user = username;
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
    }

    public String getUser() {
        return user;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public List<String> getFollowing() {
        return following;
    }
}
