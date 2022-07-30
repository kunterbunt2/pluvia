package de.bushnaq.abdalla.engine;

import de.bushnaq.abdalla.pluvia.desktop.IApplicationProperties;

public interface IContext extends IApplicationProperties {

	void disableClipping();

	void enableClipping();

}