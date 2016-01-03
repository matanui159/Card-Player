package com.redmintie.game.util;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_INVALID_ENUM;
import static org.lwjgl.opengl.GL11.GL_INVALID_OPERATION;
import static org.lwjgl.opengl.GL11.GL_INVALID_VALUE;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.GL_OUT_OF_MEMORY;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_STACK_OVERFLOW;
import static org.lwjgl.opengl.GL11.GL_STACK_UNDERFLOW;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL30.GL_INVALID_FRAMEBUFFER_OPERATION;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.util.HashMap;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.opengl.GL;

public class Game {
	private static String title = "Game";
	private static int width = 1024;
	private static int height = 512;
	private static boolean resizable = true;
	private static boolean fullscreen = false;
	
	private static GLFWErrorCallback errorCallback;
	@SuppressWarnings("unused")
	private static GLFWFramebufferSizeCallback resizeCallback;
	@SuppressWarnings("unused")
	private static GLFWKeyCallback keyCallback;
	@SuppressWarnings("unused")
	private static GLFWMouseButtonCallback mouseCallback;
	
	//TODO: mouse move and scroll callbacks
	
	private static long window = NULL;
	
	private static HashMap<String, Scene> scenes = new HashMap<String, Scene>();
	private static Scene scene;
	
	public static void init() {
		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint());
		if (glfwInit() != GLFW_TRUE) {
			throw new RuntimeException("Could not initialise GLFW.");
		}
		createWindow();
	}
	private static void createWindow() {
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE);
		
		long old = window;
		window = glfwCreateWindow(width, height, title, fullscreen ? glfwGetPrimaryMonitor() : NULL, old);
		if (old != NULL) {
			Callbacks.glfwReleaseCallbacks(window);
			glfwDestroyWindow(window);
		}
		
		glfwSetFramebufferSizeCallback(window, resizeCallback = new GLFWFramebufferSizeCallback() {
			public void invoke(long window, int width, int height) {
				glViewport(0, 0, width, height);
				glMatrixMode(GL_PROJECTION);
				glLoadIdentity();
				glOrtho(0, width, height, 0, 1, -1);
				glMatrixMode(GL_MODELVIEW);
				glLoadIdentity();
			}
		});
		
		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
			public void invoke(long window, int key, int scan, int action, int mods) {
				if (action != GLFW_REPEAT && scene != null) {
					scene.keyStateChanged(key, action == GLFW_PRESS);
				}
			}
		});
		
		
		// TODO: key types callback
		// TODO: mouse callbacks
		
		glfwMakeContextCurrent(window);
		if (old == NULL) {
			GL.createCapabilities();
			glEnable(GL_TEXTURE_2D);
		}
	}
	public static void start() {
		glfwShowWindow(window);
		
		while (glfwWindowShouldClose(window) == GLFW_FALSE) {
			glfwPollEvents();
			
			if (scene != null) {
				scene.update();
				scene.draw();
			}
			
			glfwSwapBuffers(window);
			checkErrors();
		}
		end();
	}
	public static void addScene(String name, Scene scene) {
		scenes.put(name, scene);
	}
	public static void setScene(String scene) {
		setScene(scenes.get(scene));
	}
	public static void setScene(Scene scene) {
		if (Game.scene != null) {
			Game.scene.end();
		}
		Game.scene = scene;
		scene.init();
	}
	public static void checkErrors() {
		int error = glGetError();
		if (error != GL_NO_ERROR) {
			String str = "UNKNOWN ERROR";
			switch (error) {
			case GL_INVALID_ENUM:
				str = "INVALID ENUM";
				break;
			case GL_INVALID_VALUE:
				str = "INVALID VALUE";
				break;
			case GL_INVALID_OPERATION:
				str = "INVALID OPERATION";
				break;
			case GL_INVALID_FRAMEBUFFER_OPERATION:
				str = "INVALID FRAMEBUFFER OPERATION";
				break;
			case GL_OUT_OF_MEMORY:
				str = "OUT OF MEMORY";
				break;
			case GL_STACK_UNDERFLOW:
				str = "STACK UNDERFLOW";
				break;
			case GL_STACK_OVERFLOW:
				str = "STACK OVERFLOW";
				break;
			}
			throw new RuntimeException("OpenGL error: " + str + " (0x" + Integer.toHexString(error) + ")");
		}
	}
	public static void end() {
		ResourceManager.releaseResources();
		Callbacks.glfwReleaseCallbacks(window);
		glfwDestroyWindow(window);
		glfwTerminate();
		errorCallback.release();
		System.exit(0);
	}
}