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
    private final JPanel header = new JPanel();
    private JComboBox<String> selector = new JComboBox<String>();
    private final JButton addNoteButton = new JButton();

    private final JPanel content = new JPanel();
    private final JTextField titleEditor = new JTextField();
    private final JTextArea bodyEditor = new JTextArea();
    private final JButton deleteNoteButton = new JButton();

    private NoteData currentNote;
    private NoteDataArray noteDataArray;

    private BetterNotesConfig config;

    @Inject
    private EventBus eventBus;

    void init(BetterNotesConfig config)
    {
        this.config = config;
        load();



        // initial setting of panel layout
        getParent().setLayout(new BorderLayout());
        getParent().add(this, BorderLayout.CENTER); // <- this allows the panel to take up entire space

        setLayout(new BorderLayout());
        setBackground(ColorScheme.DARK_GRAY_COLOR);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        header.setLayout(new GridLayout(0, 1, 0, 5));
        header.setBorder(new EmptyBorder(10, 10, 10, 10));
        header.setBackground(ColorScheme.DARK_GRAY_COLOR);

        selector = new JComboBox<String>(getComboBoxLabels(noteDataArray));
        selector.setSelectedIndex(config.currentIndex());

        addNoteButton.setText("<< NEW NOTE >>");

        deleteNoteButton.setText("<< DELETE NOTE >>");
        if (noteDataArray.size() == 1) {
            // you can't get to zero notes (as of now)
            deleteNoteButton.setEnabled(false);
        }

        content.setLayout(new BorderLayout(0, 5));
        content.setBorder(new EmptyBorder(10, 10, 10, 10));
        content.setBackground(ColorScheme.DARK_GRAY_COLOR);

        titleEditor.setText(currentNote.title);

        bodyEditor.setTabSize(2);
        bodyEditor.setLineWrap(true);
        bodyEditor.setWrapStyleWord(true);
        bodyEditor.setText(currentNote.content);



        // all event listeners
        selector.addItemListener(e -> onChangeSelectedNote(selector.getSelectedIndex()));

        addNoteButton.addActionListener(e -> onPressAddNote());

        titleEditor.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                final Document doc = titleEditor.getDocument();
                onEditNoteTitle(doc);
            }
        });

        bodyEditor.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                final Document doc = bodyEditor.getDocument();
                onEditNoteContent(doc);
            }
        });



        // final construction of panel
        header.add(selector, BorderLayout.NORTH);
        header.add(addNoteButton, BorderLayout.CENTER);

        content.add(titleEditor, BorderLayout.NORTH);
        content.add(bodyEditor, BorderLayout.CENTER);
        content.add(deleteNoteButton, BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);

        eventBus.register(this);
    }

    void deinit() {
        eventBus.unregister(this);
    }

    String[] getComboBoxLabels(NoteDataArray allNoteData) {
        // generate labels from noteData
        // e.g., [ "0: Note 1", ... ]
        List<NoteData> notes = allNoteData.getAllNotes();
        String[] allLabels = new String[notes.size()];
        for (int i = 0; i < allLabels.length; i++) {
            allLabels[i] = (i + 1) + ": " + (notes.get(i).title.isBlank() ? "Unnamed Note" : notes.get(i).title);
        }
        return allLabels;
    }

    void updateNoteSelector() {
        // since we can't edit each item of the combobox individually, we have to recreate all labels from the
        // current state
        DefaultComboBoxModel<String> newTitles = new DefaultComboBoxModel<String>(getComboBoxLabels(noteDataArray));
        selector.setModel(newTitles);

        // we have to do this because updating the model as above resets the selectedIndex
        selector.setSelectedIndex(config.currentIndex());
    }

    void save() {
        final Gson g = new Gson();
        config.notesJSON(g.toJson(noteDataArray));
    }

    void load() {
        final Gson g = new Gson();
        noteDataArray = g.fromJson(config.notesJSON(), NoteDataArray.class);
        if (this.config.currentIndex() >= noteDataArray.size()) {
            this.config.currentIndex(0);
        }
        currentNote = noteDataArray.get(config.currentIndex());
    }

    void onChangeSelectedNote(int index) {
        // change current note and index and update panel content
        config.currentIndex(index);
        currentNote = noteDataArray.getAllNotes().get(index);

        // set panel to match currentNote data
        bodyEditor.setText(currentNote.content);
        titleEditor.setText(currentNote.title);
    }

    void onPressAddNote() {
        noteDataArray.addNote(new NoteData());

        // update combo box
        updateNoteSelector();

        // change the selected note to the new one
        selector.setSelectedIndex(noteDataArray.size() - 1);
    }

    void onEditNoteTitle(Document doc) {
        try {
            // change data of currentNote to match panel text field and save to config
            currentNote.title = doc.getText(0, doc.getLength());
            save();

            // update the selector text to match
            updateNoteSelector();
        }
        catch (BadLocationException ex) {
            log.warn("Notes Document Bad Location: " + ex);
        }
    }

    void onEditNoteContent(Document doc) {
        try {
            // notes are only saved when written to
            // i.e., new, empty notes won't be saved
            currentNote.content = doc.getText(0, doc.getLength());
            save();
        }
        catch (BadLocationException ex) {
            log.warn("Notes Document Bad Location: " + ex);
        }
    }
}
