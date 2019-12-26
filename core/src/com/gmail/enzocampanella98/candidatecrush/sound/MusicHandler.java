package com.gmail.enzocampanella98.candidatecrush.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class MusicHandler extends Thread implements IMusicHandler, Runnable, Disposable, Music.OnCompletionListener {
    private static final String POP_SOUND_LOCATION = "data/sounds/effects/pop_sound.mp3";
    protected static List<BlockSound> allBlockSounds = BlockSoundBank.getInstance().getAllBlockSounds();

    private Sound popSound;

    protected ConcurrentLinkedQueue<BlockSound> queue;
    private boolean tryPlayPop = false;
    private boolean playPop = false;
    private boolean shouldRun;
    private Music lastMusic;

    public MusicHandler() {
        queue = new ConcurrentLinkedQueue<>();
        popSound = Gdx.audio.newSound(Gdx.files.internal(POP_SOUND_LOCATION));
    }

    @Override
    public void playPopIfNoMusicPlaying() {
        tryPlayPop = true;
    }

    @Override
    public void playPop() {
        playPop = true;
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
                if (tryPlayPop || playPop) {
                    if (playPop || lastMusic == null || !lastMusic.isPlaying()) {
                        popSound.play(1.0f);
                    }
                    tryPlayPop = playPop = false;
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
