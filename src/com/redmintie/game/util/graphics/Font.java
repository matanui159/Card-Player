package com.redmintie.game.util.graphics;

import static org.lwjgl.opengl.GL11.GL_ALPHA;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.stb.STBTruetype.stbtt_BakeFontBitmap;
import static org.lwjgl.stb.STBTruetype.stbtt_GetBakedQuad;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.system.MemoryUtil;

import com.redmintie.game.util.Resource;
import com.redmintie.game.util.ResourceManager;

public class Font implements Resource {
	public static final int FILTER_NEAREST = GL_NEAREST;
	public static final int FILTER_LINEAR = GL_LINEAR;
	private static final int BITMAP_SIZE = 512;
	private static final STBTTAlignedQuad QUAD = STBTTAlignedQuad.create();
	
	private int bitmap;
	private STBTTBakedChar.Buffer chars = STBTTBakedChar.callocBuffer(256);
	
	public Font(String path, int size, int filter) throws IOException {
		ByteBuffer buffer = ResourceManager.getResourceAsBuffer(path);
		ByteBuffer data = MemoryUtil.memAlloc(BITMAP_SIZE * BITMAP_SIZE);
		
		stbtt_BakeFontBitmap(buffer, size, data, BITMAP_SIZE, BITMAP_SIZE, 0, chars);
		ResourceManager.freeBuffer(buffer);
		
		bitmap = TextureUtil.createTexture(data, BITMAP_SIZE, BITMAP_SIZE, GL_ALPHA, filter, GL_CLAMP_TO_EDGE);
		MemoryUtil.memFree(data);
		ResourceManager.addResource(this);
	}
	public Font(String path, int size) throws IOException {
		this(path, size, FILTER_LINEAR);
	}
	public void drawText(String text, double x, double y) {
		FloatBuffer xBuffer = MemoryUtil.memAllocFloat(1);
		xBuffer.put((float)x).flip();
		
		FloatBuffer yBuffer = MemoryUtil.memAllocFloat(1);
		yBuffer.put((float)y).flip();
		
		for (int i = 0; i < text.length(); i++) {
			stbtt_GetBakedQuad(chars, BITMAP_SIZE, BITMAP_SIZE, text.charAt(i), xBuffer, yBuffer, QUAD, 1);
			TextureUtil.drawTexture(bitmap, QUAD.x0(), QUAD.y0(), QUAD.x1(), QUAD.y1(),
					QUAD.s0(), QUAD.t0(), QUAD.s1(), QUAD.t1());
		}
	}
	@Override
	public void release() {
		
	}
}