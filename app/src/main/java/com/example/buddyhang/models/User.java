package com.example.buddyhang.models;
/**
 * Represents a user on BuddyHang
 */
public class User {

    private String id;
    private String username;
    private String name;
    private String profile_picture;
    private String bio;

    public User() {
    }

    public User(String id, String username, String name, String profile_picture, String bio) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.profile_picture = profile_picture;
        this.bio = bio;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setProfilePicture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getProfilePicture() {
        return profile_picture;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getBio() {
        return bio;
    }

}