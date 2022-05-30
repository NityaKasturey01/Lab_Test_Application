package com.example.labtestapplication.model;

import java.io.Serializable;

public class Bookings implements Serializable {
    private String testName;
    private String members;
    private String collectionType;
    private String date;
    private String time;
    private String picode;
    private String city;
    private String area;
    private String contact;
    private String price;
    private String status;
    private String sample_given;
    private String sample_recieved;
    private String reports_provided;
    private String reports_recieved;
    private String report;
    private String dname;

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public Bookings(String testName, String members, String date, String time) {
        this.testName = testName;
        this.members = members;
        this.date = date;
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSample_given() {
        return sample_given;
    }

    public void setSample_given(String sample_given) {
        this.sample_given = sample_given;
    }

    public String getSample_recieved() {
        return sample_recieved;
    }

    public void setSample_recieved(String sample_recieved) {
        this.sample_recieved = sample_recieved;
    }

    public String getReports_provided() {
        return reports_provided;
    }

    public void setReports_provided(String reports_provided) {
        this.reports_provided = reports_provided;
    }

    public String getReports_recieved() {
        return reports_recieved;
    }

    public void setReports_recieved(String reports_recieved) {
        this.reports_recieved = reports_recieved;
    }

    public Bookings(String testName, String members, String collectionType, String date, String time, String picode, String city, String area, String contact, String price, String status, String sample_given, String sample_recieved, String reports_provided, String reports_recieved) {
        this.testName = testName;
        this.members = members;
        this.collectionType = collectionType;
        this.date = date;
        this.time = time;
        this.picode = picode;
        this.city = city;
        this.area = area;
        this.contact = contact;
        this.price = price;
        this.status = status;
        this.sample_given = sample_given;
        this.sample_recieved = sample_recieved;
        this.reports_provided = reports_provided;
        this.reports_recieved = reports_recieved;
    }

    public Bookings(String testName, String members, String date, String time, String collectionType, String picode, String city, String area, String contact, String price, String status) {
        this.testName = testName;
        this.members = members;
        this.collectionType = collectionType;
        this.date = date;
        this.time = time;
        this.picode = picode;
        this.city = city;
        this.area = area;
        this.contact = contact;
        this.price = price;
        this.status = status;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPicode() {
        return picode;
    }

    public void setPicode(String picode) {
        this.picode = picode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
