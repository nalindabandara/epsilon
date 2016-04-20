package com.epsilon.LeakHawk.scrapper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.epsilon.LeakHawk.cluster.ClusterJob;



//import org.jsoup.nodes.Document;

public class ScrapperManager {

	private static SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd 'at' hh:mm:ss");

	private static int FEEDS_PER_HIT = 100;
	
	public static boolean isApplyingFilter;

	private static Properties prop = new Properties();
	
	public static List<String> keyWordList = null;
	
	public static List<String> allowedSyntaxList = null;

	public static void main(String[] args) {

		readConfigFile();
		ScrapperManager scrapperManager = new ScrapperManager();
		scrapperManager.execute();
	}

	public void execute() {

		System.out.print("Do you want to apply the context filtering ? [y/n] : ");
		
		try{
		    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		    String s = bufferRead.readLine();
		      
		    
		    if( s.equalsIgnoreCase("y")){
		    	setApplyingFilter(true);		    	
		    } else {
		    	setApplyingFilter(false);		    	
		    }		    
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		int count = 0;
		FeedEntry firstElement = null;
		while (true) {			
			System.out.println("*************** Scrapper Job Started ****************");
			printDate();
			long start = System.currentTimeMillis();

			// Scan the web page and extract required url list
			List<FeedEntry> feedEntryList = performScrapper("http://pastebin.com/api_scraping.php?limit=" + FEEDS_PER_HIT);
			List<FeedEntry> newFeedEntryList = null;
					
			
			if( count > 0 ){	
				newFeedEntryList = new ArrayList<FeedEntry>();
				for( FeedEntry key : feedEntryList ){					
					if( key.equals( firstElement )){
						break;
					} else {
						newFeedEntryList.add(key);
					}
				}
				printList(newFeedEntryList);
				firstElement = newFeedEntryList.get(0);					
			} else {	
				printList(feedEntryList);
				firstElement = feedEntryList.get(0);
				newFeedEntryList = feedEntryList;
			}
					
			// Start a separate thread to download the selected pages.
			ScrapperJob scrapperJob = new ScrapperJob();
			scrapperJob.setFeedEntryList( newFeedEntryList );				
			scrapperJob.start();
			
			if( count == 0 ){
				System.out.println("****************** Custer Job Started *******************");
				ClusterJob clusterJob = new ClusterJob();
				clusterJob.start();
			}
			
			long end = System.currentTimeMillis();				
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

	
	private void printList(List<FeedEntry> lst ){
		
		for( FeedEntry ls : lst ){
			System.out.println(ls.toString());
		}
		System.out.println("Number of entries retrived  : " + lst.size());
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

	private List<FeedEntry> performScrapper(String url) {

		System.out.println("Scanning page : " + url);
		List<FeedEntry> feedEntryList = new ArrayList<FeedEntry>();
		URL urlObj = null;
		JSONParser parser = new JSONParser();
		try {

			urlObj = new URL( url );
			String webPageContent = getStringFromInputStream( urlObj.openStream() );
			
			//System.out.println( webPageContent );
			Object obj = parser.parse( webPageContent );
	        JSONArray array = (JSONArray)obj;
	        	        
	        for( int i = 0; i < array.size(); i++ ) {	        	
	        	JSONObject jsonObj = (JSONObject) array.get(i);	        	
	        	FeedEntry feedEntry = new FeedEntry( jsonObj );	        		        	
	        	feedEntryList.add( feedEntry);	        	
	        }	        	       
		} catch ( MalformedURLException e ) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return feedEntryList;
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
	
		
	private static void readConfigFile( ){
		
		InputStream input = null;
		try {
			System.out.println("************* Reading the configuration file ***************");
			input = new FileInputStream("config.properties");

			// load a properties file
			prop.load(input);
					
			keyWordList = Arrays.asList( prop.getProperty("keyword.list").split(","));
			allowedSyntaxList = Arrays.asList( prop.getProperty("allowed.syntax.list").split(","));
			
			// get the property value and print it out
			System.out.println( "Key Word List : " + keyWordList );
			System.out.println( "Allowed Syntax List : " + allowedSyntaxList );	

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
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
	
	public boolean isApplyingFilter() {
		return isApplyingFilter;
	}

	public void setApplyingFilter(boolean isApplyingFilter) {
		this.isApplyingFilter = isApplyingFilter;
	}

}