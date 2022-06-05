/* ---------------------------------------------------------------------------
 * BEGIN_PROJECT_HEADER
 *
 *       RRRR  RRR    IIIII    CCCCC      OOOO    HHH  HHH
 *       RRRR  RRRR   IIIII   CCC  CC    OOOOOO   HHH  HHH
 *       RRRR  RRRRR  IIIII  CCCC  CCC  OOO  OOO  HHH  HHH
 *       RRRR  RRRR   IIIII  CCCC       OOO  OOO  HHH  HHH
 *       RRRR RRRR    IIIII  CCCC       OOO  OOO  HHHHHHHH
 *       RRRR  RRRR   IIIII  CCCC       OOO  OOO  HHH  HHH
 *       RRRR   RRRR  IIIII  CCCC  CCC  OOO  OOO  HHH  HHH
 *       RRRR   RRRR  IIIII   CCC  CC    OOOOOO   HHH  HHH
 *       RRRR   RRRR  IIIII    CCCCC      OOOO    HHH  HHH
 *
 *       Copyright 2005 by Ricoh Europe B.V.
 *
 *       This material contains, and is part of a computer software program
 *       which is, proprietary and confidential information owned by Ricoh
 *       Europe B.V.
 *       The program, including this material, may not be duplicated, disclosed
 *       or reproduced in whole or in part for any purpose without the express
 *       written authorization of Ricoh Europe B.V.
 *       All authorized reproductions must be marked with this legend.
 *
 *       Department : European Development and Support Center
 *       Group      : Printing & Fax Solution Group
 *       Author(s)  : bushnaq
 *       Created    : 13.02.2005
 *
 *       Project    : com.abdalla.bushnaq.mercator
 *       Product Id : <Product Key Index>
 *       Component  : <Project Component Name>
 *       Compiler   : Java/Eclipse
 *
 * END_PROJECT_HEADER
 * -------------------------------------------------------------------------*/
package com.abdalla.bushnaq.pluvia.desktop;

import com.abdalla.bushnaq.pluvia.game.Game;
import com.abdalla.bushnaq.pluvia.game.GameList;
import com.abdalla.bushnaq.pluvia.game.LevelManager;
import com.abdalla.bushnaq.pluvia.game.ScoreList;
import com.abdalla.bushnaq.pluvia.game.model.stone.StoneList;
import com.abdalla.bushnaq.pluvia.scene.model.bubble.Bubble;
import com.abdalla.bushnaq.pluvia.scene.model.digit.Digit;
import com.abdalla.bushnaq.pluvia.scene.model.firefly.Firefly;
import com.abdalla.bushnaq.pluvia.scene.model.fish.Fish;
import com.abdalla.bushnaq.pluvia.scene.model.fly.Fly;
import com.abdalla.bushnaq.pluvia.scene.model.rain.Rain;
import com.abdalla.bushnaq.pluvia.scene.model.turtle.Turtle;
import com.abdalla.bushnaq.pluvia.util.MercatorRandomGenerator;

enum operatingSystem {
	windows, linux, osx, ios, android, unknonw, webgl, applet, headlessDesktop
}

/**
 * @author kunterbunt
 */
public class Context extends ApplicationProperties {
	private static String			appFolderName		= "app";
	private static String			configFolderName	= "app/config";
	public static final float		WORLD_SCALE			= 2.0f;
	public long						currentTime			= 8L * 10000;
	private boolean					enableTime			= true;
	public ModelList<Fish>			fishList			= new ModelList<>();
	public ModelList<Fly>			fireflyList			= new ModelList<>();
	public ModelList<Firefly>		flyList				= new ModelList<>();
	public ModelList<Rain>			rainList			= new ModelList<>();
	public ModelList<Bubble>		bubbleList			= new ModelList<>();
	public ModelList<Digit>			digitList			= new ModelList<>();
//	private final long				fixedDelta		= 20L;
	public Game						game				= null;				// the current game
	public GameList					gameList			= new GameList();
	private long					lastTime			= 0;
	public LevelManager				levelManager		= null;
	public Object					selected			= null;
	public StoneList				stoneList			= new StoneList();
	public long						timeDelta			= 0L;
	public ModelList<Turtle>		turtleList			= new ModelList<>();
	public MercatorRandomGenerator	universeRG;
//	private boolean					useFixedDelta	= false;
	protected ScoreList				scoreList			= new ScoreList(3);
	public boolean					restart				= false;

	public static String getAppFolderName() {
		return appFolderName;
	}

	public static String getConfigFolderName() {
		return configFolderName;
	}

	public Context() {
		switch (getOeratingSystemType()) {
		case windows:
		default:
			appFolderName = "app";
			configFolderName = getAppFolderName() + "/config";
			break;
		case linux:
			appFolderName = "../lib/app";
			configFolderName = getAppFolderName() + "/config";
			break;
		}
		init();
		scoreList.init(gameList);
	}

	public ScoreList getScoreList() {
		return scoreList;
	}

	public void setScoreList(ScoreList aScoreList) {
		scoreList = aScoreList;
	}

	public void advanceInTime() throws Exception {
		advanceInTime(enableTime);
	}

	public void advanceInTime(final boolean enable) throws Exception {
		if (enable) {
			levelManager.updateFps();
//			if (useFixedDelta)
//				timeDelta = fixedDelta;
//			else
			timeDelta = System.currentTimeMillis() - lastTime;
			if (timeDelta > 1000)
				timeDelta = 0;// we probably just started
//			if (timeDelta >= fixedDelta)
			// float[] before = queryDetailedCredits(false);
			{
//				timeDelta -= fixedDelta;
				currentTime += timeDelta;
				for (Fish fish : fishList) {
					fish.advanceInTime(currentTime);
				}
				for (Fly firefly : fireflyList) {
					firefly.advanceInTime(currentTime);
				}
				for (Firefly fly : flyList) {
					fly.advanceInTime(currentTime);
				}
				for (Bubble bubble : bubbleList) {
					bubble.advanceInTime(currentTime);
				}
				for (Turtle turtle : turtleList) {
					turtle.advanceInTime(currentTime);
				}
				for (Rain rain : rainList) {
					rain.advanceInTime(currentTime);
				}
			}
			lastTime = System.currentTimeMillis();
		}
	}

	public void create(final int randomGeneratorSeed, final long age) throws Exception {
		{
			universeRG = new MercatorRandomGenerator(randomGeneratorSeed);
		}
	}

	public void setSelected(final Object selected, final boolean setDirty) throws Exception {
		this.selected = selected;
	}

	public void selectGame(int gameIndex) {
		game = gameList.get(gameIndex);
	}

	public void dispose() {
		levelManager.destroy();
		levelManager = null;
	}

	public static operatingSystem getOeratingSystemType() {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")) {
			return operatingSystem.windows;
			// Operating system is based on Windows
		} else if (os.contains("osx")) {
			return operatingSystem.osx;
			// Operating system is Apple OSX based
		} else if (os.contains("nix") || os.contains("nux")) {
			return operatingSystem.linux;
			// Operating system is based on Linux/Unix/*AIX
		} else
			return operatingSystem.unknonw;
	}

}
