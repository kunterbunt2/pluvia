package com.abdalla.bushnaq.pluvia.ui;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abdalla.bushnaq.pluvia.desktop.Context;
import com.abdalla.bushnaq.pluvia.engine.GameEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.VisUI.SkinScale;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

//enum BlurMode {
//	up, down
//}

public abstract class AbstractDialog {
	protected static final Color	LIGHT_BLUE_COLOR	= new Color(0x1BA1E2FF);
	protected static final int		DIALOG_WIDTH		= 150;
	protected static final int		BUTTON_WIDTH		= 150;
	private final Batch				batch;
	private GameEngine				gameEngine;
	private final InputMultiplexer	inputMultiplexer;
	private List<InputProcessor>	inputProcessorCache	= new ArrayList<>();
	// private float blurAmount = 1f;
	//	private int						blurPasses			= 1;
	//	private BlurMode				blurMode			= BlurMode.up;
	final Logger					logger				= LoggerFactory.getLogger(this.getClass());
	private Stage					stage;
	private boolean					visible				= false;
	private VisDialog				dialog;
	private VisTable				table				= new VisTable(true);
	private AbstractDialog			parent;

	public AbstractDialog(GameEngine gameEngine, final Batch batch, final InputMultiplexer inputMultiplexer) throws Exception {
		this.gameEngine = gameEngine;
		this.batch = batch;
		this.inputMultiplexer = inputMultiplexer;
	}

	protected abstract void create();

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
		}
		stage.addActor(createWindow(title));
		create();
		packAndPosition();

	}

	protected void packAndPosition() {
		dialog.pack();
		positionWindow();
	}

	protected void enterAction() {
		close();
	}

	protected void escapeAction() {
		close();
	}

	protected VisDialog createWindow(String title) {
		dialog = new VisDialog(title);
		dialog.setColor(0.0f, 0.0f, 0.0f, 0.6f);
		dialog.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		dialog.setMovable(false);
//		table.setDebug(true);
		table.pad(0, 16, 16, 16);
		dialog.getContentTable().add(this.table);
		return dialog;
	}

	protected void close() {
		pop();
	}

	public void dispose() {
		inputMultiplexer.removeProcessor(stage);
		stage.dispose();
	}

//	public void disposeWindow() {
//		stage.clear();
//	}

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

	public GameEngine getGameEngine() {
		return gameEngine;
	}

	public Viewport getViewport() {
		return stage.getViewport();
	}

	public VisTable getTable() {
		return table;
	}

	public VisDialog getDialog() {
		return dialog;
	}

	public boolean isVisible() {
		return visible;
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

	public void pop() {
		setVisible(false);
		parent.setVisible(true);
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
			gameEngine.renderEngine.updateBlurEffect(1, 1f);
//			gameEngine.renderEngine.addBlurEffect();
			gameEngine.renderEngine.updateBlurEffect(2, 1f);
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
			gameEngine.renderEngine.removeBlurEffect();
			afterInvisible();
		}
	}

	protected void afterInvisible() {
	}

	protected void beforeVisible() {
	}

	public void update(final Context universe) {
	}

	protected void addHoverEffect(VisTextButton button) {
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

}
