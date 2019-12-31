package com.gmail.enzocampanella98.candidatecrush.sound;

import com.badlogic.gdx.utils.ObjectMap;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.level.LevelFactory;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.CrushType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PersistentTierMusicHandler extends MusicHandler {
    private static ObjectMap<Integer, ObjectMap<String, List<SoundByte>>> soundsLeft = initSoundsLeft();

    private static ObjectMap<Integer, ObjectMap<String, List<SoundByte>>> initSoundsLeft() {
        soundsLeft = new ObjectMap<>();
        for (int soundTier = 1; soundTier <= LevelFactory.NUM_TIERS; ++soundTier) {
            ObjectMap<String, List<SoundByte>> soundsByCandidate = new ObjectMap<>();
            for (SoundByte sound : soundBank.soundbyteTiers.get(soundTier)) {
                String name = sound.getLastname();
                if (!soundsByCandidate.containsKey(name)) {
                    soundsByCandidate.put(name, new ArrayList<SoundByte>());
                }
                soundsByCandidate.get(name).add(sound);
            }
            soundsLeft.put(soundTier, soundsByCandidate);
        }
        return soundsLeft;
    }

    private int soundTier;
    private Random rand = new Random();

    public PersistentTierMusicHandler(int soundTier) {
        this.soundTier = soundTier;
    }

    @Override
    public SoundByte getNextSoundByte(BlockType type, CrushType crushType) {
        repopulateCandidateIfNecessary(type.getLname());
        List<SoundByte> sounds = soundsLeft.get(soundTier).get(type.getLname());
        int randIdx = rand.nextInt(sounds.size());
        SoundByte sound = sounds.get(randIdx);
        sounds.remove(randIdx);

        return sound;
    }

    private void repopulateCandidateIfNecessary(String name) {
        if (soundsLeft.get(soundTier).get(name).size() > 0) return;

        for (SoundByte sound : soundBank.soundbyteTiers.get(soundTier)) {
            if (sound.getLastname().equalsIgnoreCase(name)) {
                soundsLeft.get(soundTier).get(name).add(sound);
            }
        }
    }
}
