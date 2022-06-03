package com.abdalla.bushnaq.pluvia.scene;

import java.util.List;
import java.util.Random;

import com.abdalla.bushnaq.pluvia.engine.GameEngine;
import com.abdalla.bushnaq.pluvia.engine.GameObject;
import com.abdalla.bushnaq.pluvia.engine.ModelManager;
import com.abdalla.bushnaq.pluvia.scene.model.firefly.Firefly;
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
		logo.setColor(getInfoColor());
		version.setColor(getInfoColor());
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
		gameEngine.renderEngine.getWater().setPresent(false);
		gameEngine.renderEngine.getWater().setWaveStrength(0.005f);
		gameEngine.renderEngine.getWater().setTiling(16f);
		gameEngine.renderEngine.getWater().setWaveSpeed(0.02f);
		gameEngine.renderEngine.getWater().setRefractiveMultiplicator(0f);
		createWater();
		// mirror
		gameEngine.renderEngine.getMirror().setPresent(false);
		gameEngine.renderEngine.getMirror().setReflectivity(0.9f);
		createMirror(Color.BLACK);

		createPlane(Color.BLACK);

		// generate instances
		createCity(gameEngine, 0, 0, -CITY_SIZE * 5, true, 2);
		createFirefly(0.02f, 0.02f);
	}

	private void createFirefly(float minSize, float maxSize) {
		Vector3	min	= gameEngine.renderEngine.sceneBox.min;
		Vector3	max	= gameEngine.renderEngine.sceneBox.max;
		for (int i = 0; i < Math.min(gameEngine.context.getMaxSceneObjects(), 500); i++) {
			int			type	= rand.nextInt(ModelManager.MAX_NUMBER_OF_FIRELY_MODELS);
			float		size	= minSize + (float) Math.random() * (maxSize - minSize);
			BoundingBox	b		= new BoundingBox(new Vector3(min.x + 4f, 1 + size / 2, min.z), new Vector3(max.x - 4f, 4f, -2));
			Firefly		fly		= new Firefly(gameEngine, type, size, b);
			gameEngine.context.flyList.add(fly);
		}
	}

	@Override
	public Color getInfoColor() {
		return Color.WHITE;
	}

}
