package com.abdalla.bushnaq.pluvia.util;

import java.util.Random;

/**
 * A random generator that can be resumed later
 * 
 * @author abdal
 *
 */
public class PersistentRandomGenerator {
	protected Random	rand	= null;
	protected int		calls	= 0;
	protected int		seed	= 0;

	public PersistentRandomGenerator() {
		rand = new Random(seed);
	}

	public void set(int randomSeed, int randCalls) {
		this.seed = randomSeed;
		rand = new Random(randomSeed);
		this.calls = 0;
		for (int i = 0; i < randCalls; i++) {
			this.calls++;
			rand.nextInt(1);
		}
	}

	public int nextInt(int bound) {
		calls++;
		return rand.nextInt(bound);
	}

	public int getCalls() {
		return calls;
	}

	public int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		this.seed = seed;
		rand = new Random(seed);
		calls = 0;
	}

}
