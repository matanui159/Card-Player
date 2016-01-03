package com.redmintie.game.cards.scenes;

import com.redmintie.game.cards.StandardButton;
import com.redmintie.steelplate.core.Game;
import com.redmintie.steelplate.entity.ui.Button;
import com.redmintie.steelplate.entity.ui.Scene;
import com.redmintie.steelplate.render.Canvas;

public class MainScene extends Scene {
	private Button host = new StandardButton("Host") {
		@Override
		public void update(double delta) {
			super.update(delta);
			position.set(
					Game.getGameInstance().getWidth() / 2,
					Game.getGameInstance().getHeight() / 2 - 30);
		}
		@Override
		public void buttonPressed() {
			super.buttonPressed();
			getView().setScene(new HostScene());
		}
	};
	private Button join = new StandardButton("Join") {
		@Override
		public void update(double delta) {
			super.update(delta);
			position.set(
					Game.getGameInstance().getWidth() / 2,
					Game.getGameInstance().getHeight() / 2 + 30);
		}
	};
	@Override
	public void init() {
	}
	@Override
	public void update(double delta) {
		host.update(delta);
		join.update(delta);
	}
	@Override
	public void draw(Canvas canvas) {
		host.draw(canvas);
		join.draw(canvas);
	}
	@Override
	public void end() {
	}
}