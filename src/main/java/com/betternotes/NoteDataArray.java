package com.betternotes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoteDataArray {
    private List<NoteData> allNotes;

    public NoteDataArray() {
        this.allNotes = new ArrayList<NoteData>();
    }

    public NoteDataArray(NoteData[] notes) {
        this.allNotes = Arrays.asList(notes);
    }

    public NoteDataArray(List<NoteData> notes) {
        this.allNotes = notes;
    }

    public List<NoteData> getAllNotes() {
        return this.allNotes;
    }

    public void setAllNotes(NoteData[] notes) {
        this.allNotes = Arrays.asList(notes);
    }

    public void setAllNotes(ArrayList<NoteData> notes) {
        allNotes = notes;
    }

    public void addNote(NoteData note) {
        this.allNotes.add(note);
    }

    public int size() {
        return this.allNotes.size();
    }
}
