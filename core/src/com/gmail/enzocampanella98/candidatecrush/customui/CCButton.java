package com.gmail.enzocampanella98.candidatecrush.customui;

import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;

public class CCButton extends ImageTextButton {

    public CCButton(String text, ImageTextButtonStyle style) {
        super(text, style);
    }

    private float getHeightToWidthRatio() {
        return getStyle().checked.getMinHeight() / getStyle().checked.getMinWidth();
    }

    private float getWidthToHeightRadio() {
        return 1.0f / getHeightToWidthRatio();
    }

    public float scaledWidth(float h) {
        return h * getWidthToHeightRadio();
    }

    public float scaledHeight(float w) {
        return w * getHeightToWidthRatio();
    }

}
