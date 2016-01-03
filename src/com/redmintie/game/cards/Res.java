package com.redmintie.game.cards;

import java.io.IOException;

import com.redmintie.steelplate.render.Color;
import com.redmintie.steelplate.render.Font;
import com.redmintie.steelplate.render.Image;

public class Res {
	public static final Color BACKGROUND = new Color(82, 152, 72);
	public static Font FONT;
	
	public static Image BUTTON_UP;
	public static Image BUTTON_DOWN;
	
	public static final Image[] CARDS = new Image[53];
	public static void init() throws IOException {
		FONT = Font.loadFont("res/font.ttf", Font.PLAIN, 24);
		
		BUTTON_UP = Image.loadImage("res/images/ui/button/up.png");
		BUTTON_DOWN = Image.loadImage("res/images/ui/button/down.png");
		
		for (int i = 0; i < CARDS.length; i++) {
			Image card = Image.loadImage("res/images/cards/" + i + ".png");
			int width = card.getWidth() / 2;
			int height = card.getHeight() / 2;
			CARDS[i] = Image.createImage(width, height);
			CARDS[i].getCanvas().drawImage(card, 0, 0, width, height);
		}
	}
}