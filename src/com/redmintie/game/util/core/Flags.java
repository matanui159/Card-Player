package com.redmintie.game.util.core;

public class Flags {
	public static final boolean DEBUG = Boolean.getBoolean("com.redmintie.DEBUG");
	public static final boolean TRACK_LEAKS = Boolean.getBoolean("com.redmintie.TRACKLEAKS");
	public static final String LOG = System.getProperty("com.redmintie.LOG");
	public static final boolean FORCE_PoT_TEXTURES = Boolean.getBoolean("com.redmintie.PoTTEXTURES");
	public static final int FPS_LIMIT = Integer.getInteger("com.redmintie.FPSLIMIT", 120);
}