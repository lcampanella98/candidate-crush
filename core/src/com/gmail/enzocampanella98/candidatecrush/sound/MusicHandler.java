package com.gmail.enzocampanella98.candidatecrush.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;

import java.util.Random;

/**
 * Created by Lorenzo Campanella on 7/30/2016.
 */
public class MusicHandler {

    private Random rand;
    private static final String POP_SOUND_LOCATION = "sounds/effects/pop_sound.mp3";
    private static Sound popSound;
    private Music lastPlayedMusic;

    public MusicHandler() {
        rand = new Random();
        if (popSound == null)
            popSound = Gdx.audio.newSound(Gdx.files.internal(POP_SOUND_LOCATION));
    }

    public boolean isMusicPlaying() {
        return lastPlayedMusic != null && lastPlayedMusic.isPlaying();
    }

    public void playPopSound() {
        popSound.play(1.0f);
    }

    private FileHandle getRandomMusic(BlockType type, char level) {
        Array<FileHandle> musicFileHandles = type.getBlockSounds().getSoundsArray(level);
        if (musicFileHandles.size > 0) {
            return musicFileHandles.get(rand.nextInt(musicFileHandles.size));
        } else return null;
    }

    public void playRandomMusic(BlockType type, char level) {
        FileHandle musicFileHandle = getRandomMusic(type, level);
        if (musicFileHandle != null) {
            lastPlayedMusic = Gdx.audio.newMusic(musicFileHandle);
            lastPlayedMusic.play();
            lastPlayedMusic.setOnCompletionListener(new Music.OnCompletionListener() {
                @Override
                public void onCompletion(Music music) {
                    music.dispose();
                }
            });
        }
    }
}
