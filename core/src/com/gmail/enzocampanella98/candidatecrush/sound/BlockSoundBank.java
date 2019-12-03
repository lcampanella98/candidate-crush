package com.gmail.enzocampanella98.candidatecrush.sound;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BlockSoundBank {
    // singleton class to keep track of all sounds

    private static final BlockSoundBank instance = new BlockSoundBank();

    public static final String SOUND_ROOT = "data/sounds/block_sounds/";
    private List<BlockSound> allBlockSounds;

    private BlockSoundBank() {
        initFilenames();
    }

    public static BlockSoundBank getInstance() {
        return instance;
    }

    private void initFilenames() {
        allBlockSounds = new ArrayList<>();
        File root = new File(SOUND_ROOT);
        for (File f: root.listFiles()) {
            allBlockSounds.add(new BlockSound(f.getName()));
        }
        System.out.println("all block sounds with size " + allBlockSounds.size());
    }

    public List<BlockSound> getAllBlockSounds() {
        return new ArrayList<>(allBlockSounds);
    }

    public List<BlockSound> getBlockSounds(String lastname) {
        List<BlockSound> sounds = new ArrayList<BlockSound>();
        for (BlockSound bs : allBlockSounds)
            if (lastname.equalsIgnoreCase(bs.getLastname()))
                sounds.add(bs);
        return sounds;
    }

    public List<BlockSound> getBlockSounds(String lastname, char level) {
        List<BlockSound> sounds = new ArrayList<BlockSound>();
        for (BlockSound bs : allBlockSounds) {
            if (lastname.equalsIgnoreCase(bs.getLastname())
                    && level == bs.getLevel())
                sounds.add(bs);
        }
        return sounds;
    }

}
