package com.redmintie.game.util.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;

public class NetUtil {
	public static String getLocalAddress() throws IOException {
		return InetAddress.getLocalHost().getHostAddress();
	}
	public static String getPublicAddress() throws IOException {
		BufferedReader reader = null;
		try {
			URL url = new URL("http://api.ipify.org");
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			return reader.readLine();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
}