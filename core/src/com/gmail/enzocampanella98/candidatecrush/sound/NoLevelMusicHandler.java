package com.gmail.enzocampanella98.candidatecrush.sound;

import com.badlogic.gdx.utils.ObjectMap;
import com.gmail.enzocampanella98.candidatecrush.board.BlockType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class NoLevelMusicHandler extends MusicHandler {
    private static ObjectMap<String, List<BlockSound>> soundsLeft = initSoundsLeft();

    private static ObjectMap<String, List<BlockSound>> initSoundsLeft() {
        soundsLeft = new ObjectMap<>();
        for (BlockSound sound : allBlockSounds) {
            String name = sound.getLastname();
            if (!soundsLeft.containsKey(name)) {
                soundsLeft.put(name, new ArrayList<BlockSound>());
            }
            soundsLeft.get(name).add(sound);
        }
        return soundsLeft;
    }

    private Random rand = new Random();

    private void repopulateCandidateIfNecessary(String name) {
        if (soundsLeft.get(name).size() > 0) return;

        for (BlockSound sound : allBlockSounds) {
            if (sound.getLastname().equalsIgnoreCase(name)) {
                soundsLeft.get(name).add(sound);
            }
        }
    }

    @Override
    public void queueSoundByte(BlockType type, char level) {
        repopulateCandidateIfNecessary(type.getLname());
        List<BlockSound> sounds = soundsLeft.get(type.getLname());
        int randIdx = rand.nextInt(sounds.size());
        BlockSound sound = sounds.get(randIdx);
        sounds.remove(randIdx);

        queue.add(sound);
    }

}
