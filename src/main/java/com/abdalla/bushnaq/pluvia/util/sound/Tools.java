package com.abdalla.bushnaq.pluvia.util.sound;

import com.abdalla.bushnaq.pluvia.engine.AtlasManager;

public class Tools {
	static boolean		enable			= true;
	static SoundManager	soundManager	= new SoundManager();

	public static void beep() {
		if (enable)
			soundManager.play(AtlasManager.getAssetsFolderName() + "/sound/ping.wav");
	}

	public static void error(final String format, final Object... arguments) {
		System.err.printf(format, arguments);
	}

	public static void play(String filenMame) {
		if (enable)
			soundManager.play(filenMame);
	}

	public static void print(final String format, final Object... arguments) {
		System.out.printf(format, arguments);
	}

	public static void speak(final String text) {
		error(text);
		if (enable) {
			soundManager.speak(text);
		}
	}

	public static void waitForSoundManager() {
		while (soundManager.isBusy()) {
		}
	}
}
