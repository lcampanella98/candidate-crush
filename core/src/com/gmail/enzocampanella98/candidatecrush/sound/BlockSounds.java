package com.gmail.enzocampanella98.candidatecrush.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Lorenzo Campanella on 7/30/2016.
 */
public class BlockSounds {
    private static final String SOUND_ROOT = "sounds/block_sounds/";

    public Array<FileHandle> threes;
    public Array<FileHandle> fours;
    public Array<FileHandle> ts;
    public Array<FileHandle> fives;

    private String lname;

    public BlockSounds(String lname) {
        threes = new Array<FileHandle>();
        fours = new Array<FileHandle>();
        fives = new Array<FileHandle>();
        ts = new Array<FileHandle>();
        this.lname = lname;
        init();
    }

    public Array<FileHandle> getSoundsArray(char level) {
        switch (level) {
            case '3':
                return threes;
            case '4':
                return fours;
            case '5':
                return fives;
            case 't':
            case 'T':
                return ts;
            default:
                return null;
        }
    }

    private void init() {
        FileHandle root = Gdx.files.internal(SOUND_ROOT);
        if (root.exists()) {
            for (FileHandle f : root.list()) {
                if (!f.isDirectory()) {
                    String[] pieces = f.nameWithoutExtension().split("_");
                    if (pieces.length >= 2) {
                        if (pieces[0].equalsIgnoreCase(lname)) {
                            if (pieces[1].length() == 1) {
                                char charLevel = pieces[1].charAt(0);
                                Array<FileHandle> musicHandleArray = getSoundsArray(charLevel);
                                if (musicHandleArray != null) musicHandleArray.add(f);
                            }
                        }
                    }
                }
            }
        }

    }
}
