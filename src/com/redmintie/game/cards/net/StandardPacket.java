package com.redmintie.game.cards.net;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.redmintie.steelplate.util.data.DataObject;
import com.redmintie.steelplate.util.data.DataUtil;

public class StandardPacket implements DataObject {
	private static final long HEADER = DataUtil.generateHeader("CARDS ", "NORMAL");
	public static final int ERROR_FULL = 0;
	public static final int ERROR_INGAME = 1;
	public static final int AVATARS = 2;
	public static final int AVATAR_REMOVED = 3;
	public static final int AVATAR_CHOSEN = 4;
	public static final int OK = 5;
	public static final int START = 6;
	public static final int DRAW_CARD = 7;
	public static final int GIVE_CARD = 8;
	public static final int PLACE_CARD = 9;
	
	public int size;
	public int code;
	public int info;
	public int player;
	
	public StandardPacket(int code) {
		size = 1;
		this.code = code;
	}
	public StandardPacket(int code, int info) {
		size = 2;
		this.code = code;
		this.info = info;
	}
	public StandardPacket(int code, int info, int player) {
		size = 3;
		this.code = code;
		this.info = info;
		this.player = player;
	}
	
	@Override
	public long getHeader() {
		return HEADER;
	}
	@Override
	public int getSize() {
		return size;
	}
	@Override
	public int getMinSize() {
		return 1;
	}
	@Override
	public void writeData(DataOutput out) throws IOException {
		out.writeByte(code);
		if (size >= 2) {
			out.writeByte(info);
		}
		if (size >= 3) {
			out.writeByte(player);
		}
	}
	@Override
	public void readData(DataInput in, int size) throws IOException {
		code = info = player = 0;
		code = in.readUnsignedByte();
		if (size >= 2) {
			info = in.readUnsignedByte();
		}
		if (size >= 3) {
			player = in.readUnsignedByte();
		}
	}
}