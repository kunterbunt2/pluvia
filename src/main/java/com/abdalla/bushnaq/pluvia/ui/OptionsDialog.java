package com.abdalla.bushnaq.pluvia.ui;

import com.abdalla.bushnaq.pluvia.desktop.ApplicationProperties;
import com.abdalla.bushnaq.pluvia.engine.GameEngine;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.Sizes;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter;

public class OptionsDialog extends AbstractDialog {

	private VisLabel				graphicsQualityLabel;
	private VisSlider				graphicsQualitySlider;
	private VisSlider				maxPointLightsSlider;
	private VisLabel				maxPointLightsLabel;
	private VisSlider				maxSceneObjectsSlider;
	private VisLabel				maxSceneObjectsLabel;
	private VisCheckBox				pbrCheckBox;
	private VisCheckBox				showFpsCheckBox;
	private VisCheckBox				vsyncCheckBox;
	private VisCheckBox				fullScreenModeCheckBox;
	private VisCheckBox				debugModeCheckBox;
	private VisSlider				foregroundFpsSlider;
	private VisLabel				foregroundFpsLabel;
	private VisSelectBox<Integer>	shadowMapSizeSelectBox;
	private VisLabel				shadowMapSizeLabel;
	private VisLabel				msaaSamplesLabel;
	private VisSlider				msaaSamplesSlider;
	private VisSelectBox<Integer>	monitorSelectBox;
	private VisCheckBox				showGraphsCheckBox;
	private TabbedPane				tabbedPane	= new TabbedPane();
	private Sizes					sizes;
	private VisTable				mainTable	= new VisTable();
	Cell<?>							tableSpacerCell;				// used to measure height adn set height of total dialog

	private class TestTab extends Tab {
		private String	title;
		private Table	content;

		public TestTab(String title) {
			super(false, false);
			this.title = title;

			content = new VisTable();
		}

		@Override
		public String getTabTitle() {
			return title;
		}

		@Override
		public Table getContentTable() {
			return content;
		}
	}

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
		sizes = VisUI.getSizes();
		getTable().add(mainTable).top();
		tableSpacerCell = getTable().add();

		createCaption(mainTable);
		createTabButtons(mainTable);
		{
			Table table1 = tabbedPane.getTabs().get(0).getContentTable();
			createGraphcsQuality(table1);
			createMaxPointLights(table1);
			createMaxSceneObjects(table1);
			createMsaaSamples(table1);
			createShadowMapSize(table1);
		}
		{
			Table table2 = tabbedPane.getTabs().get(1).getContentTable();
			createShowFps(table2);
			createShowGraphs(table2);
			createPbr(table2);
			createDebugMode(table2);
			createVsync(table2);
			createFullScreenMode(table2);
			createForgroundFps(table2);
			createMonitor(table2);
		}
		createButtons(mainTable);

		calculateBestTableHight();
	}

	/**
	 * in the tabbed panel, every panel has a different height we need to find the maximum and then set the spacer cell to that size
	 */
	private void calculateBestTableHight() {
		dialogHeight = Math.max(dialogHeight, getTable().getHeight());
		tabbedPane.switchTab(0);
		dialogHeight = Math.max(dialogHeight, getTable().getHeight());
		tableSpacerCell.height(dialogHeight);
	}

	float dialogHeight = 0;

	private void createTabButtons(VisTable table) {
		table.add(tabbedPane.getTable()).colspan(2).expandX().fillX();
		tabbedPane.addListener(new TabbedPaneAdapter() {
			@Override
			public void switchedTab(Tab tab) {
				container.clearChildren();
				container.add(tab.getContentTable()).expand().fill();
			}
		});
		tabbedPane.add(new TestTab("Graphics Quality"));
		tabbedPane.add(new TestTab("Graphics"));
		table.row().pad(16);
		table.add(container);
		table.row().pad(16);
	}

	private void createCaption(VisTable table) {
		table.row();
		VisLabel label = new VisLabel("Options");
		label.setColor(LIGHT_BLUE_COLOR);
		label.setAlignment(Align.center);
		table.add(label).width(DIALOG_WIDTH * sizes.scaleFactor).pad(0, 16, 16, 16).center();
		table.row().pad(16);
	}

	private void createButtons(VisTable table) {
		VisTable t = new VisTable();
		table.add(t);
		{
			VisTextButton button = new VisTextButton("OK");
			addHoverEffect(button);
			button.addListener(new ClickListener() {

				@Override
				public void clicked(final InputEvent event, final float x, final float y) {
					getGameEngine().context.restart = true;
					getGameEngine().context.SetGraphicsQuality((int) graphicsQualitySlider.getValue());
					getGameEngine().context.setPbr(pbrCheckBox.isChecked());
					getGameEngine().context.setShowFps(showFpsCheckBox.isChecked());
					getGameEngine().context.setVsync(vsyncCheckBox.isChecked());
					getGameEngine().context.setFullScreenMode(fullScreenModeCheckBox.isChecked());
					getGameEngine().context.setDebugMode(debugModeCheckBox.isChecked());
					getGameEngine().context.setForegroundFps((int) foregroundFpsSlider.getValue());
					getGameEngine().context.setMonitor(monitorSelectBox.getSelected());
					getGameEngine().context.setShowGraphs(showGraphsCheckBox.isChecked());
					if (isCustomMode()) {
						getGameEngine().context.setMaxPointLights((int) maxPointLightsSlider.getValue());
						getGameEngine().context.setMaxSceneObjects((int) maxSceneObjectsSlider.getValue());
						getGameEngine().context.setShadowMapSize(shadowMapSizeSelectBox.getSelected());
						getGameEngine().context.setMsaaSamples((int) msaaSamplesSlider.getValue());
					} else {
					}
					getGameEngine().context.write();
					close();
				}
			});
			t.add(button).center().width(BUTTON_WIDTH * sizes.scaleFactor).pad(16);
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
			t.add(button).center().width(BUTTON_WIDTH * sizes.scaleFactor).pad(16);
		}
		getTable().pack();
	}

	final VisTable container = new VisTable();

	private void createGraphcsQuality(Table table) {
		table.row().pad(16);
		{
			VisLabel label = new VisLabel("Graphics Quality");
			label.setAlignment(Align.right);
			table.add(label).width(LABEL_WIDTH * sizes.scaleFactor);
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
							setShadowMapSize(getGameEngine().context.predefinedShadowMapSize[value]);
							setMsaaSamples(getGameEngine().context.predefinedMssaSamples[value]);
							maxPointLightsSlider.setDisabled(true);
							maxSceneObjectsSlider.setDisabled(true);
							shadowMapSizeSelectBox.setDisabled(true);
							msaaSamplesSlider.setDisabled(true);
						} else {
							maxPointLightsSlider.setDisabled(false);
							maxSceneObjectsSlider.setDisabled(false);
							shadowMapSizeSelectBox.setDisabled(false);
							msaaSamplesSlider.setDisabled(false);
						}
					} else {
					}
				}

			});
			table.add(graphicsQualitySlider).center().width(BUTTON_WIDTH * sizes.scaleFactor);
		}
		{
			graphicsQualityLabel = new VisLabel("" + getGameEngine().context.getGraphicsQuality());
			graphicsQualityLabel.setAlignment(Align.right);
			table.add(graphicsQualityLabel).width(100).center();
		}
		table.row().pad(16);
	}

	private void createMaxPointLights(Table table) {
		{
			VisLabel label = new VisLabel("Max Point Lights");
			label.setAlignment(Align.right);
			table.add(label).width(LABEL_WIDTH * sizes.scaleFactor);
		}
		{
			maxPointLightsSlider = new VisSlider(0f, 500f, 1f, false);
			if (!isCustomMode())
				maxPointLightsSlider.setDisabled(true);
			maxPointLightsSlider.setValue(getGameEngine().context.getMaxPointLights());
			maxPointLightsSlider.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					maxPointLightsLabel.setText((int) maxPointLightsSlider.getValue());
				}

			});
			table.add(maxPointLightsSlider).center().width(BUTTON_WIDTH * sizes.scaleFactor);
		}
		{
			maxPointLightsLabel = new VisLabel("" + getGameEngine().context.getMaxPointLights());
			maxPointLightsLabel.setAlignment(Align.right);
			table.add(maxPointLightsLabel).width(100).center();
		}
		table.row().pad(16);
	}

	private void createMsaaSamples(Table table) {
		{
			VisLabel label = new VisLabel("Multisample Anti-Aliasing Samples");
			label.setAlignment(Align.right);
			table.add(label).width(LABEL_WIDTH * sizes.scaleFactor);
		}
		{
			msaaSamplesSlider = new VisSlider(0f, 16f, 1f, false);
			if (!isCustomMode())
				msaaSamplesSlider.setDisabled(true);
			msaaSamplesSlider.setValue(getGameEngine().context.getMSAASamples());
			msaaSamplesSlider.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					msaaSamplesLabel.setText((int) msaaSamplesSlider.getValue());
				}

			});
			table.add(msaaSamplesSlider).center().width(BUTTON_WIDTH * sizes.scaleFactor);
		}
		{
			msaaSamplesLabel = new VisLabel("" + getGameEngine().context.getMSAASamples());
			msaaSamplesLabel.setAlignment(Align.right);
			table.add(msaaSamplesLabel).width(100).center();
		}
		table.row().pad(16);
	}

	private void createShadowMapSize(Table table) {
		{
			VisLabel label = new VisLabel("Shadow Map Size");
			label.setAlignment(Align.right);
			table.add(label).width(LABEL_WIDTH * sizes.scaleFactor);
		}
		{
			shadowMapSizeSelectBox = new VisSelectBox<Integer>();
			shadowMapSizeSelectBox.setItems(1024, 2048, 4096, 8192);
			shadowMapSizeSelectBox.setSelected(getGameEngine().context.getShadowMapSizeProperty());
			if (!isCustomMode()) {
				shadowMapSizeSelectBox.setDisabled(true);
			}
			shadowMapSizeSelectBox.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					shadowMapSizeLabel.setText(shadowMapSizeSelectBox.getSelected());
				}
			});
			table.add(shadowMapSizeSelectBox).center().width(BUTTON_WIDTH * sizes.scaleFactor);
		}
		{
			shadowMapSizeLabel = new VisLabel("" + getGameEngine().context.getShadowMapSizeProperty());
			shadowMapSizeLabel.setAlignment(Align.right);
			table.add(shadowMapSizeLabel).width(100).center();
		}
		table.row().pad(16);
	}

	private void createForgroundFps(Table table) {
		{
			VisLabel label = new VisLabel("Foreground Frames Per Second");
			label.setAlignment(Align.right);
			table.add(label).width(LABEL_WIDTH * sizes.scaleFactor);
		}
		{
			foregroundFpsSlider = new VisSlider(25f, 1000f, 1f, false);
			foregroundFpsSlider.setValue(getGameEngine().context.getForegroundFPSProperty());
			foregroundFpsSlider.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					foregroundFpsLabel.setText((int) foregroundFpsSlider.getValue());
				}

			});
			table.add(foregroundFpsSlider).center().width(BUTTON_WIDTH * sizes.scaleFactor);
		}
		{
			foregroundFpsLabel = new VisLabel("" + getGameEngine().context.getForegroundFPSProperty());
			foregroundFpsLabel.setAlignment(Align.right);
			table.add(foregroundFpsLabel).width(100).center();
		}
		table.row().pad(16);
	}

	private void createMonitor(Table table) {
		{
			VisLabel label = new VisLabel("Monitor");
			label.setAlignment(Align.right);
			table.add(label).width(LABEL_WIDTH * sizes.scaleFactor);
		}
		{
			monitorSelectBox = new VisSelectBox<Integer>();
			final Monitor[]	monitors	= Lwjgl3ApplicationConfiguration.getMonitors();
			Integer[]		values		= new Integer[monitors.length];
			for (int i = 0; i < monitors.length; i++) {
				values[i] = i;
			}

			monitorSelectBox.setItems(values);
			monitorSelectBox.setSelected(getGameEngine().context.getMonitorProperty());
			table.add(monitorSelectBox).colspan(2).left().width(BUTTON_WIDTH * sizes.scaleFactor);
		}
		table.row().pad(16);
	}

	private boolean isCustomMode() {
		return (int) graphicsQualitySlider.getValue() == ApplicationProperties.MAX_GRAPHICS_QUALITY;
	}

	private void createMaxSceneObjects(Table table) {
		{
			VisLabel label = new VisLabel("Max Scene Objects");
			label.setAlignment(Align.right);
			table.add(label).width(LABEL_WIDTH * sizes.scaleFactor);
		}
		{
			maxSceneObjectsSlider = new VisSlider(0f, 500f, 1f, false);
			if (!isCustomMode())
				maxSceneObjectsSlider.setDisabled(true);
			maxSceneObjectsSlider.setValue(getGameEngine().context.getMaxSceneObjects());
			maxSceneObjectsSlider.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					maxSceneObjectsLabel.setText((int) maxSceneObjectsSlider.getValue());
				}

			});
			table.add(maxSceneObjectsSlider).center().width(BUTTON_WIDTH * sizes.scaleFactor);
		}
		{
			maxSceneObjectsLabel = new VisLabel("" + getGameEngine().context.getMaxSceneObjects());
			maxSceneObjectsLabel.setAlignment(Align.right);
			table.add(maxSceneObjectsLabel).width(100).center();
		}
		table.row().pad(16);
	}

	private void createPbr(Table table) {
		{
			VisLabel label = new VisLabel("Physical Based Rendering");
			label.setAlignment(Align.right);
			table.add(label).width(LABEL_WIDTH * sizes.scaleFactor);
		}
		{
			pbrCheckBox = new VisCheckBox("", getGameEngine().context.getPbrModeProperty());
			table.add(pbrCheckBox).colspan(2).left();
		}
		table.row().pad(16);
	}

	private void createShowFps(Table table) {
		table.row().pad(16);
		{
			VisLabel label = new VisLabel("Show Frames Per Second");
			label.setAlignment(Align.right);
			table.add(label).width(LABEL_WIDTH * sizes.scaleFactor);
		}
		{
			showFpsCheckBox = new VisCheckBox("", getGameEngine().context.getShowFpsProperty());
			table.add(showFpsCheckBox).colspan(2).left();
		}
		table.row().pad(16);
	}

	private void createVsync(Table table) {
		{
			VisLabel label = new VisLabel("Vertical Synchronization");
			label.setAlignment(Align.right);
			table.add(label).width(LABEL_WIDTH * sizes.scaleFactor);
		}
		{
			vsyncCheckBox = new VisCheckBox("", getGameEngine().context.getVsyncProperty());
			table.add(vsyncCheckBox).colspan(2).left();
		}
		table.row().pad(16);
	}

	private void createFullScreenMode(Table table) {
		{
			VisLabel label = new VisLabel("Full Screen Mode");
			label.setAlignment(Align.right);
			table.add(label).width(LABEL_WIDTH * sizes.scaleFactor);
		}
		{
			fullScreenModeCheckBox = new VisCheckBox("", getGameEngine().context.getFullscreenModeProperty());
			table.add(fullScreenModeCheckBox).colspan(2).left();
		}
		table.row().pad(16);
	}

	private void createDebugMode(Table table) {
		{
			VisLabel label = new VisLabel("Debug Mode");
			label.setAlignment(Align.right);
			table.add(label).width(LABEL_WIDTH * sizes.scaleFactor);
		}
		{
			debugModeCheckBox = new VisCheckBox("", getGameEngine().context.getDebugModeProperty());
			table.add(debugModeCheckBox).colspan(2).left();
		}
		table.row().pad(16);
	}

	private void createShowGraphs(Table table) {
		{
			VisLabel label = new VisLabel("Show CPU/GPU Time Graphs");
			label.setAlignment(Align.right);
			table.add(label).width(LABEL_WIDTH * sizes.scaleFactor);
		}
		{
			showGraphsCheckBox = new VisCheckBox("", getGameEngine().context.getShowGraphsProperty());
			table.add(showGraphsCheckBox).colspan(2).left();
		}
		table.row().pad(16);
	}

	private void setGraphicsQuality(int graphicsQuality) {
		graphicsQualityLabel.setText("" + graphicsQuality);
		graphicsQualitySlider.setValue(graphicsQuality);
	}

	private void setMaxPointLights(int maxPointLights) {
		maxPointLightsLabel.setText("" + maxPointLights);
		maxPointLightsSlider.setValue(maxPointLights);
	}

	private void setMsaaSamples(int value) {
		msaaSamplesLabel.setText("" + value);
		msaaSamplesSlider.setValue(value);
	}

	private void setMaxSceneObjects(int maxSceneObjects) {
		maxSceneObjectsLabel.setText("" + maxSceneObjects);
		maxSceneObjectsSlider.setValue(maxSceneObjects);
	}

	private void setShadowMapSize(int value) {
		shadowMapSizeLabel.setText("" + value);
		shadowMapSizeSelectBox.setSelected(value);
	}

}
