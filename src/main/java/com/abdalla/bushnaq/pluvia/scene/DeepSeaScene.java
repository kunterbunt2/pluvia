package com.abdalla.bushnaq.pluvia.scene;

import java.util.List;
import java.util.Random;

import com.abdalla.bushnaq.pluvia.engine.GameEngine;
import com.abdalla.bushnaq.pluvia.engine.GameObject;
import com.badlogic.gdx.graphics.Color;

public class DeepSeaScene extends AbstractScene {
	public DeepSeaScene(GameEngine gameEngine, Random rand, List<GameObject> renderModelInstances) {
		super(gameEngine, rand, renderModelInstances);
	}

	@Override
	public void create() {
		super.create();
		logo.setColor(Color.WHITE);
		gameEngine.renderEngine.setShadowEnabled(false);
		gameEngine.renderEngine.getFog().setColor(Color.BLUE);
		gameEngine.renderEngine.getFog().setBeginDistance(13f);
		gameEngine.renderEngine.getFog().setFullDistance(25f);
		gameEngine.renderEngine.getWater().setPresent(false);
//		createCity(gameEngine, 0, 0, -CITY_SIZE * 5);
		createFish(0.1f, 3f);
//		createFirefly();
	}
	@Override
	public Color getInfoColor() {
		return Color.WHITE;
	}

}
