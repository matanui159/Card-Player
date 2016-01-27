package com.redmintie.game.util.sound;

import static org.lwjgl.openal.AL10.AL_BUFFER;
import static org.lwjgl.openal.AL10.AL_DISTANCE_MODEL;
import static org.lwjgl.openal.AL10.AL_EXTENSIONS;
import static org.lwjgl.openal.AL10.AL_FALSE;
import static org.lwjgl.openal.AL10.AL_GAIN;
import static org.lwjgl.openal.AL10.AL_INVERSE_DISTANCE;
import static org.lwjgl.openal.AL10.AL_INVERSE_DISTANCE_CLAMPED;
import static org.lwjgl.openal.AL10.AL_MAX_DISTANCE;
import static org.lwjgl.openal.AL10.AL_NONE;
import static org.lwjgl.openal.AL10.AL_PLAYING;
import static org.lwjgl.openal.AL10.AL_REFERENCE_DISTANCE;
import static org.lwjgl.openal.AL10.AL_ROLLOFF_FACTOR;
import static org.lwjgl.openal.AL10.AL_SOURCE_STATE;
import static org.lwjgl.openal.AL10.AL_TRUE;
import static org.lwjgl.openal.AL10.alBufferData;
import static org.lwjgl.openal.AL10.alDeleteBuffers;
import static org.lwjgl.openal.AL10.alEnable;
import static org.lwjgl.openal.AL10.alGenBuffers;
import static org.lwjgl.openal.AL10.alGenSources;
import static org.lwjgl.openal.AL10.alGetSourcei;
import static org.lwjgl.openal.AL10.alGetString;
import static org.lwjgl.openal.AL10.alSourcePause;
import static org.lwjgl.openal.AL10.alSourcePlay;
import static org.lwjgl.openal.AL10.alSourceStop;
import static org.lwjgl.openal.AL10.alSourcef;
import static org.lwjgl.openal.AL10.alSourcei;
import static org.lwjgl.openal.AL11.AL_EXPONENT_DISTANCE;
import static org.lwjgl.openal.AL11.AL_EXPONENT_DISTANCE_CLAMPED;
import static org.lwjgl.openal.AL11.AL_LINEAR_DISTANCE;
import static org.lwjgl.openal.AL11.AL_LINEAR_DISTANCE_CLAMPED;
import static org.lwjgl.openal.EXTSourceDistanceModel.AL_SOURCE_DISTANCE_MODEL;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.openal.ALContext;

import com.redmintie.game.util.core.Settings;
import com.redmintie.game.util.core.Resource;
import com.redmintie.game.util.core.ResourceManager;
import com.redmintie.game.util.sound.codec.Codec;
import com.redmintie.game.util.sound.codec.WavCodec;

public class Sound implements Resource {
	public static final int NONE = AL_NONE;
	public static final int LINEAR_DISTANCE = AL_LINEAR_DISTANCE;
	public static final int LINEAR_DISTANCE_CLAMPED = AL_LINEAR_DISTANCE_CLAMPED;
	public static final int INVERSE_DISTANCE = AL_INVERSE_DISTANCE;
	public static final int INVERSE_DISTANCE_CLAMPED = AL_INVERSE_DISTANCE_CLAMPED;
	public static final int EXPONENT_DISTANCE = AL_EXPONENT_DISTANCE;
	public static final int EXPONENT_DISTANCE_CLAMPED = AL_EXPONENT_DISTANCE_CLAMPED;
	
	private static ALContext context;
	
	public static void init() {
		if ((context = ALContext.create()) == null) {
			throw new RuntimeException("Could not initialise OpenAL.");
		}
		context.makeCurrent();
		if (Settings.DEBUG) {
			System.err.println("[OPENAL] Supported OpenAL Extensions:");
			String[] extensions = alGetString(AL_EXTENSIONS).split(" ");
			for (String ext : extensions) {
				System.err.println("[OPENAL]\t" + ext);
			}
		}
		
		alEnable(AL_SOURCE_DISTANCE_MODEL);
	}
	public static void end() {
		context.getDevice().destroy();
		context.destroy();
	}
	
	private int buffer;
	private int source;
	private double volume = 1;
	private boolean looping = false;
	
	private int model = LINEAR_DISTANCE_CLAMPED;
	private double refDist = 1;
	private double maxDist = Float.MAX_VALUE;
	private double rolloff = 1;
	
	public Sound(String path, double volume) throws IOException {
		ByteBuffer buffer = ResourceManager.getResourceAsBuffer(path);
		
		Codec codec = null;
		if (WavCodec.checkHeader(buffer)) {
			codec = new WavCodec(buffer, volume);
		} else {
			throw new IOException("Unsupported file format.");
		}
		ResourceManager.freeBuffer(buffer);
		
		this.buffer = alGenBuffers();
		alBufferData(this.buffer, codec.getFormat(), codec.getData(), codec.getFrequency());
		codec.destroy();
		
		source = alGenSources();
		alSourcei(source, AL_BUFFER, this.buffer);
		ResourceManager.addResource(this);
	}
	public Sound(String path) throws IOException {
		this(path, 1);
	}
	public void setVolume(double volume) {
		alSourcef(source, AL_GAIN, (float)(this.volume = volume));
	}
	public double getVolume() {
		return volume;
	}
	public void setLooping(boolean looping) {
		alSourcei(source, AL_DISTANCE_MODEL, (this.looping = looping) ? AL_TRUE : AL_FALSE);
	}
	public boolean isLooping() {
		return looping;
	}
	public void setDistanceModel(int model) {
		alSourcei(source, AL_DISTANCE_MODEL, this.model = model);
	}
	public int getDistanceModel() {
		return model;
	}
	public void setReferenceDistance(double refDist) {
		alSourcef(source, AL_REFERENCE_DISTANCE, (float)(this.refDist = refDist));
	}
	public double getReferenceDistance() {
		return refDist;
	}
	public void setMaxDistance(double maxDist) {
		alSourcef(source, AL_MAX_DISTANCE, (float)(this.maxDist = maxDist));
	}
	public double getMaxDistance() {
		return maxDist;
	}
	public void setRolloffFactor(double rolloff) {
		alSourcef(source, AL_ROLLOFF_FACTOR, (float)(this.rolloff = rolloff));
	}
	public double getRolloffFactor() {
		return rolloff;
	}
	public void play() {
		alSourcePlay(source);
	}
	public void pause() {
		alSourcePause(source);
	}
	public void stop() {
		alSourceStop(source);
	}
	public boolean isPlaying() {
		return alGetSourcei(source, AL_SOURCE_STATE) == AL_PLAYING;
	}
	@Override
	public void destroy() {
		alDeleteBuffers(buffer);
		ResourceManager.removeResource(this);
	}
}