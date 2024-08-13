package com.betternotes;

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
        // empty single note
        return "{\"allNotes\":[{\"content\":\"\"}]}";
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
            description = "",
            hidden = true
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
