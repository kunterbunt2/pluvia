package de.bushnaq.abdalla.pluvia.desktop;

import java.util.ArrayList;

import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import de.bushnaq.abdalla.pluvia.engine.Renderable;

public class ModelList<T extends Renderable> extends ArrayList<T> {
	public void destroy(GameEngine gameEngine) {
		for (T t : this) {
			t.get3DRenderer().destroy(gameEngine);
		}
		clear();
	}
}
