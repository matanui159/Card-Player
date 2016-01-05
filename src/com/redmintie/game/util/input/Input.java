package com.redmintie.game.util.input;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_HIDDEN;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;
import static org.lwjgl.glfw.GLFW.glfwSetCursor;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;

public class Input {
	
	public static final int MOUSE_BUTTON1 = 0;
	public static final int MOUSE_BUTTON2 = 1;
	public static final int MOUSE_BUTTON3 = 2;
	public static final int MOUSE_BUTTON4 = 3;
	public static final int MOUSE_BUTTON5 = 4;
	public static final int MOUSE_BUTTON6 = 5;
	public static final int MOUSE_BUTTON7 = 6;
	public static final int MOUSE_BUTTON8 = 7;
	public static final int MOUSE_BUTTON_LEFT = 0;
	public static final int MOUSE_BUTTON_RIGHT = 1;
	public static final int MOUSE_BUTTON_MIDDLE = 2;
	
	public static final int CURSOR_NORMAL = GLFW_CURSOR_NORMAL;
	public static final int CURSOR_HIDDEN = GLFW_CURSOR_HIDDEN;
	public static final int CURSOR_DISABLED = GLFW_CURSOR_DISABLED;
	
	private static DoubleBuffer pos = BufferUtils.createDoubleBuffer(1);
	private static Cursor cursor;
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
	public static void setCursor(Cursor cursor) {
		Input.cursor = cursor;
		if (cursor == null) {
			glfwSetCursor(WindowAccess.getWindow(), NULL);
		} else {
			glfwSetCursor(WindowAccess.getWindow(), cursor.getCursor());
		}
	}
	public static Cursor getCursor() {
		return cursor;
	}
	public static void setCursorMode(int mode) {
		glfwSetInputMode(WindowAccess.getWindow(), GLFW_CURSOR, Input.mode = mode);
	}
	public static int getCursorMode() {
		return mode;
	}
}