/*
 * Created on Jul 25, 2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package com.abdalla.bushnaq.pluvia.game;

import com.abdalla.bushnaq.pluvia.model.stone.Stone;

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

//	@Override
//	public void newStone() {
//	}

	@Override
	public void addStoneScore() {
		// do not increment score for every stone that is in the level
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

	/**
	 *if you lost the game, you get 
	 */
	public int getScore(Stone patch[][]) {
		int heapHeight = queryHeapHeight(patch);
		if (heapHeight < 9)
			score = Math.max(score, (nrOfRows - heapHeight) * nrOfColumns * getStoneScore());
		return score;
	}
	protected void reset() {
		score = 0;
		steps = 0;
		relativeTime = 0;
	}

//	@Override
//	public void updatePatchScore(Stone aPatch[][]) {
//		for (int y = preview; y < nrOfRows; y++) {
//			for (int x = 0; x < nrOfColumns; x++) {
//				if (aPatch[x][y] != null) {
//					if (aPatch[x][y].isLeftAttached()) {
//						int newScore = aPatch[x - 1][y].score * 2;
//						if (newScore > aPatch[x][y].score) {
//							aPatch[x][y].score = newScore;
//						} else {
//						}
//					} else if (aPatch[x][y].isRightAttached()) {
//						int newScore = 2;
//						if (newScore > aPatch[x][y].score) {
//							aPatch[x][y].score = newScore;
//						} else {
//						}
//					}
//				}
//			}
//		}
	/*
	 * for( int y = Preview; y < NrOfRows; y++ ) { for( int x = 0; x < NrOfColumns; x++ ) { if( aPatch[x][y] != null ) { if( aPatch[x][y].Score == 0 ) { //---Find the sum of all attached patched //---Find the left side
	 * int sum = 0; boolean attached = false; int tempX = x; while( (tempX > 0) && aPatch[tempX][y].isLeftAttached() ) { tempX--; sum += aPatch[tempX][y].Score; attached = true; } tempX = x; while( (tempX <
	 * NrOfColumns-1) && aPatch[tempX][y].isRightAttached() ) { tempX++; sum += aPatch[tempX][y].Score; attached = true; } if( (sum == 0) && attached ) { aPatch[x][y].Score = 2; } else { aPatch[x][y].Score = sum; } }
	 * else { if( !aPatch[x][y].isLeftAttached() && !aPatch[x][y].isRightAttached() ) { aPatch[x][y].Score = 0; } } } } }
	 */
	/*
	 * for( int y = Preview; y < NrOfRows; y++ ) { for( int x = 0; x < NrOfColumns; x++ ) { if( aPatch[x][y] != null ) { if( aPatch[x][y].Score == 0 ) { //---Find the sum of all attached patched //---Find the left side
	 * int sum = 0; boolean attached = false; int tempX = x; while( (tempX > 0) && aPatch[tempX][y].isLeftAttached() ) { tempX--; sum += aPatch[tempX][y].Score; attached = true; } tempX = x; while( (tempX <
	 * NrOfColumns-1) && aPatch[tempX][y].isRightAttached() ) { tempX++; sum += aPatch[tempX][y].Score; attached = true; } if( (sum == 0) && attached ) { aPatch[x][y].Score = 2; } else { aPatch[x][y].Score = sum; } }
	 * else { if( !aPatch[x][y].isLeftAttached() && !aPatch[x][y].isRightAttached() ) { aPatch[x][y].Score = 0; } } } } }
	 */
//	}
}