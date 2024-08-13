package com.multinotes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoteDataArray {
    private List<NoteData> allNotes;

    public NoteDataArray(NoteData[] notes) {
        this.allNotes = Arrays.asList(notes);
    }

    public List<NoteData> getAllNotes() {
        return this.allNotes;
    }

    public void addNote(NoteData note) {
        this.allNotes.add(note);
    }

    public void deleteNote(int index) {
        this.allNotes.remove(index);
    }

    public int size() {
        return this.allNotes.size();
    }

    public NoteData get(int index) {
        return allNotes.get(index);
    }
}
