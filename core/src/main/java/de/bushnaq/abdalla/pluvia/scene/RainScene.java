package de.bushnaq.abdalla.pluvia.scene;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import de.bushnaq.abdalla.engine.GameObject;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import de.bushnaq.abdalla.pluvia.engine.ModelManager;
import de.bushnaq.abdalla.pluvia.scene.model.rain.Rain;

/**
 * @author kunterbunt
 *
 */
public class RainScene extends AbstractScene {

	public RainScene(GameEngine gameEngine, List<GameObject> renderModelInstances) {
		super(gameEngine, renderModelInstances);
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
		// white fog
		gameEngine.renderEngine.getFog().setBeginDistance(20f);
		gameEngine.renderEngine.getFog().setFullDistance(50f);
		gameEngine.renderEngine.getFog().setColor(Color.BLACK);
		// water
		gameEngine.renderEngine.getWater().setPresent(false);
		createWater();
		// mirror
		gameEngine.renderEngine.getMirror().setPresent(true);
		gameEngine.renderEngine.getMirror().setReflectivity(0.5f);
		createMirror(Color.BLACK);
		createTurtles(1f, 1f);
		createRain(.02f, .01f);
	}

	protected void createRain(float minSize, float maxSize) {
		Vector3	min	= gameEngine.renderEngine.sceneBox.min;
		Vector3	max	= gameEngine.renderEngine.sceneBox.max;
		for (int i = 0; i < Math.min(gameEngine.context.getMaxSceneObjects(), 500); i++) {
			int			type	= rand.nextInt(ModelManager.MAX_NUMBER_OF_RAIN_MODELS);
			float		size	= minSize + (float) Math.random() * (maxSize - minSize);
			BoundingBox	b		= new BoundingBox(new Vector3(min.x + 10f, size / 2, min.z + 25), new Vector3(max.x - 10f, 4f, 0));
			Rain		rain	= new Rain(gameEngine, type, size, b);
			gameEngine.context.rainList.add(rain);
		}
	}

	@Override
	public Color getInfoColor() {
		return Color.WHITE;
	}

}
