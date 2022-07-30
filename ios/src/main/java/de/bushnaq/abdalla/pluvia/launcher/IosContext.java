package de.bushnaq.abdalla.pluvia.launcher;

import com.badlogic.gdx.Gdx;

import de.bushnaq.abdalla.pluvia.desktop.Context;

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
	protected String getInstallationFolder() {
		// dummy implementation, as this is never used for ios
		return null;
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
	public boolean isDebugModeSupported() {
		return false;
	}

	@Override
	public boolean isForegroundFpsSupported() {
		return false;
	}

	@Override
	public boolean isFullscreenModeSupported() {
		return false;
	}

	@Override
	public boolean isMonitorSupported() {
		return false;
	}

	@Override
	public boolean isMSAASamplesSupported() {
		return false;
	}

	@Override
	public boolean isPbrModeSupported() {
		return false;
	}

	@Override
	public boolean isRestartSuported() {
		return false;
	}

	@Override
	public boolean isVsyncSupported() {
		return false;
	}

	@Override
	public void setMonitor(int value) {
	}
}
