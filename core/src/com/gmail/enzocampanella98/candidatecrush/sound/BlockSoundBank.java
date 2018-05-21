package com.gmail.enzocampanella98.candidatecrush.sound;


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
        allBlockSounds = new ArrayList<BlockSound>();
        allBlockSounds.add(new BlockSound("clinton_3_And I thank all of you.ogg"));
        allBlockSounds.add(new BlockSound("clinton_3_Im exhausted.ogg"));
        allBlockSounds.add(new BlockSound("clinton_4_I remember when my husband.ogg"));
        allBlockSounds.add(new BlockSound("clinton_4_Marching orders.ogg"));
        allBlockSounds.add(new BlockSound("clinton_5_This is Hillary Clinton.ogg"));
        allBlockSounds.add(new BlockSound("clinton_t_As we gather here today.ogg"));
        allBlockSounds.add(new BlockSound("clinton_t_Every American gets a cup cake.ogg"));
        allBlockSounds.add(new BlockSound("cruz_3_halahala.ogg"));
        allBlockSounds.add(new BlockSound("cruz_3_hehe.ogg"));
        allBlockSounds.add(new BlockSound("cruz_3_thank you.ogg"));
        allBlockSounds.add(new BlockSound("cruz_4_goose diarrhea.ogg"));
        allBlockSounds.add(new BlockSound("cruz_4_i need a bogle for the glotch.ogg"));
        allBlockSounds.add(new BlockSound("cruz_4_you're the devil.ogg"));
        allBlockSounds.add(new BlockSound("cruz_5_barfed on the beach.ogg"));
        allBlockSounds.add(new BlockSound("cruz_5_hooray for you.ogg"));
        allBlockSounds.add(new BlockSound("cruz_t_garden knomes.ogg"));
        allBlockSounds.add(new BlockSound("cruz_t_i just love to twerk.ogg"));
        allBlockSounds.add(new BlockSound("meza_3_crush.ogg"));
        allBlockSounds.add(new BlockSound("meza_4_crush.ogg"));
        allBlockSounds.add(new BlockSound("meza_5_crush.ogg"));
        allBlockSounds.add(new BlockSound("meza_t_crush.ogg"));
        allBlockSounds.add(new BlockSound("sanders_3_black lives matter.ogg"));
        allBlockSounds.add(new BlockSound("sanders_3_marijuana.ogg"));
        allBlockSounds.add(new BlockSound("sanders_3_thank you.ogg"));
        allBlockSounds.add(new BlockSound("sanders_4_take back out government.ogg"));
        allBlockSounds.add(new BlockSound("sanders_4_we can do it.ogg"));
        allBlockSounds.add(new BlockSound("sanders_4_wealth and income inequality.ogg"));
        allBlockSounds.add(new BlockSound("sanders_5_don't underestimate me.ogg"));
        allBlockSounds.add(new BlockSound("sanders_5_Huge.ogg"));
        allBlockSounds.add(new BlockSound("sanders_t_enough of the emails.ogg"));
        allBlockSounds.add(new BlockSound("sanders_t_middle class of this country is collapsing.ogg"));
        allBlockSounds.add(new BlockSound("sanders_t_What this campaign is about.ogg"));
        allBlockSounds.add(new BlockSound("trump_3_cheyah.ogg"));
        allBlockSounds.add(new BlockSound("trump_3_fantastic.ogg"));
        allBlockSounds.add(new BlockSound("trump_3_no you're finished.ogg"));
        allBlockSounds.add(new BlockSound("trump_3_Okay.ogg"));
        allBlockSounds.add(new BlockSound("trump_4_get out of here.ogg"));
        allBlockSounds.add(new BlockSound("trump_4_Thank you darling.ogg"));
        allBlockSounds.add(new BlockSound("trump_4_we want deal!.ogg"));
        allBlockSounds.add(new BlockSound("trump_4_we're going to make america great.ogg"));
        allBlockSounds.add(new BlockSound("trump_5_I beat China all the time.ogg"));
        allBlockSounds.add(new BlockSound("trump_5_I'm really rich ver2.ogg"));
        allBlockSounds.add(new BlockSound("trump_5_Not that it matters but I'm much richer.ogg"));
        allBlockSounds.add(new BlockSound("trump_t_I'm really rich.ogg"));
        allBlockSounds.add(new BlockSound("trump_t_i've been watching you for the last couple of weeks.ogg"));
        allBlockSounds.add(new BlockSound("trump_t_you know you're really beautiful.ogg"));
        allBlockSounds.add(new BlockSound("trump_t_you used to call me.ogg"));
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
