package com.redmintie.game.cards;

import com.redmintie.steelplate.entity.ui.Button;
import com.redmintie.steelplate.render.Canvas;
import com.redmintie.steelplate.render.Color;

public class StandardButton extends Button {
	private String text;
	public StandardButton(String text) {
		this.text = text;
		width = Res.BUTTON_DOWN.getWidth();
		height = Res.BUTTON_DOWN.getHeight();
	}
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		
		if (getButtonState()) {
			canvas.drawImage(Res.BUTTON_DOWN);
		} else {
			canvas.translate(0, -4);
			canvas.drawImage(Res.BUTTON_UP);
		}
		canvas.setColor(Color.BLACK);
		canvas.drawText(text,
				width / 2 - Res.FONT.getTextWidth(text) / 2,
				height / 2 - Res.FONT.getTextHeight(text) / 2
		);
	}
	@Override
	public void buttonPressed() {
//		TODO: play sound
	}
}