package com.gmail.enzocampanella98.candidatecrush.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

public class CCSound {

    private Sound sound;

    public CCSound(String internalPath) {
        FileHandle fh = Gdx.files.internal(internalPath);
        sound = Gdx.audio.newSound(fh);
    }
}
