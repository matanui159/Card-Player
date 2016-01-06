package com.redmintie.game.cards;

import com.redmintie.game.util.graphics.Canvas;

public abstract class Button extends Clickable {	
	private String text;
	public Button(String text) {
		super(Res.BUTTON_PRESSED.getWidth(), Res.BUTTON_PRESSED.getHeight());
		this.text = text;
	}
	@Override
	public void draw() {
		super.draw();
		
		if (isItemPressed()) {
			Res.BUTTON_PRESSED.draw(0, 0);
		} else {
			Canvas.translate(0, -4);
			Res.BUTTON.draw(0, 0);
		}
		Canvas.setTint(0, 0, 0);
		Res.FONT.drawText(text, Res.BUTTON.getWidth() / 2 - Res.FONT.getTextWidth(text) / 2,
				Res.FONT.getFontAscent());
		Canvas.setTint(255, 255, 255);
		
		Canvas.popMatrix();
	}
	@Override
	public void itemClicked() {
		// TODO: make sound
	}
}