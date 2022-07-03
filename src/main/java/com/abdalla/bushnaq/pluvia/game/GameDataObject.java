package com.abdalla.bushnaq.pluvia.game;

public class GameDataObject {
	private StoneDataObject[][]	patch			= null;
	private int					randCalls		= 0;
	private long				relativeTime	= 0;
	private int					score			= 0;
	private int					seed			= 0;
	private int					steps			= 0;

	public GameDataObject() {

	}

	public GameDataObject(Level level) {
		this.score = level.game.score;
		this.steps = level.game.steps;
		this.seed = level.rand.getSeed();
		this.randCalls = level.rand.getCalls();
		this.relativeTime = level.game.relativeTime;
		patch = new StoneDataObject[level.game.nrOfColumns][];
		for (int x = 0; x < patch.length; x++) {
			patch[x] = new StoneDataObject[level.game.nrOfRows];
		}
		for (int y = level.game.nrOfRows - 1; y >= 0; y--) {
			for (int x = 0; x < level.game.nrOfColumns; x++) {
				if (level.patch[x][y] != null) {
					patch[x][y] = new StoneDataObject(level.patch[x][y].type, level.patch[x][y].score);
				}
			}
		}
	}

	public StoneDataObject[][] getPatch() {
		return patch;
	}

	public int getRandCalls() {
		return randCalls;
	}

	public long getRelativeTime() {
		return relativeTime;
	}

	public int getScore() {
		return score;
	}

	public int getSeed() {
		return seed;
	}

	public int getSteps() {
		return steps;
	}

	public void setPatch(StoneDataObject[][] patch) {
		this.patch = patch;
	}

	public void setRandCalls(int randCalls) {
		this.randCalls = randCalls;
	}

	public void setRelativeTime(long relativeTime) {
		this.relativeTime = relativeTime;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

}
