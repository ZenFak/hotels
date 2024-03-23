package com.example.hotels.models;

import jakarta.persistence.*;

/**
 * Сущность UserProfile
 */
@Entity
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userProfileId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    User user;

    String firstname;

    public UserProfile(User user, String firstname) {
        this.user = user;
        this.firstname = firstname;
    }


    public UserProfile() {
    }

    public Long getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(Long userProfileId) {
        this.userProfileId = userProfileId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
}