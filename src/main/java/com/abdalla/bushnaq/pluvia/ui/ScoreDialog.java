package com.abdalla.bushnaq.pluvia.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.abdalla.bushnaq.pluvia.desktop.Context;
import com.abdalla.bushnaq.pluvia.engine.GameEngine;
import com.abdalla.bushnaq.pluvia.game.Score;
import com.abdalla.bushnaq.pluvia.game.ScoreList;
import com.abdalla.bushnaq.pluvia.util.TimeUnit;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.Sizes;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;

public class ScoreDialog extends AbstractDialog {
	Map<VisTextButton, Score>	buttonToScoreMap	= new HashMap<>();
	private long				changed;
	final String				pattern				= "yyyy-MMM-dd HH:mm";
	SimpleDateFormat			simpleDateFormat;

	Sizes						sizes				= VisUI.getSizes();

	public ScoreDialog(GameEngine gameEngine, final Batch batch, final InputMultiplexer inputMultiplexer) throws Exception {
		super(gameEngine, batch, inputMultiplexer);
		simpleDateFormat = new SimpleDateFormat(pattern);
		createStage("", true);
	}

	private void addRow(Score s) {

		String	gameName	= "" + s.getGame();
		String	seed		= "" + s.getSeed();
		String	userName	= "" + s.getUserName();
		String	score		= "" + s.getScore();
		String	steps		= "" + s.getSteps();
		String	time		= TimeUnit.createDurationString(s.getRelativeTime(), true, true, false);
		String	date		= simpleDateFormat.format(new Date(s.getTime()));
		{
			VisTextField label = new VisTextField(gameName);
			label.setAlignment(Align.left);
			getTable().add(label).width(100 * sizes.scaleFactor).left();
		}
		{
			VisTextField label = new VisTextField(seed);
			label.setReadOnly(true);
			label.setAlignment(Align.right);
			getTable().add(label).width(50 * sizes.scaleFactor).right();
		}
		{
			VisTextField label = new VisTextField(userName);
			label.setAlignment(Align.left);
			getTable().add(label).width(100 * sizes.scaleFactor).left();
		}
		{
			VisTextField label = new VisTextField(score);
			label.setReadOnly(true);
			label.setAlignment(Align.right);
			getTable().add(label).width(50 * sizes.scaleFactor).right();
		}
		{
			VisTextField label = new VisTextField(steps);
			label.setAlignment(Align.right);
			getTable().add(label).width(50 * sizes.scaleFactor).right();
		}
		{
			VisTextField label = new VisTextField(time);
			label.setAlignment(Align.right);
			getTable().add(label).width(100 * sizes.scaleFactor).right();
		}
		{
			VisTextField label = new VisTextField(date);
			label.setAlignment(Align.right);
			getTable().add(label).width(150 * sizes.scaleFactor).right();
		}
		{
			VisTextButton button = new VisTextButton("Replay", "blue");
			buttonToScoreMap.put(button, s);
			addHoverEffect(button);
			button.addListener(new ClickListener() {
				@Override
				public void clicked(final InputEvent event, final float x, final float y) {
					logger.info("replay");
					Score buttonScore = buttonToScoreMap.get(button);
					setVisible(false);
					createGame(getGameEngine().context.getGameIndex(buttonScore.getGame()), false, buttonScore.getSeed());
				}
			});
			getTable().add(button).width(BUTTON_WIDTH * sizes.scaleFactor);
		}
	}

	@Override
	public void create() {
		getTable().clear();
		{
			getTable().row();
//			VisLabel label = new VisLabel("");
//			getTable().add(label).pad(0, 16, 16, 16).center();
		}
		{
			VisLabel label = new VisLabel("High Score");
			label.setColor(LIGHT_BLUE_COLOR);
			label.setAlignment(Align.center);
			getTable().add(label).colspan(8).width(DIALOG_WIDTH * sizes.scaleFactor).pad(0, 16, 16, 16).center();
		}
		createHeader();
		ScoreList scoreList = getGameEngine().context.getScoreList();
		for (Score score : scoreList) {
			getTable().row();
			addRow(score);
		}
		// close button
		{
			getTable().row();
//			VisLabel label = new VisLabel("");
//			getTable().add(label).pad(0, 16, 16, 16).center();
		}
		{
			VisTextButton button = new VisTextButton("Close", "blue");
			addHoverEffect(button);
			button.addListener(new ClickListener() {
				@Override
				public void clicked(final InputEvent event, final float x, final float y) {
					close();
				}
			});
			getTable().add(button).colspan(8).center().width(BUTTON_WIDTH * sizes.scaleFactor).pad(16);
		}
		changed = getGameEngine().context.getScoreList().getChanged();
	}

	private void createHeader() {
		getTable().row();
		{
			getTable().row();
			VisLabel label = new VisLabel("Game");
//			label.setColor(LIGHT_BLUE_COLOR);
			label.setAlignment(Align.center);
			getTable().add(label).width(100).center();
		}
		{
			VisLabel label = new VisLabel("Level");
			label.setAlignment(Align.right);
			getTable().add(label).width(100).center();
		}
		{
			VisLabel label = new VisLabel("User Name");
			label.setAlignment(Align.left);
			getTable().add(label).width(200).center();
		}
		{
			VisLabel label = new VisLabel("Score");
			label.setAlignment(Align.right);
			getTable().add(label).width(100).center();
		}
		{
			VisLabel label = new VisLabel("Steps");
			label.setAlignment(Align.right);
			getTable().add(label).width(100).center();
		}
		{
			VisLabel label = new VisLabel("Time");
			label.setAlignment(Align.right);
			getTable().add(label).width(100).center();
		}
		{
			VisLabel label = new VisLabel("Date");
			label.setAlignment(Align.right);
			getTable().add(label).width(100).center();
		}
		{
			VisLabel label = new VisLabel("Replay");
			label.setAlignment(Align.right);
			getTable().add(label).width(100).center();
		}
	}

	@Override
	public void update(final Context universe) {
		if (changed != getGameEngine().context.getScoreList().getChanged()) {
			create();
		}
	}

}
