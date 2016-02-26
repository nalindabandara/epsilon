package com.epsilon.LeakHawk.Visitor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Visitor {

	/**
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException,InterruptedException {
		
		Visitor visitor = new Visitor();
		
		visitor.visit();

		while (true) {

			String[] newLines = visitor.setProxyServerAuto();
			for (int j = 0; j < newLines.length; j++) {
				System.out.println(newLines[j]);

			}
			Thread.sleep(1000);
		}

		// visitor.visit();

		// while(true){
		// visitor.randInt(1, 15);

		// }

	}

	public void visit() throws IOException {
		String initialValue = null;
		int i = 1;
		while (true) {
			
			String[] proxy = getRandomProxy();
				
		//	System.out.println("Proxy IP:"+proxy[0]);
		//	System.out.println("Proxy PORT:"+proxy[1]);
			
			int port = Integer.parseInt(proxy[1]);
			
			Proxy proxy2 = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy[0], port));
			URL url = new URL("http://icanhazip.com/");
			HttpURLConnection uc = (HttpURLConnection)url.openConnection(proxy2);
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
			// initialValue = line;
			// System.out.println("initial value"+);

			reader.close();

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setProxyServerManual(String Host, String Port) {
		System.getProperties().put("proxySet", "true");
		System.getProperties().put("socksProxyHost", Host);
		System.getProperties().put("socksProxyPort", Port);
	}

	public String[] setProxyServerAuto() throws FileNotFoundException, IOException {

		
			
		
		try (BufferedReader br = new BufferedReader(new FileReader(
				"proxyList.txt"))) {

			List<String> lines = new ArrayList<String>();
			String line = null;
			
			while ((line = br.readLine()) != null) {
				
				lines.add(line);
			}
			
			int rand = ThreadLocalRandom.current().nextInt(1,);
			String s1 = lines.get(rand);
			
			String[] arr = new String[2];
			
			arr[0] = s1.substring(0, s1.indexOf(":"));
			arr[1] = s1.substring(s1.indexOf(":") + 1, s1.length());
		
			
			
			//for (int j=0; j < lines.size(); j++){
			//	System.out.println(lines.get(j));
			//		
			//}
				
			//	String IPaddr = line.substring(0, line.indexOf(":"));
			//	String Port = line.substring(line.indexOf(":") + 1,
			//			line.length());
			//	System.out.println(IPaddr);
			//	System.out.println(Port);
			return arr;

		}
	}

	public int randInt(int min, int max){
		
		Random rand;
		return ThreadLocalRandom.current().nextInt(min, max + 1);
		}

	public String[] getRandomProxy() throws FileNotFoundException, IOException {

		try (BufferedReader br = new BufferedReader(new FileReader(
				"proxyList.txt"))) {

			List<String> lines = new ArrayList<String>();
			String line = null;

			while ((line = br.readLine()) != null) {

				lines.add(line);
			}

			int rand = randInt(0, 5);

			String s1 = lines.get(rand);

			String[] arr = new String[2];

			arr[0] = s1.substring(0, s1.indexOf(":"));
			arr[1] = s1.substring(s1.indexOf(":") + 1, s1.length());

			return arr;

		}

	}
}