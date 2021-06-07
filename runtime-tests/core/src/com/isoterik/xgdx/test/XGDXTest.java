package com.isoterik.xgdx.test;

import io.github.isoterik.xgdx.Scene;
import io.github.isoterik.xgdx.XGdxGame;
import io.github.isoterik.xgdx.x2d.scenes.transition.SceneTransitions;

public class XGDXTest extends XGdxGame {
	@Override
	protected Scene initGame() {
		splashTransition = SceneTransitions.fade(1f);
		return new GameCamera2dTest();
	}
}
