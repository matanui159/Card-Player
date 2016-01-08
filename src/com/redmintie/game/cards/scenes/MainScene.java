package com.redmintie.game.cards.scenes;

import com.redmintie.game.cards.Button;
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