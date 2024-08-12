package com.betternotes;

import net.runelite.client.eventbus.EventBus;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class BetterNotesPanel extends PluginPanel implements ItemListener {
    private final JPanel noteToolbar = new JPanel();
    private final JComboBox<String> noteSelector = new JComboBox<String>();
    private final JButton newNoteButton = new JButton();

    private final JPanel noteContent = new JPanel();
    private final JTextField noteTitleEditor = new JTextField();
    private final JTextArea noteEditor = new JTextArea();

    @Inject
    private EventBus eventBus;

    void init(BetterNotesConfig config)
    {
        getParent().setLayout(new BorderLayout());
        getParent().add(this, BorderLayout.CENTER);

        // window
        setLayout(new BorderLayout());
        setBackground(ColorScheme.DARK_GRAY_COLOR);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // top toolbar
        noteToolbar.setLayout(new GridLayout(0, 1, 0, 5));
        noteToolbar.setBorder(new EmptyBorder(10, 10, 10, 10));
        noteToolbar.setBackground(ColorScheme.DARK_GRAY_COLOR);

        noteSelector.addItemListener(this);

        newNoteButton.setText("<< NEW NOTE >>");

        // editing window
        noteContent.setLayout(new BorderLayout(0, 5));
        noteContent.setBorder(new EmptyBorder(10, 10, 10, 10));
        noteContent.setBackground(ColorScheme.DARK_GRAY_COLOR);

        noteEditor.setTabSize(2);
        noteEditor.setLineWrap(true);
        noteEditor.setWrapStyleWord(true);



        noteToolbar.add(noteSelector, BorderLayout.NORTH);
        noteToolbar.add(newNoteButton, BorderLayout.CENTER);
        noteContent.add(noteTitleEditor, BorderLayout.NORTH);
        noteContent.add(noteEditor, BorderLayout.CENTER);

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
