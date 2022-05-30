package com.example.labtestapplication.model;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String email;
    private String password;
    private String contact;

    public User(){}

    public User(String username, String email, String password, String contact) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.contact = contact;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
