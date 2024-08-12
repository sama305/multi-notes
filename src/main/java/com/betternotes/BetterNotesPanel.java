package com.betternotes;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.input.KeyListener;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

@Slf4j
public class BetterNotesPanel extends PluginPanel {
    private final JPanel noteToolbar = new JPanel();
    private JComboBox<String> noteSelector = new JComboBox<String>();
    private final JButton newNoteButton = new JButton();

    private final JPanel noteContent = new JPanel();
    private final JButton deleteNoteButton = new JButton();
    private final JTextField noteTitleEditor = new JTextField();
    private final JTextArea noteEditor = new JTextArea();

    private NoteData currentNote;
    private NoteDataArray noteDataArray;

    private BetterNotesConfig config;

    @Inject
    private EventBus eventBus;

    void init(BetterNotesConfig config)
    {
        this.config = config;

        // load in the notes as a Java object
        final Gson g = new Gson();
        noteDataArray = g.fromJson(config.notesJSON(), NoteDataArray.class);
        List<NoteData> allNotes = noteDataArray.getAllNotes();

        // set current note
        currentNote = allNotes.get(config.currentIndex());


        getParent().setLayout(new BorderLayout());
        getParent().add(this, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        setBackground(ColorScheme.DARK_GRAY_COLOR);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        noteToolbar.setLayout(new GridLayout(0, 1, 0, 5));
        noteToolbar.setBorder(new EmptyBorder(10, 10, 10, 10));
        noteToolbar.setBackground(ColorScheme.DARK_GRAY_COLOR);

        noteSelector = new JComboBox<String>(getComboBoxLabels(noteDataArray));
        noteSelector.setSelectedIndex(config.currentIndex());

        newNoteButton.setText("<< NEW NOTE >>");

        deleteNoteButton.setText("<< DELETE NOTE >>");
        if (allNotes.size() == 1) {
            deleteNoteButton.setEnabled(false);
        }

        noteContent.setLayout(new BorderLayout(0, 5));
        noteContent.setBorder(new EmptyBorder(10, 10, 10, 10));
        noteContent.setBackground(ColorScheme.DARK_GRAY_COLOR);

        noteTitleEditor.setText(currentNote.title);

        noteEditor.setTabSize(2);
        noteEditor.setLineWrap(true);
        noteEditor.setWrapStyleWord(true);
        noteEditor.setText(currentNote.content);

        noteSelector.addItemListener(e -> changeSelectedNote(noteSelector.getSelectedIndex()));

        newNoteButton.addActionListener(e -> addNewNote());

        noteTitleEditor.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                final Document doc = noteTitleEditor.getDocument();
                try
                {
                    currentNote.title = doc.getText(0, doc.getLength());
                    config.notesJSON(g.toJson(noteDataArray));

                    updateNoteSelector();
                }
                catch (BadLocationException ex)
                {
                    log.warn("Notes Document Bad Location: " + ex);
                }
            }
        });

        noteEditor.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                final Document doc = noteEditor.getDocument();
                try
                {
                    currentNote.content = doc.getText(0, doc.getLength());
                    config.notesJSON(g.toJson(noteDataArray));
                }
                catch (BadLocationException ex)
                {
                    log.warn("Notes Document Bad Location: " + ex);
                }
            }
        });

        noteToolbar.add(noteSelector, BorderLayout.NORTH);
        noteToolbar.add(newNoteButton, BorderLayout.CENTER);

        noteContent.add(noteTitleEditor, BorderLayout.NORTH);
        noteContent.add(noteEditor, BorderLayout.CENTER);
        noteContent.add(deleteNoteButton, BorderLayout.SOUTH);

        add(noteToolbar, BorderLayout.NORTH);
        add(noteContent, BorderLayout.CENTER);

        eventBus.register(this);
    }

    void deinit() {
        eventBus.unregister(this);
    }

    String[] getComboBoxLabels(NoteDataArray allNoteData) {
        List<NoteData> notes = allNoteData.getAllNotes();
        String[] allLabels = new String[notes.size()];
        for (int i = 0; i < allLabels.length; i++) {
            allLabels[i] = i + ": " + (notes.get(i).title.isBlank() ? "Unnamed Note" : notes.get(i).title);
        }
        return allLabels;
    }

    void updateNoteSelector() {
        DefaultComboBoxModel<String> newTitles = new DefaultComboBoxModel<String>(getComboBoxLabels(noteDataArray));
        noteSelector.setModel(newTitles);
        noteSelector.setSelectedIndex(config.currentIndex());
    }

    void changeSelectedNote(int index) {
        // change current note and index and update panel content
        config.currentIndex(index);
        currentNote = noteDataArray.getAllNotes().get(index);
        syncCurrentNote();
    }

    void syncCurrentNote() {
        noteEditor.setText(currentNote.content);
        noteTitleEditor.setText(currentNote.title);
    }

    void addNewNote() {
        noteDataArray.addNote(new NoteData());

        // update combo box
        updateNoteSelector();

        // change the selected note to the new one
        noteSelector.setSelectedIndex(noteDataArray.size() - 1);
//
//        // update config
//        Gson g = new Gson();
//        config.notesJSON(g.toJson(noteDataArray));
    }
}
