package com.gmail.enzocampanella98.candidatecrush.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;

import java.util.List;
import java.util.Random;


public class MusicHandler {

    private Random rand;
    private static final String POP_SOUND_LOCATION = "data/sounds/effects/pop_sound.mp3";
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
        List<BlockSound> sounds = BlockSoundBank.getInstance().getBlockSounds(type.getLname(), level);
        if (sounds.size() > 0) {
            return sounds.get(rand.nextInt(sounds.size())).getFileHandle();
        } else return null;
    }

    public void playRandomMusic(BlockType type, char level) {
        FileHandle musicFileHandle = getRandomMusic(type, level);
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
