//package com.redmintie.game.cards.scenes;
//
//import java.io.IOException;
//
//import com.redmintie.steelplate.entity.ui.Scene;
//import com.redmintie.steelplate.render.Canvas;
//
//public class HostScene extends Scene {
//	private IOException error;
//	@Override
//	public void init() {
//		try {
////			CardsServer.getServer();
//			throw new IOException("MWA HA HA");
//		} catch (IOException ex) {
//			error = ex;
//		}
//	}
//	@Override
//	public void update(double delta) {
//		if (error != null) {
//			getView().setScene(new ErrorScene(error));
//			return;
//		}
//	}
//	@Override
//	public void draw(Canvas canvas) {
//	}
//	@Override
//	public void end() {
//	}
//}