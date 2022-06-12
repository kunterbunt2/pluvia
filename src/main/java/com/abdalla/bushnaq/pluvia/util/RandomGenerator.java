package com.abdalla.bushnaq.pluvia.util;

import java.util.Random;

public class RandomGenerator {
	public int				index;
	private final Random	random;

	public RandomGenerator(final int seed) {
		random = new Random(seed);
	}

	public float nextFloat() {
		return random.nextFloat();
	}

	public int nextInt(final float bound) {
		return random.nextInt((int) bound);
	}

	public int nextInt(final int bound) {
		return random.nextInt(bound);
	}

	public int nextInt(final long currentTime, final Object who, final int bound) {
		if (bound == 0)
			return 0;
		final int nextInt = random.nextInt(bound);
		return nextInt;
	}
}
