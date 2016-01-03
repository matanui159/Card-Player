package com.redmintie.game.util.graphics;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexCoord2d;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.nio.ByteBuffer;

public class TextureUtil {
	public static int createTexture(ByteBuffer data, int width, int height, int format, int filter, int wrap) {
		int texture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texture);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrap);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrap);
		glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, data);
		return texture;
	}
	public static void drawTexture(int texture, double x0, double y0, double x1, double y1,
			double s0, double t0, double s1, double t1) {
		glBindTexture(GL_TEXTURE_2D, texture);
		glBegin(GL_QUADS);
		
		glTexCoord2d(s0, t0);
		glVertex2d(x0, y0);
		
		glTexCoord2d(s1, t0);
		glVertex2d(x1, y0);
		
		glTexCoord2d(s1, t1);
		glVertex2d(x1, y1);
		
		glTexCoord2d(s0, t1);
		glVertex2d(x0, y1);
		
		glEnd();
	}
}