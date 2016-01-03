package com.redmintie.game.cards.scenes;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import com.redmintie.game.util.Game;
import com.redmintie.game.util.Scene;
import com.redmintie.game.util.Sprite;
//
//import com.redmintie.game.cards.StandardButton;
//import com.redmintie.steelplate.core.Game;
//import com.redmintie.steelplate.entity.ui.Button;
//import com.redmintie.steelplate.entity.ui.Scene;
//import com.redmintie.steelplate.render.Canvas;
//
//public class MainScene extends Scene {
//	private Button host = new StandardButton("Host") {
//		@Override
//		public void update(double delta) {
//			super.update(delta);
//			position.set(
//					Game.getGameInstance().getWidth() / 2,
//					Game.getGameInstance().getHeight() / 2 - 30);
//		}
//		@Override
//		public void buttonPressed() {
//			super.buttonPressed();
//			getView().setScene(new HostScene());
//		}
//	};
//	private Button join = new StandardButton("Join") {
//		@Override
//		public void update(double delta) {
//			super.update(delta);
//			position.set(
//					Game.getGameInstance().getWidth() / 2,
//					Game.getGameInstance().getHeight() / 2 + 30);
//		}
//	};
//	@Override
//	public void init() {
//	}
//	@Override
//	public void update(double delta) {
//		host.update(delta);
//		join.update(delta);
//	}
//	@Override
//	public void draw(Canvas canvas) {
//		host.draw(canvas);
//		join.draw(canvas);
//	}
//	@Override
//	public void end() {
//	}
//}

public class MainScene extends Scene {
	private Sprite test;
	public MainScene() {
		try {
			test = new Sprite("res/images/cards/0.png", Sprite.FILTER_NEAREST, Sprite.WRAP_REPEAT);
		} catch (IOException ex) {
			ex.printStackTrace();
			Game.end();
		}
	}
	public void draw() {
		GL11.glClearColor(0, 0, 0, 0);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		test.draw(0, 0, test.getWidth(), test.getHeight());
	}
}