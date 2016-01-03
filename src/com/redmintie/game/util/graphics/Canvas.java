package com.redmintie.game.util.graphics;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor4f;

public class Canvas {
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
	
	// TODO: matrix operations
}