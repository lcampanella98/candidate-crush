package com.gmail.enzocampanella98.candidatecrush.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gmail.enzocampanella98.candidatecrush.CandidateCrush;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 600;
		config.height = 900;
		new LwjglApplication(new CandidateCrush(), config);
}	}

