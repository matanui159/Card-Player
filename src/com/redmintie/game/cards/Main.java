package com.redmintie.game.cards;

import java.io.IOException;

import com.redmintie.game.cards.scenes.MainScene;
import com.redmintie.game.util.Game;
import com.redmintie.game.util.input.Cursor;
import com.redmintie.game.util.input.Input;

public class Main {
	public static void main(String[] args) {
		setFlag("com.redmintie.DEBUG", "true");
		setFlag("com.redmintie.LOG", "log.txt");
		
		Game.init();
		
		try {
			Input.setCursor(new Cursor("res/cursor.png", 0, 0));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		Game.addScene("main", new MainScene());
		Game.setScene("main");
		
		Game.start();
	}
	public static void setFlag(String name, String value) {
		System.setProperty(name, System.getProperty(name, value));
	}
}