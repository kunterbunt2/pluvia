package com.abdalla.bushnaq.pluvia.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.abdalla.bushnaq.pluvia.desktop.Context;
import com.abdalla.bushnaq.pluvia.game.Game;
import com.abdalla.bushnaq.pluvia.game.Score;
import com.abdalla.bushnaq.pluvia.game.ScoreList;
import com.abdalla.bushnaq.pluvia.renderer.GameEngine;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.Sizes;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.VisUI.SkinScale;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;

public class ScoreDialog extends AbstractDialog {
	private long		changed;
	final String		pattern	= "yyyy-MMM-dd HH:mm";
	SimpleDateFormat	simpleDateFormat;
	Sizes				sizes	= VisUI.getSizes();

	public ScoreDialog(GameEngine gameEngine, final Batch batch, final InputMultiplexer inputMultiplexer) throws Exception {
		super(gameEngine, batch, inputMultiplexer);
		simpleDateFormat = new SimpleDateFormat(pattern);
		createStage("", true);
	}

	@Override
	protected void close() {
		pop();
	};

	@Override
	public void update(final Context universe) {
		if (changed != getGameEngine().context.getScoreList().getChanged()) {
			create();
		}
	}

	@Override
	public void create() {
		getTable().clear();
		{
			getTable().row();
			VisLabel label = new VisLabel("");
			getTable().add(label).pad(0, 16, 16, 16).center();
		}
		{
			VisLabel label = new VisLabel("High Score");
			label.setColor(LIGHT_BLUE_COLOR);
			label.setAlignment(Align.center);
			getTable().add(label).colspan(4).width(DIALOG_WIDTH * sizes.scaleFactor).pad(0, 16, 16, 16).center();
		}
		ScoreList scoreList = getGameEngine().context.getScoreList();

		for (Game game : getGameEngine().context.gameList) {
			int index = scoreList.getSize();
			if (scoreList.get(game.getName()) != null) {
				createHeader(game);
				for (Score score : scoreList.get(game.getName())) {
					getTable().row();
					addRow("" + score.getUserName(), "" + score.getScore(), "" + score.getSteps(), simpleDateFormat.format(new Date(score.getAbsoluteTime())));
					index--;
				}
				for (int i = 0; i < index; i++) {
					getTable().row();
					addRow("", "", "", "");
				}
			}
		}
		// close button
		{
			getTable().row();
			VisLabel label = new VisLabel("");
			getTable().add(label).pad(0, 16, 16, 16).center();
		}
		{
			VisTextButton button = new VisTextButton("Close", "blue");
			addHoverEffect(button);
			button.addListener(new ClickListener() {
				@Override
				public void clicked(final InputEvent event, final float x, final float y) {
					ScoreDialog.this.close();
				}
			});
			getTable().add(button).colspan(4).center().width(BUTTON_WIDTH * sizes.scaleFactor).pad(16);
		}
		changed = getGameEngine().context.getScoreList().getChanged();
	}

	private void createHeader(Game game) {
		getTable().row();
		{
			getTable().row();
			VisLabel label = new VisLabel(game.getName());
			label.setColor(LIGHT_BLUE_COLOR);
			label.setAlignment(Align.center);
			getTable().add(label).colspan(1).width(DIALOG_WIDTH * sizes.scaleFactor).pad(16, 16, 0, 16).center();
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
			VisLabel label = new VisLabel("Date");
			label.setAlignment(Align.right);
			getTable().add(label).width(100).center();
		}
	}

	private void addRow(String userName, String score, String steps, String date) {
		{
			VisLabel label = new VisLabel("");
			label.setAlignment(Align.left);
			getTable().add(label).width(100 * sizes.scaleFactor).left();
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
			VisTextField label = new VisTextField(date);
			label.setAlignment(Align.right);
			getTable().add(label).width(150 * sizes.scaleFactor).right();
		}
	}

}
