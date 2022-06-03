/*
 * Created on 10.07.2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package com.abdalla.bushnaq.pluvia.game;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abdalla.bushnaq.pluvia.desktop.Context;
import com.abdalla.bushnaq.pluvia.engine.AtlasManager;
import com.abdalla.bushnaq.pluvia.game.model.stone.Stone;
import com.abdalla.bushnaq.pluvia.util.RcBoolean;
import com.abdalla.bushnaq.pluvia.util.sound.Tools;

public abstract class Level {
	public int			animationPhase			= 0;
	Set<Stone>			droppingStones			= new HashSet<>();
	Set<Stone>			droppingStonesBuffer	= new HashSet<>();
	protected Game		game					= null;
	public GamePhase	gamePhase				= GamePhase.waiting;
	public int			height					= 0;										// number of rows in the level
	protected Logger	logger					= LoggerFactory.getLogger(this.getClass());
	public int			maxAnimaltionPhase		= 12;
	protected int		nrOfFallenRows			= 0;
	protected int		nrOfFallingStones		= 0;										// Number of stones that can drop simultanuously from top
	protected int		nrOfStones				= 0;										// Number of different patches (colors) in the game
	protected int		NrOfTotalStones			= 0;										// The sum of all follen patches within this game
	protected Stone[][]	patch					= null;
	protected int		preview					= 0;										// Number of rows that the user can preview before they actually drop into the game
	Set<Stone>			pushingLeftStones		= new HashSet<>();
	Set<Stone>			pushingRightStones		= new HashSet<>();
	protected Random	rand					= null;
	private long		randomSeed				= System.currentTimeMillis();				// System.currentTimeMillis();
	private boolean		tilt					= false;									// mark that game has finished
	private boolean		userReacted				= false;									// user has either moved a stones left or right and we need to generate new stones.
	public int			width					= 0;										// number of columns

	public Level(Game game) {
		this.width = game.nrOfColumns;
		this.height = game.nrOfRows;
		this.preview = game.preview;
		this.nrOfFallingStones = game.nrOfFallingStones;
		this.nrOfStones = game.nrOfStones;
		this.nrOfFallenRows = game.nrOfFallenRows;
		this.game = game;
		patch = new Stone[width][];
		for (int x = 0; x < patch.length; x++) {
			patch[x] = new Stone[height];
		}
		rand = new Random(randomSeed);
		game.reset();
		game.setReset(false);
	}

	public int getSteps() {
		return game.steps;
	}

	protected boolean clearCommandAttributes() {
		boolean somethingHasChanged = false;
		for (int y = height - 1; y >= 0; y--) {
			for (int x = 0; x < width; x++) {
				if (patch[x][y] != null) {
					if (patch[x][y].clearCommandAttributes())
						somethingHasChanged = true;
				} else {
				}
			}
		}
		return somethingHasChanged;
	}

	protected boolean clearPuchingAttributes() {
		boolean somethingHasChanged = false;
		for (int y = height - 1; y >= 0; y--) {
			for (int x = 0; x < width; x++) {
				if (patch[x][y] != null) {
					if (patch[x][y].clearPushingAttributes())
						somethingHasChanged = true;
				} else {
				}
			}
		}
		return somethingHasChanged;
	}

	protected void clearTemporaryAttributes() {
		for (int y = height - 1; y >= 0; y--) {
			for (int x = 0; x < width; x++) {
				if (patch[x][y] != null) {
					patch[x][y].clearTemporaryAttributes();
				} else {
				}
			}
		}
	}

	public void createLevel() {
		// if this is a level that already (resumed game) has stones, we do nto fill it and we do nto generate stones right at thestart
		if (queryHeapHeight() == 0) {
			fillLevel();
			generateStones();
		}
		createLevelBackground();
	}

	abstract void createLevelBackground();

	protected Stone createStoneAndUpdateScore(int x, int y, int type) {
		game.addStoneScore();
		return createStone(x, y, type);
	}

	protected abstract Stone createStone(int x, int y, int type);

	public abstract void disposeLevel();

	protected void dropStones() {
		for (int y = height - 2; y >= preview; y--) {
			for (int x = 0; x < width; x++) {
				if ((patch[x][y] != null) && patch[x][y].isCanDrop() /* && !Patch[x][y].isVanishing() */) {
					patch[x][y].y++;
					patch[x][y + 1] = patch[x][y];
					patch[x][y + 1].setDropping(true);
					patch[x][y] = null;
				}
			}
		}
	}

	public void fillLevel() {
		for (int y = 0; y < nrOfFallenRows; y++) {
			for (int x = 0; x < width; x++) {
				patch[x][height - 1 - y] = createStoneAndUpdateScore(x, height - 1 - y, rand.nextInt(nrOfStones));
				NrOfTotalStones++;
			}
			// GenerateStones();
		}
		boolean Changed = false;
	}

	public void generateStones() {
		for (int y = preview; y > 0; y--) {
			for (int x = 0; x < width; x++) {
				if (patch[x][y - 1] != null) {
					patch[x][y] = patch[x][y - 1];
					patch[x][y - 1] = null;
				}
			}
		}
		for (int i = 0; i < nrOfFallingStones; i++) {
			int location = rand.nextInt(width);
			if (patch[location][0] == null) {
				patch[location][0] = createStoneAndUpdateScore(location, 0, rand.nextInt(nrOfStones));
				NrOfTotalStones++;
			}
		}
		game.steps++;
	}

	protected String getLastGameName() {
		return "save/" + game.getUserName() + "/" + game.getName() + ".xml";
	}

//	public int getScore() {
//		if (queryHeapHeight() == 0)
//			return game.getScore();
//		return -1;
//	}

	protected boolean isUserReacted() {
		return userReacted;
	}

	public void markMoveLeftOption() {
		// ---Stones of same type on top of each other should vanish
//		clearTemporaryAttributes();
		RcBoolean changedOnce = new RcBoolean(false);
		do {
			changedOnce.setFalse();
			for (int y = height - 1; y >= preview; y--) {
				for (int x = 0; x < width; x++) {
					if (patch[x][y] != null) {
						markStoneMoveLeftOption(changedOnce, x, y);
					}
				}
			}
		} while (changedOnce.getBooleanValue());
	}

	public void markMoveRightOption() {
		// ---Stones of same type on top of each other should vanish
//		clearTemporaryAttributes();
		RcBoolean changedOnce = new RcBoolean(false);
		do {
			changedOnce.setFalse();
			for (int y = height - 1; y >= preview; y--) {
				for (int x = 0; x < width; x++) {
					if (patch[x][y] != null) {
						markStoneMoveRightOption(changedOnce, x, y);
					}
				}
			}
		} while (changedOnce.getBooleanValue());
	}

	protected void markStickyPatches() {
		for (int y = preview; y < height; y++) {
			for (int x = 0; x < width; x++) {
				boolean	stickyLeft	= false;
				boolean	stickyRight	= false;
				// int attribute =
				// (RcPatch.ATTRIBUTE_DROPPING|RcPatch.ATTRIBUTE_MOVINGLEFT|RcPatch.ATTRIBUTE_MOVINGRIGHT)
				// ;
				// ---CHECK FOR STICKY STONES
				if (patch[x][y] != null) {
					if ((x < width - 1) && (patch[x + 1][y] != null) && (patch[x][y].getType() == patch[x + 1][y].getType()))
//						if ((Patch[x][y].isDropping() == Patch[x + 1][y].isDropping())
//								&& (Patch[x][y].isMovingRight() == Patch[x + 1][y].isMovingRight())
//								&& (Patch[x][y].isMovingLeft() == Patch[x + 1][y].isMovingLeft()))
						stickyRight = true;
					if ((x > 0) && (patch[x - 1][y] != null) && (patch[x][y].getType() == patch[x - 1][y].getType()))
//						if ((Patch[x][y].isDropping() == Patch[x - 1][y].isDropping())
//								&& (Patch[x][y].isMovingRight() == Patch[x - 1][y].isMovingRight())
//								&& (Patch[x][y].isMovingLeft() == Patch[x - 1][y].isMovingLeft()))
						stickyLeft = true;
					if (stickyRight && !patch[x][y].isRightAttached()) {
						Tools.play(AtlasManager.ASSETS_FOLDER+"/sound/sticky.wav");
						patch[x][y].setRightAttached(true);
					}
					if (!stickyRight && patch[x][y].isRightAttached()) {
						patch[x][y].setRightAttached(false);
					}
					if (stickyLeft && !patch[x][y].isLeftAttached()) {
						Tools.play(AtlasManager.ASSETS_FOLDER+"/sound/sticky.wav");
						patch[x][y].setLeftAttached(true);
					}
					if (!stickyLeft && patch[x][y].isLeftAttached()) {
						patch[x][y].setLeftAttached(false);
					}
				}
			}
		}
	}

	protected void markStoneDroppingOption(RcBoolean aThereWasAChange, int x, int y) {
		// ---CHECK IF WE CANNOT DROP
		boolean cannotDrop = false;
		// ---IF WE CAN VANISH, WE CANNOT DROP OR MOVE!
		if (patch[x][y].isVanishing())
			cannotDrop = true;

		// ---WE ARE At THE BOTTOM
		else if (y == height - 1)
			cannotDrop = true;

		// ---THERE IS A STONE UNDER US THAT CANNOT DROP
		else if ((patch[x][y + 1] != null) && patch[x][y + 1].isCannotDrop())
			cannotDrop = true;

		// ---THERE IS A STICKY STONE LEFT FROM US THAT CANNOT DROP
		else if ((x != 0) && (patch[x - 1][y] != null) && (patch[x][y].getType() == patch[x - 1][y].getType()) && patch[x - 1][y].isCannotDrop())
			cannotDrop = true;
		// ---THERE IS A STICKY STONE RIGHT FROM US THAT CANNOT DROP
		else if ((x != width - 1) && (patch[x + 1][y] != null) && (patch[x][y].getType() == patch[x + 1][y].getType()) && patch[x + 1][y].isCannotDrop())
			cannotDrop = true;
		if (cannotDrop && !patch[x][y].isCannotDrop()) {
			aThereWasAChange.setTrue();
			patch[x][y].setCannotDrop(true);
			droppingStones.remove(patch[x][y]);
			if (patch[x][y].isCanDrop()) {
				patch[x][y].setCanDrop(false);
			}
		}
		if (!cannotDrop && patch[x][y].isCannotDrop()) {
			aThereWasAChange.setTrue();
			patch[x][y].setCannotDrop(false);
		}
		// ---CHECK IF WE CAN DROP
		if (!cannotDrop) {
			// ---THERE IS SPACE FOR DROPPING OR A DROPPING STONE UNDER US
			if ((y != height - 1) && ((patch[x][y + 1] == null) || patch[x][y + 1].isCanDrop())) {
				// ---THERE IS NO STICKY STONE LEFT FROM US OR IT CAN DROP TOO
				if ((x == 0) || (patch[x - 1][y] == null) || (patch[x][y].getType() != patch[x - 1][y].getType()) || !patch[x - 1][y].isCannotDrop()) {
					// ---THERE IS NO STICKY STONE RIGHT FROM US OR IT CAN DROP TOO
					if ((x == width - 1) || (patch[x + 1][y] == null) || (patch[x][y].getType() != patch[x + 1][y].getType()) || !patch[x + 1][y].isCannotDrop()) {
//						if (x == 1 && y == 5)
//							logger.info(String.format("1=%b 2=%b 3=%b", (x == width - 1) || (patch[x + 1][y] == null), (patch[x + 1][y] != null) && (patch[x][y].getType() != patch[x + 1][y].getType()),(patch[x + 1][y] != null) && !patch[x + 1][y].isCannotDrop()));
						if (!patch[x][y].isCanDrop()) {
							patch[x][y].setCanDrop(true);
							droppingStones.add(patch[x][y]);
							aThereWasAChange.setTrue();
						} else {
							droppingStones.add(patch[x][y]);
						}
					} else
						logger.error("3");
				} else
					logger.error("2");
			} else
				logger.error("1");
		}
	}

	protected void markStoneMoveLeftOption(RcBoolean aThereWasAChange, int x, int y) {
		// ---CHECK IF WE CANNOT MOVE
		boolean notFree = false;
		// ---IF WE CAN DROP OR VANISH, WE CANNOT MOVE!
		if (patch[x][y].isVanishing())
			notFree = true;
		else if (patch[x][y].isCanDrop())
			notFree = true;
		// ---CHECK IF WE CANNOT MOVE LEFT
		else if ((x == 0) || ((patch[x - 1][y] != null) && patch[x - 1][y].isCannotMoveLeft()))
			notFree = true;
		if (notFree && !patch[x][y].isCannotMoveLeft()) {
			patch[x][y].setCannotMoveLeft(true);
			pushingLeftStones.remove(patch[x][y]);
			patch[x][y].setCanMoveLeft(false);
			aThereWasAChange.setTrue();
		} else if (!notFree && patch[x][y].isCannotMoveLeft()) {
			patch[x][y].setCannotMoveLeft(false);
			aThereWasAChange.setTrue();
		}
		{
			boolean isfree = false;
			// ---CHECK IF WE CAN MOVE LEFT
			{
				if (!notFree)
					if ((x != 0) && ((patch[x - 1][y] == null) || patch[x - 1][y].isCanMoveLeft()))
						isfree = true;
				if (isfree && !patch[x][y].isCanMoveLeft()) {
					patch[x][y].setCanMoveLeft(true);
					aThereWasAChange.setTrue();
				}
				if (!isfree && patch[x][y].isCanMoveLeft()) {
					patch[x][y].setCanMoveLeft(false);
					aThereWasAChange.setTrue();
				}
			}
			// ---INHERIT OUR PUSH TO THE LEFT STONE AND STICKING RIGHT ONE
			if (isfree) {
				if (patch[x][y].isPushingLeft()) {
					pushingLeftStones.add(patch[x][y]);
					if ((x != 0) && (patch[x - 1][y] != null) && !patch[x - 1][y].isPushingLeft()) {
						patch[x - 1][y].setPushingLeft(true);
						pushingLeftStones.add(patch[x - 1][y]);
						aThereWasAChange.setTrue();
					}
					if ((x != width - 1) && (patch[x + 1][y] != null) && (patch[x][y].getType() == patch[x + 1][y].getType()) && !patch[x + 1][y].isPushingLeft()) {
						patch[x + 1][y].setPushingLeft(true);
						pushingLeftStones.add(patch[x + 1][y]);
						aThereWasAChange.setTrue();
					}
				}
			}
		}
	}

	protected void markStoneMoveRightOption(RcBoolean aThereWasAChange, int x, int y) {
//		boolean canMove = false;
		// ---CHECK IF WE CANNOT MOVE
		boolean notFree = false;
		// ---IF WE CAN VANISH, WE CANNOT DROP OR MOVE!
		if (patch[x][y].isVanishing())
			notFree = true;
		// ---IF WE CAN DROP OR VANISH, WE CANNOT MOVE!
		else if (patch[x][y].isCanDrop())
			notFree = true;
		// ---CHECK IF WE CANNOT MOVE RIGHT
		else if ((x == width - 1) || ((patch[x + 1][y] != null) && patch[x + 1][y].isCannotMoveRight()))
			notFree = true;
		if (notFree && !patch[x][y].isCannotMoveRight()) {
			patch[x][y].setCannotMoveRight(true);
			pushingRightStones.remove(patch[x][y]);
			patch[x][y].setCanMoveRight(false);
			aThereWasAChange.setTrue();
		}
		if (!notFree && patch[x][y].isCannotMoveRight()) {
			patch[x][y].setCannotMoveRight(false);
			aThereWasAChange.setTrue();
		}
		{
			boolean isfree = false;
			// ---CHECK IF WE CAN MOVE RIGHT
			{
				if (!notFree)
					if ((x != width - 1) && ((patch[x + 1][y] == null) || patch[x + 1][y].isCanMoveRight()))
						isfree = true;
				if (isfree && !patch[x][y].isCanMoveRight()) {
					patch[x][y].setCanMoveRight(true);
					aThereWasAChange.setTrue();
//					canMove = true;
				}
				if (!isfree && patch[x][y].isCanMoveRight()) {
					patch[x][y].setCanMoveRight(false);
					aThereWasAChange.setTrue();
				}
			}
			// ---INHERIT OUR PUSH TO THE RIGHT STONE AND STICKING LEFT ONE
			if (isfree) {
				if (patch[x][y].isPushingRight()) {
					pushingRightStones.add(patch[x][y]);
					if ((x != width - 1) && (patch[x + 1][y] != null) && !patch[x + 1][y].isPushingRight()) {
						patch[x + 1][y].setPushingRight(true);
						pushingRightStones.add(patch[x + 1][y]);
						aThereWasAChange.setTrue();
					}
					if ((x != 0) && (patch[x - 1][y] != null) && (patch[x][y].getType() == patch[x - 1][y].getType()) && !patch[x - 1][y].isPushingRight()) {
						patch[x - 1][y].setPushingRight(true);
						pushingRightStones.add(patch[x - 1][y]);
						aThereWasAChange.setTrue();
					}
				}
			}
		}
//		if (x == W - 1 && canMove)
//			logger.error("Error");
//		return canMove;
	}

	protected boolean markVanishingOption(RcBoolean aThereWasAChange, int x, int y) {
		boolean vanish = false;
		// ---The patch below us might be the same
		if ((y != height - 1) && (patch[x][y + 1] != null))
			if (patch[x][y].getType() == patch[x][y + 1].getType())
				vanish = true;
		// ---The patch above us might be the same
		if ((y != preview) && (patch[x][y - 1] != null))
			if (patch[x][y].getType() == patch[x][y - 1].getType())
				vanish = true;
		if (vanish && !patch[x][y].isVanishing()) {
			aThereWasAChange.setTrue();
			patch[x][y].setisVanishing(true);
		}
		if (!vanish && patch[x][y].isVanishing()) {
			patch[x][y].setisVanishing(false);
			aThereWasAChange.setTrue();
		}
		return vanish;
	}

	protected boolean moveOneStepLeft() {
		markMoveLeftOption();
		// setStoneOptions();
		boolean ChangedOnce = false;
		for (int y = height - 1; y >= preview; y--) {
			for (int x = 0; x < width; x++) {
				if ((patch[x][y] != null) && patch[x][y].isCanMoveLeft() && patch[x][y].isPushingLeft()) {
					patch[x - 1][y] = patch[x][y];
					patch[x][y].x--;
					patch[x][y] = null;
					patch[x - 1][y].setMovingLeft(true);
					ChangedOnce = true;
				}
			}
		}
		return ChangedOnce;
	}

	protected boolean moveOneStepRight() {
		markMoveRightOption();
		// setStoneOptions();
		boolean ChangedOnce = false;
		for (int y = height - 1; y >= preview; y--) {
			for (int x = width - 1; x >= 0; x--) {
				if ((patch[x][y] != null) && (patch[x][y].isCanMoveRight()) && (patch[x][y].isPushingRight())) {
					patch[x + 1][y] = patch[x][y];
					patch[x][y].x++;
					patch[x][y] = null;
					patch[x + 1][y].setMovingRight(true);
					ChangedOnce = true;
				}
			}
		}
		return ChangedOnce;
	}

	public void nextRound() {
		if (userCanReact()) {
			generateStones();
		}
	}

	protected int queryHeapHeight() {
		for (int y = preview; y < height; y++)
			for (int x = 0; x < width; x++)
				if (patch[x][y] != null)
					return height - y;
		return 0;
	}

	protected boolean queryWin() {
		return game.queryWin(patch);
	}

	protected boolean queryTilt() {
		return game.queryTilt(patch);
	}

	public void reactLeft(Object selected) {
		if (selected != null && userCanReact()) {
			if (Stone.class.isInstance(selected)) {
				Stone selectedStone = (Stone) selected;
				if ((selectedStone.y >= preview)) {
					selectedStone.setPushingLeft(true);
					pushingLeftStones.add(selectedStone);
					if (moveOneStepLeft()) {
						animationPhase = maxAnimaltionPhase;
						setUserReacted(true);
					}
				}

			}
		} else {
		}
	}

	public void reactRight(Object selected) {
		if (selected != null && userCanReact()) {
			if (Stone.class.isInstance(selected)) {
				Stone selectedStone = (Stone) selected;
				if ((selectedStone.y >= preview)) {
					selectedStone.setPushingRight(true);
					pushingRightStones.add(selectedStone);
					if (moveOneStepRight()) {
						animationPhase = maxAnimaltionPhase;
						setUserReacted(true);
					}
				}

			}
		} else {
		}
	}

//	protected void readFromDisk() {
//		try {
//			XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(getLastGameName())));
//			readFromDisk(decoder);
//			NrOfTotalStones = ((Integer) decoder.readObject()).intValue();
//			game.score = ((Integer) decoder.readObject()).intValue();
//			game.relativeTime = ((Long) decoder.readObject()).longValue();
//			decoder.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	protected void readFromDisk(XMLDecoder aDecoder) {
//		for (int y = height - 1; y >= 0; y--) {
//			for (int x = 0; x < width; x++) {
//				boolean exits = ((Boolean) aDecoder.readObject()).booleanValue();
//				if (exits) {
//					int type = ((Integer) aDecoder.readObject()).intValue();
//					patch[x][y] = createStone(x, y, type);
//					patch[x][y].readFromDisk(aDecoder);
//				} else {
//				}
//			}
//		}
//	}

	protected abstract void removeStone(Stone stone);

	private void removeValishedStones() {
		boolean Changed = false;
		do {
			Changed = false;
			{
				for (int y = preview; y < height; y++) {
					for (int x = 0; x < width; x++) {
						if (patch[x][y] != null && patch[x][y].isVanishing()) {
							removeStone(patch[x][y]);
							patch[x][y] = null;
							Changed = true;
						}
					}
				}
			}
		} while (Changed);
//		game.updateScore(queryHeapHeight());
	}

	public GamePhase setStoneOptions() {
		// ---Stones of same type on top of each other should vanish
		clearTemporaryAttributes();
		pushingLeftStones.clear();
		pushingRightStones.clear();
		RcBoolean	changedOnce	= new RcBoolean(false);
		boolean		canVanish	= false;
		do {
			changedOnce.setFalse();
			for (int y = height - 1; y >= preview; y--) {
				for (int x = 0; x < width; x++) {
					if (patch[x][y] != null) {
						if (markVanishingOption(changedOnce, x, y))
							canVanish = true;
					}
				}
			}
		} while (changedOnce.getBooleanValue());
//		game.updateScore(patch);
		droppingStonesBuffer.addAll(droppingStones);// remember which stones where dropping
		droppingStones.clear();
		do {
			changedOnce.setFalse();
			for (int y = height - 1; y >= preview; y--) {
				for (int x = 0; x < width; x++) {
					if (patch[x][y] != null) {
						markStoneDroppingOption(changedOnce, x, y);
						markStoneMoveLeftOption(changedOnce, x, y);
						markStoneMoveRightOption(changedOnce, x, y);
					}
				}
			}
		} while (changedOnce.getBooleanValue());
		droppingStonesBuffer.removeAll(droppingStones);
		for (Stone stone : droppingStonesBuffer) {
			// stones that where droopping but now cannto drop
			// Vis.PlaySoundEffect(SOUND_EFFECT_DROP);
			Tools.play(AtlasManager.ASSETS_FOLDER+"/sound/drop.wav");
		}
		droppingStonesBuffer.clear();

		if (canVanish) {
			Tools.play(AtlasManager.ASSETS_FOLDER+"/sound/Vanish.wav");
			return GamePhase.vanishing;
		}
		markStickyPatches();
		testIntegrity();
		if (!droppingStones.isEmpty())
			return GamePhase.dropping;
		if (!pushingLeftStones.isEmpty())
			return GamePhase.pushingLeft;
		if (!pushingRightStones.isEmpty())
			return GamePhase.pushingRight;
		return GamePhase.waiting;
	}

	public int getScore() {
		return game.getScore(patch);
	}

	protected void setUserReacted(boolean userReacted) {
		this.userReacted = userReacted;
	}

	private void testIntegrity() {
		for (int y = height - 1; y >= preview; y--) {
			for (int x = 0; x < width; x++) {
				Stone stone = patch[x][y];
				if (stone != null) {
					if (stone.isPushingLeft() && stone.isCanMoveLeft() && pushingLeftStones.isEmpty()) {
						logger.error(String.format("pushingLeftStones is empty, but at least one stone is still pushing left"));
						System.exit(1);
					}
					if (stone.isPushingRight() && stone.isCanMoveRight() && pushingRightStones.isEmpty()) {
						logger.error(String.format("pushingRightStones is empty, but at least one stone is still pushing right"));
						System.exit(1);
					}
					if (stone.isDropping() && droppingStones.isEmpty()) {
						logger.error(String.format("droppingStones is empty, but at least one stone is still dropping"));
						System.exit(1);
					}
				}
			}
		}

	}

	public void tilt() {
		gamePhase = GamePhase.tilt;
	}

	public boolean update() {

		if (animationPhase == 0) {
			removeValishedStones();
			clearCommandAttributes();
			gamePhase = setStoneOptions();
			switch (gamePhase) {
			case vanishing:
			case pushingLeft:
			case pushingRight:
			case dropping: {
//				logger.info(String.format("gamePhase=%s", gamePhase.name()));
				dropStones();
				moveOneStepRight();
				moveOneStepLeft();
				animationPhase = maxAnimaltionPhase;
				animationPhase = maxAnimaltionPhase;
			}
				break;
			case waiting:
				if (queryTilt()) {
					tilt = true;
				}
				if (queryWin()) {
					tilt = true;
				}
				if (isUserReacted()) {
					setUserReacted(false);
					generateStones();
				}
				clearPuchingAttributes();
				break;
			case tilt:
				break;
			default:
				break;
			}
		} else {
			animationPhase--;
		}
		return tilt;
	}

	protected boolean userCanReact() {
		return gamePhase.equals(GamePhase.waiting) && animationPhase == 0;
	}

	public void writeToDisk() {
		try {
			XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(String.format(Context.CONFIG_FOLDER + "/%s.xml", game.name))));
			write(encoder);
			encoder.close();
		} catch (FileNotFoundException e) {
			logger.warn(e.getMessage(), e);
		}
	}

	public void readFromDisk() {
		// only if this is a real game type and not the UI type
		if (!game.name.equals(GameName.UI.name())) {
			try {
				XMLDecoder encoder;
				encoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(String.format(Context.CONFIG_FOLDER + "/%s.xml", game.name))));
				read(encoder);
				encoder.close();
			} catch (FileNotFoundException e) {
				logger.info(e.getMessage(), e);
			}
		}
	}

	private void read(XMLDecoder decoder) {
		// only if this is a real game type and not the UI type
		if (!game.name.equals(GameName.UI.name())) {
			game.score = (int) decoder.readObject();
			game.steps = (int) decoder.readObject();
			game.relativeTime = (long) decoder.readObject();
			for (int y = height - 1; y >= 0; y--) {
				for (int x = 0; x < width; x++) {
					boolean exists = (boolean) decoder.readObject();
					if (exists) {
						int type = (int) decoder.readObject();
						patch[x][y] = createStoneAndUpdateScore(x, y, type);
						patch[x][y].read(decoder);
					}
				}
			}
		}
	}

	protected void write(XMLEncoder encoder) {
//		encoder.writeObject(game.Name);
//		encoder.writeObject(game.nrOfRows);
//		encoder.writeObject(game.nrOfFallingStones);
//		encoder.writeObject(game.nrOfStones);
		encoder.writeObject(game.score);
		encoder.writeObject(game.steps);
		encoder.writeObject(game.relativeTime);
		for (int y = height - 1; y >= 0; y--) {
			for (int x = 0; x < width; x++) {
				if (patch[x][y] != null) {
					encoder.writeObject(true);
					patch[x][y].write(encoder);
				} else {
					encoder.writeObject(false);
				}
			}
		}
	}

	public String getName() {
		return game.name;
	}

}