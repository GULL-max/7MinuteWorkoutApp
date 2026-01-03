package com.gullafshan.workoutplans;

public class NoteItem {
    private final String title;
    private final String content;

    public NoteItem(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() { return title; }
    public String getContent() { return content; }
}

