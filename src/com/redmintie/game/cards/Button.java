package com.redmintie.game.cards;

import com.redmintie.game.util.graphics.Canvas;

public abstract class Button extends Clickable {	
	private String text;
	public Button(String text) {
		super(Res.BUTTON.getWidth(), Res.BUTTON.getHeight());
		this.text = text;
	}
	@Override
	public void draw() {
		super.draw();
		
		if (isItemPressed()) {
			Canvas.translate(0, 4);
			Res.BUTTON_PRESSED.draw(0, 0);
		} else {
			Res.BUTTON.draw(0, 0);
		}
		Canvas.setTint(0, 0, 0);
		Res.FONT.drawText(text, 0, Res.FONT.getFontAscent());
		Canvas.setTint(255, 255, 255);
		
		Canvas.popMatrix();
	}
	@Override
	public void itemClicked() {
		// TODO: make sound
	}
}