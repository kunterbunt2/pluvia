package com.abdalla.bushnaq.audio.synthesis;

import java.io.IOException;

import com.badlogic.gdx.math.Vector3;

public abstract class AbstractAudioProducer implements AudioProducer {
	protected boolean		enabled		= false;		// a disabled synth does not possess an audio source and any of the source attached resource like filters and buffers
	protected float			gain		= 1.0f;
	protected boolean		play		= false;		// is the source playing?
	protected final Vector3	position	= new Vector3();// position of the audio source
	protected OpenAlSource	source		= null;			// if enabled, this will hold the attached openal source, otherwise null
	protected final Vector3	velocity	= new Vector3();// velocity of the audio source

	/**
	 * adapt synthesizer to the current source velocity
	 *
	 * @param speed
	 * @throws OpenAlException
	 */
	@Override
	public void adaptToVelocity(final float speed, final float minSpeed, final float maxSpeed) throws OpenAlException {
	}

	@Override
	public OpenAlSource disable() throws OpenAlException {
		enabled = false;
		source.pause();
		final OpenAlSource sourceBuffer = source;
		source = null;
		return sourceBuffer;
	}

	@Override
	public void dispose() throws OpenAlException {
		if (isEnabled())
			source.dispose();
	}

	@Override
	public void enable(final OpenAlSource source) throws OpenAlException {
		enabled = true;
		this.source = source;
		this.source.attach(this);
		this.source.setGain(gain);
		if (isPlaying())
			this.source.play();// we should be playing
		this.source.unparkOrStartThread();
	}

	public float getGain() {
		return gain;
	}

	@Override
	public Vector3 getPosition() {
		return position;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public boolean isKeepCopy() throws OpenAlcException {
		if (isEnabled()) {
			return source.isKeepCopy();
		} else {
			throw new OpenAlcException("Synth is disabled");
		}
	}

	@Override
	public boolean isPlaying() throws OpenAlException {
		return play;
	}

	@Override
	public void pause() throws OpenAlException {
		if (this.play) {
			play = false;
		}
		if (isEnabled())
			source.pause();
	}

	@Override
	public void play() throws OpenAlException {
		if (!this.play) {
			play = true;
		}
		if (isEnabled())
			source.play();
	}

	/**
	 * Convenience method used for debugging
	 *
	 * @throws OpenAlcException
	 */
	public void renderBuffer() throws OpenAlcException {
		if (isEnabled()) {
			source.renderBuffer();
		} else {
			throw new OpenAlcException("Synth is disabled");
		}
	}

	@Override
	public void setGain(final float gain) throws OpenAlException {
		if (Math.abs(this.gain - gain) > 0.1f && isEnabled()) {
			source.setGain(gain);
		}
		this.gain = gain;
	}

	public void setKeepCopy(final boolean enable) throws OpenAlcException {
		if (isEnabled()) {
			source.setKeepCopy(enable);
		} else {
			throw new OpenAlcException("Synth is disabled");
		}
	}

	@Override
	public void setPositionAndVelocity(final float[] position, final float[] velocity, float minSpeed, float maxSpeed) throws OpenAlException {
		if (this.getPosition().x != position[0] || this.getPosition().y != position[1] || this.getPosition().z != position[2]) {
			this.getPosition().set(position[0], position[1], position[2]);
		}
		if (isEnabled()) {
			source.setPosition(position);
			// source.setPosition(new float[] {0,0,0});
		}
		if (this.velocity.x != velocity[0] || this.velocity.y != velocity[1] || this.velocity.z != velocity[2]) {
			this.velocity.set(velocity[0], velocity[1], velocity[2]);
			adaptToVelocity(this.velocity.len(), minSpeed, maxSpeed);
		}
		if (isEnabled()) {
			// source.setVelocity(position, velocity);
		}
	}

	@Override
	public void waitForPlay() throws InterruptedException, OpenAlException {
		if (isEnabled()) {
			do {
				Thread.sleep(100); // should use a thread sleep NOT sleep() for a more responsive finish
			} while (source.isPlaying());
		}
	}

	@Override
	public void writeWav(final String fileName) throws IOException, OpenAlcException {
		if (isEnabled()) {
			source.writeWav(fileName);
		} else {
			throw new OpenAlcException("Synth is disabled");
		}
	}

}
