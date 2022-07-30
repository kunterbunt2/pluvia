package de.bushnaq.abdalla.pluvia.game.model.stone;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import de.bushnaq.abdalla.engine.GameObject;
import de.bushnaq.abdalla.engine.ObjectRenderer;
import de.bushnaq.abdalla.engine.RenderEngine;
import de.bushnaq.abdalla.pluvia.engine.GameEngine;
import net.mgsx.gltf.scene3d.attributes.PBRColorAttribute;
import net.mgsx.gltf.scene3d.model.ModelInstanceHack;

public class Stone3DRenderer extends ObjectRenderer {
	private static final float		NORMAL_LIGHT_INTENSITY		= 10f;
	private static final Color		SELECTED_TRADER_NAME_COLOR	= Color.BLACK;
	private static final Color		TRADER_NAME_COLOR			= Color.BLACK;
	private static final float		TRADER_SIZE_X				= 1f;
	private static final float		TRADER_SIZE_Y				= 1f;
	private static final float		TRADER_SIZE_Z				= 1f;
	private static final float		VANISHING_LIGHT_INTENSITY	= 1000;
	private BitmapFont				font;
	private GameObject				instance;
	private float					lightIntensity				= 0f;
	private boolean					lightIsOne					= false;
	private final List<PointLight>	pointLight					= new ArrayList<>();
	private Stone					stone;
	private final Vector3			translation					= new Vector3();	// intermediate value

	public Stone3DRenderer(final Stone patch) {
		this.stone = patch;
	}

	@Override
	public void create(final GameEngine gameEngine) {
		if (instance == null && gameEngine != null) {
			instance = new GameObject(new ModelInstanceHack(gameEngine.modelManager.stone[stone.getType()].scene.model), stone);
			gameEngine.renderEngine.addDynamic(instance);
			instance.update();
			font = gameEngine.getAtlasManager().modelFont;
		}
	}

	@Override
	public void destroy(final GameEngine gameEngine) {
		if (gameEngine != null) {
			gameEngine.renderEngine.removeDynamic(instance);
			for (PointLight pl : pointLight) {
				gameEngine.renderEngine.remove(pl, true);
			}
		}
	}

	@Override
	public void renderText(final RenderEngine renderEngine, final int index, final boolean selected) {
		if (renderEngine.isDebugMode()) {
			Color color;
			if (selected) {
				color = SELECTED_TRADER_NAME_COLOR;
			} else {
				color = TRADER_NAME_COLOR;
			}
			float h = TRADER_SIZE_Y / 4;
			renderTextOnFrontSide(renderEngine, 0, 0 + h, "" + stone.getType(), TRADER_SIZE_Y / 2, color);
			h = TRADER_SIZE_Y / 8;
			renderTextOnFrontSide(renderEngine, 0, 0 - 0 * h, stone.getCannotAttributesAsString(), TRADER_SIZE_Y / 8, color);
			renderTextOnFrontSide(renderEngine, 0, 0 - 1 * h, stone.getCanAttributesAsString(), TRADER_SIZE_Y / 8, color);
			renderTextOnFrontSide(renderEngine, 0, 0 - 2 * h, stone.getGlueStatusAsString(), TRADER_SIZE_Y / 8, color);
			renderTextOnFrontSide(renderEngine, 0, 0 - 3 * h, stone.getDoingStatusAsString(), TRADER_SIZE_Y / 8, color);
		}
	}

	private void renderTextOnFrontSide(final RenderEngine renderEngine, final float dx, final float dy, final String text, final float size, final Color color) {
		final PolygonSpriteBatch batch = renderEngine.batch2D;
		{
			final Matrix4		m			= new Matrix4();
			final float			fontSize	= font.getLineHeight();
			final float			scaling		= size / fontSize;
			final GlyphLayout	layout		= new GlyphLayout();
			layout.setText(font, text);
			final float	width	= layout.width;		// contains the width of the current set text
			final float	height	= layout.height;	// contains the height of the current set text
			// on top
			{
				m.setToTranslation(translation.x, translation.y, translation.z + TRADER_SIZE_Z / 2f + 0.01f);
				m.translate(-width * scaling / 2.0f + dx, height * scaling / 2.0f + dy, 0);
				m.scale(scaling, scaling, 1f);

			}
			batch.setTransformMatrix(m);
			font.setColor(color);
			font.draw(batch, text, 0, 0);
		}
	}

	private void tuneLightIntensity() {
		if (lightIsOne) {
			if (stone.isVanishing()) {
				if (lightIntensity < VANISHING_LIGHT_INTENSITY)
					lightIntensity += Math.signum(VANISHING_LIGHT_INTENSITY - lightIntensity) * 0.3f;
			} else {
				if (lightIntensity < NORMAL_LIGHT_INTENSITY)
					lightIntensity += Math.signum(NORMAL_LIGHT_INTENSITY - lightIntensity) * 0.1f;
			}
			for (PointLight pl : pointLight) {
				pl.intensity = lightIntensity;
			}
		}
	}

	private void turnLightOff(final GameEngine gameEngine) {
		if (lightIsOne) {
			for (PointLight pl : pointLight) {
				gameEngine.renderEngine.remove(pl, true);
			}
			lightIsOne = false;
		}
	}

	private void turnLightOn(final GameEngine gameEngine) {
		if (!lightIsOne) {
			lightIntensity = 0f;
			Color color;
			if (gameEngine.renderEngine.isPbr()) {
				Material			material	= instance.instance.model.materials.get(0);
				Attribute			attribute	= material.get(PBRColorAttribute.BaseColorFactor);
				PBRColorAttribute	a			= (PBRColorAttribute) attribute;
				color = a.color;
			} else {
				Material		material	= instance.instance.model.materials.get(0);
				Attribute		attribute	= material.get(ColorAttribute.Diffuse);
				ColorAttribute	a			= (ColorAttribute) attribute;
				color = a.color;
			}

			final PointLight light = new PointLight().set(color, 0f, 0f, 0f, lightIntensity);
			pointLight.add(light);
			gameEngine.renderEngine.add(light, true);
			lightIsOne = true;
		}

	}

	@Override
	public void update(final float x, final float y, final float z, final GameEngine gameEngine, final long currentTime, final float timeOfDay, final int index, final boolean selected) throws Exception {
		float fraction = (float) (gameEngine.context.levelManager.animationPhase) / ((float) gameEngine.context.levelManager.maxAnimaltionPhase);
		if (stone.isVanishing()) {
			stone.setTx(x);
			stone.setTy(y);
		} else if (stone.isDropping()) {
			stone.setTx(x);
			stone.setTy(y - fraction * TRADER_SIZE_Y);
		} else if (stone.isMovingLeft()) {
			stone.setTx(x + fraction * TRADER_SIZE_X);
			stone.setTy(y);
		} else if (stone.isMovingRight()) {
			stone.setTx(x - fraction * TRADER_SIZE_X);
			stone.setTy(y);
		} else {
			stone.setTx(x);
			stone.setTy(y);
		}
		float scaleDx = 0;

		if (stone.isRightAttached())
			scaleDx += 0.2f;
		if (stone.isLeftAttached())
			scaleDx += 0.2f;

		if (stone.isRightAttached() && !stone.isLeftAttached())
			translation.x = stone.tx + 0.1f;
		else if (!stone.isRightAttached() && stone.isLeftAttached())
			translation.x = stone.tx - 0.1f;
		else
			translation.x = stone.tx;
		translation.y = -stone.ty;
		translation.z = 0;
		for (PointLight pl : pointLight) {
			pl.setPosition(translation);
		}

		{
			instance.instance.transform.setToTranslation(translation);
			if (stone.isVanishing()) {
				instance.instance.transform.scale(fraction, fraction, fraction);
			} else {
				instance.instance.transform.scale(1f + scaleDx, 1f, 1f);
			}
			instance.update();

		}

	}
}
