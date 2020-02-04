package com.gmail.enzocampanella98.candidatecrush.level;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.gmail.enzocampanella98.candidatecrush.gamemode.CCGameMode;
import com.gmail.enzocampanella98.candidatecrush.gamemode.config.GameModeConfig;

public interface IGameModeGenerator {
    CCGameMode generate(Stage stage, GameModeConfig config);
}
