package com.redmintie.game.cards.scenes;

import java.io.IOException;

import com.redmintie.game.util.Game;
import com.redmintie.game.util.Scene;
import com.redmintie.game.util.graphics.Canvas;
import com.redmintie.game.util.graphics.Font;
import com.redmintie.game.util.graphics.Sprite;

public class MainScene extends Scene {
	private Sprite test;
	private Font font;
	public MainScene() {
		try {
			test = new Sprite("res/images/cards/0.png");
			font = new Font("res/test.ttf", 128);
		} catch (IOException ex) {
			ex.printStackTrace();
			Game.end();
		}
	}
	public void draw() {
		Canvas.clear(0, 0, 0);

		Canvas.setTint(255, 255, 255, 255);
		test.draw(0, 0, test.getWidth(), test.getHeight());
		
		Canvas.setTint(255, 0, 0, 255);
		font.drawText("Hello, World!", 100, 100);
	}
}