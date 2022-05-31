package com.abdalla.bushnaq.pluvia.ui;

import com.abdalla.bushnaq.pluvia.desktop.ApplicationProperties;
import com.abdalla.bushnaq.pluvia.engine.GameEngine;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.Sizes;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class OptionsDialog extends AbstractDialog {

	private VisLabel	graphicsQualityLabel;
	private VisSlider	graphicsQualitySlider;
	private VisSlider	maxPointLightsSlider;
	private VisLabel	maxPointLightsLabel;
	private VisSlider	maxSceneObjectsSlider;
	private VisLabel	maxSceneObjectsLabel;

	public OptionsDialog(GameEngine gameEngine, final Batch batch, final InputMultiplexer inputMultiplexer) throws Exception {
		super(gameEngine, batch, inputMultiplexer);
		createStage("", true);
	}

	public void setVisible(final boolean visible) {
		super.setVisible(visible);
		if (visible) {
			setGraphicsQuality(getGameEngine().context.getGraphicsQuality());
		}
	}

	@Override
	public void create() {
		Sizes sizes = VisUI.getSizes();
		createCaption(sizes);
		getTable().row().pad(16);
		createGraphcsQuality(sizes);
		getTable().row().pad(16);
		createMaxPointLights(sizes);
		getTable().row().pad(16);
		createMaxSceneObjects(sizes);
		getTable().row().pad(16);
		createButtons(sizes);

		getTable().pack();
		positionWindow();
	}

	private void createCaption(Sizes sizes) {
		getTable().row();
		VisLabel label = new VisLabel("Options");
		label.setColor(LIGHT_BLUE_COLOR);
		label.setAlignment(Align.center);
		getTable().add(label).colspan(4).width(DIALOG_WIDTH * sizes.scaleFactor).pad(0, 16, 16, 16).center();
	}

	private void createButtons(Sizes sizes) {
		{
			VisTextButton button = new VisTextButton("OK");
			addHoverEffect(button);
			button.addListener(new ClickListener() {

				@Override
				public void clicked(final InputEvent event, final float x, final float y) {
					getGameEngine().context.restart = true;
					getGameEngine().context.SetGraphicsQuality((int) graphicsQualitySlider.getValue());
					getGameEngine().context.write();
					close();
				}
			});
			getTable().add(button).colspan(2).center().width(BUTTON_WIDTH * sizes.scaleFactor).pad(16);
		}
		{
			VisTextButton button = new VisTextButton("Cancel", "blue");
			addHoverEffect(button);
			button.addListener(new ClickListener() {

				@Override
				public void clicked(final InputEvent event, final float x, final float y) {
					close();
				}
			});
			getTable().add(button).colspan(2).center().width(BUTTON_WIDTH * sizes.scaleFactor).pad(16);
		}
	}

	private void createGraphcsQuality(Sizes sizes) {
		{
			VisLabel label = new VisLabel("Graphics Quality");
			label.setAlignment(Align.right);
			getTable().add(label).width(BUTTON_WIDTH * sizes.scaleFactor);
		}
		{
			graphicsQualitySlider = new VisSlider(0f, ApplicationProperties.MAX_GRAPHICS_QUALITY, 1f, false);
			graphicsQualitySlider.setValue(getGameEngine().context.getGraphicsQuality());
			graphicsQualitySlider.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					int value = (int) graphicsQualitySlider.getValue();
					if (value <= ApplicationProperties.MAX_GRAPHICS_QUALITY) {
						graphicsQualityLabel.setText(value);
						if (value < ApplicationProperties.MAX_GRAPHICS_QUALITY) {
							setMaxPointLights(getGameEngine().context.predefinedMaxPointLights[value]);
							setMaxSceneObjects(getGameEngine().context.predefinedMaxSceneObjects[value]);
						}
					} else {
						setMaxPointLights(getGameEngine().context.getMaxPointLights());
						setMaxSceneObjects(getGameEngine().context.getMaxSceneObjects());
					}
				}

			});
			getTable().add(graphicsQualitySlider).colspan(2).colspan(1).center().width(BUTTON_WIDTH * sizes.scaleFactor);
		}
		{
			graphicsQualityLabel = new VisLabel("" + getGameEngine().context.getGraphicsQuality());
			graphicsQualityLabel.setAlignment(Align.right);
			getTable().add(graphicsQualityLabel).width(100).center();
		}
	}

	private void createMaxPointLights(Sizes sizes) {
		{
			VisLabel label = new VisLabel("Max Point Lights");
			label.setAlignment(Align.right);
			getTable().add(label).width(BUTTON_WIDTH * sizes.scaleFactor);
		}
		{
			maxPointLightsSlider = new VisSlider(0f, 500f, 1f, false);
			maxPointLightsSlider.setDisabled(true);
			maxPointLightsSlider.setValue(getGameEngine().context.getMaxPointLights());
			maxPointLightsSlider.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					maxPointLightsLabel.setText((int) maxPointLightsSlider.getValue());
				}

			});
			getTable().add(maxPointLightsSlider).colspan(2).colspan(1).center().width(BUTTON_WIDTH * sizes.scaleFactor);
		}
		{
			maxPointLightsLabel = new VisLabel("" + getGameEngine().context.getMaxPointLights());
			maxPointLightsLabel.setAlignment(Align.right);
			getTable().add(maxPointLightsLabel).width(100).center();
		}
	}

	private void createMaxSceneObjects(Sizes sizes) {
		{
			VisLabel label = new VisLabel("Max Scene Objects");
			label.setAlignment(Align.right);
			getTable().add(label).width(BUTTON_WIDTH * sizes.scaleFactor);
		}
		{
			maxSceneObjectsSlider = new VisSlider(0f, 500f, 1f, false);
			maxSceneObjectsSlider.setDisabled(true);
			maxSceneObjectsSlider.setValue(getGameEngine().context.getMaxSceneObjects());
			maxSceneObjectsSlider.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					maxSceneObjectsLabel.setText((int) maxSceneObjectsSlider.getValue());
				}

			});
			getTable().add(maxSceneObjectsSlider).colspan(2).colspan(1).center().width(BUTTON_WIDTH * sizes.scaleFactor);
		}
		{
			maxSceneObjectsLabel = new VisLabel("" + getGameEngine().context.getMaxSceneObjects());
			maxSceneObjectsLabel.setAlignment(Align.right);
			getTable().add(maxSceneObjectsLabel).width(100).center();
		}
	}

	private void setGraphicsQuality(int graphicsQuality) {
		graphicsQualityLabel.setText("" + graphicsQuality);
		graphicsQualitySlider.setValue(graphicsQuality);
	}

	private void setMaxPointLights(int maxPointLights) {
		maxPointLightsLabel.setText("" + maxPointLights);
		maxPointLightsSlider.setValue(maxPointLights);
	}

	private void setMaxSceneObjects(int maxSceneObjects) {
		maxSceneObjectsLabel.setText("" + maxSceneObjects);
		maxSceneObjectsSlider.setValue(maxSceneObjects);
	}

}
