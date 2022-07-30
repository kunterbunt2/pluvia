package de.bushnaq.abdalla.pluvia.launcher;

import de.bushnaq.abdalla.engine.IContextFactory;
import de.bushnaq.abdalla.pluvia.desktop.Context;

public class IosContextFactory implements IContextFactory {
	private Context context;

	@Override
	public Context create() {
		if (context == null) {
			context = new IosContext();
		}
		return getContext();
	}

	Context getContext() {
		return context;
	}

}
