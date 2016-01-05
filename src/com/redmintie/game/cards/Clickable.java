package com.redmintie.game.cards;

import com.redmintie.game.util.graphics.Canvas;
import com.redmintie.game.util.input.Input;

public abstract class Clickable {
	private int width;
	private int height;
	public Clickable(int width, int height) {
		this.width = width;
		this.height = height;
	}
	private boolean isHovering() {
		int x = getX();
		int y = getY();
		double cx = Input.getCursorX();
		double cy = Input.getCursorY();
		return cx > x - width / 2 && cx < x + width / 2
				&& cy > y - height / 2 && cy < y + height / 2;
	}
	public void mouseButtonReleased(int button) {
		if (button == Input.MOUSE_BUTTON1 && isHovering()) {
			itemClicked();
		}
	}
	public boolean isItemPressed() {
		return Input.isMouseButtonDown(Input.MOUSE_BUTTON1) && isHovering();
	}
	public void draw() {
		Canvas.pushMatrix();
		Canvas.translate(getX() - width / 2, getY() - height / 2);
	}
	public abstract int getX();
	public abstract int getY();
	public abstract void itemClicked();
}