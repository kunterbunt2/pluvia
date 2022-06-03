package com.abdalla.bushnaq.pluvia.desktop;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class ApplicationProperties {
	public static final int			MAX_GRAPHICS_QUALITY		= 4;
	private static final String		PLUVIA_PROPERTIES_FILE_NAME	= Context.CONFIG_FOLDER + "/pluvia.properties";
	private static final String		PLUVIA_MAX_SCENE_OBJECTS	= "pluvia.maxSceneObjects";
	private static final String		PLUVIA_SHOW_FPS				= "pluvia.showFps";
	private static final String		PLUVIA_DEBUG_MODE			= "pluvia.debugMode";
	private static final String		PLUVIA_VSYNC				= "pluvia.vsync";
	private static final String		PLUVIA_PBR_MODE				= "pluvia.pbr";
	private static final String		PLUVIA_FOREGROUND_FPS		= "pluvia.foregroundFPS";
	protected static final String	PLUVIA_SHADOW_MAP_SIZE		= "pluvia.shadowMapSize";
	private static final String		PLUVIA_GRAPHICS_QUALITY		= "pluvia.graphicsQuality";
	private static final String		PLUVIA_MSAA_SAMPLES			= "pluvia.msaaSamples";
	private static final String		PLUVIA_MAX_POINT_LIGHTS		= "pluvia.maxPointLights";
	private static final String		PLUVIA_MONITOR				= "pluvia.monitor";
	private static final String		PLUVIA_SHOW_GRAPHS			= "pluvia.showGraphs";
	private static final String		PLUVIA_FULLSCREEN_MODE		= "pluvia.fullscreenMode";
	private Logger					logger						= LoggerFactory.getLogger(this.getClass());
	private boolean					debugMode;																	// debug mode is allowed
	private boolean					showGraphs;
	public Properties				properties					= new Properties();
	private int						maxSceneObjects;
	public int						predefinedMaxPointLights[]	= { 0, 5, 10, 20 };
	public int						predefinedShadowMapSize[]	= { 1024, 2048, 4096, 8192 };
	public int						predefinedMssaSamples[]		= { 0, 4, 8, 16 };
	public int						predefinedMaxSceneObjects[]	= { 0, 25, 50, 100 };

	public ApplicationProperties() {
		read();
		updateGrphicsQuality();
		debugMode = getDebugModeProperty();
		showGraphs = getShowGraphsProperty();
	}

	public void write() {
		try {
			FileOutputStream inStream = new FileOutputStream(PLUVIA_PROPERTIES_FILE_NAME);
			properties.store(inStream, "");
			inStream.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void read() {
		try {
			FileInputStream inStream = new FileInputStream(PLUVIA_PROPERTIES_FILE_NAME);
			properties.load(inStream);
			inStream.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void updateGrphicsQuality() {
		if (getGraphicsQuality() < MAX_GRAPHICS_QUALITY) {
			properties.setProperty(PLUVIA_MAX_POINT_LIGHTS, "" + predefinedMaxPointLights[getGraphicsQuality()]);
			properties.setProperty(PLUVIA_SHADOW_MAP_SIZE, "" + predefinedShadowMapSize[getGraphicsQuality()]);
			properties.setProperty(PLUVIA_MSAA_SAMPLES, "" + predefinedMssaSamples[getGraphicsQuality()]);
			properties.setProperty(PLUVIA_MAX_SCENE_OBJECTS, "" + predefinedMaxSceneObjects[getGraphicsQuality()]);
		}
		logger.info("--- read following properties ---");
		for (String property : properties.stringPropertyNames()) {
			logger.info(String.format("%s=%s", property, properties.get(property)));
		}
		logger.info("---");
		maxSceneObjects = readIntegerProperty(PLUVIA_MAX_SCENE_OBJECTS, 10, 0, 500);
	}

	public boolean getFullscreenModeProperty() {
		return readBooleanProperty(PLUVIA_FULLSCREEN_MODE, true);
	}

	public boolean getShowGraphsProperty() {
		return readBooleanProperty(PLUVIA_SHOW_GRAPHS, false);
	}

	public int getMonitorProperty() {
		final Monitor[]	monitors	= Lwjgl3ApplicationConfiguration.getMonitors();
		int				monitor		= readIntegerProperty(PLUVIA_MONITOR, 0, 0, monitors.length);
		if (monitor < 0 || monitor >= monitors.length) {
			monitor = 0;
			logger.error(String.format("pluvia.monitiro=%d cannot be negative or higher than the number of monitors %d.", monitor, monitors.length));
		}
		return monitor;
	}

	public int getMaxPointLights() {
		return readIntegerProperty(PLUVIA_MAX_POINT_LIGHTS, 20, 0, 500);
	}

	public int getMaxSceneObjects() {
		return maxSceneObjects;
	}

	public int getMSAASamples() {
		return readIntegerProperty(PLUVIA_MSAA_SAMPLES, 16, 0, 32);
	}

	public int getGraphicsQuality() {
		return readIntegerProperty(PLUVIA_GRAPHICS_QUALITY, 2, 0, MAX_GRAPHICS_QUALITY);
	}

	private int readIntegerProperty(String propertyName, int propertyDefaultValue, int propertyMinValue, int propertyMaxValue) {
		int		propertyValue		= propertyDefaultValue;
		String	propertyStringValue	= properties.getProperty(propertyName);
		if (propertyStringValue != null) {
			try {
				propertyValue = Integer.parseInt(propertyStringValue);
				if (propertyValue < propertyMinValue || propertyValue > propertyMaxValue) {
					propertyValue = propertyDefaultValue;
					logger.error(String.format("%s=%s must be a number bigger or equal %d and smaller or equal %d.", propertyName, propertyStringValue, propertyMinValue, propertyMaxValue));
				}
			} catch (NumberFormatException e) {
				propertyValue = propertyDefaultValue;
				logger.error(String.format("%s=%s must be a number bigger or equal %d and smaller or equal %d.", propertyName, propertyStringValue, propertyMinValue, propertyMaxValue));
			}
		}
		return propertyValue;
	}

	private boolean readBooleanProperty(String propertyName, boolean propertyDefaultValue) {
		boolean	propertyValue		= propertyDefaultValue;
		String	propertyStringValue	= properties.getProperty(propertyName);
		if (propertyStringValue != null) {
			try {
				propertyValue = Boolean.parseBoolean(propertyStringValue);
			} catch (NumberFormatException e) {
				logger.error(String.format("%s=%s must be a boolean value.", propertyName, propertyStringValue));
			}
		}
		return propertyValue;
	}

	private int readMultipleOf2Property(String propertyName, int propertyDefaultValue, int propertyMinValue, int propertyMaxValue) {
		int		propertyValue		= propertyDefaultValue;
		String	propertyStringValue	= properties.getProperty(propertyName);
		if (propertyStringValue != null) {
			try {
				propertyValue = Integer.parseInt(propertyStringValue);
				if (propertyValue < propertyMinValue || propertyValue > propertyMaxValue) {
					propertyValue = propertyDefaultValue;
					logger.error(String.format("%s=%s must be a number bigger or equal %d and smaller or equal %d.", propertyName, propertyStringValue, propertyMinValue, propertyMaxValue));
				} else {
					if (!isMultipleOf2(propertyValue, propertyMinValue, propertyMaxValue)) {
						propertyValue = propertyDefaultValue;
						logger.error(String.format("%s=%s must be a nultiple of 2.", propertyName, propertyStringValue));
					}
				}
			} catch (NumberFormatException e) {
				propertyValue = propertyDefaultValue;
				logger.error(String.format("%s=%s must be a number bigger or equal %d and smaller or equal %d.", propertyName, propertyStringValue));
			}
		}
		return propertyValue;
	}

	private boolean isMultipleOf2(int propertyValue, int propertyMinValue, int propertyMaxValue) {
		for (int allowedValues = propertyMinValue; allowedValues <= propertyMaxValue; allowedValues *= 2) {
			if (propertyValue == allowedValues)
				return true;
		}
		return false;
	}

	public int getShadowMapSizeProperty() {
		return readMultipleOf2Property(PLUVIA_SHADOW_MAP_SIZE, 4096, 128, 8192);
	}

	public int getForegroundFPSProperty() {
		return readIntegerProperty(PLUVIA_FOREGROUND_FPS, 60, 25, 10000);
	}

	public boolean getPbrModeProperty() {
		return readBooleanProperty(PLUVIA_PBR_MODE, true);
	}

	public boolean getVsyncProperty() {
		return readBooleanProperty(PLUVIA_VSYNC, true);
	}

	public boolean getDebugModeProperty() {
		return readBooleanProperty(PLUVIA_DEBUG_MODE, false);
	}

	public boolean getShowFpsProperty() {
		return readBooleanProperty(PLUVIA_SHOW_FPS, false);
	}

	public boolean isShowGraphs() {
		return showGraphs;
	}

	public boolean isDebugMode() {
		return debugMode;
	}

	public void SetGraphicsQuality(int value) {
		properties.setProperty(PLUVIA_GRAPHICS_QUALITY, "" + value);
//		updateGrphicsQuality();
	}

	public void setForegroundFps(int value) {
		properties.setProperty(PLUVIA_FOREGROUND_FPS, "" + value);
	}

	public void setPbr(boolean checked) {
		if (checked)
			properties.setProperty(PLUVIA_PBR_MODE, "true");
		else
			properties.setProperty(PLUVIA_PBR_MODE, "false");
	}

	public void setShowFps(boolean checked) {
		if (checked)
			properties.setProperty(PLUVIA_SHOW_FPS, "true");
		else
			properties.setProperty(PLUVIA_SHOW_FPS, "false");
	}

	public void setVsync(boolean checked) {
		if (checked)
			properties.setProperty(PLUVIA_VSYNC, "true");
		else
			properties.setProperty(PLUVIA_VSYNC, "false");
	}

	public void setFullScreenMode(boolean checked) {
		if (checked)
			properties.setProperty(PLUVIA_FULLSCREEN_MODE, "true");
		else
			properties.setProperty(PLUVIA_FULLSCREEN_MODE, "false");
	}

	public void setMaxPointLights(int value) {
		properties.setProperty(PLUVIA_MAX_POINT_LIGHTS, "" + value);
	}

	public void setMaxSceneObjects(int value) {
		properties.setProperty(PLUVIA_MAX_SCENE_OBJECTS, "" + value);
	}

	public void setDebugMode(boolean checked) {
		properties.setProperty(PLUVIA_DEBUG_MODE, "" + checked);
	}

	public void setShadowMapSize(int value) {
		properties.setProperty(PLUVIA_SHADOW_MAP_SIZE, "" + value);
	}

	public void setMsaaSamples(int value) {
		properties.setProperty(PLUVIA_MSAA_SAMPLES, "" + value);
	}

	public void setMonitor(int value) {
		properties.setProperty(PLUVIA_MONITOR, "" + value);
	}

	public void setShowGraphs(boolean checked) {
		properties.setProperty(PLUVIA_SHOW_GRAPHS, "" + checked);
	}

}
