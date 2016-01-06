package com.redmintie.game.util;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetFramebufferSize;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCharCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.IntBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.Configuration;

import com.redmintie.game.util.graphics.Canvas;
import com.redmintie.game.util.input.Input;
import com.redmintie.game.util.sound.Sound;

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
	private static GLFWCharCallback keyTypedCallback;
	@SuppressWarnings("unused")
	private static GLFWMouseButtonCallback mouseCallback;
	@SuppressWarnings("unused")
	private static GLFWCursorPosCallback mouseMoveCallback;
	@SuppressWarnings("unused")
	private static GLFWScrollCallback mouseScrollCallback;
	
	protected static long window = NULL;
	private static boolean running;
	
	private static HashMap<String, Scene> scenes = new HashMap<String, Scene>();
	private static Scene scene;
	
	public static void init() {
		if (Flags.LOG != null) {
			try {
				FileOutputStream file = new FileOutputStream(Flags.LOG);
				System.setOut(new PrintStream(new DualOutputStream(System.out, file)));
				System.setErr(new PrintStream(new DualOutputStream(System.err, file)));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		if (Flags.TRACK_LEAKS) {
			Configuration.DEBUG_MEMORY_ALLOCATOR.set(true);
		}
		if (Flags.DEBUG) {
			Configuration.DEBUG.set(true);
		}
		
		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint());
		if (glfwInit() != GLFW_TRUE) {
			throw new RuntimeException("Could not initialise GLFW.");
		}
		
		size = BufferUtils.createIntBuffer(1);
		createWindow();
		Sound.init();
	}
	protected static void createWindow() {
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
		Input.setCursor(Input.getCursor());
		Input.setCursorMode(Input.getCursorMode());
		
		glfwSetFramebufferSizeCallback(window, resizeCallback = new GLFWFramebufferSizeCallback() {
			@Override
			public void invoke(long window, int width, int height) {
				Canvas.resize();
			}
		});
		
		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scan, int action, int mods) {
				if (scene != null) {
					switch (action) {
					case GLFW_PRESS:
						scene.keyPressed(key);
						break;
					case GLFW_RELEASE:
						scene.keyReleased(key);
						break;
					}
				}
			}
		});
		
		glfwSetCharCallback(window, keyTypedCallback = new GLFWCharCallback() {
			@Override
			public void invoke(long window, int codepoint) {
				if (scene != null) {
					scene.keyTyped((char)codepoint);
				}
			}
		});
		
		glfwSetMouseButtonCallback(window, mouseCallback = new GLFWMouseButtonCallback() {
			@Override
			public void invoke(long window, int button, int action, int mods) {
				if (scene != null) {
					switch (action) {
					case GLFW_PRESS:
						scene.mouseButtonPressed(button);
						break;
					case GLFW_RELEASE:
						scene.mouseButtonReleased(button);
						break;
					}
				}
			}
		});
		
		glfwSetCursorPosCallback(window, mouseMoveCallback = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double x, double y) {
				if (scene != null) {
					scene.mouseMoved(x, y);
				}
			}
		});
		
		glfwSetScrollCallback(window, mouseScrollCallback = new GLFWScrollCallback() {
			@Override
			public void invoke(long window, double x, double y) {
				if (scene != null) {
					scene.mouseScrolled(x, y);
				}
			}
		});
		
		glfwMakeContextCurrent(window);
		if (old == NULL) {
			Canvas.init();
		} else {
			Canvas.reload();
		}
		
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
		} else {
			glfwShowWindow(window);
		}
		if (scene != null) {
			scene.init();
		}
		
		double last = glfwGetTime();
		double min = Flags.FPS_LIMIT == 0 ? 0 : 1.0 / Flags.FPS_LIMIT;
		while (glfwWindowShouldClose(window) == GLFW_FALSE) {
			double delta = glfwGetTime() - last;
			while (min > 0 && delta < min) {
				try {
					Thread.sleep((int)Math.floor((min - delta) * 1000 + 0.5));
				} catch (InterruptedException ex) {}
				delta = glfwGetTime() - last;
			}
			last = glfwGetTime();
			
			glfwPollEvents();
			
			
			if (scene != null) {
				scene.update(delta);
				scene.draw();
			}
			
			glfwSwapBuffers(window);
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
		if (running && Game.scene != null) {
			Game.scene.end();
		}
		Game.scene = scene;
		if (running) {
			scene.init();
		}
	}
	public static void end() {
		if (scene != null) {
			scene.end();
		}
		ResourceManager.destroyResources();
		Sound.end();
		
		Callbacks.glfwReleaseCallbacks(window);
		glfwDestroyWindow(window);
		glfwTerminate();
		errorCallback.release();
		
		System.exit(0);
	}
}