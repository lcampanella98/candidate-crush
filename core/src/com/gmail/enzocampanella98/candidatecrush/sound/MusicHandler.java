package com.gmail.enzocampanella98.candidatecrush.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class MusicHandler extends Thread implements IMusicHandler, Runnable, Disposable, Music.OnCompletionListener {
    private static final String POP_SOUND_LOCATION = "data/sounds/effects/pop_sound.mp3";
    private Sound popSound;

    protected List<BlockSound> allBlockSounds;
    protected ConcurrentLinkedQueue<BlockSound> queue;
    private boolean tryPlayPop = false;
    private boolean shouldRun;
    private Music lastMusic;

    public MusicHandler(Set<String> candidates) {
        queue = new ConcurrentLinkedQueue<>();
        allBlockSounds = BlockSoundBank.getInstance().getAllBlockSounds();
        popSound = Gdx.audio.newSound(Gdx.files.internal(POP_SOUND_LOCATION));

    }

    @Override
    public void playPopIfNoMusicPlaying() {
        tryPlayPop = true;
    }


    @Override
    public void run() {
        while (shouldRun) {
            try {
                if (lastMusic == null || !lastMusic.isPlaying()) {
                    BlockSound nextSound = queue.poll();
                    if (nextSound != null) {
                        lastMusic = Gdx.audio.newMusic(nextSound.getFileHandle());
                        lastMusic.setOnCompletionListener(this);
                        lastMusic.play();
                    }
                }
                if (tryPlayPop) {
                    if (lastMusic == null || !lastMusic.isPlaying()) {
                        popSound.play(1.0f);
                    }
                    tryPlayPop = false;
                }
                Thread.sleep(50);
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted");
            }
        }
    }

    @Override
    public synchronized void start() {
        shouldRun = true;
        super.start();
    }

    @Override
    public void onCompletion(Music music) {
        music.dispose();
    }

    @Override
    public void dispose() {
        shouldRun = false;
        popSound.dispose();
    }
}
