package com.example.labtestapplication.model;

import android.util.Log;

import java.io.Serializable;

public class Pathology implements Serializable {
    private String dname;
    private String fullname;
    private String password;
    private String contact;
    private String address;

    public Pathology() {}

    public Pathology(String dname, String fullname, String password, String contact, String address) {
        this.dname = dname;
        this.fullname = fullname;
        this.password = password;
        this.contact = contact;
        this.address = address;
        Log.i("Info2", this.dname+this.fullname+this.password+this.contact+this.address);
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
