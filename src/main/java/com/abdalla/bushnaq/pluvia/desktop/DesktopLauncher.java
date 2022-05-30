package com.abdalla.bushnaq.pluvia.desktop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abdalla.bushnaq.pluvia.engine.GameEngine;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration.GLEmulation;

public class DesktopLauncher {
	private Logger	logger	= LoggerFactory.getLogger(this.getClass());
	boolean			useOGL3	= true;

	/**
	 * called by GameEngine to create Lwjgl3Application
	 *
	 * @param context
	 * @param screen
	 * @throws Exception
	 */
	public DesktopLauncher(final Context context, final GameEngine gameEngine) throws Exception {
		final Lwjgl3ApplicationConfiguration config = createConfig(context);
		logger.info("Lwjgl3Application ");
		try {
			new Lwjgl3Application(gameEngine, config);

		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
			Thread.sleep(5000);
		}
		logger.info("DesktopLauncher constructed");
		System.exit(0);
	}

	private Lwjgl3ApplicationConfiguration createConfig(Context context) {
		int	monitor			= 0;
		int	foregroundFPS	= 60;
		monitor = context.getMonitorProperty();
		foregroundFPS = context.getForegroundFPSProperty(foregroundFPS);
		Lwjgl3ApplicationConfiguration config;
		config = new Lwjgl3ApplicationConfiguration();
		config.useVsync(context.getVsyncProperty());
		config.setForegroundFPS(foregroundFPS);
		config.setResizable(true);
//		config.useOpenGL3(true, 3, 2);
		config.setOpenGLEmulation(GLEmulation.GL30, 3, 2);
		config.setBackBufferConfig(8, 8, 8, 8, 16, 0, context.getMSAASamples());
		config.setTitle("PLuvia");
		config.setAutoIconify(false);
		final Monitor[] monitors = Lwjgl3ApplicationConfiguration.getMonitors();
		if (monitor < 0 || monitor >= monitors.length) {
			monitor = 0;
			logger.error(String.format("pluvia.monitiro=%d cannot be negative or higher than the number of monitors %d.", monitor, monitors.length));
		}
		final DisplayMode	primaryMode		= Lwjgl3ApplicationConfiguration.getDisplayMode(monitors[monitor]);
		boolean				fullScreenMode	= context.getFullscreenModeProperty();
		if (fullScreenMode)
			config.setFullscreenMode(primaryMode);
		config.setWindowPosition(0, 0);
		config.setWindowedMode(primaryMode.width, primaryMode.height);
		config.setMaximized(true);
		config.setMaximizedMonitor(monitors[monitor]);
		return config;
	}

}
