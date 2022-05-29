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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abdalla.bushnaq.pluvia.game.Game;
import com.abdalla.bushnaq.pluvia.game.GameList;
import com.abdalla.bushnaq.pluvia.game.LevelManager;
import com.abdalla.bushnaq.pluvia.game.ScoreList;
import com.abdalla.bushnaq.pluvia.model.bubble.Bubble;
import com.abdalla.bushnaq.pluvia.model.digit.Digit;
import com.abdalla.bushnaq.pluvia.model.firefly.Firefly;
import com.abdalla.bushnaq.pluvia.model.fish.Fish;
import com.abdalla.bushnaq.pluvia.model.fly.Fly;
import com.abdalla.bushnaq.pluvia.model.rain.Rain;
import com.abdalla.bushnaq.pluvia.model.stone.StoneList;
import com.abdalla.bushnaq.pluvia.model.turtle.Turtle;
import com.abdalla.bushnaq.pluvia.util.MercatorRandomGenerator;

/**
 * @author kunterbunt
 */
public class Context {
	public static final float		WORLD_SCALE		= 2.0f;
	public long						currentTime		= 8L * 10000;
	private boolean					enableTime		= true;
	public ModelList<Fish>			fishList		= new ModelList<>();
	public ModelList<Fly>			fireflyList		= new ModelList<>();
	public ModelList<Firefly>		flyList			= new ModelList<>();
	public ModelList<Rain>			rainList		= new ModelList<>();
	public ModelList<Bubble>		bubbleList		= new ModelList<>();
	public ModelList<Digit>			digitList		= new ModelList<>();
	private final long				fixedDelta		= 20L;
	public Game						game			= null;										// the current game
	public GameList					gameList		= new GameList();
	private long					lastTime		= 0;
	public LevelManager				levelManager	= null;
	public Object					selected		= null;
	public StoneList				stoneList		= new StoneList();
	public long						timeDelta		= 0L;
	public ModelList<Turtle>		turtleList		= new ModelList<>();
	public MercatorRandomGenerator	universeRG;
	private boolean					useFixedDelta	= false;
	protected ScoreList				scoreList		= new ScoreList(3);
	public Properties				properties		= new Properties();
	private Logger					logger			= LoggerFactory.getLogger(this.getClass());
	private boolean					debugMode;													// debug mode is allowed
	private boolean					showGraphs;

	public Context() {
		try {
			FileInputStream inStream = new FileInputStream("config/pluvia.properties");
			properties.load(inStream);
			logger.info("--- read following properties ---");
			for (String property : properties.stringPropertyNames()) {
				logger.info(String.format("%s=%s", property, properties.get(property)));
			}
			logger.info("---");
			inStream.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		debugMode = getDebugModeProperty(false);
		showGraphs = getShowGraphsProperty(false);
		scoreList.init(gameList);
	}

	public ScoreList getScoreList() {
		return scoreList;
	}

	public void setScoreList(ScoreList aScoreList) {
		scoreList = aScoreList;
	}

	public boolean isShowGraphs() {
		return showGraphs;
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

	public void selectGamee(int gameIndex) {
		game = gameList.get(gameIndex);
	}

	public int getMonitorProperty(int monitor) {
		String propertyString = properties.getProperty("pluvia.monitor");
		if (propertyString != null) {
			try {
				monitor = Integer.parseInt(propertyString);
			} catch (NumberFormatException e) {
				logger.error(e.getMessage(), e);
				logger.error(String.format("pluvia.monitiro=%s must be a positive number.", System.getProperty("pluvia.monitor")));
			}
		}
		return monitor;
	}

	public int getShadowMapSizeProperty(int shadowMapSize) {
		String propertyString = properties.getProperty("pluvia.shadowMapSize");
		if (propertyString != null) {
			try {
				shadowMapSize = Integer.parseInt(propertyString);
				if (shadowMapSize < 0) {
					shadowMapSize = 4096;
				}
			} catch (NumberFormatException e) {
				logger.error(e.getMessage(), e);
				logger.error(String.format("pluvia.shadowMapSize=%s must be a positive number.", System.getProperty("pluvia.shadowMapSize")));
			}
		}
		return shadowMapSize;
	}

	public int getForegroundFPSProperty(int foregroundFPS) {
		String propertyString = properties.getProperty("pluvia.foregroundFPS");
		if (propertyString != null) {
			try {
				foregroundFPS = Integer.parseInt(propertyString);
				if (foregroundFPS < 0) {
					foregroundFPS = 60;
				}
			} catch (NumberFormatException e) {
				logger.error(e.getMessage(), e);
				logger.error(String.format("pluvia.foregroundFPS=%s must be a positive number.", System.getProperty("pluvia.foregroundFPS")));
			}
		}
		return foregroundFPS;
	}

	public boolean getVsyncProperty(boolean vsync) {
		String propertyString = properties.getProperty("pluvia.vsync");
		if (propertyString != null) {
			try {
				vsync = Boolean.parseBoolean(propertyString);
			} catch (NumberFormatException e) {
				logger.error(e.getMessage(), e);
				logger.error(String.format("pluvia.vsync=%s must be a booleanvalue.", System.getProperty("pluvia.vsync")));
			}
		}
		return vsync;
	}

	public boolean getDebugModeProperty(boolean debugMode) {
		String propertyString = properties.getProperty("pluvia.debugMode");
		if (propertyString != null) {
			try {
				debugMode = Boolean.parseBoolean(propertyString);
			} catch (NumberFormatException e) {
				logger.error(e.getMessage(), e);
				logger.error(String.format("pluvia.vsync=%s must be a booleanvalue.", System.getProperty("pluvia.debugMode")));
			}
		}
		return debugMode;
	}

	public boolean getShowFpsProperty(boolean showFps) {
		String propertyString = properties.getProperty("pluvia.showFps");
		if (propertyString != null) {
			try {
				showFps = Boolean.parseBoolean(propertyString);
			} catch (NumberFormatException e) {
				logger.error(e.getMessage(), e);
				logger.error(String.format("pluvia.showFps=%s must be a booleanvalue.", System.getProperty("pluvia.showFps")));
			}
		}
		return showFps;
	}

	public boolean isDebugMode() {
		return debugMode;
	}

	public boolean getFullscreenModeProperty(boolean fullscreenMode) {
		String propertyString = properties.getProperty("pluvia.fullscreenMode");
		if (propertyString != null) {
			try {
				fullscreenMode = Boolean.parseBoolean(propertyString);
			} catch (NumberFormatException e) {
				logger.error(e.getMessage(), e);
				logger.error(String.format("pluvia.vsync=%s must be a booleanvalue.", System.getProperty("pluvia.fullscreenMode")));
			}
		}
		return fullscreenMode;
	}

	public boolean getShowGraphsProperty(boolean showGraphs) {
		String propertyString = properties.getProperty("pluvia.showGraphs");
		if (propertyString != null) {
			try {
				showGraphs = Boolean.parseBoolean(propertyString);
			} catch (NumberFormatException e) {
				logger.error(e.getMessage(), e);
				logger.error(String.format("pluvia.showGraphs=%s must be a booleanvalue.", System.getProperty("pluvia.showGraphs")));
			}
		}
		return showGraphs;
	}

}
