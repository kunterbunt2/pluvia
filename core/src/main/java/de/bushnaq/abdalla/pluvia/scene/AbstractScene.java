package de.bushnaq.abdalla.pluvia.scene;

import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import de.bushnaq.abdalla.engine.GameObject;
import de.bushnaq.abdalla.engine.Text2D;
import de.bushnaq.abdalla.engine.util.logger.Logger;
import de.bushnaq.abdalla.engine.util.logger.LoggerFactory;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import de.bushnaq.abdalla.pluvia.engine.ModelManager;
import de.bushnaq.abdalla.pluvia.scene.model.fish.Fish;
import de.bushnaq.abdalla.pluvia.scene.model.turtle.Turtle;
import net.mgsx.gltf.scene3d.attributes.PBRColorAttribute;
import net.mgsx.gltf.scene3d.model.ModelInstanceHack;

public abstract class AbstractScene {
	protected static final float	CITY_SIZE	= 3;
	private static final float		WATER_X		= 100;
	private static final float		WATER_Y		= 0;
	private static final float		WATER_Z		= 50;
	protected GameEngine			gameEngine;
	protected int					index		= 0;
	protected Logger				logger		= LoggerFactory.getLogger(this.getClass());
	protected Text2D				logo;
	protected Random				rand;
	protected List<GameObject>		renderModelInstances;
	protected Text2D				version;

	public AbstractScene(GameEngine gameEngine, List<GameObject> renderModelInstances) {
		this.gameEngine = gameEngine;
		this.renderModelInstances = renderModelInstances;
		this.rand = new Random(System.currentTimeMillis());
	}

	public void create() {
		logo = new Text2D("Pluvia", 100, Gdx.graphics.getHeight() - 200, Color.WHITE, gameEngine.renderEngine.getAtlasManager().logoFont);
		gameEngine.renderEngine.add(logo);
		try {
			String				v		= gameEngine.context.getAppVersion();
			final GlyphLayout	layout	= new GlyphLayout();
			layout.setText(gameEngine.renderEngine.getAtlasManager().logoFont, "Pluvia");
			float h1 = layout.height;
			layout.setText(gameEngine.renderEngine.getAtlasManager().versionFont, v);
			float h2 = layout.height;
			version = new Text2D(v, 400 + 20, Gdx.graphics.getHeight() - 200 - (int) (h1 - h2), Color.WHITE, gameEngine.renderEngine.getAtlasManager().versionFont);
			gameEngine.renderEngine.add(version);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	protected void createCity(final GameEngine gameEngine, final float x, final float y, final float z, boolean up, float ScaleY) {
		final int	maxIteration			= 27;
		final int	iteration				= maxIteration;
		final float	scaleX					= (CITY_SIZE * 5) / (iteration - 1);
		final float	scaleY					= (ScaleY) / (iteration - 1);
		final float	scaleZ					= (CITY_SIZE * 5) / (iteration - 1);
		final float	averrageBuildingHight	= 5f;
		createCity(gameEngine, x, y, z, iteration, maxIteration, scaleX, scaleY, scaleZ, averrageBuildingHight, up);
	}

	private void createCity(final GameEngine gameEngine, final float x, final float y, final float z, int iteration, int maxIteration, final float scaleX, final float scaleY, final float scaleZ,
			float averrageBuildingHight, boolean up) {
		// we are responsible for the 4 corners
		final float screetSize = 0.02f;
		iteration /= 2;
		// System.out.println(String.format("iteration=%d scale=%f x=%f z=%f", iteration, scale, x, z));
		// int i = 0;
		averrageBuildingHight = averrageBuildingHight + averrageBuildingHight * (0.5f - rand.nextFloat());
		final TwinBuilding twinChances[][] = { { new TwinBuilding(0.3f, 0.0f, 1, 1)/* 0,0 */, new TwinBuilding(0.0f, 0.3f, 1, -1) }/* 0,1 */,
				{ new TwinBuilding(0.0f, 0.3f, -1, 1)/* 1,0 */, new TwinBuilding(0.3f, 0.0f, -1, -1) }/* 1,1 */ };
		// z=1, x=1, x=0
		// 0,0
		// 0,1
		// 1,0
		// 1,1

		for (int xi = 0; xi < 2; xi++) {
			for (int zi = 0; zi < 2; zi++) {
				final TwinBuilding twinChance = twinChances[xi][zi];
				if (!twinChance.occupided) {
					twinChance.occupided = true;
					float		xx						= x + (xi * 2 - 1) * iteration * scaleX;
					float		zz						= z + (zi * 2 - 1) * iteration * scaleZ;
					// the bigger the block, the lower the change for it to be one building
					final float	changceOfOneBuilding	= 0.5f / iteration;
					final float	changceOfNoBuilding		= 0.0f / iteration;
					if (rand.nextFloat() < changceOfNoBuilding) {

					} else if (iteration > 1f && rand.nextFloat() > changceOfOneBuilding)
						// create smaller buildings
						createCity(gameEngine, xx, y, zz, iteration, maxIteration, scaleX, scaleY, scaleZ, averrageBuildingHight, up);
					else {
						float	twinFactorXs	= 1f;
						float	twinFactorZs	= 1f;
						if (rand.nextFloat() < twinChance.chanceHorizontal) {
							final TwinBuilding twin = twinChances[xi + twinChance.deltaX][zi];
							if (!twin.occupided) {
								twinFactorXs = 2f;
								xx = x;
								twin.occupided = true;
							}
						}
						if (rand.nextFloat() < twinChance.chanceVertical) {
							final TwinBuilding twin = twinChances[xi][zi + twinChance.deltaZ];
							if (!twin.occupided) {
								twinFactorZs = 2f;
								zz = z;
								twin.occupided = true;
							}
						}

						final GameObject	inst	= instanciateBuilding(gameEngine, index++);
						final float			xs		= iteration * scaleX * twinFactorXs - screetSize;
						// the bigger the building, the lower the change for it to get big
						final float			ys		= (maxIteration + 1 - iteration) * scaleY /* ;averrageBuildingHight */ * (0.1f + 3 * rand.nextFloat());
						final float			zs		= iteration * scaleZ * twinFactorZs - screetSize;
						// System.out.println(String.format(" xx=%f zz=%f xs=%f", xx, zz, xs));
						if (up)
							inst.instance.transform.setToTranslationAndScaling(xx, y /* + ys / 2 + 0.1f */, zz, xs, ys, zs);
						else
							inst.instance.transform.setToTranslationAndScaling(xx, y - ys / 2 - 0.1f, zz, xs, ys, zs);
						inst.update();
//						gameEngine.renderEngine.addStatic(inst);
						renderModelInstances.add(inst);
					}
				}
				// i++;
			}
		}
	}

	protected void createFish(float minSize, float maxSize) {
		Vector3		min	= gameEngine.renderEngine.sceneBox.min;
		Vector3		max	= gameEngine.renderEngine.sceneBox.max;
		BoundingBox	b	= new BoundingBox(new Vector3(min.x, -5f, min.z), new Vector3(max.x, 0f, 0));
		for (int i = 0; i < Math.min(gameEngine.context.getMaxSceneObjects(), 100); i++) {
			int		type	= rand.nextInt(ModelManager.MAX_NUMBER_OF_FISH_MODELS);
			float	size	= minSize + (float) Math.random() * (maxSize - minSize);
			Fish	fish	= new Fish(gameEngine, type, size, b);
			gameEngine.context.fishList.add(fish);
		}
	}

	protected void createMirror(Color color) {
		if (gameEngine.renderEngine.isMirrorPresent()) {
			Model model;
			model = gameEngine.modelManager.mirror;
			GameObject cube = new GameObject(new ModelInstanceHack(model), null);
			cube.instance.materials.get(0).set(ColorAttribute.createDiffuse(color));
			cube.instance.transform.setToTranslationAndScaling(0f, WATER_Y, -15f, WATER_X, 0.1f, WATER_Z);
			renderModelInstances.add(cube);
		}
	}

	protected void createPlane(Color color) {
		Model model;
		model = gameEngine.modelManager.square;
		GameObject	cube	= new GameObject(new ModelInstanceHack(model), null);
		Material	m		= cube.instance.materials.get(0);
		m.set(PBRColorAttribute.createBaseColorFactor(color));
		cube.instance.transform.setToTranslationAndScaling(0f, WATER_Y, -15f, WATER_X, 0.1f, WATER_Z);
		renderModelInstances.add(cube);
	}

	protected void createTurtles(float minSize, float maxSize) {
		Vector3	min	= gameEngine.renderEngine.sceneBox.min;
		Vector3	max	= gameEngine.renderEngine.sceneBox.max;
		for (int i = 0; i < Math.min(gameEngine.context.getMaxSceneObjects(), 10); i++) {
			int			type	= rand.nextInt(ModelManager.MAX_NUMBER_OF_TURTLE_MODELS);
			float		size	= minSize + (float) Math.random() * (maxSize - minSize);
			BoundingBox	b		= new BoundingBox(new Vector3(min.x, size / 2, min.z), new Vector3(max.x, size / 2, 0));
			Turtle		turtle	= new Turtle(gameEngine, type, size, b);
			gameEngine.context.turtleList.add(turtle);
		}
	}

	protected void createWater() {
		// water
		if (gameEngine.renderEngine.isWaterPresent()) {
			final GameObject water = new GameObject(new ModelInstanceHack(gameEngine.modelManager.water), null);
			water.instance.transform.setToTranslationAndScaling(0, WATER_Y, -15, WATER_X, 1, WATER_Z);
			water.update();
			renderModelInstances.add(water);
			// plane below the water
			{
				Model model;
				model = gameEngine.modelManager.square;
				GameObject cube = new GameObject(new ModelInstanceHack(model), null);
				if (gameEngine.renderEngine.isPbr()) {
					cube.instance.materials.get(0).set(PBRColorAttribute.createBaseColorFactor(Color.GREEN));
				} else {
					cube.instance.materials.get(0).set(ColorAttribute.createDiffuse(Color.GREEN));
				}
				cube.instance.transform.setToTranslationAndScaling(0f, -30f, 0f, 100f, 0.1f, 100f);
				renderModelInstances.add(cube);
			}
		}
	}

	public abstract Color getInfoColor();

	private GameObject instanciateBuilding(final GameEngine gameEngine, final int index) {
//		int i = rand.nextInt(ModelManager.MAX_NUMBER_OF_BUILDING_MODELS);
		final GameObject	go	= new GameObject(new ModelInstanceHack(gameEngine.modelManager.buildingCube[(int) (Math.random() * ModelManager.MAX_NUMBER_OF_BUILDING_MODELS)]), null);
		Material			m	= go.instance.model.materials.get(0);
		if (gameEngine.renderEngine.isPbr()) {
			m.set(PBRColorAttribute.createBaseColorFactor(Color.BLACK));
			return go;
		} else {
//			final GameObject	go	= new GameObject(new ModelInstanceHack(gameEngine.modelManager.buildingCube[(int) (Math.random() * ModelManager.MAX_NUMBER_OF_BUILDING_MODELS)]), null);
//			Material			m	= go.instance.model.materials.get(0);
			m.set(ColorAttribute.createDiffuse(Color.GRAY));
			return go;
		}

	}

}
