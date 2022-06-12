package com.abdalla.bushnaq.audio.synthesis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SinOscillator implements Oscilator {
	double					delta			= 0.0;
	double					lastDelta		= 0.0;
	double					lastValue		= 0.0;
	private float			lfoDepth		= 0;										// cent
	private float			lfoFreq			= 1;										// Hz
	private final Logger	logger			= LoggerFactory.getLogger(this.getClass());
	public float			maxFrequency	= 0f;
	public float			minFrequency	= 1000000f;
	// private volatile double lastOscFreq = 440f;
	private volatile double	oscFreq			= 440f;

	private double			oscFreqShift	= 0.0f;

	double					phase			= 0.0;

	// private double targetOscFreq = 440f;
	private int				samplerate;

	public SinOscillator() {
	}

	@Override
	public void dispose() {
		// logger.info(String.format("minFrequency=%f oscFreq=%f maxFrequency=%f", minFrequency, oscFreq, maxFrequency));
	}

	@Override
	public double gen(final long i) {
		// final float oscFreqDetune = (float) (lfoDepth / 10 * Math.sin((2 * Math.PI * lfoFreq / samplerate) * i));
		// slowly change frequency to prevent crackling
		// if (Math.abs(oscFreq - targetOscFreq) > Math.abs(oscFreq + oscFreqShift - targetOscFreq)) {
		// oscFreq += oscFreqShift;
		// // logger.info(String.format("oscFreq %f", oscFreq));
		// } else {
		// oscFreqShift = 0.0f;
		// }
		final float		oscFreqDetune		= (float) (lfoDepth / 20 * Math.sin((2 * Math.PI * lfoFreq / samplerate) * i));
		final float		oscFreqWithVibrato	= (float) (oscFreq * Math.pow(2, oscFreqDetune / 1200));
		// minFrequency = Math.min(minFrequency, oscFreqWithVibrato);
		// maxFrequency = Math.max(maxFrequency, oscFreqWithVibrato);

		// final double value = Math.sin(((2 * Math.PI * oscFreqWithVibrato) / samplerate) * i);
		// logger.info(String.format("%f", oscFreq));
		// if (oscFreq != lastOscFreq) {
		// phase alignment needed
		// double a = (lastDelta + ((2 * Math.PI * (lastOscFreq)) / samplerate) * (i - 1)) % (2 * Math.PI);
		// double b = (lastDelta + ((2 * Math.PI * (oscFreq)) / samplerate) * (i - 1)) % (2 * Math.PI);
		// delta = a - b;
		// a = lastOscFreq - oscFreq;
		// int j = 0;
		// final double value1 = Math.sin(lastDelta + ((2 * Math.PI * (lastOscFreq)) / samplerate) * (i - 1));
		// final double value2 = Math.sin(lastDelta + delta + ((2 * Math.PI * (oscFreq)) / samplerate) * (i - 1));
		// if (Math.abs(value1 - value2) > 0.0000001) {
		// logger.info(String.format("%f %f %f%%", value1, value2, (value1 - value2) * 100));
		// }
		// lastDelta = delta;
		// }
		final double	phaseDelta			= 2 * Math.PI * (oscFreqWithVibrato / samplerate);
		phase += phaseDelta;
		// final double value = Math.sin(delta + ((2 * Math.PI * (oscFreq)) / samplerate) * i);
		final double value = Math.sin(phase);
		lastValue = value;
		// lastOscFreq = oscFreq;
		return value;
	}

	@Override
	public double getFrequency() {
		return oscFreq;
	}

	public double getOscFreqShift() {
		return oscFreqShift;
	}

	public void setFrequency(final double frequency) {
		oscFreq = frequency;
		// lastOscFreq = frequency;
	}

	/**
	 * Vibrato control of the Oscillator
	 *
	 * @param lfoFreq  in Hz
	 * @param lfoDepth in cent
	 */
	@Override
	public void setLfo(final float lfoFreq, final float lfoDepth) {
		this.lfoFreq = lfoFreq;
		this.lfoDepth = lfoDepth;
	}

	public void setOscFreqShift(final double oscFreqShift) {
		this.oscFreqShift = oscFreqShift;
	}

	@Override
	public void setOscillator(final float targetOscFreq) {
		this.oscFreq = targetOscFreq;
		// this.targetOscFreq = targetOscFreq;
		// oscFreqShift = Math.signum(targetOscFreq - oscFreq) / (4410 / 4);
	}

	@Override
	public void setSampleRate(final int samplerate) {
		this.samplerate = samplerate;
	}
}