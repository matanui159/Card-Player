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
	SocketChannel channel;
	private Selector selector;
	private SelectionKey key;
	
	private ByteBuffer size = BufferUtils.createByteBuffer(2).order(ByteOrder.BIG_ENDIAN);
	private ByteBuffer data;
	private ByteBuffer output = BufferUtils.createByteBuffer(65535).order(ByteOrder.BIG_ENDIAN);
	
	private boolean close;
	
	public Client(String address, int port) throws IOException {
		channel = SocketChannel.open();
		channel.configureBlocking(false);
		selector = Selector.open();
		key = channel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ);
		channel.connect(new InetSocketAddress(InetAddress.getByName(address), port));
	}
	Client(SocketChannel channel) throws IOException {
		this.channel = channel;
		channel.configureBlocking(false);
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
				close = true;
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
	boolean closeRequested() {
		return close;
	}
	public ByteBuffer getOutputBuffer() {
		return output;
	}
	public void update() throws IOException {
		if (selector.select() > 0) {
			if ((key.readyOps() & SelectionKey.OP_CONNECT) == SelectionKey.OP_CONNECT) {
				channel.finishConnect();
			}
			if ((key.readyOps() & SelectionKey.OP_CONNECT) == SelectionKey.OP_CONNECT) {
				ByteBuffer data = read();
				if (data != null) {
					dataRecieved(data);
					freeData();
				}
				if (closeRequested()) {
					channel.close();
					selector.close();
					clientDisconnected();
					return;
				}
			}
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
	public void close() throws IOException {
		send(0);
		channel.close();
		selector.close();
	}
	
	public abstract void clientConnected();
	public abstract void clientDisconnected();
	public abstract void dataRecieved(ByteBuffer data);
}