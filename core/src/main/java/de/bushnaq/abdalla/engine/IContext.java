package de.bushnaq.abdalla.engine;

import de.bushnaq.abdalla.pluvia.desktop.IApplicationProperties;

/**
 * @author kunterbunt
 *
 */
public interface IContext extends IApplicationProperties {

	void disableClipping();

	void enableClipping();

}