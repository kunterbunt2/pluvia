package com.abdalla.bushnaq.pluvia.desktop;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

enum OperatingSystem {
	windows, linux, osx, ios, android, unknonw, webgl, applet, headlessDesktop
}

/**
 * @author kunterbunt
 */
public class Context extends ApplicationProperties {
	private static String			appFolderName		= "app";
	private static String			configFolderName	= "app/config";
	private static String			homeFolderName;
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
	public Game						game				= null;										// the current game
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
	private OperatingSystem			operatingSystem;
	private String					installationFolder;
	protected static Logger			logger				= LoggerFactory.getLogger(Context.class);

	public static String getAppFolderName() {
		return appFolderName;
	}

	private static String getInstallationFolder() {
		try {
			String path = new File(DesktopLauncher.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
			if (path.endsWith(".jar")) {
				path = path.substring(0, path.lastIndexOf("/") - 1);
			}
			path = cleanupPath(path);
			return path;
		} catch (URISyntaxException e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}

	private static String cleanupPath(String path) {
		try {
			path = new File(path).getCanonicalPath();// get rid of all the /..
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return path;
	}

	public static String getHomeFolderName() {
		return homeFolderName;
	}

	public static String getConfigFolderName() {
		return configFolderName;
	}

	public Context() {
		homeFolderName = System.getProperty("user.home") + "/.pluvia";
		operatingSystem = getOeratingSystemType();
		switch (operatingSystem) {
		case windows:
		default:
			if (isRunningInEclipse()) {
				logger.info("Detected Windows system and we are running inside of Eclipse.");
				installationFolder = cleanupPath(getInstallationFolder() + "/../..");
				logger.info("Detected installation folder " + installationFolder);
				appFolderName = installationFolder + "/app";
				configFolderName = appFolderName + "/config";
			} else {
				logger.info("Detected Windows system.");
				installationFolder = cleanupPath(getInstallationFolder() + "/../../..");
				logger.info("Detected installation folder " + installationFolder);
				appFolderName = installationFolder + "/app";
				configFolderName = getHomeFolderName() + "/config";
			}
			break;
		case linux:
			if (isRunningInEclipse()) {
				logger.info("Detected linux system and we are running inside of Eclipse.");
				installationFolder = cleanupPath(getInstallationFolder() + "/../..");
				logger.info("Detected installation folder " + installationFolder);
				appFolderName = installationFolder + "/app";
				configFolderName = appFolderName + "/config";
			} else {
				logger.info("Detected linux system.");
				installationFolder = cleanupPath(getInstallationFolder() + "/../../../bin");
				logger.info("Detected installation folder " + installationFolder);
				appFolderName = cleanupPath(installationFolder + "/../lib/app");
				configFolderName = getHomeFolderName() + "/config";
			}
			break;
		}
		createFolder(homeFolderName);
		createFolder(configFolderName);
		init();
		scoreList.init(gameList);
	}

	private void createFolder(String folderName) {
		File directory = new File(folderName);
		if (!directory.exists()) {
			directory.mkdirs(); // If you require it to make the entire directory path including parents, use directory.mkdirs(); here instead.
		}
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

	public static OperatingSystem getOeratingSystemType() {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")) {
			return OperatingSystem.windows;
			// Operating system is based on Windows
		} else if (os.contains("osx")) {
			return OperatingSystem.osx;
			// Operating system is Apple OSX based
		} else if (os.contains("nix") || os.contains("nux")) {
			return OperatingSystem.linux;
			// Operating system is based on Linux/Unix/*AIX
		} else
			return OperatingSystem.unknonw;
	}

	public static boolean isRunningInEclipse() {
		String	path		= System.getProperty("java.class.path").toLowerCase();
		boolean	isEclipse	= path.contains(".m2");
		return isEclipse;
	}

}
