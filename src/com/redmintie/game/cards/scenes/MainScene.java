package com.redmintie.game.cards.scenes;

import com.redmintie.game.cards.Button;
import com.redmintie.game.util.Game;
import com.redmintie.game.util.Scene;
import com.redmintie.game.util.graphics.Canvas;

public class MainScene extends Scene {
	private Button host = new Button("HOST") {
		@Override
		public int getX() {
			return Game.getWidth() / 2;
		}
		@Override
		public int getY() {
			return Game.getHeight() / 2;
		}
	};
	@Override
	public void mouseButtonReleased(int button) {
		host.mouseButtonReleased(button);
	}
	@Override
	public void draw() {
		Canvas.clear(82, 152, 72);
		
		host.draw();
	}
}