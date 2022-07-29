package com.abdalla.bushnaq.pluvia.scene;

import java.util.List;

import com.abdalla.bushnaq.pluvia.engine.GameEngine;
import com.abdalla.bushnaq.pluvia.engine.GameObject;
import com.badlogic.gdx.graphics.Color;

public class NightFishScene extends AbstractScene {

	public NightFishScene(GameEngine gameEngine, List<GameObject> renderModelInstances) {
		super(gameEngine, renderModelInstances);
	}

	@Override
	public void create() {
		super.create();
		logo.setColor(getInfoColor());
		version.setColor(getInfoColor());
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
		gameEngine.renderEngine.getWater().setWaveStrength(0.05f);
		gameEngine.renderEngine.getWater().setRefractiveMultiplicator(1f);
		createWater();
		// mirror
		gameEngine.renderEngine.getMirror().setPresent(false);
		createCity(gameEngine, 0, 0, -CITY_SIZE * 5, false, 2f);
		createFish(1.0f, 0.2f);
	}

	@Override
	public Color getInfoColor() {
		return Color.WHITE;
	}

}
