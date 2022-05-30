package com.abdalla.bushnaq.pluvia.engine.camera;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abdalla.bushnaq.pluvia.desktop.Context;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;

public class MyCameraInputController extends CameraInputController {

	private final Logger	logger	= LoggerFactory.getLogger(this.getClass());
	private final Vector3	tmpV1	= new Vector3();
//	private final Vector3	tmpV2	= new Vector3();

	public MyCameraInputController(final Camera camera) throws Exception {
		super(camera);
		rotateButton = Buttons.MIDDLE;
		pinchZoomFactor = 0.1f / Context.WORLD_SCALE;
	}

//	@Override
//	protected boolean process(final float deltaX, final float deltaY, final int button) {
//		try {
//			final MovingCamera myCamera = (MovingCamera) camera;
//			if (button == rotateButton) {
//				myCamera.setDirty(true);
//				tmpV1.set(myCamera.direction).crs(myCamera.up)/* .y = 0f */;
//				myCamera.rotateAround(myCamera.lookat, tmpV1.nor(), deltaY * rotateAngle);
//				myCamera.rotateAround(myCamera.lookat, Vector3.Y, deltaX * -rotateAngle);
//			} else {
//				return false;
//			}
//			if (autoUpdate)
//				myCamera.update();
//		} catch (final Exception e) {
//			logger.error(e.getMessage(), e);
//		}
//		return true;
//	}

	@Override
	public boolean zoom(final float amount) {
		try {
			if (!alwaysScroll && activateKey != 0 && !activatePressed)
				return false;
			final MovingCamera myCamera = (MovingCamera) camera;
			myCamera.translate(0, 0, -amount * pinchZoomFactor);
			myCamera.setDirty(true);
			// notifyListener(myCamera);
			if (autoUpdate)
				camera.update();
		} catch (final Exception e) {
			logger.error(e.getMessage(), e);
		}
		return true;
	}

}
