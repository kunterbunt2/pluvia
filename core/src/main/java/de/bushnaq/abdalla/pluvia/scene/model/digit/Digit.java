/*
 * Created on 10.07.2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package de.bushnaq.abdalla.pluvia.scene.model.digit;

import java.util.List;

import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import de.bushnaq.abdalla.pluvia.engine.GameObject;
import de.bushnaq.abdalla.pluvia.engine.Renderable;
import de.bushnaq.abdalla.pluvia.game.Level;

public class Digit extends Renderable {
	private char				digit	= 0;
	private int					digitPosition;
	private DigitType			digitType;
	private List<GameObject>	renderModelInstances;
	private String				text;
	float						x		= 0;
	float						y		= 0;
	float						z		= 0;

	public Digit(List<GameObject> renderModelInstances, GameEngine gameEngine, float x, float y, float z, int digitPosition, DigitType digitType) {
		this.renderModelInstances = renderModelInstances;
		set3DRenderer(new Digit3DRenderer(this));
		this.x = x;
		this.y = y;
		this.z = z;
		this.digitPosition = digitPosition;
		this.digitType = digitType;
		get3DRenderer().create(gameEngine);
	}

	public char getDigit() {
		return digit;
	}

	public int getDigitPosition() {
		return digitPosition;
	}

	public DigitType getDigitType() {
		return digitType;
	}

	public List<GameObject> getRenderModelInstances() {
		return renderModelInstances;
	}

	public String getText() {
		return text;
	}

	public void setDigit(char digit) {
		this.digit = digit;
	}

	public void update(Level level) {
		String sI = "";
		switch (getDigitType()) {
		case score:
			sI = String.format("%+05d", level.getScore());
			break;
		case steps:
			sI = String.format("%+05d", level.getSteps());
			break;
		case name:
			text = level.getName();
			break;
		case seed:
			sI = String.format("%4d", level.getSeed());
			break;
		}
		if (getDigitPosition() >= sI.length())
			setDigit('0');
		else {
			char c = sI.charAt(getDigitPosition());
			setDigit(c);
		}
	}

}