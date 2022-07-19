package com.abdalla.bushnaq.pluvia.launcher;

import org.lwjgl.opengl.GL30C;

import com.abdalla.bushnaq.pluvia.desktop.Context;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/**
 * @author kunterbunt
 */
public class DesktopContext extends Context {
	private static final String PLUVIA_MONITOR = "pluvia.monitor";

	@Override
	public void disableClipping() {
		if (!Context.isIos())
			Gdx.gl.glDisable(GL30C.GL_CLIP_DISTANCE0);
	}

	@Override
	public void enableClipping() {
		if (!Context.isIos())
			Gdx.gl.glEnable(GL30C.GL_CLIP_DISTANCE0);

	}

	@Override
	public int getMonitorProperty() {
		final Monitor[]	monitors	= Lwjgl3ApplicationConfiguration.getMonitors();
		int				monitor		= readIntegerProperty(PLUVIA_MONITOR, 0, 0, monitors.length);
		if (monitor < 0 || monitor >= monitors.length) {
			monitor = 0;
			logger.error(String.format("pluvia.monitiro=%d cannot be negative or higher than the number of monitors %d.", monitor, monitors.length));
		}
		return monitor;
	}

	@Override
	public int getNumberOfMonitors() {
		final Monitor[] monitors = Lwjgl3ApplicationConfiguration.getMonitors();
		return monitors.length;
	}

	@Override
	public void setMonitor(int value) {
		properties.setProperty(PLUVIA_MONITOR, "" + value);
	}

}
