package com.abdalla.bushnaq.pluvia.scene;

import java.util.List;
import java.util.Random;

import com.abdalla.bushnaq.pluvia.renderer.GameEngine;
import com.abdalla.bushnaq.pluvia.renderer.GameObject;
import com.badlogic.gdx.graphics.Color;

public class TurtlesScene extends AbstractScene {

	public TurtlesScene(GameEngine gameEngine, Random rand, List<GameObject> renderModelInstances) {
		super(gameEngine, rand, renderModelInstances);
	}

	@Override
	public void create() {
		super.create();
		logo.setColor(Color.BLACK);
		gameEngine.renderEngine.setSkyBox(false);
		gameEngine.renderEngine.setShadowEnabled(true);
		// time
		gameEngine.renderEngine.setAlwaysDay(true);
		gameEngine.renderEngine.setDynamicDayTime(true);
		gameEngine.renderEngine.setFixedDayTime(8);
		// white fog
		gameEngine.renderEngine.getFog().setColor(Color.WHITE);
		gameEngine.renderEngine.getFog().setBeginDistance(20f);
		gameEngine.renderEngine.getFog().setFullDistance(50f);
		// water
		gameEngine.renderEngine.getWater().setPresent(false);
		// mirror
		gameEngine.renderEngine.getMirror().setPresent(false);
		gameEngine.renderEngine.getMirror().setReflectivity(0.5f);
//		createMirror(Color.WHITE);

		createPlane(Color.WHITE);
		createTurtles(1f, 1f);
	}
	@Override
	public Color getInfoColor() {
		return Color.BLACK;
	}

}
