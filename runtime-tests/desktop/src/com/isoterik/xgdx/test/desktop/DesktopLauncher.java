package com.isoterik.xgdx.test.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.isoterik.xgdx.XGdx;
import com.isoterik.xgdx.test.XGDXTest;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		XGdx xGdx = XGdx.instance();
		int width = 1280;
		int height = 720;
		config.setWindowSizeLimits(width, height, width, height);
		new Lwjgl3Application(new XGDXTest(), config);
	}
}
