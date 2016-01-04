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
import static org.lwjgl.glfw.GLFW.glfwGetFramebufferSize;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.IntBuffer;
import java.util.HashMap;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;

import com.redmintie.game.util.graphics.Canvas;
import com.sun.scenario.effect.impl.BufferUtil;

public class Game {
	private static String title = "Game";
	private static int width = 1024;
	private static int height = 512;
	private static boolean resizable = true;
	private static boolean fullscreen = false;
	private static IntBuffer size;
	
	private static GLFWErrorCallback errorCallback;
	@SuppressWarnings("unused")
	private static GLFWFramebufferSizeCallback resizeCallback;
	@SuppressWarnings("unused")
	private static GLFWKeyCallback keyCallback;
	@SuppressWarnings("unused")
	private static GLFWMouseButtonCallback mouseCallback;
	
	//TODO: mouse move and scroll callbacks
	
	private static long window = NULL;
	private static boolean running;
	
	private static HashMap<String, Scene> scenes = new HashMap<String, Scene>();
	private static Scene scene;
	
	public static void init() {
		if (Flags.DEBUG) {
			System.setProperty("org.lwjgl.util.Debug", "true");
		}
		if (Flags.LOG != null) {
			try {
				FileOutputStream file = new FileOutputStream(Flags.LOG);
				System.setOut(new PrintStream(new DualOutputStream(System.out, file)));
				System.setErr(new PrintStream(new DualOutputStream(System.err, file)));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint());
		if (glfwInit() != GLFW_TRUE) {
			throw new RuntimeException("Could not initialise GLFW.");
		}
		
		size = BufferUtil.newIntBuffer(1);
		createWindow();
	}
	private static void createWindow() {
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE);
		
		long old = window;
		if (fullscreen && running) {
			long monitor = glfwGetPrimaryMonitor();
			GLFWVidMode mode = glfwGetVideoMode(monitor);
			window = glfwCreateWindow(mode.width(), mode.height(), title, monitor, old);
		} else {
			window = glfwCreateWindow(width, height, title, NULL, old);
		}
		if (old != NULL) {
			Callbacks.glfwReleaseCallbacks(old);
			glfwDestroyWindow(old);
		}
		
		glfwSetFramebufferSizeCallback(window, resizeCallback = new GLFWFramebufferSizeCallback() {
			public void invoke(long window, int width, int height) {
				Canvas.resize();
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
			if (Flags.DEBUG) {
				GLUtil.setupDebugMessageCallback();
			}
		}
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		if (running) {
			glfwShowWindow(window);
		}
	}
	public static void setTitle(String title) {
		glfwSetWindowTitle(window, Game.title = title);
	}
	public static String getTitle() {
		return title;
	}
	public static void setSize(int width, int height) {
		glfwSetWindowSize(window, Game.width = width, Game.height = height);
	}
	public static int getWidth() {
		size.rewind();
		glfwGetFramebufferSize(window, size, null);
		return size.get();
	}
	public static int getHeight() {
		size.rewind();
		glfwGetFramebufferSize(window, null, size);
		return size.get();
	}
	public static void setResizable(boolean resizable) {
		Game.resizable = resizable;
		createWindow();
	}
	public static boolean isResizable() {
		return resizable;
	}
	public static void setFullscreen(boolean fullscreen) {
		Game.fullscreen = fullscreen;
		createWindow();
	}
	public static boolean isFullscreen() {
		return fullscreen;
	}
	public static void start() {
		running = true;
		if (fullscreen) {
			createWindow();
		}
		glfwShowWindow(window);
		Canvas.resize();
		
		while (glfwWindowShouldClose(window) == GLFW_FALSE) {
			glfwPollEvents();
			
			if (scene != null) {
				scene.update();
				scene.draw();
			}
			
			glfwSwapBuffers(window);
			GLUtil.checkGLError();
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
	public static void end() {
		ResourceManager.destroyResources();
		Callbacks.glfwReleaseCallbacks(window);
		glfwDestroyWindow(window);
		glfwTerminate();
		errorCallback.release();
		System.exit(0);
	}
}