package com.redmintie.game.cards.scenes;

import com.redmintie.game.cards.Res;
import com.redmintie.game.cards.StandardButton;
import com.redmintie.steelplate.core.Game;
import com.redmintie.steelplate.entity.ui.Scene;
import com.redmintie.steelplate.render.Canvas;
import com.redmintie.steelplate.render.Color;

public class ErrorScene extends Scene {
	private StandardButton done = new StandardButton("Done") {
		@Override
		public void update(double delta) {
			super.update(delta);
			position.set(
					Game.getGameInstance().getWidth() - width - 10,
					Game.getGameInstance().getHeight() - height - 10
			);
		}
		@Override
		public void buttonPressed() {
			super.buttonPressed();
			getView().setScene(new MainScene());
		}
	};
	private String error;
	public ErrorScene(String error) {
		this.error = error;
	}
	public ErrorScene(Exception ex) {
		error = ex.toString();
	}
	@Override
	public void init() {
	}
	@Override
	public void update(double delta) {
		done.update(delta);
	}
	@Override
	public void draw(Canvas canvas) {
		canvas.setColor(Color.WHITE);
		canvas.drawText(error,
				Game.getGameInstance().getWidth() / 2 - Res.FONT.getTextWidth(error) / 2,
				Game.getGameInstance().getHeight() / 2 - Res.FONT.getTextHeight(error) / 2);
		done.draw(canvas);
	}
	@Override
	public void end() {
	}
}