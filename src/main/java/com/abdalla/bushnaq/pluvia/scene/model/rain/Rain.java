/*
 * Created on 10.07.2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package com.abdalla.bushnaq.pluvia.scene.model.rain;

import com.abdalla.bushnaq.pluvia.renderer.GameEngine;
import com.abdalla.bushnaq.pluvia.scene.model.fish.Fish;
import com.abdalla.bushnaq.pluvia.util.TimeUnit;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

/**
 * @author bushnaq TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
public class Rain extends Fish {
//	int		pause	= 0;
	float radius = 10f;

	public Rain(GameEngine gameEngine, int type, float size, BoundingBox cage) {
		super();
		this.gameEngine = gameEngine;
		set3DRenderer(new Rain3DRenderer(this));
		this.type = type;
		this.size = size;
		this.cage = cage;
		position = new Vector3(0, 0, 0);
		choseStartingPoint(gameEngine);
		position.set(poi);
		get3DRenderer().create(gameEngine);
		setMaxSpeed(0.7f);
		setMinSpeed(0.7f);
		setAccellerationDistance(0f);
		currentMaxEngineSpeed = minSpeed;
	}

	@Override
	public void advanceInTime(long currentTime) {
		timeDelta = currentTime - lastTimeAdvancement;
		lastTimeAdvancement = currentTime;
		final float delta = (currentMaxEngineSpeed * timeDelta) / TimeUnit.TICKS_PER_DAY;
		position.y -= delta;

		{
			if (position.y < -1) {
				// we have arrived
				position = new Vector3(0, 0, 0);
				choseStartingPoint(gameEngine);
				position.set(poi);
			}
		}

	}

	@Override
	protected void choseStartingPoint(GameEngine gameEngine) {
		{
			poi.y = 10f + (float) Math.random() * 10;
		}
		{
			float	minX	= Math.max(cage.getCenterX() - cage.getWidth() / 2, position.x - radius);
			float	maxX	= Math.min(cage.getCenterX() + cage.getWidth() / 2, position.x + radius);
			poi.x = minX + (maxX - minX) * (float) Math.random();
		}
		{
			float	minZ	= Math.max(cage.getCenterZ() - cage.getDepth() / 2, position.z - radius);
			float	maxZ	= Math.min(cage.getCenterZ() + cage.getDepth() / 2, position.z + radius);
			poi.z = minZ + (maxZ - minZ) * (float) Math.random();
		}
	}

}