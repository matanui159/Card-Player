package com.redmintie.game.cards;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import com.redmintie.game.cards.scenes.MainScene;
import com.redmintie.game.util.DualOutputStream;
import com.redmintie.game.util.Game;

public class Main {
	public static void main(String[] args) {
		try {
			FileOutputStream file = new FileOutputStream("log.txt");
			System.setOut(new PrintStream(new DualOutputStream(System.out, file)));
			System.setErr(new PrintStream(new DualOutputStream(System.err, file)));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		Game.init();
		Game.addScene("main", new MainScene());
		Game.setScene("main");
		Game.start();
	}
}