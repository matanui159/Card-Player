package com.redmintie.game.cards.scenes;

import java.io.IOException;
import java.net.UnknownHostException;

import com.redmintie.game.cards.Res;
import com.redmintie.game.cards.net.CardsClient;
import com.redmintie.game.util.core.Game;

public class ConnectionScene extends NetScene {
	private double time;
	private int dots = 0;
	private String address;
	private boolean host;
	public ConnectionScene(String address, boolean host) {
		this.address = address;
		this.host = host;
	}
	@Override
	public void init() {
		try {
			new CardsClient(address, host);
		} catch (UnknownHostException ex) {
			Game.setScene(new ErrorScene("UNKNOWN HOST: " + address));
		} catch (IOException ex) {
			Game.setScene(new ErrorScene(ex));
		}
	}
	@Override
	public void update(double delta) {
		super.update(delta);
		time += delta;
		if (time >= 1) {
			dots++;
			if (dots > 3) {
				dots = 0;
			}
			time = 0;
		}
	}
	@Override
	public void draw() {
		super.draw();
		
		String text = "CONNECTING";
		for (int i = 0; i < dots; i++) {
			text += ".";
		}
		Res.FONT.drawText(text, Game.getWidth() / 2 - Res.FONT.getTextWidth(text) / 2,
				Game.getHeight() / 2 - Res.FONT.getFontHeight() / 2 + Res.FONT.getFontAscent());
		
		postDraw();
	}
}