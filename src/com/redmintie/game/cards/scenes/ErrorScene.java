package com.redmintie.game.cards.scenes;

import com.redmintie.game.cards.Button;
import com.redmintie.game.cards.Res;
import com.redmintie.game.util.core.Game;

public class ErrorScene extends StandardScene {
	private Button ok = new Button("OK") {
		@Override
		public int getX() {
			return Game.getWidth() - Res.BUTTON.getWidth() / 2 - 10;
		}
		@Override
		public int getY() {
			return Game.getHeight() - Res.BUTTON.getHeight() / 2 - 10;
		}
		@Override
		public void itemClicked() {
			Game.setScene(new MainScene());
		}
	};
	
	private String error;
	public ErrorScene(String error) {
		this.error = error;
	}
	public ErrorScene(Exception ex) {
		ex.printStackTrace();
		error = ex.getMessage() == null ? "UNKNOWN ERROR" : ex.getMessage();
	}
	@Override
	public void draw() {
		super.draw();
		
		Res.SMALL_FONT.drawText(error, Game.getWidth() / 2 - Res.SMALL_FONT.getTextWidth(error) / 2,
				Game.getHeight() / 2 - Res.SMALL_FONT.getFontHeight() / 2 + Res.SMALL_FONT.getFontAscent());
		ok.draw();
		
		postDraw();
	}
	@Override
	public void mouseButtonReleased(int button) {
		ok.mouseButtonReleased(button);
	}
}