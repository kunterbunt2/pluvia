package com.abdalla.bushnaq.audio.synthesis;

/**
 * use 2 saw oscillators with 5 cent vibrato, one generator is detuned -2.5 cent while the other is detuned 2.5 cent
 *
 * @author abdal
 *
 */
public class MercatorSynthesizer extends Synthesizer {

	private static final float	HIGHEST_FREQUENCY	= 6 * 261.6256f;// C5
	private static final float	LOWEST_FREQUENCY	= 32.70320f;	// C1

	private final Lfo			lfo1;
	private final SinOscillator	saw1;
	private final SinOscillator	saw2;
	private final SinOscillator	saw3;
	private final SinOscillator	saw4;
	private final SinOscillator	sin1;
	private final SinOscillator	sin2;

	public MercatorSynthesizer() throws OpenAlException {
		super(44100);

		saw1 = new SinOscillator();
		add(saw1);
		saw2 = new SinOscillator();
		add(saw2);
		saw3 = new SinOscillator();
		add(saw3);
		saw4 = new SinOscillator();
		add(saw4);
		sin1 = new SinOscillator();
		add(sin1);
		sin2 = new SinOscillator();
		add(sin2);

		lfo1 = new Lfo1();
		add(lfo1);
		adaptToVelocity(15, 5, 25);
	}

	// c = 1200 × log2(f2 / f1)
	// f1*2^(c/1200) = f2
	// log 2 = 0.301029995

	@Override
	public void adaptToVelocity(float speed, float minSpeed, float maxSpeed) throws OpenAlException {
		speed = Math.min(maxSpeed, speed);
		speed = Math.max(minSpeed, speed);

		final float	frequency	= LOWEST_FREQUENCY + (HIGHEST_FREQUENCY - LOWEST_FREQUENCY) * (speed - minSpeed) / (maxSpeed - minSpeed);
		final float	detune		= 2.5f;																										// cent
		final float	frequency1	= (float) (frequency * Math.pow(2, detune / 1200));

		final float	bassGain	= 1 - (speed - minSpeed) / (maxSpeed - minSpeed);

		// sin1.setOscillator(frequency1);
		// sin1.setLfo(1f, 5f);
		saw1.setOscillator(frequency1);
		saw1.setLfo(.87f, 5f);
		// System.out.println(String.format("speed=%f frequency=%f", speed, frequency));

		final float frequency2 = (float) (frequency * Math.pow(2, -detune / 1200));
		saw2.setOscillator(frequency2);
		saw2.setLfo(0.86f, 5f);

		saw3.setOscillator(frequency1 / 2);
		saw3.setLfo(.88f, 15f);
		//
		saw4.setOscillator(frequency2 / 2);
		saw4.setLfo(.85f, 15f);
		//
		sin1.setOscillator(frequency1 * 2);
		sin1.setLfo(.88f, 15f);
		//
		sin2.setOscillator(frequency2 * 2);
		sin2.setLfo(.85f, 15f);

		final float factor = (speed - minSpeed) / (maxSpeed - minSpeed);
		// setGain( 10.0f);
		setGain(0.1f + bassGain * 20);
		lfo1.setFrequency(1f + 5f * factor, 0.1f);
		// lfo1.setFrequency(1f, 1.0f);

		final float	lowGain		= bassGain;
		final float	highGain	= (1 - bassGain);
		// setFilter(true);
		setFilterGain(lowGain, highGain);
		if (lowGain < 0.5f) {
			setFilter(false);
			setBassBoost(false);
		} else {
			setFilter(true);
			setBassBoost(true);
		}
		setBassBoostGain(frequency / 2, bassGain * (24));
	}

}
