package com.abdalla.bushnaq.pluvia.game;

public class StoneDataObject {

	public StoneDataObject() {

	}

	public int type = 0;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int score = 0;

	public StoneDataObject(int type, int score) {
		this.type = type;
		this.score = score;
	}

}
