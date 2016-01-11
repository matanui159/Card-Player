package com.redmintie.game.cards.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import com.redmintie.game.util.net.Client;
import com.redmintie.game.util.net.Server;

public class CardsServer extends Server {
	private static CardsServer instance;
	public static CardsServer getServerInstance() {
		return instance;
	}
	
	private boolean started = false;
	private Client[] players = new Client[4];
	private byte[] avatars = new byte[4];
	private int count;
	
	public CardsServer() throws IOException {
		super(NetConstants.PORT);
		instance = this;
		Arrays.fill(avatars, (byte)-1);
	}
	private void error(Client client, byte code) {
		client.getOutputBuffer().put(code);
		try {
			client.send(1);
		} catch (IOException ex) {}
		clientDisconnected(client);
	}
	@Override
	public void close() {
		super.close();
		instance = null;
	}
	@Override
	public void clientConnected(Client client) {
		try {
			if (started) {
				error(client, NetConstants.ERROR_INGAME);
			}
			if (count == 4) {
				error(client, NetConstants.ERROR_FULL);
			}
			players[count++] = client;
			
			for (byte avatar: avatars) {
				if (avatar != -1) {
					client.getOutputBuffer().put(NetConstants.AVATAR_TAKEN);
					client.getOutputBuffer().put(avatar);
					client.send(2);
				}
			}
		} catch (Exception ex) {
			error(client, NetConstants.ERROR_COMMUNICATION);
		}
	}
	@Override
	public void clientDisconnected(Client client) {
		for (int i = 0; i < count; i++) {
			if (players[i] == client) {
				players[i] = null;
				if (!started) {
					for (int j = i; j < count - 1; j++) {
						players[j] = players[j + 1];
					}
					players[--count] = null;
				}
				break;
			}
		}
		System.out.println("client disconnected");
	}
	@Override
	public void dataRecieved(Client client, ByteBuffer data) {
		try {
			
		} catch (Exception ex) {
			error(client, NetConstants.ERROR_COMMUNICATION);
		}
	}
}