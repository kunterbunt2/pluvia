package com.abdalla.bushnaq.pluvia.launcher;

import java.io.IOException;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.glkit.GLKViewDrawableDepthFormat;
import org.robovm.apple.glkit.GLKViewDrawableMultisample;
import org.robovm.apple.uikit.UIApplication;

import com.abdalla.bushnaq.pluvia.desktop.Context;
import com.abdalla.bushnaq.pluvia.engine.GameEngine;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.badlogic.gdx.graphics.glutils.HdpiMode;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class IOSLauncher extends IOSApplication.Delegate {
	public static void main(String[] argv) throws IOException {
		System.out.println("start-1");
		NSAutoreleasePool pool = new NSAutoreleasePool();
		System.out.println("start-2");
//		UIApplication.main(argv, null, IOSLauncher.class);

		{
			ShaderProgram.prependVertexCode = "#version 300 es\n"//
					+ "#define GLSL3\n"//
					+ "#ifdef GLSL3\n"//
					+ "  #define attribute in\n"//
					+ "  #define varying out\n"//
					+ "  //precision highp float\n"//
					+ "#endif\n";//
			ShaderProgram.prependFragmentCode = "#version 300 es\n"//
					+ "#define GLSL3\n"//
					+ "#ifdef GLSL3\n"//
					+ "  #define textureCube texture\n"//
					+ "  #define texture2D texture\n"//
					+ "  #define varying in\n"//
					+ "  //precision highp float\n"//
					+ "#endif\n";//
		}

		UIApplication.main(argv, null, IOSLauncher.class);
		pool.close();
//		context.dispose();
	}

	Context context;

	@Override
	protected IOSApplication createApplication() {
		try {
			System.out.println("start-3");
			IOSApplicationConfiguration config = new IOSApplicationConfiguration();
			config.useGL30 = true;
			config.multisample = GLKViewDrawableMultisample.None;
			config.depthFormat = GLKViewDrawableDepthFormat._24;
			config.hdpiMode = HdpiMode.Pixels;
			config.preferredFramesPerSecond = 60;
//		        config.useHaptics = false;
//				final Context context = new Context();
			final GameEngine gameEngine = new GameEngine(new IosContextFactory());
			return new IOSApplication(gameEngine, config);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

//	private void start() throws InterruptedException, Exception {
//		logger.info("------------------------------------------------------------------------------");
//		logger.info(String.format("Starting pluvia v%s.", MavenPropertiesProvider.getProperty("module.version")));
//		logger.info("------------------------------------------------------------------------------");
//		logger.info("user.home = " + property);
//		loop();
//		logger.info("------------------------------------------------------------------------------");
//		logger.info(String.format("Shutting down pluvia v%s.", MavenPropertiesProvider.getProperty("module.version")));
//		logger.info("------------------------------------------------------------------------------");
//	}

}
