package com.redmintie.game.cards;

import com.redmintie.game.cards.scenes.MainScene;
import com.redmintie.game.util.Game;

public class Main {
	public static void main(String[] args) {
		setFlag("com.redmintie.DEBUG", "true");
		setFlag("com.redmintie.LOG", "log.txt");
		Game.init();
		Res.init();
		
		Game.addScene("main", new MainScene());
		Game.setScene("main");
		
		Game.start();
	}
	public static void setFlag(String name, String value) {
		System.setProperty(name, System.getProperty(name, value));
	}
}