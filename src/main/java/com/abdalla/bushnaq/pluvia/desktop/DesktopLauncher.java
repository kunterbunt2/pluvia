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
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class DesktopLauncher {
	private static final int UNIVERSE_GENERATION_RANDOM_SEED = 1;

	public static void main(final String[] args) throws Exception {
		DesktopLauncher desktopLauncher = new DesktopLauncher();
		desktopLauncher.start();
	}

	private Logger	logger	= LoggerFactory.getLogger(this.getClass());

	boolean			useOGL3	= true;

	/**
	 * called by GameEngine to create Lwjgl3Application
	 *
	 * @throws Exception
	 */
	public DesktopLauncher() throws Exception {
	}

	private Lwjgl3ApplicationConfiguration createConfig(Context context) {
		int								foregroundFPS	= context.getForegroundFPSProperty();
		Lwjgl3ApplicationConfiguration	config;
		config = new Lwjgl3ApplicationConfiguration();
		config.useVsync(context.getVsyncProperty());
		config.setForegroundFPS(foregroundFPS);
		config.setResizable(true);
//		config.useOpenGL3(true, 3, 2);
		config.setOpenGLEmulation(GLEmulation.GL30, 3, 2);
//		if (Context.getOeratingSystemType() == OperatingSystem.osx) 
		{
			ShaderProgram.prependVertexCode = "#version 150\n"//
					+ "#define GLSL3\n"//
					+ "#ifdef GLSL3\n"//
					+ "#define attribute in\n"//
					+ "#define varying out\n"//
					+ "#endif\n";//
			ShaderProgram.prependFragmentCode = "#version 150\n"//
					+ "#define GLSL3\n"//
					+ "#ifdef GLSL3\n"//
					+ "#define textureCube texture\n"//
					+ "#define texture2D texture\n"//
//					+ "out vec4 out_FragColor;\n"//
					+ "#define varying in\n"//
					+ "#endif\n";//
		}

		config.setBackBufferConfig(8, 8, 8, 8, 16, 0, context.getMSAASamples());
		config.setTitle("Pluvia");
		config.setAutoIconify(true);
		final Monitor[]		monitors		= Lwjgl3ApplicationConfiguration.getMonitors();
		int					monitor			= context.getMonitorProperty();
//		if (monitor < 0 || monitor >= monitors.length) {
//			monitor = 0;
//			logger.error(String.format("pluvia.monitiro=%d cannot be negative or higher than the number of monitors %d.", monitor, monitors.length));
//		}
		final DisplayMode	primaryMode		= Lwjgl3ApplicationConfiguration.getDisplayMode(monitors[monitor]);
		boolean				fullScreenMode	= context.getFullscreenModeProperty();
		if (fullScreenMode)
			config.setFullscreenMode(primaryMode);
//		config.setWindowPosition(0, 0);
//		config.setWindowedMode(primaryMode.width, primaryMode.height);
		config.setMaximized(true);
//		config.setMaximizedMonitor(monitors[monitor]);
		return config;
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
			config.disableAudio(true);
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

	private void start() throws InterruptedException, Exception {
//		LoggerContext						lc			= (LoggerContext) LoggerFactory.getILoggerFactory();
//		ch.qos.logback.classic.Logger		rootLogger		= lc.getLogger(Logger.ROOT_LOGGER_NAME);
//		Iterator<Appender<ILoggingEvent>>	iterator	= logger2.iteratorForAppenders();
//		while (iterator.hasNext()) {
//			logger.info(iterator.next().getName());
//		}
//		RollingFileAppender<ILoggingEvent>	appender	= (RollingFileAppender) rootLogger.getAppender("FILE");
		String property = System.getProperty("user.home");
//		String file = property + "/.pluvia/pluvia1.log";
//		String								file2 = appender.getFile();
//		appender.setFile(file);
//		String								file3 = appender.getFile();
//		appender.stop();
//		appender.start();
//		rootLogger.info("test");
//		LoggerContext			logCtx		= LoggerFactory.getILoggerFactory();
//
//		PatternLayoutEncoder	logEncoder	= new PatternLayoutEncoder();
//		logEncoder.setContext(logCtx);
//		logEncoder.setPattern("%-12date{YYYY-MM-dd HH:mm:ss.SSS} %-5level - %msg%n");
//		logEncoder.start();
//
//		ConsoleAppender logConsoleAppender = new ConsoleAppender();
//		logConsoleAppender.setContext(logCtx);
//		logConsoleAppender.setName("console");
//		logConsoleAppender.setEncoder(logEncoder);
//		logConsoleAppender.start();
//
//		logEncoder = new PatternLayoutEncoder();
//		logEncoder.setContext(logCtx);
//		logEncoder.setPattern("%-12date{YYYY-MM-dd HH:mm:ss.SSS} %-5level - %msg%n");
//		logEncoder.start();
//
//		RollingFileAppender logFileAppender = new RollingFileAppender();
//		logFileAppender.setContext(logCtx);
//		logFileAppender.setName("logFile");
//		logFileAppender.setEncoder(logEncoder);
//		logFileAppender.setAppend(true);
//		logFileAppender.setFile("logs/logfile.log");
//
//		TimeBasedRollingPolicy logFilePolicy = new TimeBasedRollingPolicy();
//		logFilePolicy.setContext(logCtx);
//		logFilePolicy.setParent(logFileAppender);
//		logFilePolicy.setFileNamePattern("logs/logfile-%d{yyyy-MM-dd_HH}.log");
//		logFilePolicy.setMaxHistory(7);
//		logFilePolicy.start();
//
//		logFileAppender.setRollingPolicy(logFilePolicy);
//		logFileAppender.start();
//
//		Logger log = logCtx.getLogger("Main");
//		log.setAdditive(false);
//		log.setLevel(Level.INFO);
//		log.addAppender(logConsoleAppender);
//		log.addAppender(logFileAppender);

		logger.info("------------------------------------------------------------------------------");
		logger.info(String.format("Starting pluvia v%s.", MavenPropertiesProvider.getProperty("module.version")));
		logger.info("------------------------------------------------------------------------------");
		logger.info("user.home = " + property);
		loop();
		logger.info("------------------------------------------------------------------------------");
		logger.info(String.format("Shutting down pluvia v%s.", MavenPropertiesProvider.getProperty("module.version")));
		logger.info("------------------------------------------------------------------------------");
	}

}
