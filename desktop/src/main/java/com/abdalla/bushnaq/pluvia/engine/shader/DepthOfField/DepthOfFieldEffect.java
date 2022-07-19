/*******************************************************************************
 * Copyright 2019 metaphore
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.abdalla.bushnaq.pluvia.engine.shader.DepthOfField;

import com.abdalla.bushnaq.pluvia.engine.camera.MovingCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.crashinvaders.vfx.VfxRenderContext;
import com.crashinvaders.vfx.effects.ChainVfxEffect;
import com.crashinvaders.vfx.effects.ShaderVfxEffect;
import com.crashinvaders.vfx.framebuffer.VfxFrameBuffer;
import com.crashinvaders.vfx.framebuffer.VfxPingPongWrapper;
import com.crashinvaders.vfx.gl.VfxGLUtils;

public class DepthOfFieldEffect extends ShaderVfxEffect implements ChainVfxEffect {

	private static final String	Texture0			= "u_sourceTexture";
	private static final String	Texture1			= "u_depthTexture";
	private final MovingCamera	camera;										// 2-100
	private final float			farDistanceBlur		= 150f;
	private final Vector2		focusDistance		= new Vector2(5, 15);	// FousDistance-FacusRange,FousDistance+FacusRange
	private final float			nearDistanceBlur	= 150f;
	private final FrameBuffer	postFbo;
	private final Vector2		resolution			= new Vector2();
	private int					vertical			= 0;

	public DepthOfFieldEffect(final FrameBuffer postFbo, final MovingCamera camera, int vertical) {
		super(MyVfxGLUtils.compileShader(Gdx.files.classpath("shader/depthOfField.vs.glsl"), Gdx.files.classpath("shader/depthOfField.fs.glsl"), ""));
		this.postFbo = postFbo;
		this.camera = camera;
		this.vertical = vertical;
		rebind();
	}

	@Override
	public void rebind() {
		super.rebind();
		program.begin();
		program.setUniformf("u_pixelSize", 1f / postFbo.getWidth(), 1f / postFbo.getHeight());
		program.setUniformf("u_cameraClipping", camera.near, camera.far);
		program.setUniformf("u_focusDistance", focusDistance);
		program.setUniformf("u_nearDistanceBlur", nearDistanceBlur);
		program.setUniformf("u_farDistanceBlur", farDistanceBlur);
		program.setUniformi(Texture0, TEXTURE_HANDLE0);
		program.setUniformi(Texture1, TEXTURE_HANDLE1);
		program.end();
	}

	public void render(final VfxRenderContext context, final VfxFrameBuffer src, final VfxFrameBuffer dst) {
		// Bind src buffer's texture as a primary one.
		program.begin();
		program.setUniformi("u_vertical", vertical);
		program.end();
		src.getTexture().bind(TEXTURE_HANDLE0);
//		postFbo.getColorBufferTexture().bind(TEXTURE_HANDLE0);
		postFbo.getTextureAttachments().get(1).bind(TEXTURE_HANDLE1);
		renderShader(context, dst);
//		program.begin();
//		program.setUniformi("u_vertical", 1);
//		program.end();
//		dst.getTexture().bind(TEXTURE_HANDLE0);
////		postFbo.getColorBufferTexture().bind(TEXTURE_HANDLE0);
//		postFbo.getTextureAttachments().get(1).bind(TEXTURE_HANDLE1);
//		renderShader(context, dst);
	}

	@Override
	public void render(final VfxRenderContext context, final VfxPingPongWrapper buffers) {
		render(context, buffers.getSrcBuffer(), buffers.getDstBuffer());
	}

	@Override
	public void resize(final int width, final int height) {
		super.resize(width, height);
		this.resolution.set(width, height);
		rebind();
	}

	@Override
	public void update(final float delta) {
		super.update(delta);
	}

}

class MyVfxGLUtils extends VfxGLUtils {
	private static boolean		blurBackground	= false;
	private static int			maxBlur			= 50;
	private static final String	TAG				= MyVfxGLUtils.class.getSimpleName();

	public static ShaderProgram compileShader(final FileHandle vertexFile, final FileHandle fragmentFile, final String defines) {
		if (fragmentFile == null) {
			throw new IllegalArgumentException("Vertex shader file cannot be null.");
		}
		if (vertexFile == null) {
			throw new IllegalArgumentException("Fragment shader file cannot be null.");
		}
		if (defines == null) {
			throw new IllegalArgumentException("Defines cannot be null.");
		}

		final StringBuilder sb = new StringBuilder();
		sb.append("Compiling \"").append(vertexFile.name()).append('/').append(fragmentFile.name()).append('\"');
		if (defines.length() > 0) {
			sb.append(" w/ (").append(defines.replace("\n", ", ")).append(")");
		}
		sb.append("...");
//		Gdx.app.log(TAG, sb.toString());

		final String		prependVert	= prependVertexCode + defines;
		final String		prependFrag	= prependFragmentCode + defines;
		final String		srcVert		= vertexFile.readString();
		final String		srcFrag		= exchange(fragmentFile.readString());

		final ShaderProgram	shader		= new ShaderProgram(prependVert + "\n" + srcVert, prependFrag + "\n" + srcFrag);

		if (!shader.isCompiled()) {
			throw new GdxRuntimeException("Shader compile error: " + vertexFile.name() + "/" + fragmentFile.name() + "\n" + shader.getLog());
		}
		return shader;
	}

	public static String exchange(String shader) {
		shader = shader.replaceAll("MAX_BLUR", String.valueOf(maxBlur));
		shader = shader.replaceAll("BLUR_BACKGROUND", String.valueOf(blurBackground));
		return shader;
	}
}
