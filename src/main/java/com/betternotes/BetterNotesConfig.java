package com.betternotes;

import com.google.gson.Gson;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("betternotes")
public interface BetterNotesConfig extends Config
{
    // we could store notes in JSON
    @ConfigItem(
            keyName = "notesJSON",
            name = "notes",
            description = ""
    )
    default String notesJSON()
    {
        final Gson g = new Gson();
        final NoteDataArray defaultNotes = new NoteDataArray(new NoteData[]{ new NoteData("Default Note", "") });
        return g.toJson(defaultNotes);
    }

    @ConfigItem(
            keyName = "notesJSON",
            name = "notes",
            description = ""
    )
    void notesJSON(String json);

    // we could store notes in JSON
    @ConfigItem(
            keyName = "currentNoteIndex",
            name = "index",
            description = ""
    )
    default int currentIndex()
    {
        return 0;
    }

    @ConfigItem(
            keyName = "currentNoteIndex",
            name = "index",
            description = ""
    )
    void currentIndex(int newIndex);
}
