package com.redmintie.game.util;

import static org.lwjgl.system.MemoryUtil.memAlloc;
import static org.lwjgl.system.MemoryUtil.memFree;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

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
		ByteBuffer buffer = memAlloc(stream.available());
		int length;
		while ((length = stream.read(BUFFER)) != -1) {
			buffer.put(BUFFER, 0, length);
		}
		buffer.flip();
		stream.close();
		return buffer;
	}
	public static void freeBuffer(ByteBuffer buffer) {
		memFree(buffer);
	}
	public static void addResource(Resource resource) {
		resources.add(resource);
	}
	public static void destroyResources() {
		for (Resource resource : resources) {
			resource.destroy();
		}
		resources.clear();
	}
}