package com.redmintie.game.util.sound.codec;

import static org.lwjgl.openal.AL10.AL_FORMAT_MONO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_MONO8;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO8;
import static org.lwjgl.openal.EXTAlaw.AL_FORMAT_MONO_ALAW_EXT;
import static org.lwjgl.openal.EXTAlaw.AL_FORMAT_STEREO_ALAW_EXT;
import static org.lwjgl.openal.EXTFloat32.AL_FORMAT_MONO_FLOAT32;
import static org.lwjgl.openal.EXTFloat32.AL_FORMAT_STEREO_FLOAT32;
import static org.lwjgl.openal.EXTMulaw.AL_FORMAT_MONO_MULAW_EXT;
import static org.lwjgl.openal.EXTMulaw.AL_FORMAT_STEREO_MULAW_EXT;
import static org.lwjgl.system.MemoryUtil.memAlloc;
import static org.lwjgl.system.MemoryUtil.memFree;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.redmintie.game.util.core.Flags;

/** 
 * Supported audio formats:
 *  - PCM (up to 16 bit)
 *  - IEEE Float (only 32 bit)
 *  - A-Law (only 8 bit)
 *  - MU-Law (only 8 bit)
 * NOTE: supports unstandard sample sizes like 12-bit
 */
public class WavCodec implements Codec {
	private static final int RIFF = 0x52494646; // 'RIFF'
	private static final int RIFX = 0x5249465A; // 'RIFX'
	private static final int WAVE = 0x57415645; // 'WAVE'
	private static final int FMT  = 0x666D7420; // 'fmt '
	private static final int DATA = 0x64617461; // 'data'
	
	private static final int TYPE_PCM = 1;
	private static final int TYPE_FLOAT = 3;
	private static final int TYPE_ALAW = 6;
	private static final int TYPE_MULAW = 7;
	
	private static void log(String msg) {
		if (Flags.DEBUG) {
			System.err.println("[CODEC] " + msg);
		}
	}
	private static int readBigEndianInt(ByteBuffer buffer, int pos) {
		return (buffer.get(pos) << 24) | (buffer.get(pos + 1) << 16)
				| (buffer.get(pos + 2) << 8) | buffer.get(pos + 3);
	}
	private static void lookForChunk(ByteBuffer buffer, int name) {
		log("Looking for chunk: 0x" + Integer.toHexString(name).toUpperCase() + ".");
		int pos = buffer.position();
		while (readBigEndianInt(buffer, pos) != name) {
			pos += 8;
			buffer.position(pos += buffer.getInt(pos - 4));
			if (pos % 2 == 1) {
				buffer.position(++pos);
			}
		}
		buffer.position(pos + 4);
		log("\tChunk found.");
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
	
	public WavCodec(ByteBuffer buffer, double volume) throws IOException {
		log("Reading Wav File.");
		
		if (readBigEndianInt(buffer, 0) == RIFF) {
			log("Byte Order: LITTLE ENDIAN.");
			buffer.order(ByteOrder.LITTLE_ENDIAN);
		} else {
			log("Byte Order: BIG ENDIAN.");
			buffer.order(ByteOrder.BIG_ENDIAN);
		}
		buffer.position(12);
		
		lookForChunk(buffer, FMT);
		int size = buffer.getInt();
		int type = buffer.getShort();
		int channels = buffer.getShort();
		freq = buffer.getInt();
		buffer.position(buffer.position() + 6);
		int sampleSize = (int)Math.ceil(buffer.getShort() / 8.0) * 8;
		log("Channels: " + channels + ".");
		log("Sample Size: " + sampleSize + ".");
		log("Frequency: " + freq + ".");
		
		if (type == TYPE_PCM) {
			if (channels == 1) {
				if (sampleSize == 8) {
					log("Format: MONO 8.");
					format = AL_FORMAT_MONO8;
				} else if (sampleSize == 16) {
					log("Format: MONO 16.");
					format = AL_FORMAT_MONO16;
				} else {
					unsupportedAudio();
				}
			} else if (channels == 2) {
				if (sampleSize == 8) {
					log("Format: STEREO 8.");
					format = AL_FORMAT_STEREO8;
				} else if (sampleSize == 16) {
					log("Format: STEREO 16.");
					format = AL_FORMAT_STEREO16;
				} else {
					unsupportedAudio();
				}
			} else {
				unsupportedAudio();
			}
		} else if (type == TYPE_FLOAT) {
			if (channels == 1) {
				if (sampleSize == 32) {
					log("Format: MONO 32.");
					format = AL_FORMAT_MONO_FLOAT32;
				} else {
					unsupportedAudio();
				}
			} else if (channels == 2) {
				if (sampleSize == 32) {
					log("Format: STEREO 32.");
					format = AL_FORMAT_STEREO_FLOAT32;
				} else {
					unsupportedAudio();
				}
			} else {
				unsupportedAudio();
			}
		} else if (type == TYPE_ALAW) {
			if (channels == 1) {
				if (sampleSize == 8) {
					log("Format: MONO A-LAW");
					format = AL_FORMAT_MONO_ALAW_EXT;
				} else {
					unsupportedAudio();
				}
			} else if (channels == 2) {
				if (sampleSize == 8) {
					log("Format: STEREO A-LAW");
					format = AL_FORMAT_STEREO_ALAW_EXT;
				} else {
					unsupportedAudio();
				}
			} else {
				unsupportedAudio();
			}
		} else if (type == TYPE_MULAW) {
			if (channels == 1) {
				log("Format: MONO MU-LAW");
				format = AL_FORMAT_MONO_MULAW_EXT;
			} else if (channels == 2) {
				log("Format: STEREO MU-LAW");
				format = AL_FORMAT_STEREO_MULAW_EXT;
			} else {
				unsupportedAudio();
			}
		} else {
			unsupportedAudio();
		}
		
		buffer.position(buffer.position() + (size - 16));
		if (buffer.position() % 2 == 1) {
			buffer.position(buffer.position() + 1);
		}
		lookForChunk(buffer, DATA);
		size = buffer.getInt();
		int samples = size / (sampleSize / 8);
		log("Size: " + size);
		log("Samples: " + samples);
		
		log("Reading data.");
		data = memAlloc(size);
		for (int i = 0; i < samples; i++) {
			if (sampleSize == 8) {
				data.put((byte)(buffer.get() * volume));
			} else if (sampleSize == 16) {
				data.putShort((short)(buffer.getShort() * volume));
			} else if (sampleSize == 32) {
				data.putFloat((float)(buffer.getFloat() * volume));
			}
		}
		data.flip();
		log("\tFinished.");
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