package com.isoterik.xgdx.test;

import io.github.isoteriktech.xgdx.Scene;
import io.github.isoteriktech.xgdx.XGdxGame;
import io.github.isoteriktech.xgdx.x2d.scenes.transition.SceneTransitions;

public class XGDXTest extends XGdxGame {
	@Override
	protected Scene initGame() {
		splashTransition = SceneTransitions.fade(1f);
		return new TiledMapTest();
	}
}
