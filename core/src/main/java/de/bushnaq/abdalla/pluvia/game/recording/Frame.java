package de.bushnaq.abdalla.pluvia.game.recording;

/**
 * Recording frame records all actions of the user for later playback
 *
 * @author abdal
 *
 */

public class Frame {
	private Interaction	interaction;
	private Integer		step;
	private Integer		x;
	private Integer		y;

	public Frame() {
	}

	public Frame(int x, int y, int step, Interaction interaction) {
		this.x = x;
		this.y = y;
		this.step = step;
		this.interaction = interaction;
	}

	public Frame(int step, Interaction interaction) {
		this.step = step;
		this.interaction = interaction;
	}

	public Interaction getInteraction() {
		return interaction;
	}

	public Integer getStep() {
		return step;
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

	public void setStep(Integer step) {
		this.step = step;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public void setY(Integer y) {
		this.y = y;
	}
}
