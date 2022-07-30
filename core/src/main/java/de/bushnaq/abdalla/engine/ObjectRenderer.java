package de.bushnaq.abdalla.engine;

import de.bushnaq.abdalla.pluvia.engine.GameEngine;

/**
 * @author kunterbunt
 *
 */
public abstract class ObjectRenderer {
	public void create(final float x, final float y, final float z, final GameEngine gameEngine) {
	}

	public void create(final GameEngine gameEngine) {
	}

	public void destroy(final GameEngine gameEngine) {
	}

	public void renderText(final float aX, final float aY, final float aZ, final RenderEngine sceneManager, final int index) {
	}

	public void renderText(final RenderEngine sceneManager, final int index, final boolean selected) {
	}

	public void update(final float x, final float y, final float z, final GameEngine gameEnginer, final long currentTime, final float timeOfDay, final int index, final boolean selected) throws Exception {
	}

	public void update(final GameEngine gameEngine, final long currentTime, final float timeOfDay, final int index, final boolean selected) throws Exception {
	}

	public boolean withinBounds(final float x, final float y) {
		return false;
	}
}
