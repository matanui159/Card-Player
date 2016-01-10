package com.redmintie.game.cards.scenes;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.redmintie.game.cards.net.CardsClient;
import com.redmintie.game.cards.net.CardsServer;
import com.redmintie.game.util.core.Game;

public class NetScene extends StandardScene {
	@Override
	public void update(double delta) {
		super.update(delta);
		try {
			if (CardsServer.getServerInstance() != null) {
				CardsServer.getServerInstance().updateNow();
			}
			CardsClient.getClientInstance().updateNow();
		} catch (IOException ex) {
			
			if (CardsServer.getServerInstance() != null) {
				CardsServer.getServerInstance().close();
			}
			CardsClient.getClientInstance().close();
			
			Game.setScene(new ErrorScene(ex));
		}
	}
	public void dataRecieved(ByteBuffer data) {
	}
}