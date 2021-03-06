package com.abdalla.bushnaq.pluvia.engine.shader.water;

import com.abdalla.bushnaq.pluvia.desktop.Context;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer.FrameBufferBuilder;

public class Water {
	private boolean		present					= false;
	private FrameBuffer	reflectionFbo;
	private FrameBuffer	refractionFbo;
	private float		refractiveMultiplicator	= 1.0f;
	private float		tiling					= 4f;
	private float		waveSpeed				= 0.01f;
	private float		waveStrength			= 0.007f;

	public Water() {

	}

//	public Water(boolean present, float tiling, float waveSpeed, float waveStrength) {
//		this.present = present;
//		this.tiling = tiling;
//		this.waveSpeed = waveSpeed;
//		this.waveStrength = waveStrength;
//	}

	public void createFrameBuffer(Context context) {
		{
			final FrameBufferBuilder frameBufferBuilder = new FrameBufferBuilder(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			frameBufferBuilder.addColorTextureAttachment(GL30.GL_RGBA8, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE);
			if (Context.isIos()) {
				frameBufferBuilder.addDepthRenderBuffer(GL30.GL_DEPTH_COMPONENT24);// ios
			} else {
				frameBufferBuilder.addDepthTextureAttachment(GL30.GL_DEPTH_COMPONENT24, GL20.GL_UNSIGNED_BYTE);
			}
			refractionFbo = frameBufferBuilder.build();
		}
		{
			final FrameBufferBuilder frameBufferBuilder = new FrameBufferBuilder(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			frameBufferBuilder.addColorTextureAttachment(GL30.GL_RGBA8, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE);
			frameBufferBuilder.addDepthRenderBuffer(GL30.GL_DEPTH_COMPONENT24);
			reflectionFbo = frameBufferBuilder.build();
		}
	}

	public void dispose() {
		reflectionFbo.dispose();
		refractionFbo.dispose();
	}

	public FrameBuffer getReflectionFbo() {
		return reflectionFbo;
	}

	public FrameBuffer getRefractionFbo() {
		return refractionFbo;
	}

	public float getRefractiveMultiplicator() {
		return refractiveMultiplicator;
	}

	public float getTiling() {
		return tiling;
	}

	public float getWaveSpeed() {
		return waveSpeed;
	}

	public float getWaveStrength() {
		return waveStrength;
	}

	public boolean isPresent() {
		return present;
	}

	public void setPresent(boolean present) {
		this.present = present;
	}

	public void setRefractiveMultiplicator(float refractiveMultiplicator) {
		this.refractiveMultiplicator = refractiveMultiplicator;
	}

	public void setTiling(float tiling) {
		this.tiling = tiling;
	}

	public void setWaveSpeed(float waveSpeed) {
		this.waveSpeed = waveSpeed;
	}

	public void setWaveStrength(float waveStrength) {
		this.waveStrength = waveStrength;
	}

}
