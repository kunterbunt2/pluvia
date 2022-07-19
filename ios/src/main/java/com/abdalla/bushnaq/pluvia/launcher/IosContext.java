package com.abdalla.bushnaq.pluvia.launcher;

import com.abdalla.bushnaq.pluvia.desktop.Context;

/**
 * @author kunterbunt
 */
public class IosContext extends Context {

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

	@Override
	public void enableClipping() {
	}

	@Override
	public void disableClipping() {
	}

}
