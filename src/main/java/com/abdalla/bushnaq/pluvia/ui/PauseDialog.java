package com.abdalla.bushnaq.pluvia.ui;

import com.abdalla.bushnaq.pluvia.engine.GameEngine;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.Sizes;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class PauseDialog extends AbstractDialog {

	public PauseDialog(GameEngine gameEngine, final Batch batch, final InputMultiplexer inputMultiplexer) throws Exception {
		super(gameEngine, batch, inputMultiplexer);
		createStage("", true);
	}

	protected void close() {
		setVisible(false);
	}
//	@Override
//	protected void enterAction() {
//		setVisible(false);
//	}
//
//	protected void escapeAction() {
//		setVisible(false);
//	}

	@Override
	public void create() {
		Sizes sizes = VisUI.getSizes();
		{
			getTable().row();
			VisLabel label = new VisLabel("Pause Menu");
			label.setColor(LIGHT_BLUE_COLOR);
			label.setAlignment(Align.center);
			getTable().add(label).width(DIALOG_WIDTH * sizes.scaleFactor).pad(0, 16, 16, 16).center();
		}
		{
			getTable().row();
			VisTextButton button = new VisTextButton("Resume Game", "blue");
			addHoverEffect(button);
			button.addListener(new ClickListener() {
				@Override
				public void clicked(final InputEvent event, final float x, final float y) {
					close();
				}
			});
			getTable().add(button).center().width(BUTTON_WIDTH * sizes.scaleFactor);
		}
		{
			getTable().row();
			VisTextButton button = new VisTextButton("View Score");
			addHoverEffect(button);
			button.addListener(new ClickListener() {
				@Override
				public void clicked(final InputEvent event, final float x, final float y) {
					getGameEngine().getScoreDialog().push(PauseDialog.this);
				}
			});
			getTable().add(button).center().width(BUTTON_WIDTH * sizes.scaleFactor);
		}
		{
			getTable().row();
			VisTextButton button = new VisTextButton("New Game");
			addHoverEffect(button);
			button.addListener(new ClickListener() {
				@Override
				public void clicked(final InputEvent event, final float x, final float y) {
					getGameEngine().context.levelManager.disposeLevel();
					getGameEngine().context.levelManager.createLevel();
					close();
				}
			});
			getTable().add(button).center().width(BUTTON_WIDTH * sizes.scaleFactor);
		}
		{
			getTable().row();
			VisTextButton button = new VisTextButton("Back to Main Menu");
			addHoverEffect(button);
//			button.setColor(Color.RED);
			button.addListener(new ClickListener() {

				@Override
				public void clicked(final InputEvent event, final float x, final float y) {
					setVisible(false);
					getGameEngine().context.levelManager.writeToDisk();
					getGameEngine().getMainDialog().setVisible(true);
				}
			});
			getTable().add(button).center().width(BUTTON_WIDTH * sizes.scaleFactor).pad(16);
		}

		getTable().pack();
		positionWindow();
	}

}
