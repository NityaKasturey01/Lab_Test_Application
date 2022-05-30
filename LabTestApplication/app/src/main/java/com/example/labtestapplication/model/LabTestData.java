package com.example.labtestapplication.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LabTestData implements Serializable {
    @SerializedName("name")
    private String name;
    @SerializedName("parameter")
    private String parameter;
    @SerializedName("collection_type")
    private String collection_type;
    @SerializedName("field")
    private String field;
    @SerializedName("cost")
    private String cost;
    @SerializedName("lab_location")
    private String lab_location;
    @SerializedName("_lab_contact")
    private String lab_contact;
    @SerializedName("description")
    private String description;
    @SerializedName("age_limit")
    private String age_limit;
    @SerializedName("pre_req")
    private String pre_req;
    @SerializedName("report")
    private String report;
    @SerializedName("image")
    private String image;
    private String dname;

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public LabTestData(){}

    public LabTestData(String name, String parameter, String collection_type, String field, String cost, String lab_location, String lab_contact, String description, String age_limit, String pre_req, String report, String image) {
        this.name = name;
        this.parameter = parameter;
        this.collection_type = collection_type;
        this.field = field;
        this.cost = cost;
        this.lab_location = lab_location;
        this.lab_contact = lab_contact;
        this.description = description;
        this.age_limit = age_limit;
        this.pre_req = pre_req;
        this.report = report;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getCollection_type() {
        return collection_type;
    }

    public void setCollection_type(String collection_type) {
        this.collection_type = collection_type;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getLab_location() {
        return lab_location;
    }

    public void setLab_location(String lab_location) {
        this.lab_location = lab_location;
    }

    public String getLab_contact() {
        return lab_contact;
    }

    public void setLab_contact(String lab_contact) {
        this.lab_contact = lab_contact;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAge_limit() {
        return age_limit;
    }

    public void setAge_limit(String age_limit) {
        this.age_limit = age_limit;
    }

    public String getPre_req() {
        return pre_req;
    }

    public void setPre_req(String pre_req) {
        this.pre_req = pre_req;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
