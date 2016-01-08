package com.redmintie.game.cards;

import java.io.IOException;

import com.redmintie.game.util.core.Game;
import com.redmintie.game.util.graphics.Font;
import com.redmintie.game.util.graphics.Sprite;
import com.redmintie.game.util.sound.Sound;

public class Res {
	public static Sprite BUTTON;
	public static Sprite BUTTON_PRESSED;
	public static Font FONT;
	
	public static Sound PRESS_SOUND;
	public static Sound RELEASE_SOUND;
	
	public static void init() {
		try {
			BUTTON = new Sprite("res/images/ui/button/normal.png");
			BUTTON_PRESSED = new Sprite("res/images/ui/button/pressed.png");
			FONT = new Font("res/font.ttf", BUTTON_PRESSED.getHeight());
			PRESS_SOUND = new Sound("res/sounds/button/press.wav", 10);
			RELEASE_SOUND = new Sound("res/sounds/button/release.wav", 10);
		} catch (IOException ex) {
			ex.printStackTrace();
			Game.end();
		}
	}
}