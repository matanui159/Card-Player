package com.redmintie.game.cards.scenes;

import com.redmintie.game.cards.Button;
import com.redmintie.game.cards.Res;
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
			return Game.getHeight() / 2 - 30;
		}
	};
	
	private Button join = new Button("JOIN") {
		@Override
		public int getX() {
			return Game.getWidth() / 2;
		}
		@Override
		public int getY() {
			return Game.getHeight() / 2 + 30;
		}
	};
	
	private int frames;
	private double time;
	private int fps;
	@Override
	public void update(double delta) {
		frames++;
		time += delta;
		if (time >= 1.0) {
			fps = frames;
			time = frames = 0;
		}
	}
	@Override
	public void draw() {
		Canvas.clear(82, 152, 72);
		
		host.draw();
		join.draw();
		
		Res.FONT.drawText("FPS: " + fps, 10, Res.FONT.getFontAscent());
	}
	@Override
	public void mouseButtonReleased(int button) {
		host.mouseButtonReleased(button);
		join.mouseButtonReleased(button);
	}
}