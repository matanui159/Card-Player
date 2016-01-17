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
import java.nio.channels.SocketChannel;

import org.lwjgl.BufferUtils;

public abstract class Client {
	private Server server;
	SocketChannel channel;
	private SelectionKey key;
	private Selector selector;
	private boolean connected;
	
	private ByteBuffer size = BufferUtils.createByteBuffer(2).order(ByteOrder.BIG_ENDIAN);
	private ByteBuffer data;
	private ByteBuffer output = BufferUtils.createByteBuffer(65535).order(ByteOrder.BIG_ENDIAN);
	
	public Client(String address, int port) throws IOException {
		channel = SocketChannel.open();
		channel.configureBlocking(false);
		selector = Selector.open();
		key = channel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ);
		channel.connect(new InetSocketAddress(InetAddress.getByName(address), port));
	}
	Client(Server server, SocketChannel channel) throws IOException {
		this.server = server;
		this.channel = channel;
		channel.configureBlocking(false);
	}
	public String getAddress() {
		try {
			return ((InetSocketAddress)channel.getRemoteAddress()).getAddress().getHostAddress();
		} catch (IOException ex) {}
		return null;
	}
	public int getPort() {
		try {
			return ((InetSocketAddress)channel.getRemoteAddress()).getPort();
		} catch (IOException ex) {}
		return 0;
	}
	ByteBuffer read() throws IOException {
		if (data == null) {
			channel.read(size);
			if (size.hasRemaining()) {
				return null;
			}
			size.rewind();
			int s = size.getShort(0) & 0xFFFF;
			if (s == 0) {
				return null;
			}
			data = memAlloc(s).order(ByteOrder.BIG_ENDIAN);
		}
		channel.read(data);
		if (data.hasRemaining()) {
			return null;
		}
		data.rewind();
		return data;
	}
	void freeData() {
		memFree(data);
		data = null;
	}
	public ByteBuffer getOutputBuffer() {
		return output;
	}
	private void update() throws IOException {
		if ((key.readyOps() & SelectionKey.OP_CONNECT) == SelectionKey.OP_CONNECT && !connected) {
			try {
				channel.finishConnect();
				clientConnected();
			} catch (IOException ex) {
				close();
				clientFailed(ex);
			}
			connected = true;
			return;
		}
		if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
			ByteBuffer data = null;
			try {
				data = read();
				if (data != null) {
					dataRecieved(data);
				}
			} catch (IOException ex) {
				close();
				clientDisconnected();
				return;
			} finally {
				if (data != null) {
					freeData();
				}
			}
		}
		selector.selectedKeys().clear();
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
	public void send(int size) throws IOException {
		ByteBuffer s = memAlloc(2).order(ByteOrder.BIG_ENDIAN);
		s.putShort((short)size);
		output.rewind();
		output.limit(size);
		while (s.hasRemaining()) {
			channel.write(s);
		}
		while (output.hasRemaining()) {
			channel.write(output);
		}
		memFree(s);
	}
	public void close() {
		try {
			channel.close();
		} catch (IOException ex) {}
		if (server == null) {
			try {
				selector.close();
			} catch (IOException ex) {}
		} else {
			server.clients.remove(this);
		}
	}
	
	public abstract void clientFailed(IOException ex);
	public abstract void clientConnected();
	public abstract void clientDisconnected();
	public abstract void dataRecieved(ByteBuffer data);
}