package com.epsilon.LeakHawk.scrapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.Document;

//import org.jsoup.nodes.Document;

public class ScrapperManager {

	private static String BASE_URL = "http://pastebin.com/raw.php?i=";

	private static int MAX_URLS_PER_PAGE = 10;

	public static void main(String[] args) {

		ScrapperManager manager = new ScrapperManager();
		manager.execute();
	}

	public void execute() {

		while (true) {

			System.out
					.println("*************** Scrapper Started ****************");
			long start = System.currentTimeMillis();

			// load keyword list from a file
			List<String> keyWordList = readKeyWordList("keywords.txt");

			// Scan the web page and extract required url list
			List<String> urlList = scrapper("http://pastebin.com/archive");

			if (urlList.size() > 0) {
				launch(urlList, keyWordList);
			}
			long end = System.currentTimeMillis();

			System.out
					.println("*************** Scrapper Stopped ****************");
			System.out.println("Time taken for the scrapper process : "
					+ (end - start) + " ms");

			try {
				Thread.sleep(8000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public List<String> readKeyWordList(String fileName) {

		List<String> words = new ArrayList<String>();
		BufferedReader reader = null;
		;
		String line;
		try {
			reader = new BufferedReader(new FileReader(fileName));
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				words.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return words;

	}

	public void launch(List<String> urlList, List<String> keyWordList) {

		for (String url : urlList) {
			if (isContainKeyWord(url, keyWordList)) {
				downloadPage(url);
			}
		}
	}

	public boolean isContainKeyWord(String url, List<String> keyWordList) {

		try {
			// System.out.println(url);
			URL my_url = new URL(url);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					my_url.openStream()));
			String strTemp = "";
			while (null != (strTemp = br.readLine())) {

				for (String keyword : keyWordList) {
					if (strTemp.toUpperCase().contains(keyword.toUpperCase())) {
						return true;
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public void downloadPage(String url) {

		System.out.println("Downloading page : " + url);
		Document doc;
		try {

			String fileName = url.substring((url.length() - 8), url.length());
			URL urlObj = new URL(url);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlObj.openStream()));
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
			String line;
			while ((line = reader.readLine()) != null) {
				writer.write(line);
				writer.newLine();
			}
			reader.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private List<String> scrapper(String url) {

		System.out.println("Scanning page : " + url);

		List<String> hyperLinkList = new ArrayList<String>();

		String regExp = "<td><img src=\"/i/t.gif\"  class=\"i_p0\" alt=\"\" /><a href=\"/[0-9a-zA-Z]{8}\">.*</a></td>";

		// Create a Pattern object
		Pattern pattern = Pattern.compile(regExp);

		URL urlObj = null;
		try {
			urlObj = new URL(url);

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlObj.openStream()));

			String line;

			int count = 0;
			while ((line = reader.readLine()) != null) {
				if (count < MAX_URLS_PER_PAGE) {
					Matcher m = pattern.matcher(line);
					if (m.find()) {
						// System.out.println("Found pattern: " + m.group(0));
						String group0 = m.group(0);
						if (group0 != null) {
							String linkId = group0.substring(57, 65);
							// System.out.println( linkId );
							hyperLinkList.add(BASE_URL + linkId);
							count++;
						}
					}
				} else {
					break;
				}
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return hyperLinkList;
	}

}