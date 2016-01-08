package com.redmintie.game.cards.scenes;

import com.redmintie.game.cards.Res;
import com.redmintie.game.util.core.Scene;
import com.redmintie.game.util.graphics.Canvas;

public class StandardScene extends Scene {
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
	}
	public void postDraw() {
		Res.FONT.drawText("FPS: " + fps, 10, Res.FONT.getFontAscent());
	}
}