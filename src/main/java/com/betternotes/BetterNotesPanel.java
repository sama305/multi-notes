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
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;

@Slf4j
public class BetterNotesPanel extends PluginPanel implements ItemListener {
    private final JPanel noteToolbar = new JPanel();
    private JComboBox<String> noteSelector = new JComboBox<String>();
    private final JButton newNoteButton = new JButton();

    private final JPanel noteContent = new JPanel();
    private final JButton deleteNoteButton = new JButton();
    private final JTextField noteTitleEditor = new JTextField();
    private final JTextArea noteEditor = new JTextArea();

    private NoteData currentNote;
    private NoteDataArray allNotes;

    @Inject
    private EventBus eventBus;

    void init(BetterNotesConfig config)
    {
        final Gson g = new Gson();
        allNotes = g.fromJson(config.notesJSON(), NoteDataArray.class);
        currentNote = allNotes.getAllNotes().get(config.currentIndex());

        getParent().setLayout(new BorderLayout());
        getParent().add(this, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        setBackground(ColorScheme.DARK_GRAY_COLOR);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        noteToolbar.setLayout(new GridLayout(0, 1, 0, 5));
        noteToolbar.setBorder(new EmptyBorder(10, 10, 10, 10));
        noteToolbar.setBackground(ColorScheme.DARK_GRAY_COLOR);

        String[] allTitles = allNotes.getAllNotes().stream().map(noteData -> noteData.title).toArray(String[]::new);
        noteSelector = new JComboBox<>(allTitles);
        noteSelector.addItemListener(this);
        noteSelector.setSelectedIndex(config.currentIndex());

        newNoteButton.setText("<< NEW NOTE >>");
        deleteNoteButton.setText("<< DELETE NOTE >>");
        if (allNotes.getAllNotes().size() == 1) {
            deleteNoteButton.setEnabled(false);
        }

        noteContent.setLayout(new BorderLayout(0, 5));
        noteContent.setBorder(new EmptyBorder(10, 10, 10, 10));
        noteContent.setBackground(ColorScheme.DARK_GRAY_COLOR);

        noteTitleEditor.setText(currentNote.title);
        noteTitleEditor.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                final Document doc = noteTitleEditor.getDocument();
                try
                {
                    allNotes.getAllNotes().get(config.currentIndex()).title = doc.getText(0, doc.getLength());
                    config.notesJSON(g.toJson(allNotes));

                    String[] allTitles = allNotes.getAllNotes().stream().map(noteData -> noteData.title).toArray(String[]::new);
                    DefaultComboBoxModel<String> newTitles = new DefaultComboBoxModel<>(allTitles);
                    noteSelector.setModel(newTitles);
                }
                catch (BadLocationException ex)
                {
                    log.warn("Notes Document Bad Location: " + ex);
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        noteEditor.setTabSize(2);
        noteEditor.setLineWrap(true);
        noteEditor.setWrapStyleWord(true);
        noteEditor.setText(currentNote.content);
        noteEditor.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                final Document doc = noteEditor.getDocument();
                try
                {
                    allNotes.getAllNotes().get(config.currentIndex()).content = doc.getText(0, doc.getLength());
                    config.notesJSON(g.toJson(allNotes));
                }
                catch (BadLocationException ex)
                {
                    log.warn("Notes Document Bad Location: " + ex);
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {}
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

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == noteSelector) {
        }
    }
}
