package com.redmintie.game.util;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexCoord2d;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glVertex2d;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.stb.STBImageResize.stbir_resize_uint8;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

public class Sprite implements Resource {
	public static final int FILTER_NEAREST = GL_NEAREST;
	public static final int FILTER_LINEAR = GL_LINEAR;
	public static final int WRAP_CLAMP = GL_CLAMP_TO_EDGE;
	public static final int WRAP_REPEAT = GL_REPEAT;
	private static int current = 0;
	private static final byte[] BUFFER = new byte[65536];
	
	private int width;
	private int height;
	private int texture;
	
	public Sprite(String path, int filter, int wrap) throws IOException {
		InputStream stream = ResourceManager.getResourceAsStream(path);
		ByteBuffer mem = MemoryUtil.memAlloc(stream.available());
		int length;
		while ((length = stream.read(BUFFER)) != -1) {
			mem.put(BUFFER, 0, length);
		}
		mem.flip();
		stream.close();
		
		IntBuffer width = MemoryUtil.memAllocInt(1);
		IntBuffer height = MemoryUtil.memAllocInt(1);
		IntBuffer comp = MemoryUtil.memAllocInt(1);
		ByteBuffer data = stbi_load_from_memory(mem, width, height, comp, 4);
		this.width = width.get();
		this.height = height.get();
		
		MemoryUtil.memFree(mem);
		MemoryUtil.memFree(width);
		MemoryUtil.memFree(height);
		MemoryUtil.memFree(comp);
		
		int w = this.width;
		int h = this.height;
		if (!GL.getCapabilities().GL_ARB_texture_non_power_of_two) {
			w = 1;
			while (w <= this.width) {
				w <<= 1; // *= 2
			}
			
			h = 1;
			while (h <= this.height) {
				h <<= 1; // *= 2
			}
		}
		ByteBuffer d = MemoryUtil.memAlloc(w * h * 4);
		stbir_resize_uint8(data, this.width, this.height, 0, d, w, h, 0, 4);
		stbi_image_free(data);
		
		texture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texture);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrap);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrap);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, d);
		glBindTexture(GL_TEXTURE_2D, current);
		
		MemoryUtil.memFree(d);
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public void draw(double x, double y, double width, double height) {
		if (current != texture) {
			glBindTexture(GL_TEXTURE_2D, texture);
			current = texture;
		}
		
		glBegin(GL_QUADS);
		
		glTexCoord2d(0, 0);
		glVertex2d(x, y);
		
		glTexCoord2d(1, 0);
		glVertex2d(x + width, y);
		
		glTexCoord2d(1, 1);
		glVertex2d(x + width, y + height);
		
		glTexCoord2d(0, 1);
		glVertex2d(x, y + height);
		
		glEnd();
	}
	@Override
	public void release() {
		glDeleteTextures(texture);
	}
}