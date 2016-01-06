package com.redmintie.game.util.sound.codec;

import java.nio.ByteBuffer;

public interface Codec {
	public ByteBuffer getData();
	public int getFormat();
	public int getFrequency();
	public void destroy();
}