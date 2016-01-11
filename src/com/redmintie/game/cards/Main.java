package com.redmintie.game.cards;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import com.redmintie.game.cards.net.NetConstants;
import com.redmintie.game.cards.scenes.MainScene;
import com.redmintie.game.util.core.Game;
import com.redmintie.game.util.net.Client;
import com.redmintie.game.util.net.Server;

public class Main {
	public static void main(String[] args) {
		try {
			Server server = new Server(NetConstants.PORT) {
				@Override
				public void clientConnected(Client client) {
					System.out.println("CLIENT CONNECTED");
				}
				@Override
				public void clientDisconnected(Client clinet) {
				}
				@Override
				public void dataRecieved(Client client, ByteBuffer data) {
				}
			};
			
			// do some updates
			for (int i = 0; i < 100; i++) {
				server.updateNow();
			}
			
			Client client = new Client(InetAddress.getLocalHost().getHostAddress(), NetConstants.PORT) {
				@Override
				public void clientFailed(IOException ex) {
					ex.printStackTrace();
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
			};
			
			for (int i = 0; i < Integer.MAX_VALUE; i++) {
				server.updateNow();
				client.updateNow();
			}
			
			client.close();
			server.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.exit(0);
		
		setFlag("com.redmintie.DEBUG", "true");
		setFlag("com.redmintie.LOG", "log.txt");
		Game.init();
		Res.init();
		
		Game.setScene(new MainScene());
		
		Game.start();
	}
	public static void setFlag(String name, String value) {
		System.setProperty(name, System.getProperty(name, value));
	}
}