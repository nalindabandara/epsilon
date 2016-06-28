package com.epsilon.Leak.Hawk.sensor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.epsilon.Leak.Hawk.model.FeedEntry;
import com.epsilon.Leak.Hawk.utils.LeakHawkUtils;

public class PastbinSensor extends SensorJob {

	public PastbinSensor(){
		
		this.connectingUrl = "http://pastebin.com/api_scraping.php?limit=";
		this.feedEntriesPerHit = 100;		
	}

	public void run(){
		
		manageDownloadProcess();
	}
	
	@Override
	protected void manageDownloadProcess() {

		int count = 0;
		FeedEntry firstElement = null;
		while (true) {
			
			// Scan the web page and extract required url list
			List<FeedEntry> feedEntryList = downloadEntries( connectingUrl + feedEntriesPerHit);
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
				//printList(newFeedEntryList);
				firstElement = newFeedEntryList.get(0);					
			} else {	
				//printList(feedEntryList);
				firstElement = feedEntryList.get(0);
				newFeedEntryList = feedEntryList;
			}
			
			
			for( FeedEntry entry : newFeedEntryList ){				
				getAggregator().addFeedEntry(entry);	
				//System.out.println( "Added to Queue : " + entry );
			}
			
			try {
				Thread.sleep( 60000 );
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
			count++;
		}
		
	}
	

	private List<FeedEntry> downloadEntries(String url) {

		System.out.println("Scanning page : " + url);
		LeakHawkUtils.printDateTime();
		List<FeedEntry> feedEntryList = new ArrayList<FeedEntry>();
		URL urlObj = null;
		JSONParser parser = new JSONParser();
		try {

			urlObj = new URL( url );
			String webPageContent = LeakHawkUtils.getStringFromInputStream( urlObj.openStream() );
			
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
	
	


}
