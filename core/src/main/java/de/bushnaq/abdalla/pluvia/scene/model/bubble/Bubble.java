/*
 * Created on 10.07.2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package de.bushnaq.abdalla.pluvia.scene.model.bubble;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import de.bushnaq.abdalla.pluvia.scene.model.fish.Fish;
import de.bushnaq.abdalla.pluvia.util.TimeUnit;

/**
 * @author bushnaq TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 */
public class Bubble extends Fish {
	Vector3	positionDelta	= new Vector3((float) Math.random() * 3, (float) Math.random() * 3, (float) Math.random() * 3);
//	Vector3	positionDelta	= new Vector3(1f, 2f, 3f);
	float	radius			= 5f;
	Vector3	rotation		= new Vector3((float) Math.random(), (float) Math.random(), (float) Math.random());

	public Bubble(GameEngine gameEngine, int type, float size, BoundingBox cage) {
		super();
		this.gameEngine = gameEngine;
		set3DRenderer(new Bubble3DRenderer(this));
		this.type = type;
		this.size = size;
		this.cage = cage;
		position = new Vector3(0, 0, 0);
		choseStartingPoint(gameEngine);
		position.set(poi);
		get3DRenderer().create(gameEngine);
		choseStartingPoint(gameEngine);
		setMaxSpeed(0.01f);
		setMinSpeed(0.005f);
		setAccellerationDistance(5f);
		currentMaxEngineSpeed = minSpeed;
		positionDelta.nor();
		positionDelta.scl((float) Math.random() * 3);
	}

	@Override
	public void advanceInTime(long currentTime) {
		timeDelta = currentTime - lastTimeAdvancement;
		lastTimeAdvancement = currentTime;
		final float delta = (currentMaxEngineSpeed * timeDelta) / TimeUnit.TICKS_PER_DAY;
		destinationPlanetDistanceProgress += delta;
		if (destinationPlanetDistanceProgress >= destinationPlanetDistance /* && TimeUnit.isInt(currentTime) ( ( currentTime - (int)currentTime ) == 0.0f ) */ ) {
			position.set(poi);
			choseStartingPoint(gameEngine);
		}
	}

	@Override
	protected void choseStartingPoint(GameEngine gameEngine) {
		{
			float	minX	= Math.max(cage.getCenterX() - cage.getWidth() / 2, position.x - radius);
			float	maxX	= Math.min(cage.getCenterX() + cage.getWidth() / 2, position.x + radius);
			poi.x = minX + (maxX - minX) * (float) Math.random();
		}
		{
			float	minY	= Math.max(cage.getCenterY() - cage.getHeight() / 2, position.y - radius);
			float	maxY	= Math.min(cage.getCenterY() + cage.getHeight() / 2, position.y + radius);
			poi.y = minY + (maxY - minY) * (float) Math.random();
		}
		{
			float	minZ	= Math.max(cage.getCenterZ() - cage.getDepth() / 2, position.z - radius);
			float	maxZ	= Math.min(cage.getCenterZ() + cage.getDepth() / 2, position.z + radius);
			poi.z = minZ + (maxZ - minZ) * (float) Math.random();
		}

		destinationPlanetDistance = queryDistance(position, poi);
		destinationPlanetDistanceProgress = 0;
	}

}