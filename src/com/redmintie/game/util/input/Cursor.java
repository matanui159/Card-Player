package com.redmintie.game.util.input;

import static org.lwjgl.glfw.GLFW.GLFW_ARROW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CROSSHAIR_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_HAND_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_HRESIZE_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_IBEAM_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_VRESIZE_CURSOR;
import static org.lwjgl.glfw.GLFW.glfwCreateCursor;
import static org.lwjgl.glfw.GLFW.glfwCreateStandardCursor;
import static org.lwjgl.glfw.GLFW.glfwDestroyCursor;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAddress;
import static org.lwjgl.system.MemoryUtil.memAlloc;
import static org.lwjgl.system.MemoryUtil.memByteBuffer;
import static org.lwjgl.system.MemoryUtil.memFree;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFWImage;

import com.redmintie.game.util.Resource;
import com.redmintie.game.util.ResourceManager;

public class Cursor implements Resource {
	public static final Cursor ARROW = new Cursor(GLFW_ARROW_CURSOR);
	public static final Cursor IBEAM = new Cursor(GLFW_IBEAM_CURSOR);
	public static final Cursor CROSSHAIR = new Cursor(GLFW_CROSSHAIR_CURSOR);
	public static final Cursor HAND = new Cursor(GLFW_HAND_CURSOR);
	public static final Cursor HRESIZE = new Cursor(GLFW_HRESIZE_CURSOR);
	public static final Cursor VRESIZE = new Cursor(GLFW_VRESIZE_CURSOR);
	
	private long cursor = NULL;
	private int type;
	private Cursor(int type) {
		this.type = type;
		ResourceManager.addResource(this);
	}
	public Cursor(String path, int xhot, int yhot) throws IOException {
		ByteBuffer buffer = ResourceManager.getResourceAsBuffer(path);
		ByteBuffer image = memAlloc(16);
		ByteBuffer height = memByteBuffer(memAddress(image) + 4, 4);
		ByteBuffer comp = memAlloc(4);
		
		ByteBuffer data = stbi_load_from_memory(buffer, buffer.capacity(), image, height, comp, 4);
		ResourceManager.freeBuffer(buffer);
		memFree(comp);
		
		image.putLong(8, memAddress(data));
		
		cursor = glfwCreateCursor(new GLFWImage(image), xhot, yhot);
		memFree(image);
		stbi_image_free(data);
		ResourceManager.addResource(this);
	}
	long getCursor() {
		if (cursor == NULL) {
			cursor = glfwCreateStandardCursor(type);
		}
		return cursor;
	}
	@Override
	public void destroy() {
		if (cursor != NULL) {
			glfwDestroyCursor(cursor);
			cursor = NULL;
		}
	}
}