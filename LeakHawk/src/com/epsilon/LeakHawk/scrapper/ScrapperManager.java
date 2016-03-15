package com.epsilon.LeakHawk.scrapper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;



//import org.jsoup.nodes.Document;

public class ScrapperManager {

	private static SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd 'at' hh:mm:ss");

	private static int FEEDS_PER_HIT = 100;

	public static void main(String[] args) {

		ScrapperManager manager = new ScrapperManager();
		manager.execute();
	}

	public void execute() {

		int count = 0;
		String firstElement = "";
		while (true) {			
			System.out.println("*************** Scrapper Started ****************");
			printDate();
			long start = System.currentTimeMillis();

			// load keyword list from a file
			List<String> keyWordList = readKeyWordList("keywords.txt");

			// Scan the web page and extract required url list
			List<String> keyList = performScrapper("http://pastebin.com/api_scraping.php?limit=" + FEEDS_PER_HIT);
			List<String> newKeyList = null;
					
			printList(keyList);
			if( count > 0 ){	
				newKeyList = new ArrayList<String>();
				for( String key : keyList ){					
					if( key.equals( firstElement )){
						break;
					} else {
						newKeyList.add(key);
					}
				}
				printList(newKeyList);
				firstElement = newKeyList.get(0);					
			} else {						
				firstElement = keyList.get(0);
				newKeyList = keyList;
			}
					
			// Start a separate thread to download the selected pages.
			ScrapperJob scrapperJob = new ScrapperJob();
			scrapperJob.setKeyList( newKeyList );	
			scrapperJob.setKeywordList(keyWordList);
			scrapperJob.start();
			
			long end = System.currentTimeMillis();

			System.out.println("*************** Scrapper Stopped ****************\n");
			
			long timeTakenToProcess = (end - start);
			System.out.println("Time taken for the scrapper process : " + timeTakenToProcess + " ms");

			try {
				Thread.sleep( 60000 );
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
			count++;
		}
	}

	
	private void printList(List<String> lst ){
		
		for( String ls : lst ){
			System.out.println(ls);
		}
		System.out.println("Size of the length : " + lst.size());
	}
	
	private static void printDate(){
		
	     Date dNow = new Date( );	      
	     System.out.println("Current Time: " + ft.format(dNow));
	}
	
	
	public List<String> readKeyWordList(String fileName) {

		List<String> words = new ArrayList<String>();
		BufferedReader reader = null;		
		String line;
		try {
			reader = new BufferedReader(new FileReader(fileName));
			while ((line = reader.readLine()) != null) {
				//System.out.println(line);
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





	private List<String> performScrapper(String url) {

		System.out.println("Scanning page : " + url);
		List<String> keyList = new ArrayList<String>();
		URL urlObj = null;
		JSONParser parser = new JSONParser();
		try {

			urlObj = new URL( url );
			String webPageContent = getStringFromInputStream( urlObj.openStream() );
			
			Object obj = parser.parse( webPageContent );
	        JSONArray array = (JSONArray)obj;
	        	        
	        for( int i = 0; i < array.size(); i++ ) {	        	
	        	JSONObject jsonObj = (JSONObject) array.get(i);	        	
	        	FeedEntry feedEntry = new FeedEntry( jsonObj );	        		        	
	        	keyList.add( feedEntry.getKey() );
	        	//System.out.println( feedEntry.getKey() );
	        }	        	       
		} catch ( MalformedURLException e ) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return keyList;
	}

	
	// convert InputStream to String
	private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}	
	
		
		
	/*
	private List<String> performScrapperEx(String url) {

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
	
	*/

}