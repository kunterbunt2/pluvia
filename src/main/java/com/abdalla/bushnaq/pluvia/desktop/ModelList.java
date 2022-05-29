package com.abdalla.bushnaq.pluvia.desktop;

import java.util.ArrayList;

import com.abdalla.bushnaq.pluvia.renderer.GameEngine;
import com.abdalla.bushnaq.pluvia.renderer.Renderable;

public class ModelList<T extends Renderable> extends ArrayList<T> {
	public void destroy(GameEngine gameEngine) {
		for (T t : this) {
			t.get3DRenderer().destroy(gameEngine);
		}
		clear();
	}
}
