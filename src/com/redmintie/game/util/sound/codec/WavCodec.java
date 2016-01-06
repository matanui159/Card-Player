package com.redmintie.game.util.sound.codec;

import static org.lwjgl.openal.AL10.AL_FORMAT_MONO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_MONO8;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO8;
import static org.lwjgl.system.MemoryUtil.memAlloc;
import static org.lwjgl.system.MemoryUtil.memFree;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

// Only reads the 'canonical' wave format, as described here:
//			http://soundfile.sapp.org/doc/WaveFormat/
public class WavCodec implements Codec {
	private static final int RIFF = 0x52494646; // 'RIFF'
	private static final int RIFX = 0x5249465A; // 'RIFX'
	private static final int WAVE = 0x57415645; // 'WAVE'
	private static final int FMT  = 0x666D7420; // 'fmt '
	private static final int DATA = 0x64617461; // 'data'
	
	private static int readBigEndianInt(ByteBuffer buffer, int pos) {
		return (buffer.get(pos) << 24) | (buffer.get(pos + 1) << 16)
				| (buffer.get(pos + 2) << 8) | buffer.get(pos + 3);
	}
	private static void lookForChunk(ByteBuffer buffer, int name) {
		int pos = buffer.position();
		while (readBigEndianInt(buffer, pos) != name) {
			pos += 8;
			buffer.position(pos += buffer.getInt(pos - 4));
		}
		buffer.position(pos + 4);
	}
	private static void unsupportedAudio() throws IOException {
		throw new IOException("Unsupported WAV audio format.");
	}
	public static boolean checkHeader(ByteBuffer buffer) {
		int head = readBigEndianInt(buffer, 0);
		if (head == RIFF || head == RIFX) {
			return readBigEndianInt(buffer, 8) == WAVE;
		}
		return false;
	}
	
	private ByteBuffer data;
	private int format;
	private int freq;
	
	public WavCodec(ByteBuffer buffer) throws IOException {
		if (readBigEndianInt(buffer, 0) == RIFF) {
			buffer.order(ByteOrder.LITTLE_ENDIAN);
		} else {
			buffer.order(ByteOrder.BIG_ENDIAN);
		}
		buffer.position(12);
		
		lookForChunk(buffer, FMT);
		int size = buffer.getInt();
		if (buffer.getShort() != 1) {
			unsupportedAudio();
		}
		int channels = buffer.getShort();
		freq = buffer.getInt();
		buffer.position(buffer.position() + 6);
		int sampleSize = buffer.getShort();
		
		if (channels == 1) {
			if (sampleSize == 8) {
				format = AL_FORMAT_MONO8;
			} else if (sampleSize == 16) {
				format = AL_FORMAT_MONO16;
			} else {
				unsupportedAudio();
			}
		} else if (channels == 2) {
			if (sampleSize == 8) {
				format = AL_FORMAT_STEREO8;
			} else if (sampleSize == 16) {
				format = AL_FORMAT_STEREO16;
			} else {
				unsupportedAudio();
			}
		} else {
			unsupportedAudio();
		}
		
		buffer.position(buffer.position() + (size - 16));
		lookForChunk(buffer, DATA);
		size = buffer.getInt();
		int samples = size / (sampleSize / 8);
		data = memAlloc(size);
		for (int i = 0; i < samples; i++) {
			if (sampleSize == 8) {
				data.put(buffer.get());
			} else {
				data.putShort(buffer.getShort());
			}
		}
		data.flip();
	}
	@Override
	public ByteBuffer getData() {
		return data;
	}
	@Override
	public int getFormat() {
		return format;
	}
	@Override
	public int getFrequency() {
		return freq;
	}
	@Override
	public void destroy() {
		memFree(data);
	}
}