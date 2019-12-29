package com.gmail.enzocampanella98.candidatecrush.sound;

import com.badlogic.gdx.utils.ObjectMap;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoLevelMusicHandler extends MusicHandler {
    private static ObjectMap<String, List<SoundByte>> soundsLeft = initSoundsLeft();

    private static ObjectMap<String, List<SoundByte>> initSoundsLeft() {
        soundsLeft = new ObjectMap<>();
        for (SoundByte sound : allSoundBytes) {
            String name = sound.getLastname();
            if (!soundsLeft.containsKey(name)) {
                soundsLeft.put(name, new ArrayList<SoundByte>());
            }
            soundsLeft.get(name).add(sound);
        }
        return soundsLeft;
    }

    private Random rand = new Random();

    private void repopulateCandidateIfNecessary(String name) {
        if (soundsLeft.get(name).size() > 0) return;

        for (SoundByte sound : allSoundBytes) {
            if (sound.getLastname().equalsIgnoreCase(name)) {
                soundsLeft.get(name).add(sound);
            }
        }
    }

    @Override
    public SoundByte getNextSoundByte(BlockType type, char level) {
        repopulateCandidateIfNecessary(type.getLname());
        List<SoundByte> sounds = soundsLeft.get(type.getLname());
        int randIdx = rand.nextInt(sounds.size());
        SoundByte sound = sounds.get(randIdx);
        sounds.remove(randIdx);

        return sound;
    }

}
