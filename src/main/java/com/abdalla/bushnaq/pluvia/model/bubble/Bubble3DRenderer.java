package com.abdalla.bushnaq.pluvia.model.bubble;

import java.util.ArrayList;
import java.util.List;

import com.abdalla.bushnaq.pluvia.renderer.GameEngine;
import com.abdalla.bushnaq.pluvia.renderer.GameObject;
import com.abdalla.bushnaq.pluvia.renderer.ObjectRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.math.Vector3;

import net.mgsx.gltf.scene3d.model.ModelInstanceHack;

public class Bubble3DRenderer extends ObjectRenderer {
	private static final float		SIZE_X					= 1.0f;
	private static final float		SIZE_Y					= 1.0f;
	private static final float		SIZE_Z					= 1.0f;
	private static final float		NORMAL_LIGHT_INTENSITY	= 1f;
	private final Vector3			direction				= new Vector3();	// intermediate value
	private Bubble					bubble;
	private GameObject				instance;
	// private GameObject poiInstance;
	private float					lightIntensity			= 0f;
	private boolean					lightIsOne				= false;
	private final List<PointLight>	pointLight				= new ArrayList<>();
	private final Vector3			translation				= new Vector3();	// intermediate value
	private final Vector3			lightDelta;
	private Vector3					lightPosition			= new Vector3();

	public Bubble3DRenderer(final Bubble patch) {
		this.bubble = patch;
		lightDelta = new Vector3((float) Math.random() * bubble.getSize() * 2, (float) Math.random() * bubble.getSize() * 2, (float) Math.random() * bubble.getSize() * 2);
	}

	@Override
	public void create(final GameEngine gameEngine) {
		if (instance == null) {
			if (gameEngine.renderEngine.isPbr()) {
				instance = new GameObject(new ModelInstanceHack(gameEngine.modelManager.bubbleModelPbr[bubble.getType()].scene.model), null);
//				poiInstance = new GameObject(new ModelInstanceHack(gameEngine.modelManager.fishCubePbr[0]), fish);
			} else {
				instance = new GameObject(new ModelInstanceHack(gameEngine.modelManager.bubbleModel[bubble.getType()].scene.model), null);
			}
			gameEngine.renderEngine.addDynamic(instance);
//			gameEngine.renderEngine.addDynamic(poiInstance);
			instance.update();
//			poiInstance.update();
		}
	}

	@Override
	public void destroy(final GameEngine gameEngine) {
		gameEngine.renderEngine.removeDynamic(instance);
		for (PointLight pl : pointLight) {
			gameEngine.renderEngine.remove(pl, true);
		}
	}

	private void tuneLightIntensity() {
		if (lightIsOne) {
			if (lightIntensity < NORMAL_LIGHT_INTENSITY)
				lightIntensity += Math.signum(NORMAL_LIGHT_INTENSITY - lightIntensity) * 0.1f;
			for (PointLight pl : pointLight) {
				pl.intensity = lightIntensity;
			}
		}
	}

	private void turnLightOff(final GameEngine gameEngine) {
		if (lightIsOne) {
			for (PointLight pl : pointLight) {
				gameEngine.renderEngine.remove(pl, true);
			}
			lightIsOne = false;
		}
	}

	private static Color	DIAMON_BLUE_COLOR	= new Color(0x006ab6ff);
	private static Color	GRAY_COLOR			= new Color(0x404853ff);
	private static Color	POST_GREEN_COLOR	= new Color(0x00614eff);
	private static Color	SCARLET_COLOR		= new Color(0xb00233ff);

	private void turnLightOn(final GameEngine gameEngine) {
		if (!lightIsOne) {
			lightIntensity = 0f;
			Color[]				colors	= new Color[] { Color.WHITE, POST_GREEN_COLOR, SCARLET_COLOR, DIAMON_BLUE_COLOR, GRAY_COLOR, Color.CORAL, Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, Color.GOLD,
					Color.MAGENTA, Color.YELLOW };
//			Color				color	= Color.RED;
//			if (gameEngine.renderEngine.isPbr()) {
//				Material			material	= instance.instance.model.materials.get(0);
//				Attribute			attribute	= material.get(PBRColorAttribute.BaseColorFactor);
//				PBRColorAttribute	a			= (PBRColorAttribute) attribute;
//				color = a.color;
//			} else {
//				Material		material	= instance.instance.model.materials.get(0);
//				Attribute		attribute	= material.get(ColorAttribute.Diffuse);
//				ColorAttribute	a			= (ColorAttribute) attribute;
//				color = a.color;
//			}

			final PointLight	light	= new PointLight().set(colors[bubble.getType()], 0f, 0f, 0f, lightIntensity);
			pointLight.add(light);
			gameEngine.renderEngine.add(light, true);
			lightIsOne = true;
		}

	}

	@Override
	public void update(final float x, final float y, final float z, final GameEngine gameEngine, final long currentTime, final float timeOfDay, final int index, final boolean selected) throws Exception {
		bubble.calculateEngineSpeed();
		if (bubble.position != null)
			bubble.speed.set(bubble.poi.x - bubble.position.x, 0, bubble.poi.z - bubble.position.z);
		else
			bubble.speed.set(1, 0, 1);
		bubble.speed.nor();
		bubble.speed.scl(bubble.currentMaxEngineSpeed);
		final float	scalex	= (bubble.poi.x - bubble.position.x);
		final float	scaley	= (bubble.poi.y - bubble.position.y);
		final float	scalez	= (bubble.poi.z - bubble.position.z);
		direction.set(scalex, scaley, scalez);
		translation.x = (bubble.position.x + (bubble.poi.x - bubble.position.x) * bubble.destinationPlanetDistanceProgress / bubble.destinationPlanetDistance);
		translation.y = (bubble.position.y + (bubble.poi.y - bubble.position.y) * bubble.destinationPlanetDistanceProgress / bubble.destinationPlanetDistance);
		translation.z = (bubble.position.z + (bubble.poi.z - bubble.position.z) * bubble.destinationPlanetDistanceProgress / bubble.destinationPlanetDistance);
		lightPosition.set(translation);
		lightPosition.add(lightDelta);
		lightDelta.rotate(Vector3.Y, (float) Math.random());
		lightDelta.rotate(Vector3.X, (float) Math.random());
		lightDelta.rotate(Vector3.Z, (float) Math.random());

		bubble.rotation.x += .05f;
		bubble.rotation.y += .05f;
		bubble.rotation.z += .05f;
		// fly.positionDelta.rotate(Vector3.Y, (float) Math.random() * 2);
//		fly.positionDelta.rotate(Vector3.X, (float) Math.random() * 2);
//		fly.positionDelta.rotate(Vector3.Z, (float) Math.random() * 2);
		for (PointLight pl : pointLight) {
			pl.setPosition(lightPosition);
		}
		turnLightOn(gameEngine);
		tuneLightIntensity();
		{

			instance.instance.transform.setToTranslation(translation);
			instance.instance.transform.rotate(Vector3.X, bubble.rotation.x);
			instance.instance.transform.rotate(Vector3.Y, bubble.rotation.y);
			instance.instance.transform.rotate(Vector3.Z, bubble.rotation.z);

			instance.instance.transform.translate(bubble.positionDelta);
//			instance.instance.transform.rotateTowardDirection(direction, Vector3.Y);
			instance.instance.transform.scale(SIZE_X * bubble.getSize(), SIZE_Y * bubble.getSize(), SIZE_Z * bubble.getSize());
			instance.update();
		}

	}
}
