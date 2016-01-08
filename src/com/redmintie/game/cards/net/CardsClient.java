package com.redmintie.game.cards.net;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.redmintie.game.util.net.Client;

public class CardsClient extends Client {
	private static CardsClient instance;
	public CardsClient getClientInstance() {
		return instance;
	}
	
	public CardsClient(String address) throws IOException {
		super(address, NetConstants.PORT);
	}
	@Override
	public void clientConnected() {
		
	}
	@Override
	public void clientDisconnected() {
		
	}
	@Override
	public void dataRecieved(ByteBuffer data) {
		
	}
}