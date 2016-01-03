package com.redmintie.game.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ResourceManager {
	private static ArrayList<Resource> resources = new ArrayList<Resource>();
	public static InputStream getResourceAsStream(String path) throws IOException {
		InputStream stream = ResourceManager.class.getResourceAsStream("/" + path);
		if (stream == null) {
			stream = new FileInputStream(path);
		}
		return stream;
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