package com.redmintie.game.cards.net;

import java.util.Random;

public class NetConstants {
	public static final int PORT = 22737;
	public static final long HOST_ID = new Random().nextLong();
	
	public static final byte ERROR_FULL = 1;
	public static final byte ERROR_INGAME = 2;
	public static final byte ERROR_COMMUNICATION = 3;
	
	public static final byte AVATAR_TAKEN = 11;
	public static final byte AVATAR_CHOSEN = 12;
	public static final byte AVATAR_ACCEPTED = 13;
	
	public static final byte START = 21;
}