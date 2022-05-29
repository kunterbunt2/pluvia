/*
 * Created on 10.07.2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package com.abdalla.bushnaq.pluvia.model.digit;

import java.util.List;

import com.abdalla.bushnaq.pluvia.game.Level;
import com.abdalla.bushnaq.pluvia.renderer.GameEngine;
import com.abdalla.bushnaq.pluvia.renderer.GameObject;
import com.abdalla.bushnaq.pluvia.renderer.Renderable;

public class Digit extends Renderable {
	float						x		= 0;
	float						y		= 0;
	float						z		= 0;
	private char				digit	= 0;
	private String				text;
	private int					digitPosition;
	private DigitType			digitType;
	private List<GameObject>	renderModelInstances;

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
		}
		if (getDigitPosition() >= sI.length())
			setDigit('0');
		else {
			char c = sI.charAt(getDigitPosition());
			setDigit(c);
		}
	}

	public DigitType getDigitType() {
		return digitType;
	}

	public int getDigitPosition() {
		return digitPosition;
	}

	public List<GameObject> getRenderModelInstances() {
		return renderModelInstances;
	}

	public String getText() {
		return text;
	}

}