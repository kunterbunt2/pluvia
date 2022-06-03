package com.abdalla.bushnaq.pluvia.desktop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abdalla.bushnaq.pluvia.engine.GameEngine;
import com.abdalla.bushnaq.pluvia.util.MavenPropertiesProvider;
import com.abdalla.bushnaq.pluvia.util.TimeUnit;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration.GLEmulation;

public class DesktopLauncher {
	private static final int	UNIVERSE_GENERATION_RANDOM_SEED	= 1;
	private static Logger		logger							= LoggerFactory.getLogger(DesktopLauncher.class);
	boolean						useOGL3							= true;

	public static void main(final String[] args) throws Exception {
		logger.info(String.format("Starting pluvia v%s.", MavenPropertiesProvider.getProperty("module.version")));
		DesktopLauncher desktopLauncher = new DesktopLauncher();
		desktopLauncher.loop();
		logger.info(String.format("Shutting down pluvia v%s.", MavenPropertiesProvider.getProperty("module.version")));
	}

	/**
	 * called by GameEngine to create Lwjgl3Application
	 *
	 * @throws Exception
	 */
	public DesktopLauncher() throws Exception {
//		loop();
//		System.exit(0);
	}

	private void loop() throws Exception, InterruptedException {
		boolean restart = false;
		do {
			if (restart)
				logger.info(String.format("Restarting pluvia v%s.", MavenPropertiesProvider.getProperty("module.version")));
			final Context context = new Context();
			context.create(UNIVERSE_GENERATION_RANDOM_SEED, 10L * TimeUnit.TICKS_PER_DAY);
			final GameEngine						gameEngine	= new GameEngine(context);
			final Lwjgl3ApplicationConfiguration	config		= createConfig(context);
			logger.info("Lwjgl3Application ");
			try {
				context.restart = false;
				new Lwjgl3Application(gameEngine, config);
			} catch (Throwable e) {
				logger.error(e.getMessage(), e);
				Thread.sleep(5000);
			}
			context.dispose();
			restart = context.restart;
		} while (restart);
	}

	private Lwjgl3ApplicationConfiguration createConfig(Context context) {
		int	monitor			= 0;
		int	foregroundFPS	= context.getForegroundFPSProperty();
		monitor = context.getMonitorProperty();
		Lwjgl3ApplicationConfiguration config;
		config = new Lwjgl3ApplicationConfiguration();
		config.useVsync(context.getVsyncProperty());
		config.setForegroundFPS(foregroundFPS);
		config.setResizable(true);
//		config.useOpenGL3(true, 3, 2);
		config.setOpenGLEmulation(GLEmulation.GL30, 3, 2);
		config.setBackBufferConfig(8, 8, 8, 8, 16, 0, context.getMSAASamples());
		config.setTitle("Pluvia");
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
