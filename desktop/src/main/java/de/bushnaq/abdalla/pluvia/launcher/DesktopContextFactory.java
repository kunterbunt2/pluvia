package de.bushnaq.abdalla.pluvia.launcher;

import de.bushnaq.abdalla.pluvia.desktop.Context;
import de.bushnaq.abdalla.pluvia.desktop.IContextFactory;

public class DesktopContextFactory implements IContextFactory {
	private Context context;

	@Override
	public Context create() {
		if (context == null) {
			context = new DesktopContext();
		}
		return getContext();
	}

	Context getContext() {
		return context;
	}

}
