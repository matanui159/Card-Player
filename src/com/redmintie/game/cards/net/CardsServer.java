package com.redmintie.game.cards.net;

import java.io.IOException;

import com.redmintie.steelplate.net.Client;
import com.redmintie.steelplate.net.Server;
import com.redmintie.steelplate.net.event.ServerAdapter;
import com.redmintie.steelplate.util.array.MappedArray;

public class CardsServer extends Server {
	private static CardsServer server;
	public static CardsServer getServer() throws IOException {
		if (server == null) {
			server = new CardsServer();
		}
		return server;
	}
	private MappedArray<Client> clients = new MappedArray<Client>();
	// TODO: add CardsClient class with state variable for use in both client and server
	public CardsServer() throws IOException {
		addListener(new ServerAdapter() {
			@Override
			public void clientAccepted(Client client) {
				try {
					if (clients.size() == 4) {
						client.sendObject(new StandardPacket(StandardPacket.ERROR_FULL));
					} else {
						client.sendObject(new StandardPacket(StandardPacket.AVATARS));
						// TODO: add actual avatars
					}
				} catch (IOException ex) {}
			}
		});
	}
	@Override
	public void close() throws IOException {
		super.close();
		server = null;
	}
}