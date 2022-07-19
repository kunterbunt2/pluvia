package com.abdalla.bushnaq.pluvia.game.recording;

/**
 * Recording frame records all actions of the user for later playback
 *
 * @author abdal
 *
 */

public class Frame {
	private Interaction	interaction;
	private Integer		x;
	private Integer		y;

	public Frame() {
	}

	public Frame(int x, int y, Interaction interaction) {
		this.x = x;
		this.y = y;
		this.interaction = interaction;
	}

	public Frame(Interaction interaction) {
		this.interaction = interaction;
	}

	public Interaction getInteraction() {
		return interaction;
	}

	public Integer getX() {
		return x;
	}

	public Integer getY() {
		return y;
	}

	public void setInteraction(Interaction interaction) {
		this.interaction = interaction;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public void setY(Integer y) {
		this.y = y;
	}
}
