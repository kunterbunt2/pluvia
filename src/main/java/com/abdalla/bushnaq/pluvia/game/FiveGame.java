/*
 * Created on 20.08.2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package com.abdalla.bushnaq.pluvia.game;

/**
 *
 * 13 rows of 5 columns, 5 different stones, with up to 5 stones that can fall at the same time
 *
 *
 */
public class FiveGame extends FourGameDragon {
	public FiveGame() {
		name = GameName.FIVE.name();
	}

//	@Override
//	public void updatePatchScore(Stone aPatch[][]) {
//		for (int y = preview; y < nrOfRows; y++) {
//			for (int x = 0; x < nrOfColumns; x++) {
//				if (aPatch[x][y] != null) {
//					if (aPatch[x][y].isLeftAttached()) {
//						int newScore = /* aPatch[x - 1][y].score **/ 2;
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
//					if (aPatch[x][y].score > score) {
//						score = aPatch[x][y].score;
//					} else {
//					}
//				} else {
//				}
//			}
//		}
//	}

//	@Override
//	public void updateScore(Stone aPatch[][]) {
//		for (int y = Preview; y < NrOfRows; y++) {
//			for (int x = 0; x < NrOfColumns; x++) {
//				if ((aPatch[x][y] != null) && aPatch[x][y].isCanVanish() && (aPatch[x][y].Score > Score)) {
//					if (!aPatch[x][y].isCanDrop()) {
//						Score = aPatch[x][y].Score;
//					}
//				}
//			}
//		}
//	}
}