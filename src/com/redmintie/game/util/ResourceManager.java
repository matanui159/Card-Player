package com.redmintie.game.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.lwjgl.system.MemoryUtil;

public class ResourceManager {
	private static final byte[] BUFFER = new byte[65536];
	private static ArrayList<Resource> resources = new ArrayList<Resource>();
	public static InputStream getResourceAsStream(String path) throws IOException {
		InputStream stream = ResourceManager.class.getResourceAsStream("/" + path);
		if (stream == null) {
			stream = new FileInputStream(path);
		}
		return stream;
	}
	public static ByteBuffer getResourceAsBuffer(String path) throws IOException {
		InputStream stream = getResourceAsStream(path);
		ByteBuffer buffer = MemoryUtil.memAlloc(stream.available());
		int length;
		while ((length = stream.read(BUFFER)) != -1) {
			buffer.put(BUFFER, 0, length);
		}
		buffer.flip();
		stream.close();
		return buffer;
	}
	public static void freeBuffer(ByteBuffer buffer) {
		MemoryUtil.memFree(buffer);
	}
	public static void addResource(Resource resource) {
		resources.add(resource);
	}
	public static void releaseResources() {
		for (Resource resource : resources) {
			resource.release();
		}
		resources.clear();
	}
}