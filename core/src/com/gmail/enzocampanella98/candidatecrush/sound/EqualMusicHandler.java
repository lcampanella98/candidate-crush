package com.gmail.enzocampanella98.candidatecrush.sound;

import com.badlogic.gdx.utils.ObjectMap;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;
import com.gmail.enzocampanella98.candidatecrush.scoringsystem.CrushType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class EqualMusicHandler extends MusicHandler {

    private ObjectMap<String, ObjectMap<CrushType, List<SoundByte>>> soundsLeft;
    private Random rand = new Random();

    public EqualMusicHandler(Set<String> candidates) {
        soundsLeft = new ObjectMap<>();
        for (SoundByte sound : soundBank.allSoundBytes) {
            String name = sound.getLastname();
            if (candidates.contains(name)) {
                if (!soundsLeft.containsKey(name)) {
                    soundsLeft.put(name, new ObjectMap<CrushType, List<SoundByte>>());
                    soundsLeft.get(name).put(CrushType.THREE, new ArrayList<SoundByte>());
                    soundsLeft.get(name).put(CrushType.FOUR, new ArrayList<SoundByte>());
                    soundsLeft.get(name).put(CrushType.T, new ArrayList<SoundByte>());
                    soundsLeft.get(name).put(CrushType.FIVE, new ArrayList<SoundByte>());
                }
                soundsLeft.get(name).get(sound.getCrushType()).add(sound);
            }
        }
    }

    private void repopulateLevelIfNecessary(String name, CrushType crushType) {
        if (soundsLeft.get(name).get(crushType).size() > 0) return;

        for (SoundByte sound : soundBank.allSoundBytes) {
            if (sound.getLastname().equalsIgnoreCase(name) && sound.getCrushType() == crushType) {
                soundsLeft.get(name).get(crushType).add(sound);
            }
        }
    }

    @Override
    public SoundByte getNextSoundByte(BlockType type, CrushType crushType) {
        repopulateLevelIfNecessary(type.getLname(), crushType);
        List<SoundByte> sounds = soundsLeft.get(type.getLname()).get(crushType);
        int randIdx = rand.nextInt(sounds.size());
        SoundByte sound = sounds.get(randIdx);
        sounds.remove(randIdx);

        return sound;
    }

}
