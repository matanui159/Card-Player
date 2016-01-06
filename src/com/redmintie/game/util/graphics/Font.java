package com.redmintie.game.util.graphics;

import static org.lwjgl.opengl.GL11.GL_ALPHA;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.stb.STBTruetype.stbtt_BakeFontBitmap;
import static org.lwjgl.stb.STBTruetype.stbtt_GetBakedQuad;
import static org.lwjgl.stb.STBTruetype.stbtt_GetFontVMetrics;
import static org.lwjgl.stb.STBTruetype.stbtt_InitFont;
import static org.lwjgl.stb.STBTruetype.stbtt_ScaleForPixelHeight;
import static org.lwjgl.system.MemoryUtil.memAlloc;
import static org.lwjgl.system.MemoryUtil.memAllocInt;
import static org.lwjgl.system.MemoryUtil.memFree;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;

import com.redmintie.game.util.Resource;
import com.redmintie.game.util.ResourceManager;

public class Font implements Resource {
	public static final int FILTER_NEAREST = GL_NEAREST;
	public static final int FILTER_LINEAR = GL_LINEAR;
	
	private static final int BITMAP_SIZE = 512;
	private static final int CHARACTERS = 256;
	
	private static final FloatBuffer xBuffer = BufferUtils.createFloatBuffer(1);
	private static final FloatBuffer yBuffer = BufferUtils.createFloatBuffer(1);
	private static final STBTTAlignedQuad QUAD = STBTTAlignedQuad.create();
	
	private STBTTBakedChar.Buffer chars = STBTTBakedChar.callocBuffer(CHARACTERS);
	private int[] bitmaps = new int[CHARACTERS];
	private int ascent;
	private int descent;
	private int linegap;
	
	public Font(String path, int size, int filter) throws IOException {
		ByteBuffer buffer = ResourceManager.getResourceAsBuffer(path);
		
		STBTTFontinfo info = STBTTFontinfo.malloc();
		IntBuffer ascent = memAllocInt(1);
		IntBuffer descent = memAllocInt(1);
		IntBuffer linegap = memAllocInt(1);
		
		stbtt_InitFont(info, buffer);
		float scale = stbtt_ScaleForPixelHeight(info, size);
		stbtt_GetFontVMetrics(info, ascent, descent, linegap);
		this.ascent = (int)(ascent.get() * scale);
		this.descent = (int)(descent.get() * scale);
		this.linegap = (int)(linegap.get() * scale);
		
		info.free();
		memFree(ascent);
		memFree(descent);
		memFree(linegap);
		
		ByteBuffer data = memAlloc(BITMAP_SIZE * BITMAP_SIZE);
		int offset = 0;
		int diff;
		
		while ((diff = -stbtt_BakeFontBitmap(buffer, size, data,
				BITMAP_SIZE, BITMAP_SIZE, offset, chars)) > 0) {
			Arrays.fill(bitmaps, offset, offset + diff, TextureUtil.createTexture(data, BITMAP_SIZE, BITMAP_SIZE,
					GL_ALPHA, filter, GL_CLAMP_TO_EDGE));
			offset += diff;
			chars.position(offset);
		}
		Arrays.fill(bitmaps, offset, CHARACTERS, TextureUtil.createTexture(data, BITMAP_SIZE, BITMAP_SIZE,
				GL_ALPHA, filter, GL_CLAMP_TO_EDGE));
		chars.rewind();
		
		ResourceManager.freeBuffer(buffer);
		memFree(data);
		ResourceManager.addResource(this);
	}
	public Font(String path, int size) throws IOException {
		this(path, size, FILTER_LINEAR);
	}
	public int getFontAscent() {
		return ascent;
	}
	public int getFontDescent() {
		return descent;
	}
	public int getFontLinegap() {
		return linegap;
	}
	public int getTextWidth(String text) {
		int width = 0;
		for (int i = 0; i < text.length(); i++) {
			int c = text.charAt(i);
			if (c < CHARACTERS) {
				width += chars.get(c).xadvance();
			}
		}
		return width;
	}
	public void drawText(String text, double x, double y) {
		xBuffer.put((float)x).flip();
		yBuffer.put((float)y).flip();
		
		for (int i = 0; i < text.length(); i++) {
			int c = text.charAt(i);
			if (c < CHARACTERS) {
				stbtt_GetBakedQuad(chars, BITMAP_SIZE, BITMAP_SIZE, c, xBuffer, yBuffer, QUAD, 1);
				TextureUtil.drawTexture(bitmaps[c], QUAD.x0(), QUAD.y0(), QUAD.x1(), QUAD.y1(),
						QUAD.s0(), QUAD.t0(), QUAD.s1(), QUAD.t1());
			}
		}
	}
	@Override
	public void destroy() {
		int last = 0;
		for (int bitmap : bitmaps) {
			if (bitmap != last) {
				glDeleteTextures(bitmap);
				last = bitmap;
			}
		}
		memFree(chars);
		ResourceManager.removeResource(this);
	}
}