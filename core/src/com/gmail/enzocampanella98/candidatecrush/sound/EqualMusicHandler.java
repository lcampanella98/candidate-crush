package com.gmail.enzocampanella98.candidatecrush.sound;

import com.badlogic.gdx.utils.ObjectMap;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class EqualMusicHandler extends MusicHandler {

    private ObjectMap<String, ObjectMap<Character, List<SoundByte>>> soundsLeft;
    private Random rand = new Random();

    public EqualMusicHandler(Set<String> candidates) {
        soundsLeft = new ObjectMap<>();
        for (SoundByte sound : allSoundBytes) {
            String name = sound.getLastname();
            if (candidates.contains(name)) {
                if (!soundsLeft.containsKey(name)) {
                    soundsLeft.put(name, new ObjectMap<Character, List<SoundByte>>());
                    soundsLeft.get(name).put('3', new ArrayList<SoundByte>());
                    soundsLeft.get(name).put('4', new ArrayList<SoundByte>());
                    soundsLeft.get(name).put('5', new ArrayList<SoundByte>());
                    soundsLeft.get(name).put('t', new ArrayList<SoundByte>());
                }
                soundsLeft.get(name).get(sound.getLevel()).add(sound);
            }
        }
    }

    private void repopulateLevelIfNecessary(String name, char level) {
        if (soundsLeft.get(name).get(level).size() > 0) return;

        for (SoundByte sound : allSoundBytes) {
            if (sound.getLastname().equalsIgnoreCase(name) && sound.getLevel() == level) {
                soundsLeft.get(name).get(level).add(sound);
            }
        }
    }

    @Override
    public SoundByte getNextSoundByte(BlockType type, char level) {
        repopulateLevelIfNecessary(type.getLname(), level);
        List<SoundByte> sounds = soundsLeft.get(type.getLname()).get(level);
        int randIdx = rand.nextInt(sounds.size());
        SoundByte sound = sounds.get(randIdx);
        sounds.remove(randIdx);

        return sound;
    }

}
