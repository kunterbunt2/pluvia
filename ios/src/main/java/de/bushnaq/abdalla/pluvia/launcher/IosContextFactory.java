package de.bushnaq.abdalla.pluvia.launcher;

import de.bushnaq.abdalla.pluvia.desktop.Context;
import de.bushnaq.abdalla.pluvia.desktop.IContextFactory;

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
