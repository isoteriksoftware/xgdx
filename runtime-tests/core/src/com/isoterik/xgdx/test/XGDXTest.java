package com.isoterik.xgdx.test;

import com.isoterik.xgdx.Scene;
import com.isoterik.xgdx.XGdxGame;
import com.isoterik.xgdx.x2d.scenes.transition.SceneTransitions;

public class XGDXTest extends XGdxGame {
	@Override
	protected Scene initGame() {
		splashTransition = SceneTransitions.fade(1f);
		return new ActorGameObjectTest();
	}
}
