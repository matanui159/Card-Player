package com.redmintie.game.cards;

import java.io.IOException;

import com.redmintie.game.util.graphics.Font;
import com.redmintie.game.util.graphics.Sprite;

public class Res {
	public static Sprite BUTTON;
	public static Sprite BUTTON_PRESSED;
	public static Font FONT;
	public static void init() {
		try {
			BUTTON = new Sprite("res/images/ui/button/normal.png");
			BUTTON_PRESSED = new Sprite("res/images/ui/button/pressed.png");
			FONT = new Font("res/font.ttf", BUTTON_PRESSED.getHeight());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}