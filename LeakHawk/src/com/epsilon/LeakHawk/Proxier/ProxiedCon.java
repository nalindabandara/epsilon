package com.epsilon.LeakHawk.Proxier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class ProxiedCon {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		String initialValue = null;
		int i = 1;
		
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("124.88.67.33", 843));
		URL url = new URL("http://icanhazip.com/");
		HttpURLConnection uc = (HttpURLConnection)url.openConnection(proxy);
		InputStream in = uc.getInputStream();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		
		String line = null;
		while ((line = reader.readLine()) != null) {

			if (i <= 1) {
				initialValue = line;
				System.out.println("First IP:" + initialValue);
				i++;
			}
			if (line.equals(initialValue)) {
				System.out.println("same");
			} else {
				System.out.println(line);

			}

		}
		
	}

}