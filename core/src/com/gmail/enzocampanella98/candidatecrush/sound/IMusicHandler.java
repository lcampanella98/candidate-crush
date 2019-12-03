package com.gmail.enzocampanella98.candidatecrush.sound;

import com.gmail.enzocampanella98.candidatecrush.board.BlockType;

public interface IMusicHandler {
    void queueSoundByte(BlockType type, char level);
    void playPopIfNoMusicPlaying();
}
