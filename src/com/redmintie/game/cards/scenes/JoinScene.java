package com.redmintie.game.cards.scenes;

import com.redmintie.game.cards.Button;
import com.redmintie.game.cards.Res;
import com.redmintie.game.util.core.Game;
import com.redmintie.game.util.input.Input;

public class JoinScene extends StandardScene {
	private static final String PROMPT = "ENTER THE ADDRESS YOU WANT TO JOIN:";
	
	private Button back = new Button("BACK") {
		@Override
		public int getX() {
			return Res.BUTTON.getWidth() / 2 + 10;
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
	
	private Button join = new Button("JOIN") {
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
			Game.setScene(new ConnectionScene(address, false));
		}
	};
	
	private String address = "";
	@Override
	public void draw() {
		super.draw();
		
		Res.FONT.drawText(PROMPT, Game.getWidth() / 2 - Res.FONT.getTextWidth(PROMPT) / 2,
				Game.getHeight() / 2 - Res.FONT.getFontHeight() - Res.FONT.getFontAscent()
				- Res.FONT.getFontLinegap());
		Res.FONT.drawText(address, Game.getWidth() / 2 - Res.FONT.getTextWidth(address) / 2,
				Game.getHeight() / 2 - Res.FONT.getFontHeight() / 2 + Res.FONT.getFontAscent());
		back.draw();
		join.draw();
		
		postDraw();
	}
	@Override
	public void keyPressed(int key, boolean repeat) {
		if (key == Input.KEY_BACKSPACE && !address.isEmpty()) {
			address = address.substring(0, address.length() - 1);
		}
	}
	@Override
	public void keyReleased(int key) {
		if (key == Input.KEY_ENTER) {
			Game.setScene(new ConnectionScene(address, false));
		}
	}
	@Override
	public void keyTyped(char c) {
		address += c;
	}
	@Override
	public void mouseButtonReleased(int button) {
		back.mouseButtonReleased(button);
		join.mouseButtonReleased(button);
	}
}