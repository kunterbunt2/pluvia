package com.abdalla.bushnaq.pluvia.renderer;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import net.mgsx.gltf.loaders.glb.GLBLoader;
import net.mgsx.gltf.scene3d.attributes.PBRColorAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRFloatAttribute;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

/**
 * Loads models and textures, instantiates the SceneManager class
 *
 * @author abdal
 *
 */
public class ModelManager {
	private static Color		DIAMON_BLUE_COLOR				= new Color(0x006ab6ff);
	private static Color		GRAY_COLOR						= new Color(0x404853ff);
	public static int			MAX_NUMBER_OF_BUILDING_MODELS	= 8;
	public static final int		MAX_NUMBER_OF_FLY_MODELS		= 10;
	public static final int		MAX_NUMBER_OF_RAIN_MODELS		= 12;
	public static final int		MAX_NUMBER_OF_FIRELY_MODELS		= 10;
	public static final int		MAX_NUMBER_OF_BUBBLE_MODELS		= 10;
	public static int			MAX_NUMBER_OF_FISH_MODELS		= 8;
	private static final int	MAX_NUMBER_OF_STONE_MODELS		= 8;
	public static final int		MAX_NUMBER_OF_TURTLE_MODELS		= 1;
	private static Color		POST_GREEN_COLOR				= new Color(0x00614eff);
	private static Color		SCARLET_COLOR					= new Color(0xb00233ff);
	public Model				backPlate;																		// game grid glass background
	public Model				backPlatePbr;																	// game grid glass background
	public Model				buildingCube[]					= new Model[MAX_NUMBER_OF_BUILDING_MODELS];		// for city scene
	public Model				buildingCubePbr[]				= new Model[MAX_NUMBER_OF_BUILDING_MODELS];		// for city scene
	public SceneAsset			stone[]							= new SceneAsset[MAX_NUMBER_OF_STONE_MODELS];	// for stones
	public SceneAsset			stonePbr[]						= new SceneAsset[MAX_NUMBER_OF_STONE_MODELS];	// for stones
	public SceneAsset			flyModel[]						= new SceneAsset[MAX_NUMBER_OF_FLY_MODELS];		// for firefly
	public SceneAsset			flyModelPbr[]					= new SceneAsset[MAX_NUMBER_OF_FLY_MODELS];		// for firefly
	public SceneAsset			rainModel[]						= new SceneAsset[MAX_NUMBER_OF_RAIN_MODELS];	// for firefly
	public SceneAsset			rainModelPbr[]					= new SceneAsset[MAX_NUMBER_OF_RAIN_MODELS];	// for firefly
	public SceneAsset			firelyModel[]					= new SceneAsset[MAX_NUMBER_OF_FIRELY_MODELS];	// for fly
	public SceneAsset			firelyModelPbr[]				= new SceneAsset[MAX_NUMBER_OF_FIRELY_MODELS];	// for fly
	public SceneAsset			bubbleModel[]					= new SceneAsset[MAX_NUMBER_OF_BUBBLE_MODELS];	// for bubbles
	public SceneAsset			bubbleModelPbr[]				= new SceneAsset[MAX_NUMBER_OF_BUBBLE_MODELS];	// for bubbles
	public Model				fishCube[]						= new Model[MAX_NUMBER_OF_FISH_MODELS];			// for fish
	public Model				fishCubePbr[]					= new Model[MAX_NUMBER_OF_FISH_MODELS];			// for fish
	public Model				levelCube;																		// level edges
	public Model				levelCubePbr;																	// level edges
	public Model				square;																			// used for the ground
	public Model				squarePbr;																		// used for the ground
	public SceneAsset			turtleCube[]					= new SceneAsset[MAX_NUMBER_OF_TURTLE_MODELS];	// for turtles
	public SceneAsset			turtleCubePbr[]					= new SceneAsset[MAX_NUMBER_OF_TURTLE_MODELS];	// for turtles
	public Model				water;																			// water square
	public Model				mirror;																			// mirror square

	public ModelManager() {
	}

	public void create(boolean isPbr) throws Exception {

		final Texture		texture			= new Texture(Gdx.files.internal("assets/tiles.png"));
		final ModelBuilder	modelBuilder	= new ModelBuilder();
//		final ModelCreator	modelCreator	= new ModelCreator();
		createStoneModels(isPbr);
		createBuildingModels(isPbr, modelBuilder);
		createFishModels(isPbr, modelBuilder);
		createFireflyModels(isPbr, modelBuilder);
		createFlyModels(isPbr, modelBuilder);
		createBubbleModels(isPbr, modelBuilder);
		createTutleModels(isPbr);
		createRainModels(isPbr, modelBuilder);
		createLevelModels(texture, modelBuilder);
		createWaterModel(texture, modelBuilder);
		createMirrorModel(texture, modelBuilder);
		createSquareModel(modelBuilder);
		createBackPlateModel(modelBuilder);
	}

	private void createBackPlateModel(final ModelBuilder modelBuilder) {
		{
			final Attribute	color		= new PBRColorAttribute(PBRColorAttribute.BaseColorFactor, Color.WHITE);
			final Attribute	metallic	= PBRFloatAttribute.createMetallic(0.5f);
			final Attribute	roughness	= PBRFloatAttribute.createRoughness(0.5f);
			final Attribute	blending	= new BlendingAttribute(0.5f);											// opacity is set by pbrMetallicRoughness below
			final Material	material	= new Material(metallic, roughness, color, blending);
			backPlatePbr = createSquare(modelBuilder, 0.5f, 0.5f, material);
		}
		{
			final ColorAttribute	diffuseColor	= ColorAttribute.createDiffuse(Color.WHITE);
			final Material			material		= new Material(diffuseColor);
			backPlate = createSquare(modelBuilder, 0.5f, 0.5f, material);
		}
	}

	private void createBuildingModels(boolean isPbr, final ModelBuilder modelBuilder) {
		Color[] buildingColors = new Color[] { Color.WHITE, POST_GREEN_COLOR, SCARLET_COLOR, DIAMON_BLUE_COLOR, GRAY_COLOR, Color.CORAL, Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, Color.GOLD, Color.MAGENTA,
				Color.YELLOW };
		if (isPbr) {
			for (int i = 0; i < MAX_NUMBER_OF_BUILDING_MODELS; i++) {
				final Attribute	color		= new PBRColorAttribute(PBRColorAttribute.BaseColorFactor, buildingColors[i]);
				final Attribute	metallic	= PBRFloatAttribute.createMetallic(0.5f);
				final Attribute	roughness	= PBRFloatAttribute.createRoughness(0.5f);
				final Material	material	= new Material(metallic, roughness, color);
				buildingCubePbr[i] = modelBuilder.createBox(1.0f, 1.0f, 1.0f, material, Usage.Position | Usage.Normal);
			}
		} else {
			for (int i = 0; i < MAX_NUMBER_OF_BUILDING_MODELS; i++) {
				final ColorAttribute	diffuseColor	= ColorAttribute.createDiffuse(buildingColors[i]);
				final Material			material		= new Material(diffuseColor);
				buildingCube[i] = modelBuilder.createBox(1.0f, 1.0f, 1.0f, material, Usage.Position | Usage.Normal);
			}
		}
	}

	private void createFireflyModels(boolean isPbr, final ModelBuilder modelBuilder) {
		Color[] colors = new Color[] { Color.WHITE, POST_GREEN_COLOR, SCARLET_COLOR, DIAMON_BLUE_COLOR, GRAY_COLOR, Color.CORAL, Color.RED, Color.GREEN, Color.BLUE, Color.GOLD, Color.MAGENTA, Color.YELLOW, Color.BLACK };
		if (isPbr) {
			for (int i = 0; i < MAX_NUMBER_OF_FLY_MODELS; i++) {
				firelyModelPbr[i] = new GLBLoader().load(Gdx.files.internal(String.format("assets/models/fly.glb")));
				Material m = firelyModelPbr[i].scene.model.materials.get(0);
				m.set(PBRColorAttribute.createBaseColorFactor(colors[i]));
			}
		} else {
			for (int i = 0; i < MAX_NUMBER_OF_FLY_MODELS; i++) {
				firelyModel[i] = new GLBLoader().load(Gdx.files.internal(String.format("assets/models/fly.glb")));
				ColorAttribute	color	= ColorAttribute.createDiffuse(Color.BLACK);
				Material		m		= firelyModel[i].scene.model.materials.get(0);
				m.set(color);
			}
		}
	}

	private void createRainModels(boolean isPbr, final ModelBuilder modelBuilder) {
//		Color[] colors = new Color[] { Color.WHITE, POST_GREEN_COLOR, SCARLET_COLOR, DIAMON_BLUE_COLOR, GRAY_COLOR, Color.CORAL, Color.RED, Color.GREEN, Color.BLUE, Color.GOLD, Color.MAGENTA, Color.YELLOW, Color.BLACK };
		if (isPbr) {
			for (int i = 0; i < MAX_NUMBER_OF_RAIN_MODELS; i++) {
				rainModelPbr[i] = new GLBLoader().load(Gdx.files.internal(String.format("assets/models/rain.glb")));
				Material		m			= rainModelPbr[i].scene.model.materials.get(0);
//				m.set(PBRColorAttribute.createBaseColorFactor(colors[i]));
				final Attribute	blending	= new BlendingAttribute(0.9f);					// opacity is set by pbrMetallicRoughness below
				m.set(blending);
			}
		} else {
			for (int i = 0; i < MAX_NUMBER_OF_RAIN_MODELS; i++) {
				rainModel[i] = new GLBLoader().load(Gdx.files.internal(String.format("assets/models/rain.glb")));
			}
		}
	}

	private void createFlyModels(boolean isPbr, final ModelBuilder modelBuilder) {
//		Color[] colors = new Color[] { Color.WHITE, POST_GREEN_COLOR, SCARLET_COLOR, DIAMON_BLUE_COLOR, GRAY_COLOR, Color.CORAL, Color.RED, Color.GREEN, Color.BLUE, Color.GOLD, Color.MAGENTA, Color.YELLOW, Color.BLACK };
		if (isPbr) {
			for (int i = 0; i < MAX_NUMBER_OF_FIRELY_MODELS; i++) {
				flyModelPbr[i] = new GLBLoader().load(Gdx.files.internal(String.format("assets/models/firefly.glb")));
//				Material m = flyModelPbr[i].scene.model.materials.get(0);
//				m.set(PBRColorAttribute.createBaseColorFactor(colors[i]));
			}
		} else {
			for (int i = 0; i < MAX_NUMBER_OF_FIRELY_MODELS; i++) {

				flyModel[i] = new GLBLoader().load(Gdx.files.internal(String.format("assets/models/firefly.glb")));
				ColorAttribute	color	= ColorAttribute.createDiffuse(Color.BLACK);
				Material		m		= flyModel[i].scene.model.materials.get(0);
				m.set(color);
			}
		}
	}

	private void createBubbleModels(boolean isPbr, final ModelBuilder modelBuilder) {
		Color[] colors = new Color[] { Color.WHITE, POST_GREEN_COLOR, SCARLET_COLOR, DIAMON_BLUE_COLOR, GRAY_COLOR, Color.CORAL, Color.RED, Color.GREEN, Color.BLUE, Color.GOLD, Color.MAGENTA, Color.YELLOW, Color.BLACK };
		if (isPbr) {
			for (int i = 0; i < MAX_NUMBER_OF_BUBBLE_MODELS; i++) {
				bubbleModelPbr[i] = new GLBLoader().load(Gdx.files.internal(String.format("assets/models/bubble.glb")));
				Material m = bubbleModelPbr[i].scene.model.materials.get(0);
				m.set(PBRColorAttribute.createBaseColorFactor(colors[i]));
				final Attribute blending = new BlendingAttribute(0.03f); // opacity is set by pbrMetallicRoughness below
				m.set(blending);
			}
		} else {
			for (int i = 0; i < MAX_NUMBER_OF_BUBBLE_MODELS; i++) {

				bubbleModel[i] = new GLBLoader().load(Gdx.files.internal(String.format("assets/models/bubble.glb")));
				ColorAttribute	color	= ColorAttribute.createDiffuse(Color.BLACK);
				Material		m		= bubbleModel[i].scene.model.materials.get(0);
				m.set(color);
			}
		}
	}

	private void createFishModels(boolean isPbr, final ModelBuilder modelBuilder) {
		Color[] colors = new Color[] { POST_GREEN_COLOR, SCARLET_COLOR, DIAMON_BLUE_COLOR, GRAY_COLOR, Color.CORAL, Color.RED, Color.GREEN, Color.BLUE, Color.GOLD, Color.MAGENTA, Color.YELLOW, Color.WHITE, Color.BLACK };
		if (isPbr) {
			for (int i = 0; i < MAX_NUMBER_OF_FISH_MODELS; i++) {
				final Attribute	color		= new PBRColorAttribute(PBRColorAttribute.BaseColorFactor, colors[i]);
				final Attribute	metallic	= PBRFloatAttribute.createMetallic(1.0f);
				final Attribute	roughness	= PBRFloatAttribute.createRoughness(0.1f);
				final Material	material	= new Material(metallic, roughness, color);
				fishCubePbr[i] = modelBuilder.createBox(1.0f, 1.0f, 1.0f, material, Usage.Position | Usage.Normal);
			}
		} else {
			for (int i = 0; i < MAX_NUMBER_OF_FISH_MODELS; i++) {
				final ColorAttribute	diffuseColor	= ColorAttribute.createDiffuse(colors[i]);
				final Material			material		= new Material(diffuseColor);
				fishCube[i] = modelBuilder.createBox(1.0f, 1.0f, 1.0f, material, Usage.Position | Usage.Normal);
			}
		}
	}

	private void createLevelModels(final Texture texture, final ModelBuilder modelBuilder) {
		{
			final Attribute	color		= new PBRColorAttribute(PBRColorAttribute.BaseColorFactor, Color.WHITE);
			final Attribute	metallic	= PBRFloatAttribute.createMetallic(0.5f);
			final Attribute	roughness	= PBRFloatAttribute.createRoughness(0.5f);
			final Material	material	= new Material(metallic, roughness, color);
			levelCubePbr = modelBuilder.createBox(1f, 1f, 1f, material, Usage.Position | Usage.Normal);
		}
		{
			final ColorAttribute	diffuseColor	= ColorAttribute.createDiffuse(Color.WHITE);
			final TextureAttribute	diffuseTexture	= TextureAttribute.createDiffuse(texture);
			final Material			material		= new Material(diffuseColor, diffuseTexture);
			levelCube = createSquare(modelBuilder, 0.5f, 0.5f, material);
		}
	}

	private Model createSquare(final ModelBuilder modelBuilder, final float sx, final float sz, final Material material) {
		return modelBuilder.createRect(-sx, 0f, sz, sx, 0f, sz, sx, 0f, -sz, -sx, 0f, -sz, 0f, 1f, 0f, material, Usage.Position | Usage.Normal | Usage.TextureCoordinates);
	}

	private void createSquareModel(final ModelBuilder modelBuilder) {
		{
			final Attribute	color		= new PBRColorAttribute(PBRColorAttribute.BaseColorFactor, Color.WHITE);
			final Attribute	metallic	= PBRFloatAttribute.createMetallic(0.5f);
			final Attribute	roughness	= PBRFloatAttribute.createRoughness(0.5f);
			final Material	material	= new Material(metallic, roughness, color);
			squarePbr = createSquare(modelBuilder, 0.5f, 0.5f, material);
		}
		{
			final ColorAttribute	diffuseColor	= ColorAttribute.createDiffuse(Color.WHITE);
			final Material			material		= new Material(diffuseColor);
			square = createSquare(modelBuilder, 0.5f, 0.5f, material);
		}
	}

	private void createStoneModels(boolean isPbr) {
		Cube[] cubes = new Cube[] { //
				new Cube(null, "assets/models/stone1.glb"), //
				new Cube(null, "assets/models/stone2.glb"), //
				new Cube(null, "assets/models/stone3.glb"), //
				new Cube(null, "assets/models/stone4.glb"), //
				new Cube(null, "assets/models/stone5.glb"), //
				new Cube(null, "assets/models/stone6.glb"), //
				new Cube(null, "assets/models/stone7.glb"), //
				new Cube(null, "assets/models/stone8.glb") //
		};
		if (isPbr) {
			for (int i = 0; i < MAX_NUMBER_OF_STONE_MODELS; i++) {
				stonePbr[i] = new GLBLoader().load(Gdx.files.internal(cubes[i].gltfModel));
				if (cubes[i].color != null) {
					Material m = stonePbr[i].scene.model.materials.get(0);
					m.set(PBRColorAttribute.createBaseColorFactor(cubes[i].color));
				}
			}
		} else {
			for (int i = 0; i < MAX_NUMBER_OF_STONE_MODELS; i++) {
				stone[i] = new GLBLoader().load(Gdx.files.internal(cubes[i].gltfModel));
				ColorAttribute	color	= ColorAttribute.createDiffuse(cubes[i].color);
				Material		m		= stone[i].scene.model.materials.get(0);
				m.set(color);
			}
		}
	}

	private void createTutleModels(boolean isPbr) {
		if (isPbr) {
			for (int i = 0; i < MAX_NUMBER_OF_TURTLE_MODELS; i++) {
				turtleCubePbr[i] = new GLBLoader().load(Gdx.files.internal(String.format("assets/models/turtle.glb")));
//				Material m = turtleCubePbr[i].scene.model.materials.get(0);
//				m.set(PBRColorAttribute.createBaseColorFactor(Color.BLACK));
			}
		} else {
			for (int i = 0; i < MAX_NUMBER_OF_TURTLE_MODELS; i++) {
			}
		}
	}

	private void createWaterModel(final Texture texture, final ModelBuilder modelBuilder) {
		final ColorAttribute	diffuseColor	= ColorAttribute.createDiffuse(Color.WHITE);
		final TextureAttribute	diffuseTexture	= TextureAttribute.createDiffuse(texture);
		final Material			material		= new Material(diffuseColor, diffuseTexture);
		material.id = "water";
		water = createSquare(modelBuilder, 0.5f, 0.5f, material);
	}

	private void createMirrorModel(final Texture texture, final ModelBuilder modelBuilder) {
		final ColorAttribute	diffuseColor	= ColorAttribute.createDiffuse(Color.WHITE);
		final TextureAttribute	diffuseTexture	= TextureAttribute.createDiffuse(texture);
		final Material			material		= new Material(diffuseColor, diffuseTexture);
		material.id = "mirror";
		mirror = createSquare(modelBuilder, 0.5f, 0.5f, material);
	}

	void set(final Material material1, final Material material2) {
		final Iterator<Attribute> i = material1.iterator();
		material2.clear();
		while (i.hasNext()) {
			final Attribute a = i.next();
			material2.set(a);
		}
	}

}