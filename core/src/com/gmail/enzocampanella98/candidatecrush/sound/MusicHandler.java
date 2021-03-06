package com.gmail.enzocampanella98.candidatecrush.sound;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.board.Crush;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.CrushType;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public abstract class MusicHandler implements IMusicHandler, Music.OnCompletionListener, Disposable {
    protected static CCSoundBank soundBank = CCSoundBank.getInstance();

    protected Queue<SoundByte> soundByteQueue;
    private SoundByte lastSoundByte;
    private Set<Sound> soundSet;
    private Set<Music> musicSet;
    private Music bgMusic;
    private float bgMusicInitVolume;

    public MusicHandler() {
        soundByteQueue = new LinkedList<>();
        soundSet = new HashSet<>();
        musicSet = new HashSet<>();
    }

    @Override
    public boolean isPlayingSoundByte() {
        return lastSoundByte != null && lastSoundByte.getMusic().isPlaying();
    }

    @Override
    public void clearSoundByteQueue() {
        soundByteQueue.clear();
    }

    @Override
    public void playSound(Sound sound) {
        sound.play();
        soundSet.add(sound);
    }

    @Override
    public void playMusic(Music music) {
        music.play();
        musicSet.add(music);
    }

    @Override
    public void setBackgroundMusic(Music music) {
        this.bgMusic = music;
        this.bgMusicInitVolume = bgMusic.getVolume();
        musicSet.add(music);
    }

    @Override
    public void playBackgroundMusic() {
        if (bgMusic != null && !bgMusic.isPlaying()) {
            bgMusic.play();
        }
    }

    @Override
    public void queueSoundByte(BlockType type, CrushType crushType) {
        SoundByte next = getNextSoundByte(type, crushType);
        next.getMusic().setOnCompletionListener(this);
        next.getMusic().setLooping(false);
        if (isPlayingSoundByte()) {
            soundByteQueue.add(next);
        } else {
            lastSoundByte = next;
            next.getMusic().play();
        }
        if (bgMusic != null) {
            bgMusic.setVolume(bgMusicInitVolume / 4f);
        }
    }

    @Override
    public void onCompletion(Music music) {
        lastSoundByte = soundByteQueue.poll();
        if (lastSoundByte != null) {
            lastSoundByte.getMusic().play();
        } else {
            if (bgMusic != null) {
                bgMusic.setVolume(bgMusicInitVolume);
            }
        }
    }

    @Override
    public void stopAll() {
        soundByteQueue.clear();
        if (lastSoundByte != null) {
            lastSoundByte.getMusic().stop();
        }
        for (Sound s : soundSet) {
            s.stop();
        }
        for (Music m : musicSet) {
            m.stop();
        }
    }

    public abstract SoundByte getNextSoundByte(BlockType type, CrushType crushType);

    @Override
    public void dispose() {
        stopAll();
    }
}
