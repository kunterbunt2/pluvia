package com.abdalla.bushnaq.pluvia.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer.FrameBufferBuilder;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

class Frame {
	long delta;
}

public class TimeGraph extends Array<Frame> {
	private static final long	FACTOR		= 100000L;
	long						absolute	= 0;
	long						delta		= 0;
	private int					maxFrames;
	ScreenViewport				viewport	= new ScreenViewport();
	private FrameBuffer			fbo;
	private Color				color;

	public TimeGraph(Color color, int width, int height) {
		this.color = color;
		this.maxFrames = width;
		viewport.update(width, height, true);
		{
			final FrameBufferBuilder frameBufferBuilder = new FrameBufferBuilder(width, height);
			frameBufferBuilder.addColorTextureAttachment(GL30.GL_RGBA8, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE);
			fbo = frameBufferBuilder.build();
		}
	}

	public void dispose() {
		fbo.dispose();
	}

	public void begin() {
		absolute = System.nanoTime();

	}

	public void end() {
		delta = (System.nanoTime() - absolute) / FACTOR;
	}

	public void update() {
		{
			Frame frame;
			if (size == maxFrames) {
				frame = removeIndex(0);
			} else {
				frame = new Frame();
			}
			frame.delta = delta;
			add(frame);
		}
	}

	public void draw(PolygonSpriteBatch batch2D, AtlasManager atlasManager) {
		{
			batch2D.setProjectionMatrix(viewport.getCamera().combined);
			fbo.begin();
			batch2D.begin();
			batch2D.enableBlending();
			Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
			Gdx.gl.glClearColor(0, 0, 0, 0);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			{
				for (int y = 50; y < 500; y += 50) {
					BitmapFont			font	= atlasManager.smallFont;
					String				text	= "" + y / 10 + "ms";
					final GlyphLayout	layout	= new GlyphLayout();
					layout.setText(font, text);
					final float width = layout.width;// contains the width of the current set text
					batch2D.draw(atlasManager.systemTextureRegion, 0, y, 5, 1);
					font.draw(batch2D, text, 6, y + layout.height / 2, width, Align.left, false);
				}
			}

			for (int i = 0; i < size; i++) {
				Frame frame = get(i);
				batch2D.setColor(color);
				batch2D.draw(atlasManager.systemTextureRegion, i, 0, 1, frame.delta);
			}
			batch2D.setColor(Color.WHITE);
			batch2D.end();
			fbo.end();
		}
	}

	public FrameBuffer getFbo() {
		return fbo;
	}

}
