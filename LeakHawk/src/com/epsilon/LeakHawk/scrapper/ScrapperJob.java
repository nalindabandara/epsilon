package com.epsilon.LeakHawk.scrapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import com.epsilon.LeakHawk.db.DBConnector;


public class ScrapperJob extends Thread {

	private static String BASE_URL = "http://pastebin.com/api_scrape_item.php?i=";
	
	private List<FeedEntry> feedEntryList;
	
	private List<String> keywordList;
	
	public void run(){
		
		launch();
	}
	
	private void launch() {

		if( this.keywordList != null && this.keywordList.size() > 0 ){
			
			if( this.feedEntryList != null && this.feedEntryList.size() > 0 ){
				
				if( ScrapperManager.isApplyingFilter ){
				
					for (FeedEntry feedEntry : this.feedEntryList) {
						
						if (isContainKeyWord( feedEntry , this.keywordList )) {
							
							// as we have multiple threads, DB access is limited to one thread											
							synchronized (this) {
								feedEntry.save();
							}
						}				
					}
				} else {
					for (FeedEntry feedEntry : this.feedEntryList) {
																
						// as we have multiple threads, DB access is limited to one thread											
						synchronized (this) {
							feedEntry.save();
						}									
					}										
				}
				
				
			} else {
				System.out.println( "****************** No urls were found for the scrapper job ******************");
			}
		} else {			
			System.out.println( "****************** No keywords were found for the scrapper job ******************");
		}
		

		
	}
		
	private boolean isContainKeyWord(FeedEntry entry, List<String> keyWordList) {

		try {			
			URL my_url = new URL(entry.getScrapperUrl());
			BufferedReader br = new BufferedReader(new InputStreamReader(
					my_url.openStream()));
			String strTemp = "";
			while (null != (strTemp = br.readLine())) {

				for (String keyword : keyWordList) {
					if (strTemp.toUpperCase().contains(keyword.toUpperCase())) {
						
						entry.setMatchingKeyword(keyword);
						
						//exit after the first successful hit
						
						return true;
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	private void downloadPage(String url, String fileName ) {

		System.out.println("Downloading page at : " + url);	
		
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			
			URL urlObj = new URL(url);
		    reader = new BufferedReader(new InputStreamReader(
					urlObj.openStream()));
		    File file = new File( fileName );
		    writer = new BufferedWriter(new FileWriter(file));
			String line;
			while ((line = reader.readLine()) != null) {
				writer.write(line);
				writer.newLine();
			}
			
			writer.flush();
			System.out.println("Page successfully saved to : " + file.getAbsolutePath() );
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			
			try {
				
				if( reader != null ){
					reader.close();
				}
				
				if( writer != null ){
					writer.close();
				}
			} catch (IOException e) {				
				e.printStackTrace();
			}			
		}
	}
	



	public List<FeedEntry> getFeedEntryList() {
		return feedEntryList;
	}

	public void setFeedEntryList(List<FeedEntry> feedEntryList) {
		this.feedEntryList = feedEntryList;
	}

	public List<String> getKeywordList() {
		return keywordList;
	}

	public void setKeywordList(List<String> keywordList) {
		this.keywordList = keywordList;
	}
	
}
