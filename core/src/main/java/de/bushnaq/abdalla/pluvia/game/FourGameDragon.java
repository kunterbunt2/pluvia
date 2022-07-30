/*
 * Created on Jul 25, 2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package de.bushnaq.abdalla.pluvia.game;

import de.bushnaq.abdalla.pluvia.game.model.stone.Stone;

/**
 *
 * 13 rows of 5 columns, 8 different stones, with up to 3 stones that can fall at the same time
 *
 *
 */
public class FourGameDragon extends Game {
	public FourGameDragon() {
		super(GameName.Dragon.name(), 5, 13, 3, 8, 0, 11, 11, false);
	}

	@Override
	public void addStoneScore() {
		// do not increment score for every stone that is in the level
	}

	/**
	 * if you lost the game, you get
	 */
	@Override
	public int getScore(Stone patch[][]) {
		int heapHeight = queryHeapHeight(patch);
		if (heapHeight < 9)
			score = Math.max(score, (nrOfRows - heapHeight) * nrOfColumns * getStoneScore());
		return score;
	}

	/**
	 * if you win this game, you get a total score of 100
	 */
	@Override
	protected boolean queryWin(Stone patch[][]) {
		for (int y = nrOfRows - 2; y >= preview; y--) {
			for (int x = 0; x < nrOfColumns; x++) {
				if (patch[x][y] != null) {
					return false;
				}
			}
		}
		score = 100;
		return true;
	}

	@Override
	protected void reset() {
		score = 0;
		steps = 0;
		relativeTime = 0;
	}

}