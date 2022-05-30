package com.abdalla.bushnaq.pluvia.engine.shader.mirror;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.math.Plane;

public class MirrorShader extends DefaultShader {
	private static Plane	clippingPlane;
//	private static final String	DUDV_MAP_FILE_NAME		= "shader/texture/waterDUDV.png";
//	private static final String	NORMAL_MAP_FILE_NAME	= "shader/texture/normal.png";
//	private static FrameBuffer	waterReflectionFbo;
//	private static FrameBuffer	waterRefractionFbo;
//	private float				moveFactor				= 0f;
//	private final Texture		normalMap;
//	private float				tiling;
	private final int		u_clippingPlane		= register("u_clippingPlane");
//	private final int			u_depthMap				= register("u_depthMap");

//	private final int			u_dudvMapTexture		= register("u_dudvMapTexture");
//	private final int			u_moveFactor			= register("u_moveFactor");
	private final int		u_reflectivity		= register("u_reflectivity");
	private final int		u_reflectionTexture	= register("u_reflectionTexture");
//	private final int			u_refractionTexture		= register("u_refractionTexture");
//	private final int			u_tiling				= register("u_tiling");
//	private final int			u_waveStrength			= register("u_waveStrength");
//	private final Texture		waterDuDv;
//	private float				waveSpeed				= 0.03f;
//	private float				waveStrength			= 0.01f;
	private Mirror			mirror;

	public MirrorShader(final Renderable renderable, final Config config, final String prefix, final Mirror mirror) {
		super(renderable, config, prefix);
		this.mirror = mirror;
//		waterDuDv = new Texture(Gdx.files.internal(DUDV_MAP_FILE_NAME));
//		waterDuDv.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
//		normalMap = new Texture(Gdx.files.internal(NORMAL_MAP_FILE_NAME));
//		normalMap.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	}

	@Override
	public void begin(final Camera camera, final RenderContext context) {
		super.begin(camera, context);
		if (clippingPlane != null)
			set(u_clippingPlane, clippingPlane.normal.x, clippingPlane.normal.y, clippingPlane.normal.z, clippingPlane.d);
//		set(u_refractionTexture, mirror.waterRefractionFbo.getColorBufferTexture());
		set(u_reflectionTexture, mirror.getReflectionFbo().getColorBufferTexture());
//		set(u_depthMap, waterRefractionFbo.getTextureAttachments().get(1));
//		set(u_dudvMapTexture, waterDuDv);
//		set(u_normalMap, normalMap);
//		set(u_tiling, mirrorAttribute.getTiling());
//		moveFactor += mirrorAttribute.getWaveSpeed() * Gdx.graphics.getDeltaTime();
//		moveFactor %= 1.0;
//		set(u_moveFactor, moveFactor);
//		set(u_waveStrength, mirrorAttribute.getWaveStrength());
		set(u_reflectivity, mirror.getReflectivity());
	}

	@Override
	public boolean canRender(final Renderable renderable) {
		if (renderable.material.id.equals("mirror"))
			return true;
		else
			return false;
	}

	public String getLog() {
		return program.getLog();
	}

	@Override
	public void render(final Renderable renderable) {
		super.render(renderable);
	}

	public void setClippingPlane(final Plane clippingPlane) {
		MirrorShader.clippingPlane = clippingPlane;
	}

//	public void setTiling(final float tiling) {
//		this.tiling = tiling;
//	}
//
//	public void setWaveSpeed(final float waveSpeed) {
//		this.waveSpeed = waveSpeed;
//	}
//
//	public void setWaveStrength(final float waveStrength) {
//		this.waveStrength = waveStrength;
//	}

}
