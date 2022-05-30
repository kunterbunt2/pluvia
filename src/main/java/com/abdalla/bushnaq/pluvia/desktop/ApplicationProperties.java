package com.abdalla.bushnaq.pluvia.desktop;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class ApplicationProperties {
	private Logger		logger		= LoggerFactory.getLogger(this.getClass());
	private boolean		debugMode;												// debug mode is allowed
	private boolean		showGraphs;
	public Properties	properties	= new Properties();

	public ApplicationProperties() {
		try {
			FileInputStream inStream = new FileInputStream("config/pluvia.properties");
			properties.load(inStream);
			logger.info("--- read following properties ---");
			for (String property : properties.stringPropertyNames()) {
				logger.info(String.format("%s=%s", property, properties.get(property)));
			}
			logger.info("---");
			inStream.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		debugMode = getDebugModeProperty();
		showGraphs = getShowGraphsProperty();
	}

	public boolean getFullscreenModeProperty() {
		return readBooleanProperty("pluvia.fullscreenMode", true);
	}

	public boolean getShowGraphsProperty() {
		return readBooleanProperty("pluvia.showGraphs", false);
	}

	public int getMonitorProperty() {
		final Monitor[] monitors = Lwjgl3ApplicationConfiguration.getMonitors();
		return readIntegerProperty("pluvia.monitor", 0, 0, monitors.length);
	}

	public int getMaxPointLights() {
		return readIntegerProperty("pluvia.maxPointLights", 20, 0, 500);
	}

	public int getMSAASamples() {
		return readIntegerProperty("pluvia.msaaSamples", 16, 0, 32);
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
				logger.error(String.format("%s=%s must be a number bigger or equal %d and smaller or equal %d.", propertyName, propertyStringValue));
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

	public int getShadowMapSizeProperty(int shadowMapSize) {
		String propertyString = properties.getProperty("pluvia.shadowMapSize");
		if (propertyString != null) {
			try {
				shadowMapSize = Integer.parseInt(propertyString);
				if (shadowMapSize < 0) {
					shadowMapSize = 4096;
				}
			} catch (NumberFormatException e) {
				logger.error(e.getMessage(), e);
				logger.error(String.format("pluvia.shadowMapSize=%s must be a positive number.", System.getProperty("pluvia.shadowMapSize")));
			}
		}
		return shadowMapSize;
	}

	public int getForegroundFPSProperty(int foregroundFPS) {
		String propertyString = properties.getProperty("pluvia.foregroundFPS");
		if (propertyString != null) {
			try {
				foregroundFPS = Integer.parseInt(propertyString);
				if (foregroundFPS < 0) {
					foregroundFPS = 60;
				}
			} catch (NumberFormatException e) {
				logger.error(e.getMessage(), e);
				logger.error(String.format("pluvia.foregroundFPS=%s must be a positive number.", System.getProperty("pluvia.foregroundFPS")));
			}
		}
		return foregroundFPS;
	}

	public boolean getPbrModeProperty() {
		return readBooleanProperty("pluvia.pbrMode", true);
	}

	public boolean getVsyncProperty() {
		return readBooleanProperty("pluvia.vsync", true);
	}

	public boolean getDebugModeProperty() {
		return readBooleanProperty("pluvia.debugMode", false);
	}

	public boolean getShowFpsProperty() {
		return readBooleanProperty("pluvia.showFps", false);
	}

	public boolean isShowGraphs() {
		return showGraphs;
	}

	public boolean isDebugMode() {
		return debugMode;
	}

}
