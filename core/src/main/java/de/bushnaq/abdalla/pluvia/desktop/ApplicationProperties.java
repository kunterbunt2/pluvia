package de.bushnaq.abdalla.pluvia.desktop;

import de.bushnaq.abdalla.engine.IApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author kunterbunt
 */
public abstract class ApplicationProperties implements IApplicationProperties {
    private static final   String GAME_AMBIENT_AUDIO        = "game.ambientAudio";
    private static final   String GAME_AMBIENT_AUDIO_VOLUME = "game.ambientAudioVolume";
    private static final   String GAME_AUDIO_VOLUME         = "game.audioVolume";
    private static final   String GAME_DEBUG_MODE           = "game.debugMode";
    private static final   String GAME_FOREGROUND_FPS        = "game.foregroundFPS";
    private static final   String GAME_FULLSCREEN_MODE       = "game.fullscreenMode";
    private static final   String GAME_GRAPHICS_QUALITY      = "game.graphicsQuality";
    private static final   String GAME_MAX_POINT_LIGHTS      = "game.maxPointLights";
    private static final   String GAME_MAX_SCENE_OBJECTS     = "game.maxSceneObjects";
    private static final   String GAME_MSAA_SAMPLES          = "game.msaaSamples";
    private static final   String GAME_PBR_MODE              = "game.pbr";
    protected static final String GAME_SHADOW_MAP_SIZE       = "game.shadowMapSize";
    private static final   String GAME_SHOW_FPS              = "game.showFps";
    private static final   String GAME_SHOW_GRAPHS           = "game.showGraphs";
    private static final   String GAME_VSYNC                 = "game.vsync";
    protected static final String GAME_MONITOR               = "game.monitor";
    private static         String propertyFileName;
    Map<String, Boolean> booleanPropertiesMap = new HashMap<>();
    Map<String, Integer> integerPropertiesMap = new HashMap<>();
    //	private boolean					debugMode;																	// debug mode is allowed
    private Logger     logger                      = LoggerFactory.getLogger(this.getClass());
    //	private int						maxSceneObjects;
    public  int        predefinedMaxPointLights[]  = {0, 5, 10, 20};
    public  int        predefinedMaxSceneObjects[] = {0, 25, 50, 100};
    public  int        predefinedMssaSamples[]     = {0, 4, 8, 16};
    public  int        predefinedShadowMapSize[]   = {1024, 2048, 4096, 8192};
    public  Properties properties                  = new Properties();

//	private boolean					showGraphs;

    public ApplicationProperties() {
    }

    @Override
    public boolean getAmbientAudioProperty() {
        return readBooleanProperty(GAME_AMBIENT_AUDIO, true);
    }

    @Override
    public int getAmbientAudioVolumenProperty() {
        return readIntegerProperty(GAME_AMBIENT_AUDIO_VOLUME, 10, 1, 100);
    }

    @Override
    public int getAudioVolumenProperty() {
        return readIntegerProperty(GAME_AUDIO_VOLUME, 50, 1, 100);
    }

    @Override
    public boolean getDebugModeProperty() {
        return readBooleanProperty(GAME_DEBUG_MODE, false);
    }

    @Override
    public int getForegroundFPSProperty() {
        return readIntegerProperty(GAME_FOREGROUND_FPS, 60, 25, 10000);
    }

    @Override
    public boolean getFullscreenModeProperty() {
        return readBooleanProperty(GAME_FULLSCREEN_MODE, true);
    }

    @Override
    public int getGraphicsQuality() {
        return readIntegerProperty(GAME_GRAPHICS_QUALITY, 2, 0, MAX_GRAPHICS_QUALITY);
    }

    @Override
    public int getMaxPointLights() {
        return readIntegerProperty(GAME_MAX_POINT_LIGHTS, 20, 0, 500);
    }

    @Override
    public int getMaxSceneObjects() {
        return readIntegerProperty(GAME_MAX_SCENE_OBJECTS, 10, 0, 500);
    }

    @Override
    public int getMSAASamples() {
        return readIntegerProperty(GAME_MSAA_SAMPLES, 16, 0, 32);
    }

    @Override
    public boolean getPbrModeProperty() {
        return readBooleanProperty(GAME_PBR_MODE, true);
    }

    @Override
    public int getShadowMapSizeProperty() {
        return readMultipleOf2Property(GAME_SHADOW_MAP_SIZE, 4096, 128, 8192);
    }

    @Override
    public boolean getShowFpsProperty() {
        return readBooleanProperty(GAME_SHOW_FPS, true);
    }

    @Override
    public boolean getShowGraphsProperty() {
        return readBooleanProperty(GAME_SHOW_GRAPHS, false);
    }

    @Override
    public boolean getVsyncProperty() {
        return readBooleanProperty(GAME_VSYNC, true);
    }

    protected void init() {
        propertyFileName = Context.getConfigFolderName() + "/game.properties";
        read();
        updateGrphicsQuality();
//		debugMode = getDebugModeProperty();
//		showGraphs = getShowGraphsProperty();
    }

    @Override
    public boolean isDebugMode() {
        return getDebugModeProperty();
    }

    private boolean isMultipleOf2(int propertyValue, int propertyMinValue, int propertyMaxValue) {
        for (int allowedValues = propertyMinValue; allowedValues <= propertyMaxValue; allowedValues *= 2) {
            if (propertyValue == allowedValues) return true;
        }
        return false;
    }

    @Override
    public boolean isShowGraphs() {
        return getShowGraphsProperty();
    }

    private void read() {
        try {
            InputStream inStream = new FileInputStream(propertyFileName);
            properties.load(inStream);
            inStream.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private boolean readBooleanProperty(String propertyName, boolean propertyDefaultValue) {
        boolean propertyValue       = propertyDefaultValue;
        Boolean cachedPropertyValue = booleanPropertiesMap.get(propertyName);
        if (cachedPropertyValue == null) {
            String propertyStringValue = properties.getProperty(propertyName);
            if (propertyStringValue != null) {
                try {
                    propertyValue = Boolean.parseBoolean(propertyStringValue);
                } catch (NumberFormatException e) {
                    logger.error(String.format("%s=%s must be a boolean value.", propertyName, propertyStringValue));
                }
            }
            booleanPropertiesMap.put(propertyStringValue, propertyValue);
        }
        return propertyValue;
    }

    protected int readIntegerProperty(String propertyName, int propertyDefaultValue, int propertyMinValue, int propertyMaxValue) {
        int     propertyValue       = propertyDefaultValue;
        Integer cachedPropertyValue = integerPropertiesMap.get(propertyName);
        if (cachedPropertyValue == null) {
            String propertyStringValue = properties.getProperty(propertyName);
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
            integerPropertiesMap.put(propertyStringValue, propertyValue);
        }
        return propertyValue;
    }

    private int readMultipleOf2Property(String propertyName, int propertyDefaultValue, int propertyMinValue, int propertyMaxValue) {
        int     propertyValue       = propertyDefaultValue;
        Integer cachedPropertyValue = integerPropertiesMap.get(propertyName);
        if (cachedPropertyValue == null) {
            String propertyStringValue = properties.getProperty(propertyName);
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
            integerPropertiesMap.put(propertyStringValue, propertyValue);
        }
        return propertyValue;
    }

    @Override
    public void setAmbientAudio(boolean checked) {
        properties.setProperty(GAME_AMBIENT_AUDIO, "" + checked);
    }

    @Override
    public void setAmbientAudioVolumen(int value) {
        properties.setProperty(GAME_AMBIENT_AUDIO_VOLUME, "" + value);
    }

    @Override
    public void setAudioVolumen(int value) {
        properties.setProperty(GAME_AUDIO_VOLUME, "" + value);
    }

    @Override
    public void setDebugMode(boolean checked) {
        properties.setProperty(GAME_DEBUG_MODE, "" + checked);
    }

    @Override
    public void setForegroundFps(int value) {
        properties.setProperty(GAME_FOREGROUND_FPS, "" + value);
    }

    @Override
    public void setFullScreenMode(boolean checked) {
        if (checked) properties.setProperty(GAME_FULLSCREEN_MODE, "true");
        else properties.setProperty(GAME_FULLSCREEN_MODE, "false");
    }

    @Override
    public void setGraphicsQuality(int value) {
        properties.setProperty(GAME_GRAPHICS_QUALITY, "" + value);
//		updateGrphicsQuality();
    }

    @Override
    public void setMaxPointLights(int value) {
        properties.setProperty(GAME_MAX_POINT_LIGHTS, "" + value);
    }

    @Override
    public void setMaxSceneObjects(int value) {
        properties.setProperty(GAME_MAX_SCENE_OBJECTS, "" + value);
    }

    @Override
    public void setMsaaSamples(int value) {
        properties.setProperty(GAME_MSAA_SAMPLES, "" + value);
    }

    @Override
    public void setPbr(boolean checked) {
        if (checked) properties.setProperty(GAME_PBR_MODE, "true");
        else properties.setProperty(GAME_PBR_MODE, "false");
    }

    @Override
    public void setShadowMapSize(int value) {
        properties.setProperty(GAME_SHADOW_MAP_SIZE, "" + value);
    }

    @Override
    public void setShowFps(boolean checked) {
        if (checked) properties.setProperty(GAME_SHOW_FPS, "true");
        else properties.setProperty(GAME_SHOW_FPS, "false");
    }

    @Override
    public void setShowGraphs(boolean checked) {
        properties.setProperty(GAME_SHOW_GRAPHS, "" + checked);
    }

    @Override
    public void setVsync(boolean checked) {
        if (checked) properties.setProperty(GAME_VSYNC, "true");
        else properties.setProperty(GAME_VSYNC, "false");
    }

    private void updateGrphicsQuality() {
        if (getGraphicsQuality() < MAX_GRAPHICS_QUALITY) {
            properties.setProperty(GAME_MAX_POINT_LIGHTS, "" + predefinedMaxPointLights[getGraphicsQuality()]);
            properties.setProperty(GAME_SHADOW_MAP_SIZE, "" + predefinedShadowMapSize[getGraphicsQuality()]);
            properties.setProperty(GAME_MSAA_SAMPLES, "" + predefinedMssaSamples[getGraphicsQuality()]);
            properties.setProperty(GAME_MAX_SCENE_OBJECTS, "" + predefinedMaxSceneObjects[getGraphicsQuality()]);
        }
        logger.info("--- read following properties ---");
        for (String property : properties.stringPropertyNames()) {
            logger.info(String.format("%s=%s", property, properties.get(property)));
        }
        logger.info("---");
    }

    @Override
    public void write() {
        try {
            OutputStream outStream = new FileOutputStream(propertyFileName);
            properties.store(outStream, "game options");
            outStream.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
