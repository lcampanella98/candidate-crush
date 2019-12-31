package com.gmail.enzocampanella98.candidatecrush.sound;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.board.Crush;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.CrushType;

public interface IMusicHandler {
    void queueSoundByte(BlockType type, CrushType crushType);

    void clearSoundByteQueue();

    boolean isPlayingSoundByte();

    void playSound(Sound sound);

    void playMusic(Music music);

    void stopAll();

    void setBackgroundMusic(Music music);

    void playBackgroundMusic();
}
