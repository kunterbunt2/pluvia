package de.bushnaq.abdalla.pluvia.scene;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import de.bushnaq.abdalla.pluvia.engine.GameObject;
import de.bushnaq.abdalla.pluvia.engine.ModelManager;
import de.bushnaq.abdalla.pluvia.scene.model.bubble.Bubble;

public class BubblesScene extends AbstractScene {

	public BubblesScene(GameEngine gameEngine, List<GameObject> renderModelInstances) {
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
		gameEngine.renderEngine.setDynamicDayTime(true);
//		gameEngine.renderEngine.setFixedDayTime(8);
		// fog
		gameEngine.renderEngine.getFog().setBeginDistance(20f);
		gameEngine.renderEngine.getFog().setFullDistance(50f);
		gameEngine.renderEngine.getFog().setColor(Color.WHITE);
		// water
		gameEngine.renderEngine.getWater().setPresent(false);
		// mirror
		gameEngine.renderEngine.getMirror().setPresent(false);
		gameEngine.renderEngine.getMirror().setReflectivity(0.9f);
		createMirror(Color.WHITE);

		createPlane(Color.WHITE);
		createBubble(0.02f, 0.5f);
	}

	private void createBubble(float minSize, float maxSize) {
		Vector3	min	= gameEngine.renderEngine.sceneBox.min;
		Vector3	max	= gameEngine.renderEngine.sceneBox.max;
		for (int i = 0; i < Math.min(gameEngine.context.getMaxSceneObjects(), 500); i++) {
			int			type	= rand.nextInt(ModelManager.MAX_NUMBER_OF_BUBBLE_MODELS);
			float		size	= minSize + (float) Math.random() * (maxSize - minSize);
			BoundingBox	b		= new BoundingBox(new Vector3(min.x + 4f, 1 + size / 2, min.z), new Vector3(max.x - 4f, 4f, -2));
			Bubble		bubble	= new Bubble(gameEngine, type, size, b);
			gameEngine.context.bubbleList.add(bubble);
		}
	}

	@Override
	public Color getInfoColor() {
		return Color.BLACK;
	}

}
