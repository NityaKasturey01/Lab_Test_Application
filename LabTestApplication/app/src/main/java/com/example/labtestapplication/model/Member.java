package com.example.labtestapplication.model;

import java.io.Serializable;

public class Member implements Serializable {
    private String email;
    private String name;
    private String age;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Member(String name, String age, String gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    private String gender;
}
