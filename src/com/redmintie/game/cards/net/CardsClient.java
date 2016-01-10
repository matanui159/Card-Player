package com.redmintie.game.cards.net;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.redmintie.game.cards.scenes.AvatarScene;
import com.redmintie.game.cards.scenes.ErrorScene;
import com.redmintie.game.cards.scenes.NetScene;
import com.redmintie.game.util.core.Game;
import com.redmintie.game.util.net.Client;

public class CardsClient extends Client {
	private static CardsClient instance;
	public static CardsClient getClientInstance() {
		return instance;
	}
	
	private boolean host;
	public CardsClient(String address, boolean host) throws IOException {
		super(address, NetConstants.PORT);
		this.host = host;
		instance = this;
	}
	public boolean isHost() {
		return host;
	}
	@Override
	public void close() {
		super.close();
		instance = null;
	}
	@Override
	public void clientFailed(IOException ex) {
		Game.setScene(new ErrorScene(ex));
	}
	@Override
	public void clientConnected() {
		Game.setScene(new AvatarScene());
	}
	@Override
	public void clientDisconnected() {
		Game.setScene(new ErrorScene("Sudden Disconnect"));
	}
	@Override
	public void dataRecieved(ByteBuffer data) {
		try {
			byte code = data.get(0);
			switch (code) {
			case NetConstants.ERROR_COMMUNICATION:
				close();
				Game.setScene(new ErrorScene("Failed to Communicate"));
				break;
			case NetConstants.ERROR_FULL:
				close();
				Game.setScene(new ErrorScene("Game Full"));
				break;
			case NetConstants.ERROR_INGAME:
				close();
				Game.setScene(new ErrorScene("Game already Started"));
				break;
			default:
				((NetScene)Game.getScene()).dataRecieved(data);
			}
		} catch (Exception ex) {
			close();
			Game.setScene(new ErrorScene("Failed to Communicate"));
		}
	}
}