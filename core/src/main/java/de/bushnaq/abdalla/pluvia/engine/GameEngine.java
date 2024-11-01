package de.bushnaq.abdalla.pluvia.engine;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.profiling.GLErrorListener;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.widget.VisLabel;

import de.bushnaq.abdalla.engine.*;
import de.bushnaq.abdalla.engine.audio.AudioEngine;
import de.bushnaq.abdalla.engine.camera.MovingCamera;
import de.bushnaq.abdalla.pluvia.desktop.Context;
import de.bushnaq.abdalla.pluvia.engine.camera.MyCameraInputController;
import de.bushnaq.abdalla.pluvia.game.Game;
import de.bushnaq.abdalla.pluvia.game.model.stone.Stone;
import de.bushnaq.abdalla.pluvia.scene.model.bubble.Bubble;
import de.bushnaq.abdalla.pluvia.scene.model.digit.Digit;
import de.bushnaq.abdalla.pluvia.scene.model.firefly.Firefly;
import de.bushnaq.abdalla.pluvia.scene.model.fish.Fish;
import de.bushnaq.abdalla.pluvia.scene.model.fly.Fly;
import de.bushnaq.abdalla.pluvia.scene.model.rain.Rain;
import de.bushnaq.abdalla.pluvia.scene.model.turtle.Turtle;
import de.bushnaq.abdalla.pluvia.ui.AboutDialog;
import de.bushnaq.abdalla.pluvia.ui.InfoDialog;
import de.bushnaq.abdalla.pluvia.ui.MainDialog;
import de.bushnaq.abdalla.pluvia.ui.MessageDialog;
import de.bushnaq.abdalla.pluvia.ui.OptionsDialog;
import de.bushnaq.abdalla.pluvia.ui.PauseDialog;
import de.bushnaq.abdalla.pluvia.ui.ScoreDialog;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRFloatAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.model.ModelInstanceHack;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.utils.EnvironmentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Project dependent 3d class that generates all objects Instantiates the Render3DMaster class
 *
 * @author kunterbunt
 */
public class GameEngine implements ScreenListener, ApplicationListener, InputProcessor, IGameEngine {
    @Override
    public void render2Dxz() {

    }

    @Override
    public boolean updateEnvironment(float timeOfDay) {
        return false;
    }

    class DemoString {
        BitmapFont font;
        String text;

        public DemoString(final String text, final BitmapFont font) {
            this.text = text;
            this.font = font;
        }
    }

//    public static final Color FACTORY_COLOR = Color.DARK_GRAY;                            // 0xff000000;
//    public static final float FACTORY_HEIGHT = 1.2f;
//    public static final float FACTORY_WIDTH = 2.4f;
    public static final int FONT_SIZE = 9;
//    public static final Color NOT_PRODUCING_FACTORY_COLOR = Color.RED;                                // 0xffFF0000;
//    public static final int RAYS_NUM = 128;
    private static final float SCROLL_SPEED = 0.2f;
//    public static final Color SELECTED_PLANET_COLOR = Color.BLUE;
//    public static final Color SELECTED_TRADER_COLOR = Color.RED;                                // 0xffff0000;
//    public static final float SIM_HEIGHT = 0.3f;
//    public static final float SIM_WIDTH = 0.3f;
//    public static final float SOOM_SPEED = 8.0f * 10;
//    public static final float SPACE_BETWEEN_OBJECTS = 0.05f;
//    public static final Color TEXT_COLOR = Color.WHITE;                                // 0xffffffff;
    private static final int TOUCH_DELTA_X = 32;
    private static final int TOUCH_DELTA_Y = 32;
    private AboutDialog aboutDialog;
    private AtlasManager atlasManager;
    private AudioManager audioManager;
    private Texture brdfLUT;
    private MyCameraInputController camController;
    private MovingCamera camera;
    private float centerXD;
    private float centerYD;
    private float centerZD;
    public Context context;
    private IContextFactory contextFactory;
    private GameObject cube = null;
    private Cubemap diffuseCubemap;
    boolean enableProfiling = true;
    private Cubemap environmentDayCubemap;
    private Cubemap environmentNightCubemap;
    private InfoDialog info;
    private final InputMultiplexer inputMultiplexer = new InputMultiplexer();
    private boolean isUpdateContext;
    private final List<VisLabel> labels = new ArrayList<>();
    private final Logger         logger = LoggerFactory.getLogger(this.getClass());
    private       MainDialog     mainDialog;
//    private int maxFramesPerSecond;
    private MessageDialog messageDialog;
    public ModelManager modelManager;
    private OptionsDialog optionsDialog;
    private PauseDialog pauseDialog;
    //	private boolean					pbr;
    private GLProfiler profiler;
    public RenderEngine3D<GameEngine> renderEngine;
    private ScoreDialog scoreDialog;
    private boolean showFps;
    private Cubemap specularCubemap;
    private Stage stage;
    private StringBuilder stringBuilder;
    private boolean takeScreenShot;
    private Integer touchX = null;
    private Integer touchY = null;

    public GameEngine(final IContextFactory contextFactory) throws Exception {
        this.contextFactory = contextFactory;
        modelManager = new ModelManager();
    }

    @Override
    public void create() {
        try {
            if (context == null)// ios
            {
                context = (Context) contextFactory.create();
                evaluateConfiguation();
            }
            showFps = context.getShowFpsProperty();
//			pbr = context.getPbrModeProperty();
            profiler = new GLProfiler(Gdx.graphics);
            profiler.setListener(GLErrorListener.LOGGING_LISTENER);// ---enable exception throwing in case of error
            profiler.setListener(new MyGLErrorListener());
            if (enableProfiling) {
                profiler.enable();
            }
            try {
                context.setSelected(profiler, false);
            } catch (final Exception e) {
                logger.error(e.getMessage(), e);
            }
            createCamera();
            createInputProcessor(this);
            atlasManager = new AtlasManager();
            atlasManager.init();
            renderEngine = new RenderEngine3D<GameEngine>(context, this, camera, null, atlasManager.smallFont, atlasManager.smallFont, atlasManager.systemTextureRegion);
            renderEngine.setSceneBoxMin(new Vector3(-20, -50, -30));
            renderEngine.setSceneBoxMax(new Vector3(20, 20, 2));
            renderEngine.getDepthOfFieldEffect().setFocalDepth(10f);
            createEnvironment();
            modelManager.create(renderEngine.isPbr());
            audioManager = new AudioManager(context);
            createStage();
            context.readScoreFromDisk(this);
//			createStone();
//			createMonument();
//			createGame(0);
//			context.selectGamee(0);
//			context.levelManager = new LevelManager(this, context.game);
        } catch (final Throwable e) {
            logger.error(e.getMessage(), e);
            System.exit(1);
        }
    }

    private GameObject instance;//TODO

    private void createStone() {
        instance = new GameObject(new ModelInstanceHack(modelManager.cube.scene.model), null);
        instance.instance.transform.setToTranslationAndScaling(0, 0, -15, 16, 16, 16);
        instance.update();
        renderEngine.addStatic(instance);
    }

    private void createCamera() {
        camera = new MovingCamera(67f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        final Vector3 lookat = new Vector3(0, 0, 0);
        camera.position.set(lookat.x + 0f / 2, lookat.y + 0f / 2, lookat.z + 8);
        camera.up.set(0, 1, 0);
        camera.lookAt(lookat);
        camera.near = 2f;
        camera.far = 100f;
        camera.update();
        camera.setDirty(true);

    }

    private void createEnvironment() {
        // setup IBL (image based lighting)
        if (renderEngine.isPbr()) {
//			setupImageBasedLightingByFaceNames("ruins", "jpg", "png", "jpg", 10);
            setupImageBasedLightingByFaceNames("clouds", "jpg", "jpg", "jpg", 10);
//			setupImageBasedLightingByFaceNames("moonless_golf_2k", "jpg", "jpg", "jpg", 10);
            // setup skybox
            renderEngine.setDaySkyBox(new SceneSkybox(environmentDayCubemap));
            renderEngine.setNightSkyBox(new SceneSkybox(environmentNightCubemap));
            renderEngine.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap));
            renderEngine.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));
            renderEngine.environment.set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
            renderEngine.environment.set(new PBRFloatAttribute(PBRFloatAttribute.ShadowBias, 0f));
        } else {
        }
    }

    private void createInputProcessor(final InputProcessor inputProcessor) throws Exception {
        camController = new MyCameraInputController(camera);
        camController.scrollFactor = -0.1f;
        camController.translateUnits = 1000f;
        inputMultiplexer.addProcessor(inputProcessor);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    public void createMonument() {
        {
            GameObject cube = new GameObject(new ModelInstanceHack(modelManager.buildingCube[0]), null);
            cube.instance.transform.setToTranslationAndScaling(0, -5, 0, 1f, 10f, 1f);
            renderEngine.addStatic(cube.instance);
        }
        {
            GameObject cube = new GameObject(new ModelInstanceHack(modelManager.buildingCube[0]), null);
            cube.instance.transform.setToTranslationAndScaling(0, -10, 0, 10f, 1f, 10f);
            renderEngine.addStatic(cube.instance);
        }
    }

    private void createStage() throws Exception {
        final int height = 12 * 2;
        stage = new Stage(new ScreenViewport(), renderEngine.renderEngine2D.batch);
//		font = new BitmapFont();
        labels.clear();
        for (int i = 0; i < 8; i++) {
            final VisLabel label = new VisLabel(" ");
            label.setPosition(0, i * height);
            stage.addActor(label);
            labels.add(label);
        }
        stringBuilder = new StringBuilder();
        info = new InfoDialog(renderEngine.renderEngine2D.batch, getInputMultiplexer());
        mainDialog = new MainDialog(this, renderEngine.renderEngine2D.batch, getInputMultiplexer());
        aboutDialog = new AboutDialog(this, renderEngine.renderEngine2D.batch, getInputMultiplexer());
        pauseDialog = new PauseDialog(this, renderEngine.renderEngine2D.batch, getInputMultiplexer());
        messageDialog = new MessageDialog(this, renderEngine.renderEngine2D.batch, getInputMultiplexer());
        optionsDialog = new OptionsDialog(this, renderEngine.renderEngine2D.batch, getInputMultiplexer());
        scoreDialog = new ScoreDialog(this, renderEngine.renderEngine2D.batch, getInputMultiplexer());
        mainDialog.setVisible(true);
    }

    @Override
    public void dispose() {
        try {
            disposeStage();
            audioManager.dispose();
            disposeEnvironment();
            renderEngine.dispose();
            atlasManager.dispose();
            disposeInputProcessor();
            if (profiler.isEnabled()) {
                profiler.disable();
            }
        } catch (final Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void disposeEnvironment() {
        if (renderEngine.isPbr()) {
            diffuseCubemap.dispose();
            environmentNightCubemap.dispose();
            environmentDayCubemap.dispose();
            specularCubemap.dispose();
            brdfLUT.dispose();
        }
    }

    private void disposeInputProcessor() {
        Gdx.input.setInputProcessor(null);
        inputMultiplexer.clear();
    }

    private void disposeStage() {
        stage.dispose();
        mainDialog.dispose();
        scoreDialog.dispose();
        pauseDialog.dispose();
        messageDialog.dispose();
        aboutDialog.dispose();
        info.dispose();
    }

    private void evaluateConfiguation() {
        Gdx.graphics.setForegroundFPS(context.getForegroundFPSProperty());
        Gdx.graphics.setVSync(context.getVsyncProperty());
    }

    public AboutDialog getAboutDialog() {
        return aboutDialog;
    }

    public AtlasManager getAtlasManager() {
        return atlasManager;
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    @Override
    public AudioEngine getAudioEngine() {
        return null;
    }

    public MyCameraInputController getCamController() {
        return camController;
    }

    @Override
    public MovingCamera getCamera() {
        return camera;
    }

    @Override
    public RenderEngine3D<?> getRenderEngine() {
        return renderEngine;
    }

    public InputMultiplexer getInputMultiplexer() {
        return inputMultiplexer;
    }

    public MainDialog getMainDialog() {
        return mainDialog;
    }

//    public int getMaxFramesPerSecond() {
//        return maxFramesPerSecond;
//    }

//	private void exit() {
//		Gdx.app.exit();
//	}

//	private void export(final String fileName, final List<DemoString> Strings) throws IOException {
//		final FileWriter fileWriter = new FileWriter(fileName);
//		final PrintWriter printWriter = new PrintWriter(fileWriter);
//		for (final DemoString demoString : Strings) {
//			printWriter.println(demoString.text);
//		}
//		printWriter.close();
//	}

    public MessageDialog getMessageDialog() {
        return messageDialog;
    }

    public OptionsDialog getOptionsDialog() {
        return optionsDialog;
    }

    public ScoreDialog getScoreDialog() {
        return scoreDialog;
    }

//	public boolean isPbr() {
//		return pbr;
//	}

    @Override
    public boolean keyDown(final int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                if (context.isDebugMode()) {
                    centerXD = -SCROLL_SPEED;
                }
                return true;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                if (context.isDebugMode()) {
                    centerXD = SCROLL_SPEED;
                }
                return true;
            case Input.Keys.W:
            case Input.Keys.UP:
                if (context.isDebugMode()) {
                    centerZD = -SCROLL_SPEED;
                }
                return true;
            case Input.Keys.S:
            case Input.Keys.DOWN:
                if (context.isDebugMode()) {
                    centerZD = SCROLL_SPEED;
                }
                return true;
            case Input.Keys.E:
                if (context.isDebugMode()) {
                    centerYD = -SCROLL_SPEED;
                }
                return true;
            case Input.Keys.Q:
                if (context.isDebugMode()) {
                    centerYD = SCROLL_SPEED;
                }
                return true;


            case Input.Keys.SPACE:
                context.levelManager.nextRound();
                break;
            case Input.Keys.ESCAPE:
                togglePause();
                return true;
            case Input.Keys.PRINT_SCREEN:
                if (context.isDebugMode()) {
                    queueScreenshot();
                }
                return true;

            case Input.Keys.F1:
                renderEngine.setGammaCorrected(!renderEngine.isGammaCorrected());
                if (renderEngine.isGammaCorrected()) logger.info("gamma correction on");
                else logger.info("gamma correction off");
                return true;
            case Input.Keys.F2:
                renderEngine.getDepthOfFieldEffect().setEnabled(!renderEngine.getDepthOfFieldEffect().isEnabled());
                if (renderEngine.getDepthOfFieldEffect().isEnabled()) logger.info("depth of field on");
                else logger.info("depth of field off");
                return true;
            case Input.Keys.F3:
                renderEngine.setRenderBokeh(!renderEngine.isRenderBokeh());
                if (renderEngine.isRenderBokeh()) logger.info("render bokeh on");
                else logger.info("render bokeh off");
                return true;
            case Input.Keys.F4:
                renderEngine.getSsaoEffect().setEnabled(!renderEngine.getSsaoEffect().isEnabled());
                renderEngine.getSsaoComboneEffect().setEnabled(!renderEngine.getSsaoComboneEffect().isEnabled());
                if (renderEngine.getSsaoEffect().isEnabled()) logger.info("ssao on");
                else logger.info("ssao off");
                return true;

            case Input.Keys.F5:
                renderEngine.setAlwaysDay(!renderEngine.isAlwaysDay());
                return true;

            case Input.Keys.I:
                if (context.isDebugMode()) {
                    info.setVisible(!info.isVisible());
                }
                return true;

            case Input.Keys.F9:
                renderEngine.setShowGraphs(!renderEngine.isShowGraphs());
                if (renderEngine.isShowGraphs()) logger.info("graphs are on");
                else logger.info("graphs are off");
                return true;
            case Input.Keys.F10:
                renderEngine.setDebugMode(!renderEngine.isDebugMode());
                if (renderEngine.isDebugMode()) logger.info("debug mode on");
                else logger.info("debug mode off");
                return true;
        }
        return false;

    }

    @Override
    public boolean keyTyped(final char character) {
        return false;
    }

    @Override
    public boolean keyUp(final int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.D:
            case Input.Keys.LEFT:
            case Input.Keys.RIGHT:
                centerXD = 0;
                return true;
            case Input.Keys.W:
            case Input.Keys.S:
            case Input.Keys.UP:
            case Input.Keys.DOWN:
                centerZD = 0;
                return true;
            case Input.Keys.Q:
            case Input.Keys.E:
                centerYD = 0;
                return true;
        }
        return false;
    }

    @Override
    public boolean mouseMoved(final int screenX, final int screenY) {
        return false;
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    public void queueScreenshot() {
        takeScreenShot = true;
    }

    @Override
    public void render() {
//		int error1 = Gdx.gl.glGetError();
//		if (error1 != 0)
//			logger.error("glGetError=" + error1);
//		Gdx.gl.glEnable(0x884F);
//		int error2 = Gdx.gl.glGetError();
//		if (error2 != 0)
//			logger.error("glGetError=" + error2);
        try {
            renderEngine.cpuGraph.begin();
            context.advanceInTime();
            if (profiler.isEnabled()) {
                profiler.reset();// reset on each frame
            }
            render(context.currentTime);
        } catch (final Exception e) {
            logger.error(e.getMessage(), e);
            System.exit(0);
        }
        if (context.restart) {
            if (!Context.isIos())
                Gdx.app.exit();
            else {
                evaluateConfiguation();
            }
        }
    }

    private void render(final long currentTime) throws Exception {
        final float deltaTime = Gdx.graphics.getDeltaTime();

        if (isUpdateContext) {
            isUpdateContext = false;
            dispose();
            create();
        }

        renderEngine.updateCamera(centerXD, centerYD, centerZD);
        updateScore();
        renderStones(currentTime);
        renderDynamicBackground(currentTime);
        renderEngine.cpuGraph.end();
        renderEngine.gpuGraph.begin();
        renderEngine.render(currentTime, deltaTime, takeScreenShot);
//        renderEngine.postProcessRender();
        renderStage();
        renderEngine.gpuGraph.end();
        renderEngine.handleQueuedScreenshot(takeScreenShot);
        takeScreenShot = false;
        if (!context.levelManager.isTilt()) {
            if (context.levelManager.update()) {
                // game was won or ended
                Game game = context.game;
                game.updateTimer();
                if (context.getScoreList().add(this.context.levelManager)) {
                    {
                        // new highscore
                        context.levelManager.playSound(AudioManager.SCORE);
                        context.levelManager.writeResultToDisk();// save recording of our result
                    }
                } else {
                    {
                        context.levelManager.playSound(AudioManager.TILT);
                    }
                }
                context.levelManager.deleteFile();
                context.levelManager.tilt();
                context.levelManager.disposeLevel();
//				context.levelManager = new LevelManager(this, context.game);
                // What is the next seed?
//				int lastGameSeed = context.getLastGameSeed();
//				context.levelManager.setGameSeed(lastGameSeed + 1);
                if (!mainDialog.isVisible())
                    mainDialog.setVisible(true);
            }
        }
    }

    private void renderDynamicBackground(final long currentTime) throws Exception {
        for (Fish fish : context.fishList) {
            fish.get3DRenderer().update(0, 0, 0, renderEngine, currentTime, renderEngine.getTimeOfDay(), 0, false);
        }
        for (Fly firefly : context.fireflyList) {
            firefly.get3DRenderer().update(0, 0, 0, renderEngine, currentTime, renderEngine.getTimeOfDay(), 0, false);
        }
        for (Rain rain : context.rainList) {
            rain.get3DRenderer().update(0, 0, 0, renderEngine, currentTime, renderEngine.getTimeOfDay(), 0, false);
        }
        for (Firefly fly : context.flyList) {
            fly.get3DRenderer().update(0, 0, 0, renderEngine, currentTime, renderEngine.getTimeOfDay(), 0, false);
        }
        for (Bubble bubble : context.bubbleList) {
            bubble.get3DRenderer().update(0, 0, 0, renderEngine, currentTime, renderEngine.getTimeOfDay(), 0, false);
        }
        for (Turtle turtle : context.turtleList) {
            turtle.get3DRenderer().update(0, 0, 0, renderEngine, currentTime, renderEngine.getTimeOfDay(), 0, false);
        }
    }

    private void renderStage() throws Exception {
        int labelIndex = 0;
        // fps
        if (showFps) {
            stringBuilder.setLength(0);
            stringBuilder.append(" FPS ").append(Gdx.graphics.getFramesPerSecond());
            labels.get(labelIndex).setColor(context.levelManager.getInfoColor());
            labels.get(labelIndex).setText(stringBuilder);
            labelIndex++;
        }
        // audio sources
        // {
        // stringBuilder.setLength(0);
        // stringBuilder.append(" audio sources:
        // ").append(renderMaster.sceneManager.audioEngine.getEnabledAudioSourceCount()
        // + " / " +
        // renderMaster.sceneManager.audioEngine.getDisabledAudioSourceCount());
        // labels.get(labelIndex++).setText(stringBuilder);
        // }
        // time
        {
            stringBuilder.setLength(0);

            final float time = renderEngine.getCurrentDayTime();
            final int hours = (int) time;
            final int minutes = (int) (60 * ((time - (int) time) * 100) / 100);
            stringBuilder.append(" time ").append(hours).append(":").append(minutes);
            labels.get(labelIndex++).setText(stringBuilder);
        }
//		{
//			stringBuilder.setLength(0);
//			stringBuilder.append(" Camera ").append(renderEngine.getCamera().position.z);
//			labels.get(labelIndex++).setText(stringBuilder);
//		}
//		{
//			stringBuilder.setLength(0);
//			stringBuilder.append(" Rows ").append(context.game.getNrOfRows());
//			labels.get(labelIndex++).setText(stringBuilder);
//		}

        if (info.isVisible()) {
            info.update(context.selected, renderEngine);
            info.act(Gdx.graphics.getDeltaTime());
            info.draw();
        }
        stage.draw();
        if (mainDialog.isVisible()) {
            mainDialog.draw();
        }
        if (getAboutDialog().isVisible()) {
            getAboutDialog().draw();
        }
        if (getMessageDialog().isVisible()) {
            getMessageDialog().draw();
        }
        if (pauseDialog.isVisible()) {
            pauseDialog.draw();
        }
        if (optionsDialog.isVisible()) {
            optionsDialog.draw();
        }
        if (scoreDialog.isVisible()) {
            scoreDialog.update(context);
            scoreDialog.draw();
        }

    }

    private void renderStones(final long currentTime) throws Exception {
        float dx = ((float) context.levelManager.nrOfColumns) / 2;
        float dy = (context.levelManager.nrOfRows);
        for (Stone stone : context.stoneList) {
            stone.get3DRenderer().update(stone.x - dx, stone.y - dy, stone.z, renderEngine, currentTime, renderEngine.getTimeOfDay(), 0, false);
        }
    }

    @Override
    public void resize(final int width, final int height) {
        scheduleContextUpdate();
//		render2DMaster.width = width;
//		render2DMaster.height = height;
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    /**
     * react to changes in the configuration within the context
     *
     * @throws Exception
     */
    public void scheduleContextUpdate() {
        isUpdateContext = true;
    }

    @Override
    public boolean scrolled(final float amountX, final float amountY) {

        return false;
    }

    @Override
    public void setCamera(final float x, final float z, final boolean setDirty) throws Exception {
        renderEngine.setCameraTo(x, z, setDirty);
    }

    // private void setupImageBasedLightingByFaceNames(final String name, final String diffuseExtension, final String environmentExtension, final String specularExtension, final int specularIterations) {
//	diffuseCubemap = EnvironmentUtil.createCubemap(new InternalFileHandleResolver(), "textures/" + name + "/diffuse/diffuse_", "." + diffuseExtension, EnvironmentUtil.FACE_NAMES_NEG_POS);
//	environmentDayCubemap = EnvironmentUtil.createCubemap(new InternalFileHandleResolver(), "textures/" + name + "/environment/environment_", "." + environmentExtension, EnvironmentUtil.FACE_NAMES_NEG_POS);
////	environmentNightCubemap = EnvironmentUtil.createCubemap(new InternalFileHandleResolver(), "textures/" + name + "/environmentNight/environment_", "_0." + environmentExtension, EnvironmentUtil.FACE_NAMES_FULL);
//	specularCubemap = EnvironmentUtil.createCubemap(new InternalFileHandleResolver(), "textures/" + name + "/specular/specular_", "_", "." + specularExtension, specularIterations, EnvironmentUtil.FACE_NAMES_NEG_POS);
//	brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));
//
//	// // setup quick IBL (image based lighting)
//	// DirectionalLightEx light = new DirectionalLightEx();
//	// light.direction.set(1, -3, 1).nor();
//	// light.color.set(Color.WHITE);
//	// IBLBuilder iblBuilder = IBLBuilder.createOutdoor(light);
//	// environmentCubemap = iblBuilder.buildEnvMap(1024);
//	// diffuseCubemap = iblBuilder.buildIrradianceMap(256);
//	// specularCubemap = iblBuilder.buildRadianceMap(10);
//	// iblBuilder.dispose();
//}
    private void setupImageBasedLightingByFaceNames(final String name, final String diffuseExtension, final String environmentExtension, final String specularExtension, final int specularIterations) {
        diffuseCubemap = EnvironmentUtil.createCubemap(new InternalFileHandleResolver(), AtlasManager.getAssetsFolderName() + "/textures/" + name + "/diffuse/diffuse_", "_0." + diffuseExtension,
                EnvironmentUtil.FACE_NAMES_FULL);
        environmentDayCubemap = EnvironmentUtil.createCubemap(new InternalFileHandleResolver(), AtlasManager.getAssetsFolderName() + "/textures/" + name + "/environmentDay/environment_", "_0." + environmentExtension,
                EnvironmentUtil.FACE_NAMES_FULL);
        environmentNightCubemap = EnvironmentUtil.createCubemap(new InternalFileHandleResolver(), AtlasManager.getAssetsFolderName() + "/textures/" + name + "/environmentNight/environment_", "_0." + environmentExtension,
                EnvironmentUtil.FACE_NAMES_FULL);
        specularCubemap = EnvironmentUtil.createCubemap(new InternalFileHandleResolver(), AtlasManager.getAssetsFolderName() + "/textures/" + name + "/specular/specular_", "_", "." + specularExtension,
                specularIterations, EnvironmentUtil.FACE_NAMES_FULL);
        brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));

        // // setup quick IBL (image based lighting)
        // DirectionalLightEx light = new DirectionalLightEx();
        // light.direction.set(1, -3, 1).nor();
        // light.color.set(Color.WHITE);
        // IBLBuilder iblBuilder = IBLBuilder.createOutdoor(light);
        // environmentCubemap = iblBuilder.buildEnvMap(1024);
        // diffuseCubemap = iblBuilder.buildIrradianceMap(256);
        // specularCubemap = iblBuilder.buildRadianceMap(10);
        // iblBuilder.dispose();
    }

    public void toggleDebugmode() {
        if (context.isDebugModeSupported()) {
            renderEngine.setDebugMode(!renderEngine.isDebugMode());
        } else {
            renderEngine.setDebugMode(false);
        }
        if (renderEngine.isDebugMode()) {
            getInputMultiplexer().addProcessor(getCamController());
        } else {
            getInputMultiplexer().removeProcessor(getCamController());
        }
    }

    public void togglePause() {
        pauseDialog.setVisible(!pauseDialog.isVisible());
    }

    @Override
    public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) {
        if (!Context.isIos()) {
            switch (button) {
                case Input.Buttons.LEFT: {
                    // did we select an object?
                    // renderMaster.sceneClusterManager.createCoordinates();
                    final GameObject selected = renderEngine.getGameObject(screenX, screenY);
//				System.out.println("selected " + selected);
                    if (selected != null)
                        context.levelManager.reactLeft(selected.interactive);
                }
                break;
                case Input.Buttons.RIGHT: {
                    // did we select an object?
                    // renderMaster.sceneClusterManager.createCoordinates();
                    final GameObject selected = renderEngine.getGameObject(screenX, screenY);
//				System.out.println("selected " + selected);
                    if (selected != null)
                        context.levelManager.reactRight(selected.interactive);
                }
                break;
            }
        }
        return false;
    }

    @Override
    public boolean touchDragged(final int screenX, final int screenY, final int pointer) {
        if (Context.isIos()) {
//			logger.info(String.format("x=%d, y=%d", screenX, screenY));
            if (touchX == null || touchY == null) {
                touchX = screenX;
                touchY = screenY;
            } else {
                int dx = screenX - touchX;
                int dy = screenY - touchY;
                if (Math.abs(dx) > TOUCH_DELTA_X && Math.abs(dy) < TOUCH_DELTA_Y) {
                    if (dx > 0) {
                        // dragged finger to the right
                        // did we select an object?
                        // renderMaster.sceneClusterManager.createCoordinates();
                        final GameObject selected = renderEngine.getGameObject(touchX, touchY);
//						System.out.println("react right dx " + dx);
//						System.out.println("selected " + selected);
                        if (selected != null)
                            context.levelManager.reactRight(selected.interactive);
                    } else {
                        // dragged finger to the left
                        // did we select an object?
                        // renderMaster.sceneClusterManager.createCoordinates();
                        final GameObject selected = renderEngine.getGameObject(touchX, touchY);
//						System.out.println("react left dx " + dx);
//						System.out.println("selected " + selected);
                        if (selected != null)
                            context.levelManager.reactLeft(selected.interactive);
                    }
                }
                if (Math.abs(dy) > TOUCH_DELTA_Y && Math.abs(dx) < TOUCH_DELTA_X) {
                    if (dy > 0) {
                        // dragged finger down
                        context.levelManager.nextRound();
                    } else {
                        // dragged finger up
                        togglePause();
                    }
                }
            }

        }
        return false;
    }

    @Override
    public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
        touchX = null;
        touchY = null;
//		System.out.println("reset touch");
        return true;
    }


    public void updateContext() {
        dispose();
        create();
    }

    public void updateScore() {
        for (Digit digit : context.digitList) {
            digit.update(context.levelManager);
        }
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

}
