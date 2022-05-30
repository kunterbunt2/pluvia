package com.abdalla.bushnaq.pluvia.desktop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abdalla.bushnaq.pluvia.engine.GameEngine;
import com.abdalla.bushnaq.pluvia.util.MavenPropertiesProvider;
import com.abdalla.bushnaq.pluvia.util.TimeUnit;

/**
 * Main class, all data is stored in the Context, all game code is executed by GameEngine
 *
 * @author kunterbunt
 *
 */
public class Launcher {
	private static final int	UNIVERSE_GENERATION_RANDOM_SEED	= 1;
	private static Logger		logger							= LoggerFactory.getLogger(Launcher.class);

	public static void main(final String[] args) throws Exception {
		logger.info(String.format("Starting pluvia v%s.", MavenPropertiesProvider.getProperty("module.version")));
		final Context context = new Context();
		context.create(UNIVERSE_GENERATION_RANDOM_SEED, 10L * TimeUnit.TICKS_PER_DAY);
		final GameEngine gameEngine = new GameEngine(context);
		new DesktopLauncher(context, gameEngine);
	}

}
