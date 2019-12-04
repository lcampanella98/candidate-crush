package com.gmail.enzocampanella98.candidatecrush.customui;

import com.gmail.enzocampanella98.candidatecrush.screens.MenuScreen;


public class GameModeButton extends CCButton {

    private MenuScreen.GameMode gameModeType;
    private boolean requiresParty = false;
    private boolean requiresCandidate = false;

    public GameModeButton(String text, ImageTextButtonStyle style, MenuScreen.GameMode gameModeType) {
        super(text, style);
        this.gameModeType = gameModeType;
    }

    public MenuScreen.GameMode getGameModeType() {
        return this.gameModeType;
    }

    public boolean isRequiresParty() {
        return requiresParty;
    }

    public void setRequiresParty(boolean requiresParty) {
        this.requiresParty = requiresParty;
    }

    public boolean isRequiresCandidate() {
        return requiresCandidate;
    }

    public void setRequiresCandidate(boolean requiresCandidate) {
        this.requiresCandidate = requiresCandidate;
    }

}
