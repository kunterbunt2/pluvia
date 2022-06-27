/*
 * Created on 18.07.2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package com.abdalla.bushnaq.pluvia.game;

public class Score implements Comparable<Score> {
	private String	game;
	private long	relativeTime;
	private int		score	= 0;
	private int		seed	= 0;
	private int		steps	= 0;
	private long	time;
	private String	userName;

	public void setGame(String game) {
		this.game = game;
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public void setRelativeTime(long relativeTime) {
		this.relativeTime = relativeTime;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Score() {
	}

	public Score(String game, int seed, int score, int steps, long relativeTime, long time, String userName) {
		this.game = game;
		this.seed = seed;
		this.score = score;
		this.steps = steps;
		this.relativeTime = relativeTime;
		this.time = time;
		this.userName = userName;
	}

	public int compare(int x, int y) {
		return (x < y) ? -1 : ((x == y) ? 0 : 1);
	}

	@Override
	public int compareTo(Score another) {
		if (!another.game.equals(this.game)) {
			return this.game.compareTo(another.game);
		} else {
			if (another.seed != this.seed)
				return compare(another.seed, this.seed);
			else
				return compare(another.score, this.score);
		}
	}

	public long getTime() {
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

	public int getSeed() {
		return seed;
	}

	public Integer getSteps() {
		return steps;
	}

	public String getUserName() {
		return userName;
	}

//	public void read(XMLDecoder e) {
//		time = (long) e.readObject();
//		game = (String) e.readObject();
//		seed = (int) e.readObject();
//		relativeTime = (long) e.readObject();
//		score = (int) e.readObject();
//		steps = (int) e.readObject();
//		userName = (String) e.readObject();
//	}
//
//	public void write(XMLEncoder encoder) {
//		encoder.writeObject(time);
//		encoder.writeObject(game);
//		encoder.writeObject(seed);
//		encoder.writeObject(relativeTime);
//		encoder.writeObject(score);
//		encoder.writeObject(steps);
//		encoder.writeObject(userName);
//	}

	public void set(Score score) {
		this.userName = score.userName;
		this.score = score.score;
		this.steps = score.steps;
		this.relativeTime = score.relativeTime;
		this.time = score.time;
	}
}