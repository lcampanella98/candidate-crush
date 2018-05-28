package com.gmail.enzocampanella98.candidatecrush.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 540;
		config.height = (int)Math.round(config.width * ((double)CandidateCrush.V_HEIGHT / CandidateCrush.V_WIDTH));
		new LwjglApplication(new CandidateCrush(), config);
}	}

