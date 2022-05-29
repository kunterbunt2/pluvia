/*
 * Created on 18.07.2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package com.abdalla.bushnaq.pluvia.game;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;

public class Score implements Comparable<Score> {
	private long	time;
	private String	game;
	private long	relativeTime;
	private Integer	score	= 0;
	private Integer	steps	= 0;
	private String	userName;

	public Score() {
	}

	public Score(String game, int score, int steps, long relativeTime, long absoluteTime, String userName) {
		this.game = game;
		this.score = score;
		this.steps = steps;
		this.relativeTime = relativeTime;
		this.time = absoluteTime;
		this.userName = userName;
	}

	public int compare(int x, int y) {
		return (x < y) ? -1 : ((x == y) ? 0 : 1);
	}

	@Override
	public int compareTo(Score another) {
		return compare(another.score, this.score);
	}

	public long getAbsoluteTime() {
		return time;
	}

	public String getGame() {
		return game;
	}

	public long getRelativeTime() {
		return relativeTime;
	}

	public Integer getScore() {
		return score;
	}

	public Integer getSteps() {
		return steps;
	}

	public String getUserName() {
		return userName;
	}

	public void write(XMLEncoder encoder) {
		encoder.writeObject(time);
		encoder.writeObject(game);
		encoder.writeObject(relativeTime);
		encoder.writeObject(score);
		encoder.writeObject(steps);
		encoder.writeObject(userName);
	}

	public void read(XMLDecoder e) {
		time = (long) e.readObject();
		game = (String) e.readObject();
		relativeTime = (long) e.readObject();
		score = (int) e.readObject();
		steps = (int) e.readObject();
		userName = (String) e.readObject();
	}
}