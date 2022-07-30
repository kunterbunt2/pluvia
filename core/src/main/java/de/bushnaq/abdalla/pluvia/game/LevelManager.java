/*
 * Created on 10.07.2004 TODO To change the template for this generated file go to Window - Preferences - Java - Code Style - Code Templates
 */
package de.bushnaq.abdalla.pluvia.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Model;

import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import de.bushnaq.abdalla.pluvia.engine.GameObject;
import de.bushnaq.abdalla.pluvia.game.model.stone.Stone;
import de.bushnaq.abdalla.pluvia.scene.AbstractScene;
import de.bushnaq.abdalla.pluvia.scene.BubblesScene;
import de.bushnaq.abdalla.pluvia.scene.DeepSeaScene;
import de.bushnaq.abdalla.pluvia.scene.FireflyScene;
import de.bushnaq.abdalla.pluvia.scene.FlyScene;
import de.bushnaq.abdalla.pluvia.scene.NightFishScene;
import de.bushnaq.abdalla.pluvia.scene.RainScene;
import de.bushnaq.abdalla.pluvia.scene.TurtlesScene;
import de.bushnaq.abdalla.pluvia.scene.model.digit.Digit;
import de.bushnaq.abdalla.pluvia.scene.model.digit.DigitType;
import net.mgsx.gltf.scene3d.model.ModelInstanceHack;

public class LevelManager extends Level implements Serializable {
	private static final String	INVALID_LEVEL_RECORDING_DETECTED	= "Invalid Level Recording Detected";
	private static final String	RESETTING_LEVEL						= "\n\nResetting level...";
	private static final long	serialVersionUID					= 1L;
	private GameEngine			gameEngine;
	int							index								= 0;
	private Color				infoColor;
	final List<GameObject>		renderModelInstances				= new ArrayList<>();
	Map<String, AbstractScene>	sceneList							= new HashMap<>();
//	protected Font				TextFont							= new Font("SansSerif", Font.BOLD, 14);

	public LevelManager(GameEngine gameEngine, Game game) {
		super(game);
		this.gameEngine = gameEngine;
		sceneList.put(GameName.Bird.name(), new BubblesScene(gameEngine, renderModelInstances));
		sceneList.put(GameName.Rabbit.name(), new FireflyScene(gameEngine, renderModelInstances));
		sceneList.put(GameName.Turtle.name(), new TurtlesScene(gameEngine, renderModelInstances));
		sceneList.put(GameName.Dragon.name(), new NightFishScene(gameEngine, renderModelInstances));
		sceneList.put(GameName.FIVE.name(), new FlyScene(gameEngine, renderModelInstances));
		sceneList.put(GameName.SIX.name(), new DeepSeaScene(gameEngine, renderModelInstances));
		sceneList.put(GameName.UI.name(), new RainScene(gameEngine, renderModelInstances));
	}

	private void addToEngine() {
		for (GameObject uberModel : renderModelInstances) {
			gameEngine.renderEngine.addStatic(uberModel);
		}
	}

	@Override
	public void createLevelBackground() {
		if (gameEngine != null) {
			float	cubeSize	= 0.5f;
			float	dx			= ((float) nrOfColumns) / 2;
			float	dy			= (nrOfRows);
			Model	model		= gameEngine.modelManager.levelCube;
			// level back
			int		count		= 0;
			if (game.nrOfRows != 0) {
				// back side
//		{
//			Model m;
//			m = gameEngine.modelManager.backPlate;
//			GameObject cube = new GameObject(new ModelInstanceHack(m), null);
//			cube.instance.transform.setToTranslationAndScaling(-0.5f, height / 2 + 0.75f, -0.5f, width, height - 0.5f, 1);
//			cube.instance.transform.rotate(Vector3.X, 90);
//			renderModelInstances.add(cube);
//		}
				// left side
				for (float y = nrOfRows - 0.75f; y >= preview; y -= 0.5f) {
					GameObject cube = new GameObject(new ModelInstanceHack(model), null);
					cube.instance.transform.setToTranslationAndScaling(-0.75f - dx, -(y - dy), -0.25f, cubeSize, cubeSize, cubeSize);
					renderModelInstances.add(cube);
					count++;
				}
				// right side
				for (float y = nrOfRows - 0.75f; y >= preview; y -= 0.5f) {
					GameObject cube = new GameObject(new ModelInstanceHack(model), null);
					cube.instance.transform.setToTranslationAndScaling(nrOfColumns - 0.25f - dx, -(y - dy), 0 - 0.25f, cubeSize, cubeSize, cubeSize);
					renderModelInstances.add(cube);
					count++;
				}
				// left lower side
				for (float x = -0.75f; x <= nrOfColumns / 2 - 1.5f - 1f; x += 0.5f) {
					GameObject cube = new GameObject(new ModelInstanceHack(model), null);
					cube.instance.transform.setToTranslationAndScaling(x - dx, -(nrOfRows - dy) + 0.25f, 0 - 0.25f, cubeSize, cubeSize, cubeSize);
					renderModelInstances.add(cube);
					count++;
				}
				// game name
				int position = 0;
				for (float x = -0.75f + nrOfColumns / 2 - 0.5f - 1f; x <= nrOfColumns / 2 + .75f - 1.5f; x += 0.5f) {
					gameEngine.context.digitList.add(new Digit(renderModelInstances, gameEngine, x - dx, -(nrOfRows - dy) + 0.25f - 0.5f, -0.25f, position, DigitType.name));
					position++;
					count++;
				}
				// game seed
				position = 0;
				for (float x = nrOfColumns / 2 + .75f - 1.5f + 0.5f; x <= -0.75f + nrOfColumns / 2 + 1.5f + 1f - 0.5f; x += 0.5f) {
					gameEngine.context.digitList.add(new Digit(renderModelInstances, gameEngine, x - dx, -(nrOfRows - dy) + 0.25f - 0.5f, -0.25f, position, DigitType.seed));
					position++;
					count++;
				}
				// right lower side
				for (float x = -0.75f + nrOfColumns / 2 + 1.5f + 1f; x <= nrOfColumns; x += 0.5f) {
					GameObject cube = new GameObject(new ModelInstanceHack(model), null);
					cube.instance.transform.setToTranslationAndScaling(x - dx, -(nrOfRows - dy) + 0.25f, 0 - 0.25f, cubeSize, cubeSize, cubeSize);
					renderModelInstances.add(cube);
					count++;
				}
				// score
				position = 0;
				for (float x = -0.75f - 2.5f; x <= -1.25; x += 0.5f) {
					gameEngine.context.digitList.add(new Digit(renderModelInstances, gameEngine, x - dx, -(nrOfRows - dy) + 0.25f - 0.5f, -0.25f, position, DigitType.score));
					position++;
					count++;
				}
				// steps
				position = 0;
				for (float x = nrOfColumns + 0.25f; x <= nrOfColumns + 0.5 + 2f; x += 0.5f) {
					gameEngine.context.digitList.add(new Digit(renderModelInstances, gameEngine, x - dx, -(nrOfRows - dy) + 0.25f - 0.5f, -0.25f, position, DigitType.steps));
					position++;
					count++;
				}
			}
//	logger.info(String.format("count=%d", count));
			gameEngine.renderEngine.removeAllEffects();
			sceneList.get(game.name).create();
			infoColor = sceneList.get(game.name).getInfoColor();
			addToEngine();
		}
	}

	@Override
	protected Stone createStone(int x, int y, int type) {
		Stone stone = new Stone(gameEngine, x, y, type);
		if (gameEngine != null)
			gameEngine.context.stoneList.add(stone);
		return stone;
	}

	public void destroy() {
		disposeLevel();
	}

	public void destroyLevelBackground() {
		if (gameEngine != null) {
			gameEngine.renderEngine.removeAllText2D();
			for (GameObject go : renderModelInstances) {
				gameEngine.renderEngine.removeStatic(go);
			}
			gameEngine.context.fishList.destroy(gameEngine);
			gameEngine.context.fireflyList.destroy(gameEngine);
			gameEngine.context.flyList.destroy(gameEngine);
			gameEngine.context.rainList.destroy(gameEngine);
			gameEngine.context.bubbleList.destroy(gameEngine);
			gameEngine.context.turtleList.destroy(gameEngine);
			gameEngine.context.digitList.destroy(gameEngine);
		}
	}

	@Override
	public void disposeLevel() {
		clear();
		destroyLevelBackground();
		game.reset();
		NrOfTotalStones = 0;
	}

	public Color getInfoColor() {
		return infoColor;
	}

	@Override
	protected boolean isEnableTime() {
		if (gameEngine != null)
			return gameEngine.context.isEnableTime();
		else
			return true;
	}

	@Override
	public void playSound(String tag) {
		if (gameEngine != null)
			gameEngine.getAudioManager().play(tag);
	}

	@Override
	protected void removeStone(Stone stone) {
		if (gameEngine != null) {
			stone.get3DRenderer().destroy(gameEngine);
			gameEngine.context.stoneList.remove(stone);
		}
	}

	public boolean testValidity() {
		return getRecording().testValidity(INVALID_LEVEL_RECORDING_DETECTED, RESETTING_LEVEL, gameEngine, (Game) gameEngine.context.game.clone());
	}

	public void updateFps() {
		if (gameEngine.context.isEnableTime()) {
			maxAnimaltionPhase = Math.max(1, Gdx.graphics.getFramesPerSecond() / 10);
		}
	}
}