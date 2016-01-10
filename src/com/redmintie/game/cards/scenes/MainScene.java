package com.redmintie.game.cards.scenes;

import java.io.IOException;

import com.redmintie.game.cards.Button;
import com.redmintie.game.cards.net.CardsServer;
import com.redmintie.game.util.core.Game;

public class MainScene extends StandardScene {
	private Button host = new Button("HOST") {
		@Override
		public int getX() {
			return Game.getWidth() / 2;
		}
		@Override
		public int getY() {
			return Game.getHeight() / 2 - 30;
		}
		@Override
		public void itemClicked() {
			try {
				new CardsServer();
			} catch (IOException ex) {
				Game.setScene(new ErrorScene(ex));
				return;
			}
			Game.setScene(new ConnectionScene("127.0.0.1", true));
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
		@Override
		public void itemClicked() {
			Game.setScene(new JoinScene());
		}
	};
	
	@Override
	public void draw() {
		super.draw();
		host.draw();
		join.draw();
		postDraw();
	}
	@Override
	public void mouseButtonReleased(int button) {
		host.mouseButtonReleased(button);
		join.mouseButtonReleased(button);
	}
}