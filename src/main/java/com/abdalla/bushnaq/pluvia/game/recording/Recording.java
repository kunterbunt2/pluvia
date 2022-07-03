package com.abdalla.bushnaq.pluvia.game.recording;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abdalla.bushnaq.pluvia.engine.GameEngine;
import com.abdalla.bushnaq.pluvia.game.Game;
import com.abdalla.bushnaq.pluvia.game.GameDataObject;
import com.abdalla.bushnaq.pluvia.game.GamePhase;
import com.abdalla.bushnaq.pluvia.game.LevelManager;
import com.abdalla.bushnaq.pluvia.game.model.stone.Stone;

public class Recording {
	GameDataObject		gdo			= new GameDataObject();
	protected Logger	logger		= LoggerFactory.getLogger(this.getClass());
	private List<Frame>	recording	= new ArrayList<>();

	public Recording() {

	}

	public void addFrame(int x, int y, Interaction interaction) {
		recording.add(new Frame(x, y, interaction));
	}

	public void addFrame(Interaction interaction) {
		recording.add(new Frame(interaction));
	}

	public GameDataObject getGdo() {
		return gdo;
	}

	public List<Frame> getRecording() {
		return recording;
	}

	public void setGdo(GameDataObject gdo) {
		this.gdo = gdo;
	}

	public void setRecording(List<Frame> recording) {
		this.recording = recording;
	}

	public boolean testValidity(GameEngine gameEngine) {
		LevelManager levelManager = new LevelManager(null, (Game) gameEngine.context.game.clone());
		levelManager.setGameSeed(gdo.getSeed());
		levelManager.createLevel();
		if (((gdo.getSteps() == 0 || recording.size() == 0) && (gdo.getSteps() != recording.size())) || ((gdo.getSteps() != 0) && (gdo.getSteps() != recording.size() + 1))) {
			logger.error("Recording length does not match number of steps. Recording is not genuine.");
			return false;
		}

		for (Frame frame : recording) {
			do {
				if (levelManager.update()) {
					logger.error("Recording not fully played back, but game is already exited. Recording is not genuine.");
					return false;
				}
			} while (levelManager.gamePhase != GamePhase.waiting || (levelManager.animationPhase != 0));
			switch (frame.getInteraction()) {
			case left: {
				Stone stone = levelManager.getStone(frame.getX(), frame.getY());
				if (stone != null) {
					if (!levelManager.reactLeft(stone)) {
						logger.error("Recording is trying to move a stone to the left, but it cannot move. Recording is not genuine.");
						return false;
					}
					levelManager.animationPhase = 0;
				} else {
					logger.error("Recording is trying to access a stone that does not exist. Recording is not genuine.");
					return false;
				}
			}
				break;
			case right: {
				Stone stone = levelManager.getStone(frame.getX(), frame.getY());
				if (stone != null) {
					if (!levelManager.reactRight(stone)) {
						logger.error("Recording is trying to move a stone to the right, but it cannot move. Recording is not genuine.");
						return false;
					}
					levelManager.animationPhase = 0;
				} else {
					logger.error("Recording is trying to access a stone that does not exist. Recording is not genuine.");
					return false;
				}
			}
				break;
			case next: {
				levelManager.nextRound();
				levelManager.animationPhase = 0;
			}
				break;
			}
		}
		do {
			levelManager.update();
		} while (levelManager.gamePhase != GamePhase.waiting || (levelManager.animationPhase != 0));
		if (gdo.getScore() != levelManager.getScore()) {
			logger.error("Recording playback does not result in expected score. Recording is not genuine.");
			return false;
		}
		levelManager.destroy();
		return true;
	}

}
