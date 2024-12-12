package com.example.jobmanager;

public class Application {
    private final String companyName;
    private final String jobTitle;
    private final String appliedDate;
    private final String jobLink;
    private final String description;
    private final String currentAppStatus;

    public Application(String companyName, String jobTitle, String appliedDate, String jobLink, String description, String currentAppStatus) {
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.appliedDate = appliedDate;
        this.jobLink = jobLink;
        this.description = description;
        this.currentAppStatus = currentAppStatus;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getAppliedDate() {
        return appliedDate;
    }

    public String getJobLink() {
        return jobLink;
    }

    public String getDescription() {
        return description;
    }

    public String getCurrentAppStatus() {
        return currentAppStatus;
    }

    // Shows string for Application object for debugging
    @Override
    public String toString() {
        return "Application{" +
                "companyName='" + companyName + '\'' +
                ", jobTitle='" + jobTitle + '\'' +
                ", appliedDate='" + appliedDate + '\'' +
                ", jobLink='" + jobLink + '\'' +
                ", description='" + description + '\'' +
                ", currentAppStatus='" + currentAppStatus + '\'' +
                '}';
    }
}
