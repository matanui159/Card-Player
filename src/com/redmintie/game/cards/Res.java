package com.redmintie.game.cards;

import java.io.IOException;

import com.redmintie.game.util.graphics.Font;
import com.redmintie.game.util.graphics.Sprite;
import com.redmintie.game.util.sound.Sound;

public class Res {
	public static Sprite BUTTON;
	public static Sprite BUTTON_PRESSED;
	public static Font FONT;
	public static Sound BUTTON_PRESS_SOUND;
	public static Sound BUTTON_RELEASE_SOUND;
	public static void init() {
		try {
			BUTTON = new Sprite("res/images/ui/button/normal.png");
			BUTTON_PRESSED = new Sprite("res/images/ui/button/pressed.png");
			FONT = new Font("res/font.ttf", BUTTON_PRESSED.getHeight());
			BUTTON_PRESS_SOUND = new Sound("res/sounds/button/press.wav");
			BUTTON_RELEASE_SOUND = new Sound("res/sounds/button/release.wav");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}