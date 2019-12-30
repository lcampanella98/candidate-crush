package com.gmail.enzocampanella98.candidatecrush.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Disposable;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.CrushType;

import java.io.File;

/**
 * Created by enzoc on 5/21/2018.
 */

public class SoundByte implements Disposable {

    private char level;
    private String lastname;
    private String description;
    private Music music;

    public SoundByte(String internalPath) { // filename relative to sound root
        music = Gdx.audio.newMusic(Gdx.files.internal(internalPath));
        String filename = new File(internalPath).getName();
        String[] parts = filename.split("_");
        if (parts.length > 0) lastname = parts[0];
        if (parts.length > 1) level = parts[1].charAt(0);
        if (parts.length > 2 && filename.length() >= 4)
            description = filename.substring(0, filename.length() - 4);
    }

    public CrushType getCrushType() {
        switch (level) {
            case '3':
                return CrushType.THREE;
            case '4':
                return CrushType.FOUR;
            case 't':
                return CrushType.T;
            case '5':
                return CrushType.FIVE;
        }
        return null;
    }

    public String getDescription() {
        return description;
    }

    public String getLastname() {
        return lastname;
    }

    public Music getMusic() {
        return music;
    }

    @Override
    public void dispose() {
        music.dispose();
    }
}
