package com.abdalla.bushnaq.audio.synthesis;

import static org.lwjgl.openal.AL10.AL_FORMAT_MONO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;

import java.nio.ByteBuffer;

import com.badlogic.gdx.backends.lwjgl3.audio.OggInputStream;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.StreamUtils;

public class Mp3Player extends AbstractAudioProducer {
	static private final int bufferSize = 4096 * 10;
	static private final int bytesPerSample = 2;
	int channels;
	//	static private final int bufferSize = 4096 * 10;
	//	static private final int bytesPerSample = 2;
	//	private Bitstream bitstream;
	//	private OutputBuffer outputBuffer;
	//	private MP3Decoder decoder;
	//	private int format, sampleRate;
	//	private float renderedSeconds, maxSecondsPerBuffer;
	protected FileHandle file;
	private int format, sampleRate;
	private OggInputStream input;

	private OggInputStream previousInput;
	private float renderedSeconds, maxSecondsPerBuffer;

	public Mp3Player() {
	}

	@Override
	public int getChannels() {
		return channels;
	}

	@Override
	public int getOpenAlFormat() {
		return this.format;
	}

	@Override
	public void processBuffer(final ByteBuffer byteBuffer) {
		if (input == null) {
			input = new OggInputStream(file.read());
			setup(input.getChannels(), input.getSampleRate());
			previousInput = null; // release this reference
		}
		for (int i = 0; i < byteBuffer.capacity(); i++) {
			final int value = input.read();
			if (value == -1)
				break;
			byteBuffer.put(i, (byte) value);
		}
	}

	public void reset() {
		StreamUtils.closeQuietly(input);
		previousInput = null;
		input = null;
	}

	//	public void setFile(FileHandle file) throws OpenAlException {
	//		this.file = file;
	//		bitstream = new Bitstream(file.read());
	//		decoder = new MP3Decoder();
	//		try {
	//			Header header = bitstream.readFrame();
	//			if (header == null)
	//				throw new GdxRuntimeException("Empty MP3");
	//			channels = header.mode() == Header.SINGLE_CHANNEL ? 1 : 2;
	//			outputBuffer = new OutputBuffer(channels, false);
	//			decoder.setOutputBuffer(outputBuffer);
	//			setup(channels, header.getSampleRate());
	//		} catch (BitstreamException e) {
	//			throw new GdxRuntimeException("error while preloading mp3", e);
	//		}
	//	}
	//
	//	protected void setup(int channels, int sampleRate) {
	//		this.format = channels > 1 ? AL_FORMAT_STEREO16 : AL_FORMAT_MONO16;
	//		this.sampleRate = sampleRate;
	//		maxSecondsPerBuffer = (float) bufferSize / (bytesPerSample * channels * sampleRate);
	//	}
	//
	//	public void processBuffer(ByteBuffer byteBuffer) {
	//		try {
	//			boolean setup = bitstream == null;
	//			if (setup) {
	//				bitstream = new Bitstream(file.read());
	//				decoder = new MP3Decoder();
	//			}
	//
	//			int totalLength = 0;
	//			int minRequiredLength = byteBuffer.capacity() - OutputBuffer.BUFFERSIZE * 2;
	//			while (totalLength <= minRequiredLength) {
	//				Header header = bitstream.readFrame();
	//				if (header == null)
	//					break;
	//				if (setup) {
	//					int channels = header.mode() == Header.SINGLE_CHANNEL ? 1 : 2;
	//					outputBuffer = new OutputBuffer(channels, false);
	//					decoder.setOutputBuffer(outputBuffer);
	//					setup(channels, header.getSampleRate());
	//					setup = false;
	//				}
	//				try {
	//					decoder.decodeFrame(header, bitstream);
	//				} catch (Exception ignored) {
	//					// JLayer's decoder throws ArrayIndexOutOfBoundsException sometimes!?
	//				}
	//				bitstream.closeFrame();
	//
	//				int length = outputBuffer.reset();
	//				for (int i = 0; i < length; i++) {
	//					byteBuffer.put(i, outputBuffer.getBuffer()[i]);
	//				}
	//				//				System.arraycopy(outputBuffer.getBuffer(), 0, buffer, totalLength, length);
	//				totalLength += length;
	//			}
	//			//			return totalLength;
	//		} catch (Throwable ex) {
	//			reset();
	//			throw new GdxRuntimeException("Error reading audio data.", ex);
	//		}
	//	}
	//
	//	public void reset() {
	//		if (bitstream == null)
	//			return;
	//		try {
	//			bitstream.close();
	//		} catch (BitstreamException ignored) {
	//		}
	//		bitstream = null;
	//	}

	public void setFile(final FileHandle file) throws OpenAlException {
		this.file = file;
		input = new OggInputStream(file.read());
		channels = input.getChannels();
		setup(input.getChannels(), input.getSampleRate());
	}

	protected void setup(final int channels, final int sampleRate) {
		this.format = channels > 1 ? AL_FORMAT_STEREO16 : AL_FORMAT_MONO16;
		this.sampleRate = sampleRate;
		maxSecondsPerBuffer = (float) bufferSize / (bytesPerSample * channels * sampleRate);
	}

}
