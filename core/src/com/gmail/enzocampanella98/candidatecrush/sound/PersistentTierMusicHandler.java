package com.gmail.enzocampanella98.candidatecrush.sound;

import com.badlogic.gdx.utils.ObjectMap;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.CrushType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PersistentTierMusicHandler extends MusicHandler {

    private final Map<Integer, List<SoundByte>> soundbyteTiers;
    private final ObjectMap<Integer, ObjectMap<String, List<SoundByte>>> soundsLeft;

    private int tier;
    private Random rand = new Random();

    public PersistentTierMusicHandler(int numTiers, int tier) {
        this.tier = tier;

        soundbyteTiers = getSoundbyteTiers(numTiers);
        soundsLeft = getSoundsLeft(numTiers);
    }

    @Override
    public SoundByte getNextSoundByte(BlockType type, CrushType crushType) {
        repopulateCandidateIfNecessary(type.getLname());
        List<SoundByte> sounds = soundsLeft.get(tier).get(type.getLname());

        if (sounds.size() == 0) {
            return null;
        }

        int randIdx = rand.nextInt(sounds.size());
        SoundByte sound = sounds.get(randIdx);
        sounds.remove(randIdx);

        return sound;
    }

    private void repopulateCandidateIfNecessary(String name) {
        if (soundsLeft.get(tier).get(name).size() > 0) return;

        for (SoundByte sound : soundbyteTiers.get(tier)) {
            if (sound.getLastname().equalsIgnoreCase(name)) {
                soundsLeft.get(tier).get(name).add(sound);
            }
        }
    }

    // STATIC METHODS
    private ObjectMap<Integer, ObjectMap<String, List<SoundByte>>> getSoundsLeft(int numTiers) {
        ObjectMap<Integer, ObjectMap<String, List<SoundByte>>> soundsLeft = new ObjectMap<>();
        for (int soundTier = 1; soundTier <= numTiers; ++soundTier) {
            ObjectMap<String, List<SoundByte>> soundsByCandidate = new ObjectMap<>();
            for (BlockType candidate : BlockType.values()) {
                soundsByCandidate.put(candidate.getLname(), new ArrayList<SoundByte>());
            }
            for (SoundByte sound : soundbyteTiers.get(soundTier)) {
                String name = sound.getLastname();
                if (soundsByCandidate.containsKey(name)) {
                    soundsByCandidate.get(name).add(sound);
                } else {
                    System.out.println("no key for " + name);
                }
            }
            soundsLeft.put(soundTier, soundsByCandidate);
        }
        return soundsLeft;
    }

    private static Map<Integer, List<SoundByte>> getSoundbyteTiers(int numTiers) {
        Map<Integer, List<SoundByte>> tiers = new HashMap<>();
        for (BlockType candidate : BlockType.values()) {
            List<SoundByte> cSounds = soundBank.soundBytesOfCandidate(candidate);
            int endTier = numTiers - 1;
            for (int tier = 1; tier <= endTier; ++tier) {
                if (!tiers.containsKey(tier)) {
                    tiers.put(tier, new ArrayList<SoundByte>());
                }
                for (int idx = (tier-1)*cSounds.size()/endTier;
                     idx < (tier)*cSounds.size()/endTier;
                     ++idx) {
                    tiers.get(tier).add(cSounds.get(idx));
                }
            }
        }
        tiers.put(numTiers, soundBank.allSoundBytes); // last tier contains all bites
        return tiers;
    }
}
