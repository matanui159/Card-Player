package com.redmintie.game.util.core;

public class Settings {
	public static final boolean DEBUG = Boolean.getBoolean("com.redmintie.DEBUG");
	public static final boolean TRACK_LEAKS = Boolean.getBoolean("com.redmintie.TRACKLEAKS");
	public static final String LOG = System.getProperty("com.redmintie.LOG");
	
	public static final int FPS_LIMIT = Integer.getInteger("com.redmintie.FPSLIMIT", 120);
	
	public static final boolean FORCE_PoT_TEXTURES = Boolean.getBoolean("com.redmintie.emulate.PoTTEXTURES");
	public static final int SCALE_FACTOR = Integer.getInteger("com.redmintie.emulate.SCALEFACTOR", 0);
}