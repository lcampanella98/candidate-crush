package com.gmail.enzocampanella98.candidatecrush.board;

/**
 * Created by Lorenzo Campanella on 6/2/2016.
 */
public enum BlockType {

    TRUMP("trump", "Donald Trump"),
    CLINTON("clinton", "Hillary Clinton"),
    SANDERS("sanders", "Bernie Sanders"),
    CRUZ("cruz", "Ted Cruz"),
    BIDEN("biden", "Joe Biden"),
    WARREN("warren", "Elizabeth Warren"),
    BUTTIGIEG("buttigieg", "Pete Buttigieg"),

    // the oak baes
    CAMPANELLA("campanella", "Lorenzo Campanella"),
    YOUNG("young", "Daniel Young"),
    MEZA("meza", "Daniel Meza"),
    DAUDELIN("daudelin", "John Daudelin"),
    GHATTAS("ghattas", "Yasmine Ghattas"),
    KOZAN("kozan", "Natalie Kozan"),
    LOMUSCIO("lomuscio", "Samantha Lomuscio"),
    MORGAN("morgan", "Sarah Morgan"),

    // the oak baes items
    SPIKEBALL("spikeball", "A Spikeball"),
    QUESADILLA("quesadilla", "A Quesadilla"),
    TURKEY("turkey", "A Turkey"),
    PUMPKIN("pumpkin", "Disguised Pumpkin");

    //MEZA("meza", "Daniel Meza"),

    private static final String SPRITE_ROOT = "data/img/block_sprites/";
    private String friendlyName, internalPath, internalPathWithSound, lname;

    BlockType(String lname, String friendlyName) {
        this.lname = lname;
        this.internalPath = SPRITE_ROOT + lname + ".png";
        this.internalPathWithSound = SPRITE_ROOT + lname + "_soundbite.png";
        this.friendlyName = friendlyName;
    }

    @Override
    public String toString() {
        return getFriendlyName();
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public String getLname() {
        return lname;
    }

    public String getInternalPath() {
        return internalPath;
    }

    public String getInternalPathWithSound() {
        return internalPathWithSound;
    }
}
