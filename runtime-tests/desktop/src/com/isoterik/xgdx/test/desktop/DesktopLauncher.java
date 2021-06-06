package com.isoterik.xgdx.test.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.isoterik.xgdx.test.XGDXTest;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		int width = 630;
		int height = 252;
		config.setWindowedMode(width, height);
		config.setResizable(true);
		new Lwjgl3Application(new XGDXTest(), config);
	}
}
