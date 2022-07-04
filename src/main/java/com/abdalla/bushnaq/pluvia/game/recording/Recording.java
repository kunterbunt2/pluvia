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
	private static final String	INVALID_LEVEL_RECORDING_DETECTED							= "Invalid Level Recording Detected";
	private static final String	NOT_EXPECTED_SCORE											= "Recording playback does not result in expected score. Recording is not genuine.\n\nResetting level...";
	private static final String	RECORDING_IS_TRYING_TO_ACCESS_A_STONE_THAT_DOES_NOT_EXIST	= "Recording is trying to access a stone that does not exist. Recording is not genuine.\n\nResetting level...";
	private static final String	RECORDING_LENGTH_DOES_NOT_MATCH_NUMBER_OF_STEPS				= "Recording length does not match number of steps. Recording is not genuine.\n\nResetting level...";
	private static final String	RECORDING_TOO_LONG											= "Recording not fully played back, but game is already exited. Recording is not genuine.\n\nResetting level...";
	private static final String	STONE_CANNOT_MOVE_LEFT										= "Recording is trying to move a stone to the left, but it cannot move. Recording is not genuine.\n\nResetting level...";
	private static final String	STONE_CANNOT_MOVE_TO_THE_RIGHT								= "Recording is trying to move a stone to the right, but it cannot move. Recording is not genuine.\n\nResetting level...";
	GameDataObject				gdo															= new GameDataObject();
	protected Logger			logger														= LoggerFactory.getLogger(this.getClass());
	private List<Frame>			recording													= new ArrayList<>();

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
		if (((gdo.getSteps() == 0) && (gdo.getSteps() != recording.size())) || ((gdo.getSteps() != 0) && (gdo.getSteps() != recording.size() + 1))) {
			gameEngine.getMessageDialog().showModal(INVALID_LEVEL_RECORDING_DETECTED, RECORDING_LENGTH_DOES_NOT_MATCH_NUMBER_OF_STEPS);
			logger.error(RECORDING_LENGTH_DOES_NOT_MATCH_NUMBER_OF_STEPS);
			return false;
		}

		for (Frame frame : recording) {
			do {
				if (levelManager.update()) {
					gameEngine.getMessageDialog().showModal(INVALID_LEVEL_RECORDING_DETECTED, RECORDING_TOO_LONG);
					logger.error(RECORDING_TOO_LONG);
					return false;
				}
			} while (levelManager.gamePhase != GamePhase.waiting || (levelManager.animationPhase != 0));
			switch (frame.getInteraction()) {
			case left: {
				Stone stone = levelManager.getStone(frame.getX(), frame.getY());
				if (stone != null) {
					if (!levelManager.reactLeft(stone)) {
						gameEngine.getMessageDialog().showModal(INVALID_LEVEL_RECORDING_DETECTED, STONE_CANNOT_MOVE_LEFT);
						logger.error(STONE_CANNOT_MOVE_LEFT);
						return false;
					}
					levelManager.animationPhase = 0;
				} else {
					gameEngine.getMessageDialog().showModal(INVALID_LEVEL_RECORDING_DETECTED, RECORDING_IS_TRYING_TO_ACCESS_A_STONE_THAT_DOES_NOT_EXIST);
					logger.error(RECORDING_IS_TRYING_TO_ACCESS_A_STONE_THAT_DOES_NOT_EXIST);
					return false;
				}
			}
				break;
			case right: {
				Stone stone = levelManager.getStone(frame.getX(), frame.getY());
				if (stone != null) {
					if (!levelManager.reactRight(stone)) {
						gameEngine.getMessageDialog().showModal(INVALID_LEVEL_RECORDING_DETECTED, STONE_CANNOT_MOVE_TO_THE_RIGHT);
						logger.error(STONE_CANNOT_MOVE_TO_THE_RIGHT);
						return false;
					}
					levelManager.animationPhase = 0;
				} else {
					gameEngine.getMessageDialog().showModal(INVALID_LEVEL_RECORDING_DETECTED, RECORDING_IS_TRYING_TO_ACCESS_A_STONE_THAT_DOES_NOT_EXIST);
					logger.error(RECORDING_IS_TRYING_TO_ACCESS_A_STONE_THAT_DOES_NOT_EXIST);
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
			gameEngine.getMessageDialog().showModal(INVALID_LEVEL_RECORDING_DETECTED, NOT_EXPECTED_SCORE);
			logger.error(NOT_EXPECTED_SCORE);
			return false;
		}
		levelManager.destroy();
		return true;
	}

}
