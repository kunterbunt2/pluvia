/*
 * Created on Jul 25, 2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package de.bushnaq.abdalla.pluvia.game;

/**
 *
 * 13 rows of 5 columns, 8 different stones, with up to 3 stones that can fall at the same time and 5 rows already have fallen
 *
 *
 */
public class SixnGame extends Game {
	public SixnGame() {
		super(GameName.SIX.name(), 5, 13, 3, 8, 0, 5, 11, false);
	}

//	@Override
//	protected boolean queryWin(int aHeapHeight) {
//		if (aHeapHeight != 0) {
//			return false;
//		} else {
//			score = 1000;
//			return true;
//		}
//	}

//	@Override
//	public void updateScore(int aHeapHeight) {
//		if (aHeapHeight < 9)
//			score = Math.max(score, (nrOfRows - aHeapHeight) * nrOfColumns * getStoneScore());
//	}

//	@Override
//	public void updateScore(Stone aPatch[][]) {
//	}
}