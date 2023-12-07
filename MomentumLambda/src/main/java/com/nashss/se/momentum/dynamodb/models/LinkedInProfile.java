package com.nashss.se.momentum.dynamodb.models;











public class LinkedInProfile {

    private final String firstName;
    private final String lastName;
    private String jobTitle;
    private String currentCompany;


    public LinkedInProfile() {
        this.firstName = "scott";
        this.lastName = "griffin";
        this.jobTitle = "software developer";
        this.currentCompany = "searching...";

    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getCurrentCompany() {
        return currentCompany;
    }
}
