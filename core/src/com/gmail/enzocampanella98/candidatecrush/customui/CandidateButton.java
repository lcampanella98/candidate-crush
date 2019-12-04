package com.gmail.enzocampanella98.candidatecrush.customui;

import com.gmail.enzocampanella98.candidatecrush.board.BlockType;

import static com.gmail.enzocampanella98.candidatecrush.tools.Methods.firstToUpper;

public class CandidateButton extends CCButton {

    private BlockType blockType;

    public CandidateButton(ImageTextButtonStyle style, BlockType blockType) {
        super(firstToUpper(blockType.getLname()), style);
        this.blockType = blockType;
    }

    public BlockType getBlockType() {
        return blockType;
    }

}
