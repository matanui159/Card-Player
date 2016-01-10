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
	Selector selector;
	ArrayList<Client> clients = new ArrayList<Client>();
	
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
	private void update() throws IOException {
		Set<SelectionKey> keys = selector.selectedKeys();
		for (SelectionKey key : keys) {
			if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
				SocketChannel socket = channel.accept();
				Client client = new ClientConnection(this, socket);
				socket.register(selector, SelectionKey.OP_READ, client);
				clients.add(client);
				clientConnected(client);
			}
			if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
				Client client = (Client)key.attachment();
				ByteBuffer data = null;
				try {
					data = client.read();
					if (data != null) {
						dataRecieved(client, data);
					}
				} catch (IOException ex) {
					client.close();
					clientDisconnected(client, ex);
				} finally {
					if (data != null) {
						client.freeData();
					}
				}
			}
		}
		keys.clear();
	}
	public void update(int timeout) throws IOException {
		if (selector.select(timeout) > 0) {
			update();
		}
	}
	public void updateNow() throws IOException {
		if (selector.selectNow() > 0) {
			update();
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
	public void close() {
		try {
			channel.close();
		} catch (IOException ex) {}
		try {
			selector.close();
		} catch (IOException ex) {}
	}
	
	public abstract void clientConnected(Client client);
	public abstract void clientDisconnected(Client client, IOException ex);
	public abstract void dataRecieved(Client client, ByteBuffer data);
	
	private class ClientConnection extends Client {
		public ClientConnection(Server server, SocketChannel channel) throws IOException {
			super(server, channel);
		}
		@Override
		public void clientFailed(IOException ex) {
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
}