package com.abdalla.bushnaq.pluvia.scene;

public class TwinBuilding {
	float	chanceHorizontal	= 0.2f;
	float	chanceVertical		= 0.2f;
	int		deltaX				= 0;
	int		deltaZ				= 0;
	boolean	occupided			= false;

	public TwinBuilding(final float chanceHorizontal, final float chanceVertical, final int deltaX, final int deltaZ) {
		this.chanceHorizontal = chanceHorizontal;
		this.chanceVertical = chanceVertical;
		this.deltaX = deltaX;
		this.deltaZ = deltaZ;
	}
}