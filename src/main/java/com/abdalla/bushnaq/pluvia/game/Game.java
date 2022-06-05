/*
 * Created on 18.07.2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package com.abdalla.bushnaq.pluvia.game;

import com.abdalla.bushnaq.pluvia.engine.AtlasManager;
import com.abdalla.bushnaq.pluvia.game.model.stone.Stone;
import com.abdalla.bushnaq.pluvia.util.sound.Tools;

public class Game {
	public float		cameraZPosition;
//	protected String	helpPath			= null;
	protected String	name				= null;	// ---Game name
	protected int		nrOfColumns			= 0;	// ---Size of board
	protected int		nrOfFallenRows		= 0;	// ---Level starts with lower part filled up
	protected int		nrOfFallingStones	= 0;	// ---Max count of simultaneous falling stones
	protected int		nrOfRows			= 0;	// ---Size of board
	protected int		nrOfStones			= 0;	// ---Max count of different stone types (colors)
	protected int		preview				= 0;	// ---Rows of visible stones that will drop in the next moves
	protected long		relativeTime		= 0;
	protected boolean	reset				= false;
	protected int		score				= 0;

	public long getRelativeTime() {
		return relativeTime;
	}

	public int getSteps() {
		return steps;
	}

	protected int		steps		= 0;
	protected String	userName	= "Test User";
	protected String	description;

//	public Game() {
//		helpPath = "doc/" + Name + ".html";
//	}

	public Game(String aName, int aNrOfColumns, int aNrOfRows, int aNrOfFallingStones, int aNrOfStones, int aPreview, int aNrOfFallenRows, float cameraZPosition, boolean aReset) {
		name = aName;
		nrOfColumns = aNrOfColumns;
		nrOfRows = aNrOfRows;
		nrOfFallingStones = aNrOfFallingStones;
		nrOfStones = aNrOfStones;
		preview = aPreview;
		nrOfFallenRows = aNrOfFallenRows;
//		helpPath = "doc/" + Name + ".html";
		reset = aReset;
		this.cameraZPosition = cameraZPosition;
	}

//	public void addScore() {
//		if (score > 0) {
////			scoreList.add(new Score(userName, System.currentTimeMillis(), relativeTime, score, steps));
//		} else {
//		}
//	}

//	public String getHelpPath() {
//		return helpPath;
//	}

	public String getName() {
		return name;
	}

	public int getNrOfColumns() {
		return nrOfColumns;
	}

	public int getNrOfFallenRows() {
		return nrOfFallenRows;
	}

	public int getNrOfFallingStones() {
		return nrOfFallingStones;
	}

	public int getNrOfRows() {
		return nrOfRows;
	}

	public int getNrOfStones() {
		return nrOfStones;
	}

	public int getPreview() {
		return preview;
	}

	public int getScore(Stone patch[][]) {
//		if (queryHeapHeight(patch) == 0)
			return score;
//		return -1;
	}

	protected int queryHeapHeight(Stone patch[][]) {
		for (int y = preview; y < nrOfRows; y++)
			for (int x = 0; x < nrOfColumns; x++)
				if (patch[x][y] != null)
					return nrOfRows - y;
		return 0;
	}

	protected int getStoneScore() {
		return 1;
	}

	public String getUserName() {
		return userName;
	}

	/**
	 * @return Returns the reset.
	 */
	public boolean isReset() {
		return reset;
	}

	protected boolean queryWin(Stone patch[][]) {
		return false;
	}

	protected boolean queryTilt(Stone patch[][]) {
		for (int x = 0; x < nrOfColumns; x++) {
			if (patch[x][preview] != null) {
				Tools.play(AtlasManager.getAssetsFolderName()+"/sound/tilt.wav");
				return true;
			}
		}
		return false;
	}

	protected void reset() {
		score = -nrOfColumns * nrOfRows * getStoneScore();
		steps = 0;
		relativeTime = 0;
	}

//	public void setHelpPath(String aHelpPath) {
//		helpPath = aHelpPath;
//	}

	public void setName(String aName) {
		name = aName;
	}

	public void setNrOfColumns(int aNrOfColumns) {
		nrOfColumns = aNrOfColumns;
	}

	public void setNrOfFallenRows(int aNrOfFallenRows) {
		nrOfFallenRows = aNrOfFallenRows;
	}

	public void setNrOfFallingStones(int aNrOfFallingStones) {
		nrOfFallingStones = aNrOfFallingStones;
	}

	public void setNrOfRows(int aNrOfRows) {
		nrOfRows = aNrOfRows;
	}

	public void setNrOfStones(int aNrOfStones) {
		nrOfStones = aNrOfStones;
	}

	public void setPreview(int aPreview) {
		preview = aPreview;
	}

	/**
	 * @param aReset The reset to set.
	 */
	public void setReset(boolean aReset) {
		reset = aReset;
	}

	public void setScore(int aScore) {
		score = aScore;
	}

	public void setUserName(String aUserName) {
		userName = aUserName;
	}

//	public void updatePatchScore(Stone aPatch[][]) {
//		for (int y = preview; y < nrOfRows; y++) {
//			for (int x = 0; x < nrOfColumns; x++) {
//				if (aPatch[x][y] != null) {
//					if (aPatch[x][y].score == 0) {
//						aPatch[x][y].score = 1;
//					}
//				}
//			}
//		}
//	}

//	public void updateScore(int aHeapHeight) {
//	}

	public void addStoneScore() {
		score += getStoneScore();
	}

//	public void updateScore(Stone aPatch[][]) {
//		for (int y = preview; y < nrOfRows; y++) {
//			for (int x = 0; x < nrOfColumns; x++) {
//				if ((aPatch[x][y] != null) && aPatch[x][y].isVanishing()) {
////					if (!aPatch[x][y].isCanDrop())
//					{
//						score += aPatch[x][y].score;
//					}
//				}
//			}
//		}
//	}
}