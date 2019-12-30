package com.gmail.enzocampanella98.candidatecrush.customui;

import com.gmail.enzocampanella98.candidatecrush.level.Level;

public class LevelButton extends CCButton {

    private final Level level;

    public LevelButton(ImageTextButtonStyle style,
                       Level level,
                       boolean levelUnlocked) {
        super(getLevelButtonText(level), style);
        this.level = level;

        setDisabled(!levelUnlocked);
    }

    public Level getLevel() {
        return level;
    }

    public static String getLevelButtonText(Level level) {
        return "Level " + level.getLevelNumber();
    }

}
