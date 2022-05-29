package com.abdalla.bushnaq.pluvia.scene;

import java.util.List;
import java.util.Random;

import com.abdalla.bushnaq.pluvia.model.firefly.Firefly;
import com.abdalla.bushnaq.pluvia.renderer.GameEngine;
import com.abdalla.bushnaq.pluvia.renderer.GameObject;
import com.abdalla.bushnaq.pluvia.renderer.ModelManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class FireflyScene extends AbstractScene {

	public FireflyScene(GameEngine gameEngine, Random rand, List<GameObject> renderModelInstances) {
		super(gameEngine, rand, renderModelInstances);
	}

	@Override
	public void create() {
		super.create();
		logo.setColor(Color.WHITE);
		gameEngine.renderEngine.setSkyBox(false);
		gameEngine.renderEngine.setShadowEnabled(false);
		// time
		gameEngine.renderEngine.setAlwaysDay(true);
		gameEngine.renderEngine.setDynamicDayTime(true);
		gameEngine.renderEngine.setFixedDayTime(8);
		// fog
//		gameEngine.renderEngine.getFog().setBeginDistance(20f);
//		gameEngine.renderEngine.getFog().setFullDistance(50f);
		gameEngine.renderEngine.getFog().setBeginDistance(1120f);
		gameEngine.renderEngine.getFog().setFullDistance(1150f);
		gameEngine.renderEngine.getFog().setColor(Color.BLACK);
		// water
		gameEngine.renderEngine.getWater().setPresent(true);
		gameEngine.renderEngine.getWater().setWaveStrength(0.02f);
		createWater();
		// mirror
		gameEngine.renderEngine.getMirror().setPresent(false);
		gameEngine.renderEngine.getMirror().setReflectivity(0.5f);
		createMirror(Color.BLACK);

		// generate instances
		createCity(gameEngine, 0, 0, -CITY_SIZE * 5, true, 2);
		createFirefly(0.02f, 0.02f);
	}

	private void createFirefly(float minSize, float maxSize) {
		Vector3	min	= gameEngine.renderEngine.sceneBox.min;
		Vector3	max	= gameEngine.renderEngine.sceneBox.max;
		for (int i = 0; i < 500; i++) {
			int			type	= rand.nextInt(ModelManager.MAX_NUMBER_OF_FIRELY_MODELS);
			float		size	= minSize + (float) Math.random() * (maxSize - minSize);
			BoundingBox	b		= new BoundingBox(new Vector3(min.x + 4f, 1 + size / 2, min.z), new Vector3(max.x - 4f, 4f, -2));
			Firefly		fly		= new Firefly(gameEngine, type, size, b);
			gameEngine.context.flyList.add(fly);
		}
	}

}