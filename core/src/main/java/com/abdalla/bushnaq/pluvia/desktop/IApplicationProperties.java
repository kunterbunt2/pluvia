package com.abdalla.bushnaq.pluvia.desktop;

public interface IApplicationProperties {

	int MAX_GRAPHICS_QUALITY = 4;

	void disableClipping();

	void enableClipping();

	boolean getAmbientAudioProperty();

	int getAmbientAudioVolumenProperty();

	int getAudioVolumenProperty();

	boolean getDebugModeProperty();

	int getForegroundFPSProperty();

	boolean getFullscreenModeProperty();

	int getGraphicsQuality();

	int getMaxPointLights();

	int getMaxSceneObjects();

	int getMonitorProperty();

	int getMSAASamples();

	int getNumberOfMonitors();

	boolean getPbrModeProperty();

	int getShadowMapSizeProperty();

	boolean getShowFpsProperty();

	boolean getShowGraphsProperty();

	boolean getVsyncProperty();

	boolean isDebugMode();

	boolean isShowGraphs();

	void setAmbientAudio(boolean checked);

	void setAmbientAudioVolumen(int value);

	void setAudioVolumen(int value);

	void setDebugMode(boolean checked);

	void setForegroundFps(int value);

	void setFullScreenMode(boolean checked);

	void SetGraphicsQuality(int value);

	void setMaxPointLights(int value);

	void setMaxSceneObjects(int value);

	void setMonitor(int value);

	void setMsaaSamples(int value);

	void setPbr(boolean checked);

	void setShadowMapSize(int value);

	void setShowFps(boolean checked);

	void setShowGraphs(boolean checked);

	void setVsync(boolean checked);

	void write();
}