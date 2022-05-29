package com.abdalla.bushnaq.pluvia.scene;

import java.util.List;
import java.util.Random;

import com.abdalla.bushnaq.pluvia.renderer.GameEngine;
import com.abdalla.bushnaq.pluvia.renderer.GameObject;
import com.badlogic.gdx.graphics.Color;

public class NightFishScene extends AbstractScene {

	public NightFishScene(GameEngine gameEngine, Random rand, List<GameObject> renderModelInstances) {
		super(gameEngine, rand, renderModelInstances);
	}

	@Override
	public void create() {
		super.create();
		logo.setColor(Color.WHITE);
		gameEngine.renderEngine.setSkyBox(false);
		gameEngine.renderEngine.setShadowEnabled(true);
		// time
		gameEngine.renderEngine.setAlwaysDay(true);
		gameEngine.renderEngine.setDynamicDayTime(false);
		gameEngine.renderEngine.setFixedDayTime(8);
		// fog
		gameEngine.renderEngine.getFog().setBeginDistance(20f);
		gameEngine.renderEngine.getFog().setFullDistance(50f);
		gameEngine.renderEngine.getFog().setColor(Color.BLACK);
		// water
		gameEngine.renderEngine.getWater().setPresent(true);
		gameEngine.renderEngine.getWater().setWaveStrength(0.01f);
		createWater();
		// mirror
		gameEngine.renderEngine.getMirror().setPresent(false);
		createCity(gameEngine, 0, 0, -CITY_SIZE * 5, false, 2f);
		createFish(0.2f, 0.2f);
	}


}
