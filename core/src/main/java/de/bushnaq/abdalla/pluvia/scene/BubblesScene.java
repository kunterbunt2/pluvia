package de.bushnaq.abdalla.pluvia.scene;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import de.bushnaq.abdalla.engine.GameObject;
import de.bushnaq.abdalla.engine.RenderEngine3D;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import de.bushnaq.abdalla.pluvia.engine.ModelManager;
import de.bushnaq.abdalla.pluvia.scene.model.bubble.Bubble;

/**
 * @author kunterbunt
 *
 */
public class BubblesScene extends AbstractScene {

	public BubblesScene(RenderEngine3D<GameEngine> renderEngine, List<GameObject> renderModelInstances) {
		super(renderEngine, renderModelInstances);
	}

	@Override
	public void create() {
		super.create();
		logo.setColor(getInfoColor());
		version.setColor(getInfoColor());
		renderEngine.setSkyBox(false);
		renderEngine.setShadowEnabled(true);
		// time
		renderEngine.setAlwaysDay(true);
		renderEngine.setDynamicDayTime(true);
//		gameEngine.renderEngine.setFixedDayTime(8);
		// fog
		renderEngine.getFog().setBeginDistance(20f);
		renderEngine.getFog().setFullDistance(50f);
		renderEngine.getFog().setColor(Color.WHITE);
		// water
		renderEngine.getWater().setPresent(true);
		// mirror
		renderEngine.getMirror().setPresent(false);
		renderEngine.getMirror().setReflectivity(0.9f);
		createMirror(Color.WHITE);


		createPlane(Color.WHITE);
		createBubble(0.02f, 0.5f);
	}

	private void createBubble(float minSize, float maxSize) {
		Vector3	min	= renderEngine.getSceneBox().min;
		Vector3	max	= renderEngine.getSceneBox().max;
		for (int i = 0; i < Math.min(renderEngine.getGameEngine().context.getMaxSceneObjects(), 500); i++) {
			int			type	= rand.nextInt(ModelManager.MAX_NUMBER_OF_BUBBLE_MODELS);
			float		size	= minSize + (float) Math.random() * (maxSize - minSize);
			BoundingBox	b		= new BoundingBox(new Vector3(min.x + 4f, 1 + size / 2, min.z), new Vector3(max.x - 4f, 4f, -2));
			Bubble		bubble	= new Bubble(renderEngine, type, size, b);
			renderEngine.getGameEngine().context.bubbleList.add(bubble);
		}
	}

	@Override
	public Color getInfoColor() {
		return Color.BLACK;
	}

}
