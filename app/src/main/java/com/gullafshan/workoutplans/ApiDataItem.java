package com.gullafshan.workoutplans;

public class ApiDataItem {

    private String title;
    private String description;

    // Constructor
    public ApiDataItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // Getter for title
    public String getTitle() {
        return title;
    }

    // Getter for description
    public String getDescription() {
        return description;
    }

    // Optional: setters if you want to modify data later
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

