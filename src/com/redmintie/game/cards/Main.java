package com.redmintie.game.cards;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import com.redmintie.game.cards.scenes.MainScene;
import com.redmintie.steelplate.core.DeviceException;
import com.redmintie.steelplate.core.Game;
import com.redmintie.steelplate.entity.ui.View;
import com.redmintie.steelplate.input.Keyboard;
import com.redmintie.steelplate.input.event.KeyAdapter;
import com.redmintie.steelplate.input.event.KeyEvent;
import com.redmintie.steelplate.render.Canvas;
import com.redmintie.steelplate.util.DualOutputStream;

public class Main extends Game {
	public static void main(String[] args) {
		try {
			FileOutputStream file = new FileOutputStream("log.txt");
			System.setOut(new PrintStream(new DualOutputStream(System.out, file)));
			System.setErr(new PrintStream(new DualOutputStream(System.err, file)));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		System.setProperty("sun.java2d.opengl", "true");
		try {
			new Main();
		} catch (DeviceException ex) {
			ex.printStackTrace();
		}
	}
	public Main() throws DeviceException {
		setTitle("Card Player");
		setMaximized(true);
		start();
	}
	private View view = new View();
	@Override
	public void init() {
		Keyboard.getKeyboard().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKey() == Keyboard.KEY_ESCAPE) {
					close();
				}
			}
		});
		
		try {
			Res.init();
		} catch (IOException ex) {
			ex.printStackTrace();
			close();
		}
		
		view.setScene(new MainScene());
	}
	@Override
	public void update(double delta) {
		view.update(delta);
	}
	@Override
	public void draw(Canvas canvas) {
		canvas.setColor(Res.BACKGROUND);
		canvas.clear();
		canvas.setFont(Res.FONT);
		
		view.draw(canvas);
	}
	@Override
	public void close() {
		end();
	}
}