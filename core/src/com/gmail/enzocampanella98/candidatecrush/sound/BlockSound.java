package com.gmail.enzocampanella98.candidatecrush.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * Created by enzoc on 5/21/2018.
 */

public class BlockSound {

    private char level;
    private String lastname;
    private String description;
    private FileHandle fileHandle;

    public BlockSound(String filename) { // filename relative to sound root
        String[] parts = filename.split("_");
        if (parts.length > 0) lastname = parts[0];
        if (parts.length > 1) level = parts[1].charAt(0);
        if (parts.length > 2 && filename.length() >= 4)
            description = filename.substring(0, filename.length() - 4);
        fileHandle = Gdx.files.internal(BlockSoundBank.SOUND_ROOT + filename);
        if (!fileHandle.exists()) {
            System.out.println("WARNING: file " + filename + " was not found");
        }
    }

    public FileHandle getFileHandle() {
        return fileHandle;
    }

    public char getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }

    public String getLastname() {
        return lastname;
    }
}
