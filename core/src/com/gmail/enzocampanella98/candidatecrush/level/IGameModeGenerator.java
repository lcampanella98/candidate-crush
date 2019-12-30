package com.gmail.enzocampanella98.candidatecrush.level;

import com.gmail.enzocampanella98.candidatecrush.gamemode.CCGameMode;
import com.gmail.enzocampanella98.candidatecrush.gamemode.config.GameModeConfig;

import javafx.stage.Stage;

public interface IGameModeGenerator {
    CCGameMode generate(Stage stage, GameModeConfig config);
}
