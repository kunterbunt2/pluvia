package com.abdalla.bushnaq.pluvia.renderer.shader;

import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
import com.badlogic.gdx.math.Plane;

public interface MercatorShaderProviderInterface extends ShaderProvider {
	@Override
	public void dispose();

	public void setClippingPlane(final Plane clippingPlane);

//	public void setWaterAttribute(WaterAttribute waterAttribute);

}
