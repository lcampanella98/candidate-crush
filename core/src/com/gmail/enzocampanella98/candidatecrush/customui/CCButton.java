package com.gmail.enzocampanella98.candidatecrush.customui;

import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;

public class CCButton extends ImageTextButton {

    public CCButton(String text, ImageTextButtonStyle style) {
        super(text, style);
    }

    public float getHeightToWidthRatio() {
        return getStyle().checked.getMinHeight() / getStyle().checked.getMinWidth();
    }

}
