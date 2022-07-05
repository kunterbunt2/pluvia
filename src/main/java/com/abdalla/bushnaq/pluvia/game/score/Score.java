/*
 * Created on 18.07.2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package com.abdalla.bushnaq.pluvia.game.score;

import com.abdalla.bushnaq.pluvia.game.GameDataObject;
import com.abdalla.bushnaq.pluvia.game.Level;
import com.abdalla.bushnaq.pluvia.game.recording.Recording;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Score implements Comparable<Score> {
	private Recording recording;

	public Score() {
	}

	public Score(Level level) {
		recording = level.getRecording();
		recording.setGdo(new GameDataObject(level));
	}

	public int compare(int x, int y) {
		return (x < y) ? -1 : ((x == y) ? 0 : 1);
	}

	@Override
	public int compareTo(Score another) {
		if (!another.getGame().equals(this.getGame())) {
			return this.getGame().compareTo(another.getGame());
		} else {
			if (another.getSeed() != this.getSeed())
				return compare(another.getSeed(), this.getSeed());
			else
				return compare(another.getScore(), this.getScore());
		}
	}

	@JsonIgnore
	public String getGame() {
		return recording.getGdo().getName();
	}

	public Recording getRecording() {
		return recording;
	}

	@JsonIgnore
	public long getRelativeTime() {
		return recording.getGdo().getRelativeTime();
	}

	@JsonIgnore
	public Integer getScore() {
		return recording.getGdo().getScore();
	}

	@JsonIgnore
	public int getSeed() {
		return recording.getGdo().getSeed();
	}

	@JsonIgnore
	public Integer getSteps() {
		return recording.getGdo().getSteps();
	}

	@JsonIgnore
	public long getTime() {
		return recording.getGdo().getTime();
	}

	@JsonIgnore
	public String getUserName() {
		return recording.getGdo().getUserName();
	}

	public void set(Score score) {
		this.recording = score.recording;
	}

	public void setRecording(Recording recording) {
		this.recording = recording;
	}

}