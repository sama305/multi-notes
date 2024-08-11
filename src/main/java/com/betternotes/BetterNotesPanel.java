package com.betternotes;

import net.runelite.client.eventbus.EventBus;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.undo.UndoManager;
import java.awt.*;

public class BetterNotesPanel extends PluginPanel {
    private final JTextArea notesEditor = new JTextArea();
    private final UndoManager undoRedo = new UndoManager();

    @Inject
    private EventBus eventBus;

    void init(BetterNotesConfig config)
    {
        setLayout(new BorderLayout());
        setBackground(ColorScheme.DARK_GRAY_COLOR);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel test = new JPanel();
        test.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        test.setBorder(new EmptyBorder(10, 10, 10, 10));
        test.setLayout(new GridLayout(0, 1));

        test.add(new JLabel("Hello world!"));

        add(test, BorderLayout.NORTH);

        eventBus.register(this);
    }

    void deinit() {
        eventBus.unregister(this);
    }
}
