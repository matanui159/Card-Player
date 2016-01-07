package com.redmintie.game.util.net;

import static org.lwjgl.system.MemoryUtil.memAlloc;
import static org.lwjgl.system.MemoryUtil.memFree;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Set;

import org.lwjgl.BufferUtils;

public abstract class Server {
	private ServerSocketChannel channel;
	private Selector selector;
	private ArrayList<Client> clients = new ArrayList<Client>();
	
	private ByteBuffer output = BufferUtils.createByteBuffer(65535).order(ByteOrder.BIG_ENDIAN);
	
	public Server(int port) throws IOException {
		init(new InetSocketAddress(InetAddress.getLoopbackAddress(), port));
	}
	public Server() throws IOException {
		init(null);
	}
	private void init(InetSocketAddress address) throws IOException {
		channel = ServerSocketChannel.open();
		channel.configureBlocking(false);
		selector = Selector.open();
		channel.register(selector, SelectionKey.OP_ACCEPT);
		channel.bind(address);
	}
	public void update() throws IOException {
		if (selector.select() > 0) {
			Set<SelectionKey> keys = selector.selectedKeys();
			for (SelectionKey key : keys) {
				if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
					SocketChannel socket = ((ServerSocketChannel)key.channel()).accept();
					Client client = new ClientConnection(socket);
					socket.register(selector, SelectionKey.OP_READ, socket);
					clients.add(client);
				}
				if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
					Client client = (Client)key.attachment();
					ByteBuffer data = client.read();
					if (data != null) {
						dataRecieved(client, data);
						client.freeData();
					}
					if (client.closeRequested()) {
						client.channel.close();
						clients.remove(client);
						clientDisconnected(client);
					}
				}
			}
		}
	}
	public ByteBuffer getOutputBuffer() {
		return output;
	}
	public void sendAll(int size) throws IOException {
		ByteBuffer s = memAlloc(2).order(ByteOrder.BIG_ENDIAN);
		s.putShort((short)size);
		output.rewind();
		output.limit(size);
		for (Client client : clients) {
			s.rewind();
			while (s.hasRemaining()) {
				client.channel.write(s);
			}
			output.rewind();
			while (output.hasRemaining()) {
				client.channel.write(output);
			}
		}
		memFree(s);
	}
	public void close() throws IOException {
		sendAll(0);
		channel.close();
		selector.close();
	}
	
	public abstract void clientConnected(Client client);
	public abstract void clientDisconnected(Client client);
	public abstract void dataRecieved(Client client, ByteBuffer data);
	
	private class ClientConnection extends Client {
		public ClientConnection(SocketChannel channel) throws IOException {
			super(channel);
		}
		public void clientConnected() {
		}
		public void clientDisconnected() {
		}
		public void dataRecieved(ByteBuffer data) {
		}
	}
}