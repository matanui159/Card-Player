package com.redmintie.game.util.graphics;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.stb.STBImageResize.stbir_resize_uint8;
import static org.lwjgl.system.MathUtil.mathIsPoT;
import static org.lwjgl.system.MathUtil.mathRoundPoT;
import static org.lwjgl.system.jemalloc.JEmalloc.je_free;
import static org.lwjgl.system.jemalloc.JEmalloc.je_malloc;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL;

import com.redmintie.game.util.Flags;
import com.redmintie.game.util.Resource;
import com.redmintie.game.util.ResourceManager;

public class Sprite implements Resource {
	public static final int FILTER_NEAREST = GL_NEAREST;
	public static final int FILTER_LINEAR = GL_LINEAR;
	public static final int WRAP_CLAMP = GL_CLAMP_TO_EDGE;
	public static final int WRAP_REPEAT = GL_REPEAT;
	
	private int width;
	private int height;
	private int texture;
	
	public Sprite(String path, int filter, int wrap) throws IOException {
		ByteBuffer buffer = ResourceManager.getResourceAsBuffer(path);
		IntBuffer width = je_malloc(4).asIntBuffer();
		IntBuffer height = je_malloc(4).asIntBuffer();
		IntBuffer comp = je_malloc(4).asIntBuffer();
		
		ByteBuffer data = stbi_load_from_memory(buffer, width, height, comp, 4);
		this.width = width.get();
		this.height = height.get();
		
		ResourceManager.freeBuffer(buffer);
		je_free(width);
		je_free(height);
		je_free(comp);
		
		int w = this.width;
		int h = this.height;
		boolean resized = false;
		if (!GL.getCapabilities().GL_ARB_texture_non_power_of_two || Flags.FORCE_PoT_TEXTURES) {
			if (!mathIsPoT(w) || !mathIsPoT(h)) {
				w = mathRoundPoT(w);
				h = mathRoundPoT(h);
				resized = true;
				if (Flags.DEBUG) {
					System.err.println("[SPRITE] NPoT textures are not supported. "
							+ "Resizing \"" + path + "\" to " + w + "x" + h + ".");
					System.err.println("[SPRITE]\tTexture may look fuzzy.");
				}
				
				ByteBuffer d = je_malloc(w * h * 4);
				
				stbir_resize_uint8(data, this.width, this.height, 0, d, w, h, 0, 4);
				stbi_image_free(data);
				data = d;
			}
		}
		
		texture = TextureUtil.createTexture(data, w, h, GL_RGBA, filter, wrap);
		if (resized) {
			je_free(data);
		} else {
			stbi_image_free(data);
		}
		ResourceManager.addResource(this);
	}
	public Sprite(String path) throws IOException {
		this(path, FILTER_LINEAR, WRAP_CLAMP);
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public void draw(double x, double y, double width, double height) {
		TextureUtil.drawTexture(texture, x, y, x + width, y + height, 0, 0, 1, 1);
	}
	public void draw(double x, double y) {
		draw(x, y, width, height);
	}
	@Override
	public void destroy() {
		glDeleteTextures(texture);
	}
}