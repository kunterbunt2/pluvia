package com.abdalla.bushnaq.pluvia.launcher;

import java.io.File;
import java.net.URISyntaxException;

import com.abdalla.bushnaq.pluvia.desktop.Context;
import com.badlogic.gdx.Gdx;

/**
 * @author kunterbunt
 */
public class IosContext extends Context {
	static final int GL_CLIP_DISTANCE0_APPLE = 0x3000;

	@Override
	public void disableClipping() {
		Gdx.gl.glDisable(GL_CLIP_DISTANCE0_APPLE);
	}

	@Override
	public void enableClipping() {
		Gdx.gl.glEnable(GL_CLIP_DISTANCE0_APPLE);
	}

	@Override
	public int getMonitorProperty() {
		return 0;
	}

	@Override
	public int getNumberOfMonitors() {
		return 1;
	}

	@Override
	public void setMonitor(int value) {
	}

	protected String getInstallationFolder() {
		// dummy implementation, as this is never used for ios
		return null;
	}

}
