package com.abdalla.bushnaq.pluvia.engine;

import java.util.ArrayList;
import java.util.List;

import com.abdalla.bushnaq.pluvia.desktop.Context;
import com.abdalla.bushnaq.pluvia.desktop.IContextFactory;
import com.abdalla.bushnaq.pluvia.game.Game;
import com.abdalla.bushnaq.pluvia.game.model.stone.Stone;
import com.abdalla.bushnaq.pluvia.scene.model.bubble.Bubble;
import com.abdalla.bushnaq.pluvia.scene.model.digit.Digit;
import com.abdalla.bushnaq.pluvia.scene.model.firefly.Firefly;
import com.abdalla.bushnaq.pluvia.scene.model.fish.Fish;
import com.abdalla.bushnaq.pluvia.scene.model.fly.Fly;
import com.abdalla.bushnaq.pluvia.scene.model.rain.Rain;
import com.abdalla.bushnaq.pluvia.scene.model.turtle.Turtle;
import com.abdalla.bushnaq.pluvia.ui.AboutDialog;
import com.abdalla.bushnaq.pluvia.ui.MainDialog;
import com.abdalla.bushnaq.pluvia.ui.MessageDialog;
import com.abdalla.bushnaq.pluvia.ui.OptionsDialog;
import com.abdalla.bushnaq.pluvia.ui.PauseDialog;
import com.abdalla.bushnaq.pluvia.ui.ScoreDialog;
import com.abdalla.bushnaq.pluvia.util.logger.Logger;
import com.abdalla.bushnaq.pluvia.util.logger.LoggerFactory;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.profiling.GLErrorListener;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.widget.VisLabel;

import net.mgsx.gltf.scene3d.model.ModelInstanceHack;

/**
 * Project dependent 3d class that generates all objects Instantiates the Render3DMaster class
 *
 * @author abdal
 *
 */
public class GameEngine implements ScreenListener, ApplicationListener, InputProcessor {
	class DemoString {
		BitmapFont	font;
		String		text;

		public DemoString(final String text, final BitmapFont font) {
			this.text = text;
			this.font = font;
		}
	}

	public static final Color		FACTORY_COLOR				= Color.DARK_GRAY;							// 0xff000000;
	public static final float		FACTORY_HEIGHT				= 1.2f;
	public static final float		FACTORY_WIDTH				= 2.4f;
	public static final int			FONT_SIZE					= 9;
	public static final Color		NOT_PRODUCING_FACTORY_COLOR	= Color.RED;								// 0xffFF0000;
	public static final int			RAYS_NUM					= 128;
	private static final float		SCROLL_SPEED				= 0.2f;
	public static final Color		SELECTED_PLANET_COLOR		= Color.BLUE;
	public static final Color		SELECTED_TRADER_COLOR		= Color.RED;								// 0xffff0000;
	public static final float		SIM_HEIGHT					= 0.3f;
	public static final float		SIM_WIDTH					= 0.3f;
	public static final float		SOOM_SPEED					= 8.0f * 10;
	public static final float		SPACE_BETWEEN_OBJECTS		= 0.1f / Context.WORLD_SCALE;
	public static final Color		TEXT_COLOR					= Color.WHITE;								// 0xffffffff;
	private static final int		TOUCH_DELTA_X				= 32;
	private static final int		TOUCH_DELTA_Y				= 32;
	private AboutDialog				aboutDialog;
	private AudioManager			audioManager;
	private float					centerXD;
	private float					centerYD;
	private float					centerZD;
	public Context					context;
	private IContextFactory			contextFactory;
	private GameObject				cube						= null;
	boolean							enableProfiling				= true;
	private boolean					followMode;
	private final List<VisLabel>	labels						= new ArrayList<>();
	private final Logger			logger						= LoggerFactory.getLogger(this.getClass());
	private MainDialog				mainDialog;
	private int						maxFramesPerSecond;
	private MessageDialog			messageDialog;
	public ModelManager				modelManager;
	private OptionsDialog			optionsDialog;
	private PauseDialog				pauseDialog;
	private GLProfiler				profiler;
	public RenderEngine				renderEngine;
	private ScoreDialog				scoreDialog;
	private boolean					showFps;
	private Stage					stage;
	private StringBuilder			stringBuilder;
	private boolean					takeScreenShot;
	private Integer					touchX						= null;

	private Integer					touchY						= null;

	private boolean					vsyncEnabled				= true;

	public GameEngine(final IContextFactory contextFactory) throws Exception {
		this.contextFactory = contextFactory;
//		universe.setScreenListener(this);
		modelManager = new ModelManager();
	}

	@Override
	public void create() {
		try {
			if (context == null)// ios
			{
				context = contextFactory.create();
				evaluateConfiguation();
			}
			showFps = context.getShowFpsProperty();
//			Gdx.gl.glEnable(0x884F);
//			if (Gdx.gl.glGetError() != 0)
//				logger.error("" + Gdx.gl.glGetError());
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
			renderEngine = new RenderEngine(context, this);
			modelManager.create(renderEngine.isPbr());
			audioManager = new AudioManager();
			createStage();
			context.readScoreFromDisk(this);
//			createMonument();
//			createGame(0);
//			context.selectGamee(0);
//			context.levelManager = new LevelManager(this, context.game);
		} catch (final Throwable e) {
			logger.error(e.getMessage(), e);
			System.exit(1);
		}
	}

//	private void createGame(int gameIndex) {
//		if (context.levelManager != null)
//			context.levelManager.destroyLevel();
//		context.selectGamee(gameIndex);
//		context.levelManager = new LevelManager(this, context.game);
////		universe.GameThread.clearLevel();
//		context.levelManager.createLevel();
//		{
//			float	z			= context.game.cameraZPosition;
//			Vector3	position	= renderEngine.getCamera().position;
//			position.z = z;
//			position.y = context.game.getNrOfRows() / 2;
//			renderEngine.getCamera().lookat.y = context.game.getNrOfRows() / 2 + 0.5f;
//			renderEngine.getCamera().update();
//		}
//	}

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
		stage = new Stage(new ScreenViewport(), renderEngine.batch2D);
//		font = new BitmapFont();
		for (int i = 0; i < 8; i++) {
			final VisLabel label = new VisLabel(" ");
			label.setPosition(0, i * height);
			stage.addActor(label);
			labels.add(label);
		}
		stringBuilder = new StringBuilder();
		mainDialog = new MainDialog(this, renderEngine.batch2D, renderEngine.getInputMultiplexer());
		aboutDialog = new AboutDialog(this, renderEngine.batch2D, renderEngine.getInputMultiplexer());
		pauseDialog = new PauseDialog(this, renderEngine.batch2D, renderEngine.getInputMultiplexer());
		messageDialog = new MessageDialog(this, renderEngine.batch2D, renderEngine.getInputMultiplexer());
		optionsDialog = new OptionsDialog(this, renderEngine.batch2D, renderEngine.getInputMultiplexer());
		scoreDialog = new ScoreDialog(this, renderEngine.batch2D, renderEngine.getInputMultiplexer());
		mainDialog.setVisible(true);
	}

	@Override
	public void dispose() {
		try {
			if (profiler.isEnabled()) {
				profiler.disable();
			}
			renderEngine.dispose();
//			font.dispose();
			stage.dispose();
			mainDialog.dispose();
			scoreDialog.dispose();
			pauseDialog.dispose();
			messageDialog.dispose();
			aboutDialog.dispose();
			audioManager.dispose();
		} catch (final Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void evaluateConfiguation() {
		Gdx.graphics.setForegroundFPS(context.getForegroundFPSProperty());
		Gdx.graphics.setVSync(context.getVsyncProperty());
	}

	public AboutDialog getAboutDialog() {
		return aboutDialog;
	}

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

	public AudioManager getAudioManager() {
		return audioManager;
	}

	public MainDialog getMainDialog() {
		return mainDialog;
	}

	public int getMaxFramesPerSecond() {
		return maxFramesPerSecond;
	}

	public MessageDialog getMessageDialog() {
		return messageDialog;
	}

	public OptionsDialog getOptionsDialog() {
		return optionsDialog;
	}

	public ScoreDialog getScoreDialog() {
		return scoreDialog;
	}

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
		case Input.Keys.Q:
			if (context.isDebugMode()) {
				centerYD = SCROLL_SPEED;
			}
			return true;
		case Input.Keys.W:
		case Input.Keys.UP:
			if (context.isDebugMode()) {
				centerZD = -SCROLL_SPEED;
			}
			return true;
		case Input.Keys.E:
			if (context.isDebugMode()) {
				centerYD = -SCROLL_SPEED;
			}
			return true;
		case Input.Keys.S:
		case Input.Keys.DOWN:
			if (context.isDebugMode()) {
				centerZD = SCROLL_SPEED;
			}
			return true;
		case Input.Keys.ESCAPE:
			togglePause();
			return true;
		case Input.Keys.PRINT_SCREEN:
			if (context.isDebugMode()) {
				queueScreenshot();
			}
			return true;
//		case Input.Keys.NUM_1:
//			renderEngine.setAlwaysDay(!renderEngine.isAlwaysDay());
//			return true;
//		case Input.Keys.V:
//			vsyncEnabled = !vsyncEnabled;
//			Gdx.graphics.setVSync(vsyncEnabled);
//			return true;
		case Input.Keys.I:
			if (context.isDebugMode()) {
				renderEngine.getInfo().setVisible(!renderEngine.getInfo().isVisible());
			}
			return true;
//		case Input.Keys.N:
//			break;
		case Input.Keys.SPACE:
			context.levelManager.nextRound();
			break;
//		case Input.Keys.TAB:
//			try {
//				context.setSelected(profiler, false);
//			} catch (final Exception e) {
//				logger.error(e.getMessage(), e);
//			}
//			return true;
//		case Input.Keys.NUM_1:
//		case Input.Keys.NUM_2:
//		case Input.Keys.NUM_3:
//		case Input.Keys.NUM_4:
//		case Input.Keys.NUM_5:
//		case Input.Keys.NUM_6:
//			createGame(keycode - Input.Keys.NUM_1);
//			return true;

//		case Input.Keys.NUM_2:
//			if (launchMode == LaunchMode.demo)
//				launchMode = LaunchMode.normal;
//			return true;
		case Input.Keys.F5:
			if (context.isDebugMode())
				renderEngine.toggleDebugmode();
			return true;
		case Input.Keys.F6:
			if (context.isShowGraphs())
				renderEngine.toggleShowGraphs();
			return true;
		case Input.Keys.F:
			followMode = !followMode;
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
		renderEngine.updateCamera(centerXD, centerYD, centerZD);
		updateScore();
		renderStones(currentTime);
		renderDynamicBackground(currentTime);
		renderEngine.cpuGraph.end();
		renderEngine.gpuGraph.begin();
		renderEngine.render(currentTime, deltaTime, takeScreenShot);
		renderEngine.renderStage();
		renderEngine.gpuGraph.end();
		renderStage();
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
						audioManager.get(AudioManager.SCORE).play();
//						Tools.play(AtlasManager.getAssetsFolderName() + "/sound/score.wav");
						context.levelManager.writeResultToDisk();// save recording of our result
					}
				} else {
					{
						audioManager.get(AudioManager.TILT).play();
//						Tools.play(AtlasManager.getAssetsFolderName() + "/sound/tilt.wav");
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
			fish.get3DRenderer().update(0, 0, 0, this, currentTime, renderEngine.getTimeOfDay(), 0, false);
		}
		for (Fly firefly : context.fireflyList) {
			firefly.get3DRenderer().update(0, 0, 0, this, currentTime, renderEngine.getTimeOfDay(), 0, false);
		}
		for (Rain rain : context.rainList) {
			rain.get3DRenderer().update(0, 0, 0, this, currentTime, renderEngine.getTimeOfDay(), 0, false);
		}
		for (Firefly fly : context.flyList) {
			fly.get3DRenderer().update(0, 0, 0, this, currentTime, renderEngine.getTimeOfDay(), 0, false);
		}
		for (Bubble bubble : context.bubbleList) {
			bubble.get3DRenderer().update(0, 0, 0, this, currentTime, renderEngine.getTimeOfDay(), 0, false);
		}
		for (Turtle turtle : context.turtleList) {
			turtle.get3DRenderer().update(0, 0, 0, this, currentTime, renderEngine.getTimeOfDay(), 0, false);
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
//		{
//			stringBuilder.setLength(0);
//
//			final float	time	= renderEngine.getCurrentDayTime();
//			final int	hours	= (int) time;
//			final int	minutes	= (int) (60 * ((time - (int) time) * 100) / 100);
//			stringBuilder.append(" time ").append(hours).append(":").append(minutes);
//			labels.get(labelIndex++).setText(stringBuilder);
//		}
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
		float	dx	= ((float) context.levelManager.nrOfColumns) / 2;
		float	dy	= (context.levelManager.nrOfRows);
		for (Stone stone : context.stoneList) {
			stone.get3DRenderer().update(stone.x - dx, stone.y - dy, stone.z, this, currentTime, renderEngine.getTimeOfDay(), 0, false);
		}
	}

	@Override
	public void resize(final int width, final int height) {
//		render2DMaster.width = width;
//		render2DMaster.height = height;
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean scrolled(final float amountX, final float amountY) {

		return false;
	}

	@Override
	public void setCamera(final float x, final float z, final boolean setDirty) throws Exception {
		renderEngine.setCameraTo(x, z, setDirty);
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
				int	dx	= screenX - touchX;
				int	dy	= screenY - touchY;
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

	public void updateScore() {
		for (Digit digit : context.digitList) {
			digit.update(context.levelManager);
		}
	}

}
