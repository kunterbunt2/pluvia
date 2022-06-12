package com.abdalla.bushnaq.pluvia.engine.shader;

import com.abdalla.bushnaq.pluvia.engine.shader.mirror.Mirror;
import com.abdalla.bushnaq.pluvia.engine.shader.mirror.MirrorShader;
import com.abdalla.bushnaq.pluvia.engine.shader.water.Water;
import com.abdalla.bushnaq.pluvia.engine.shader.water.WaterShader;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader.Config;
import com.badlogic.gdx.math.Plane;

import net.mgsx.gltf.scene3d.shaders.PBRShader;
import net.mgsx.gltf.scene3d.shaders.PBRShaderConfig;
import net.mgsx.gltf.scene3d.shaders.PBRShaderProvider;

public class GamePbrShaderProvider extends PBRShaderProvider implements GameShaderProviderInterface {

	public static GamePbrShaderProvider createDefault(final PBRShaderConfig config, final Water water, final Mirror mirror) {
		return new GamePbrShaderProvider(config, water, mirror);
	}

	private Plane		clippingPlane;
	private Mirror		mirror;
	public MirrorShader	mirrorShader;
	public MyPBRShader	pbrShader;
//	private final float			waterTiling;
//	private float				waveSpeed;
//	private float				waveStrength;
	private Water		water;
	// private final FrameBuffer waterReflectionFbo;
//	private final FrameBuffer	waterRefractionFbo;
	public WaterShader	waterShader;

	public GamePbrShaderProvider(final PBRShaderConfig config, final Water water, final Mirror mirror) {
		super(config);
//		this.waterTiling = waterTiling;
//		this.waveStrength = waveStrength;
//		this.waveSpeed = waveSpeed;
		this.water = water;
		this.mirror = mirror;
	}

	private Shader createMirrorShader(final Renderable renderable) {
		final String	prefix	= createPrefixBase(renderable, config);
		final Config	config	= new Config();
		config.vertexShader = Gdx.files.internal("shader/mirror.vertex.glsl").readString();
		config.fragmentShader = Gdx.files.internal("shader/mirror.fragment.glsl").readString();
		mirrorShader = new MirrorShader(renderable, config, prefix, mirror);
		mirrorShader.setClippingPlane(clippingPlane);
		return mirrorShader;

	}

	private MyPBRShader createPBRShader(final Renderable renderable) {
		pbrShader = (MyPBRShader) super.createShader(renderable);
		pbrShader.setClippingPlane(clippingPlane);
		return pbrShader;
	}

	public String createPrefixBase(final Renderable renderable, final Config config) {

		final String	defaultPrefix	= DefaultShader.createPrefix(renderable, config);
		String			version			= null;
		if (isGL3()) {
			if (Gdx.app.getType() == ApplicationType.Desktop) {
				if (version == null)
					version = "#version 130\n" + "#define GLSL3\n";
			} else if (Gdx.app.getType() == ApplicationType.Android) {
				if (version == null)
					version = "#version 300 es\n" + "#define GLSL3\n";
			}
		}
		String prefix = "";
		if (version != null)
			prefix += version;
		// if (config.prefix != null)
		// prefix += config.prefix;
		prefix += defaultPrefix;

		return prefix;
	}

	@Override
	protected Shader createShader(final Renderable renderable) {
		if (renderable.material.id.equals("water")) {
			return createWaterShader(renderable);
		} else if (renderable.material.id.equals("mirror")) {
			return createMirrorShader(renderable);
		} else
			return createPBRShader(renderable);
	}

	@Override
	protected PBRShader createShader(final Renderable renderable, final PBRShaderConfig config, final String prefix) {
		return new MyPBRShader(renderable, config, prefix);
	}

	private Shader createWaterShader(final Renderable renderable) {
		final String	prefix	= createPrefixBase(renderable, config);
		final Config	config	= new Config();
		config.vertexShader = Gdx.files.internal("shader/water.vertex.glsl").readString();
		config.fragmentShader = Gdx.files.internal("shader/water.fragment.glsl").readString();
		waterShader = new WaterShader(renderable, config, prefix, water);
//		setWaterAttribute(waterAttribute);
		waterShader.setClippingPlane(clippingPlane);
		return waterShader;

	}

	@Override
	public void dispose() {
		// pbrShader.dispose();
		// waterShader.dispose();
		super.dispose();
	}

	@Override
	protected boolean isGL3() {
		return Gdx.graphics.getGLVersion().isVersionEqualToOrHigher(3, 0);
	}

	@Override
	public void setClippingPlane(final Plane clippingPlane) {
		this.clippingPlane = clippingPlane;
		if (waterShader != null) {
			waterShader.setClippingPlane(clippingPlane);
		}
		if (mirrorShader != null) {
			mirrorShader.setClippingPlane(clippingPlane);
		}
		if (pbrShader != null) {
			pbrShader.setClippingPlane(clippingPlane);
		}
	}

//	@Override
//	public void setWaterAttribute(WaterAttribute waterAttribute) {
//		waterShader.setTiling(waterAttribute.getTiling() * 2 * /* 4 * 2 **/ 3.0f / Context.WORLD_SCALE);
//		waterShader.setWaveStrength(waterAttribute.getWaveStrength());
//		waterShader.setWaveSpeed(waterAttribute.getWaveSpeed());
//	}

}
