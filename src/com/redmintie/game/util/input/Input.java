package com.redmintie.game.util.input;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_HIDDEN;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;

public class Input {
	public static final int CURSOR_NORMAL = GLFW_CURSOR_NORMAL;
	public static final int CURSOR_HIDDEN = GLFW_CURSOR_HIDDEN;
	public static final int CURSOR_DISABLED = GLFW_CURSOR_DISABLED;
	
	private static DoubleBuffer pos = BufferUtils.createDoubleBuffer(1);
	private static int mode = CURSOR_NORMAL;
	
	public static boolean isKeyDown(int key) {
		return glfwGetKey(WindowAccess.getWindow(), key) == GLFW_PRESS;
	}
	public static boolean isMouseButtonDown(int button) {
		return glfwGetMouseButton(WindowAccess.getWindow(), button) == GLFW_PRESS;
	}
	public static double getCursorX() {
		pos.rewind();
		glfwGetCursorPos(WindowAccess.getWindow(), pos, null);
		return pos.get();
	}
	public static double getCursorY() {
		pos.rewind();
		glfwGetCursorPos(WindowAccess.getWindow(), null, pos);
		return pos.get();
	}
	public static void setCursorMode(int mode) {
		glfwSetInputMode(WindowAccess.getWindow(), GLFW_CURSOR, Input.mode = mode);
	}
	public static int getCursorMode() {
		return mode;
	}
}