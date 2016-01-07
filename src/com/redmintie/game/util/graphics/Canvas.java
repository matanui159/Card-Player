package com.redmintie.game.util.graphics;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_RENDERER;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_VENDOR;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glScaled;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glViewport;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;

import com.redmintie.game.util.core.Flags;
import com.redmintie.game.util.core.Game;

public class Canvas {
	public static void init() {
		GL.createCapabilities();
		if (Flags.DEBUG) {
			GLUtil.setupDebugMessageCallback();
			System.err.println("[OPENGL] OpenGL Renderer: " + glGetString(GL_RENDERER) + ".");
			System.err.println("[OPENGL] OpenGL Version : " + glGetString(GL_VERSION) + ".");
			System.err.println("[OPENGL] OpenGL Vendor  : " + glGetString(GL_VENDOR) + ".");
		}
		reload();
	}
	public static void reload() {
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		resize();
	}
	public static void resize() {
		int width = Game.getWidth();
		int height = Game.getHeight();
		
		if (width > 0 && height > 0) {
			glViewport(0, 0, width, height);
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(0, width, height, 0, 1, -1);
			glMatrixMode(GL_MODELVIEW);
		}
	}
	public static void clear(int red, int green, int blue) {
		glClearColor(red / 255f, green / 255f, blue / 255f, 0);
		glClear(GL_COLOR_BUFFER_BIT);
	}
	public static void setTint(int red, int green, int blue, int alpha) {
		glColor4f(red / 255f, green / 255f, blue / 255f, alpha / 255f);
	}
	public static void setTint(int red, int green, int blue) {
		setTint(red, green, blue, 255);
	}
	public static void pushMatrix() {
		glPushMatrix();
	}
	public static void popMatrix() {
		glPopMatrix();
	}
	public static void resetMatrix() {
		glLoadIdentity();
	}
	public static void translate(double x, double y) {
		glTranslated(x, y, 0);
	}
	public static void rotate(double angle) {
		glRotated(angle, 0, 0, 1);
	}
	public static void scale(double x, double y) {
		glScaled(x, y, 0);
	}
}