package de.bushnaq.abdalla.pluvia.ui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import de.bushnaq.abdalla.pluvia.desktop.Context;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import de.bushnaq.abdalla.pluvia.game.LevelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//enum BlurMode {
//	up, down
//}

/**
 * @author kunterbunt
 *
 */
public abstract class AbstractDialog {
	protected static final int		BUTTON_WIDTH		= 150;
	protected static final int		DIALOG_WIDTH		= 150;
	protected static final int		LABEL_WIDTH			= 250;
	protected static final Color	LIGHT_BLUE_COLOR	= new Color(0x1BA1E2FF);
	private final Batch				batch;
	private VisDialog				dialog;
	private GameEngine				gameEngine;
	private final InputMultiplexer	inputMultiplexer;
	private   List<InputProcessor> inputProcessorCache = new ArrayList<>();
	// private float blurAmount = 1f;
//	private int						blurPasses			= 1;
//	private BlurMode				blurMode			= BlurMode.up;
	final     Logger               logger              = LoggerFactory.getLogger(this.getClass());
	protected boolean              modal               = false;
	private AbstractDialog			parent;
	private Stage					stage;
	private VisTable				table				= new VisTable(true);
	private boolean					visible				= false;

	public AbstractDialog(GameEngine gameEngine, final Batch batch, final InputMultiplexer inputMultiplexer) throws Exception {
		this.gameEngine = gameEngine;
		this.batch = batch;
		this.inputMultiplexer = inputMultiplexer;
	}

	protected void addHoverEffect(final VisTextButton button) {
		button.addListener(new ClickListener() {
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				button.setFocusBorderEnabled(true);
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				button.setFocusBorderEnabled(false);
			}
		});
	}

	protected void afterInvisible() {
	}

	protected void beforeVisible() {
	}

	protected void close() {
		pop();
	}

	protected abstract void create();

	protected void createGame(int gameIndex, boolean resume, int seed) {
		if (getGameEngine().context.levelManager != null)
			getGameEngine().context.levelManager.disposeLevel();
		getGameEngine().context.selectGame(gameIndex);
		getGameEngine().context.levelManager = new LevelManager(getGameEngine().renderEngine, getGameEngine().context.game);
//		universe.GameThread.clearLevel();
		if (resume) {
			if (!getGameEngine().context.levelManager.readFromDisk()) {
				// we failed to read the level
				// What is the next seed?
				getGameEngine().context.levelManager.disposeLevel();
				int lastGameSeed = getGameEngine().context.getLastGameSeed();
				getGameEngine().context.levelManager.setGameSeed(lastGameSeed + 1);
			} else {
				if (!getGameEngine().context.levelManager.testValidity()) {
					// we failed to validate the level
					logger.error("invalid recording file");
//					System.exit(1);
					// What is the next seed?
					getGameEngine().context.levelManager.disposeLevel();
					int lastGameSeed = getGameEngine().context.getLastGameSeed();
					getGameEngine().context.levelManager.setGameSeed(lastGameSeed + 1);
				}
			}
		} else {
			if (seed == -1) {
				// next seed
				int lastGameSeed = getGameEngine().context.getLastGameSeed();
				getGameEngine().context.levelManager.setGameSeed(lastGameSeed + 1);
			} else {
				// seed is defined by user choice (high score)
				getGameEngine().context.levelManager.setGameSeed(seed);
			}
		}
		getGameEngine().context.levelManager.createLevel();
		getGameEngine().context.game.startTimer();
		{
			float	z			= getGameEngine().context.game.cameraZPosition;
			Vector3	position	= getGameEngine().renderEngine.getCamera().position;
			position.z = z;
			if (getGameEngine().context.game.getNrOfRows() == 0) {
				position.y = 4;
				getGameEngine().renderEngine.getCamera().lookat.y = 4.5f;
			} else {
				position.y = getGameEngine().context.game.getNrOfRows() / 2;
				getGameEngine().renderEngine.getCamera().lookat.y = getGameEngine().context.game.getNrOfRows() / 2 + 0.5f;
			}
			getGameEngine().renderEngine.getCamera().update();
		}
	}

	public void createStage(String title, boolean closeOnEscape) {
		if (stage == null) {
			stage = new Stage(new ScreenViewport(), batch);
			if (closeOnEscape) {
				stage.addListener(new InputListener() {
					@Override
					public boolean keyDown(InputEvent event, int keycode) {
						switch (event.getKeyCode()) {
						case Input.Keys.ESCAPE:
							escapeAction();
							return true;
						}
						return false;
					}
				});
			}
			stage.addListener(new InputListener() {

				@Override
				public boolean keyDown(InputEvent event, int keycode) {
					switch (event.getKeyCode()) {
					case Input.Keys.ENTER:
						enterAction();
						return true;
					}
					return false;
				}

			});
			stage.addActor(createWindow(title));
			create();
			packAndPosition();
		}
	}

	protected VisDialog createWindow(String title) {
		dialog = new VisDialog(title);
		dialog.setColor(0.0f, 0.0f, 0.0f, 1.0f);
		dialog.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		dialog.setMovable(false);
		getDialog().setBackground((Drawable) null);
//		table.setDebug(true);
		table.pad(0, 16, 16, 16);
		dialog.getContentTable().add(this.table);
		return dialog;
	}

//	public void disposeWindow() {
//		stage.clear();
//	}

	public void dispose() {
		inputMultiplexer.removeProcessor(stage);
		stage.dispose();
	}

	public void draw() {
//		switch (blurMode) {
//		case up:
//			if (blurPasses < 32)
//				blurPasses += 1;
//			else {
//				int a = 0;
//			}
//			break;
//		case down:
//			if (blurPasses > 1)
//				blurPasses -= 1;
//			else {
//				gameEngine.renderEngine.removeBlurEffect();
//				this.visible = false;
//			}
//			break;
//		}
//		gameEngine.renderEngine.updateBlurEffect(blurPasses, blurAmount);

		stage.act();
		stage.draw();
	}

	protected void enterAction() {
		close();
	}

	protected void escapeAction() {
		close();
	}

	public VisDialog getDialog() {
		return dialog;
	}

	public GameEngine getGameEngine() {
		return gameEngine;
	}

	public VisTable getTable() {
		return table;
	}

	public Viewport getViewport() {
		return stage.getViewport();
	}

	public boolean isVisible() {
		return visible;
	}

	protected void packAndPosition() {
		dialog.pack();
		positionWindow();
	}

	public void pop() {
		setVisible(false);
		parent.setVisible(true);
	}

	protected void positionWindow() {
		dialog.setPosition(Gdx.graphics.getWidth() / 2 - dialog.getWidth() / 2, Gdx.graphics.getHeight() / 2 - (dialog.getHeight() - GameEngine.FONT_SIZE - 2) / 2);
	}

	/**
	 * switch to another dialog
	 *
	 * @param dialog
	 */
	public void push(AbstractDialog parent) {
		this.parent = parent;
		parent.setVisible(false);
		setVisible(true);
	}

	public void setVisible(final boolean visible) {
		this.visible = visible;
		if (visible) {
			beforeVisible();
			for (InputProcessor ip : inputMultiplexer.getProcessors()) {
				inputProcessorCache.add(ip);
			}
			inputMultiplexer.clear();
			inputMultiplexer.addProcessor(stage);
//			gameEngine.renderEngine.updateBlurEffect(1, 1f);
//			gameEngine.renderEngine.addBlurEffect();
//			gameEngine.renderEngine.updateBlurEffect(32, 1f);
//			gameEngine.renderEngine.addBloomEffect();
//			blurMode = BlurMode.up;
//			blurAmount = 1f;
//			blurPasses = 1;
		} else {
			inputMultiplexer.removeProcessor(stage);
			for (InputProcessor ip : inputProcessorCache) {
				inputMultiplexer.addProcessor(ip);
			}
			inputProcessorCache.clear();
//			blurMode = BlurMode.down;
//			gameEngine.renderEngine.removeBlurEffect();
//			gameEngine.renderEngine.removeBloomEffect();
			afterInvisible();
		}
	}

	public void update(final Context universe) {
	}

}
