package com.abdalla.bushnaq.pluvia.launcher;

import com.abdalla.bushnaq.pluvia.desktop.Context;
import com.abdalla.bushnaq.pluvia.desktop.IContextFactory;

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
