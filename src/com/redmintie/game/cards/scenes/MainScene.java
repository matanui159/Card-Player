package com.redmintie.game.cards.scenes;

import java.io.IOException;

import com.redmintie.game.cards.Button;
import com.redmintie.game.cards.Res;
import com.redmintie.game.util.core.Game;
import com.redmintie.game.util.core.Scene;
import com.redmintie.game.util.graphics.Canvas;
import com.redmintie.game.util.sound.Sound;

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
	
	public void init() {
		try {
			new Sound("res/sounds/junk.wav").play();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
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