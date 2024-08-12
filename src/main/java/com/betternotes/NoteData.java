package com.betternotes;

public class NoteData {
    protected String title;
    protected String content;

    public NoteData(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public NoteData() {
        this.title = "";
        this.content = "";
    }
}