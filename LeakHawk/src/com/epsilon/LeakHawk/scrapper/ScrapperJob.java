package com.epsilon.LeakHawk.scrapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.epsilon.LeakHawk.db.DBConnector;
import com.epsilon.LeakHawk.db.DBManager;


public class ScrapperJob extends Thread {

	private static String BASE_URL = "http://pastebin.com/api_scrape_item.php?i=";
	
	private List<FeedEntry> feedEntryList;
	
	private List<FeedEntry> eligibleEntryList = new ArrayList<FeedEntry>();
		
	public void run(){
		
		launch();
	}
	
	private void launch() {

		System.out.println("*************** Scrapper Job Started ****************");
		if( ScrapperManager.keyWordList != null && ScrapperManager.keyWordList.size() > 0 ){
			
			if( this.feedEntryList != null && this.feedEntryList.size() > 0 ){
				
				DBManager dbManager = new DBManager();
				if( ScrapperManager.isApplyingFilter ){
				
					eligibleEntryList.clear();
					for (FeedEntry feedEntry : this.feedEntryList) {
						
						boolean isContainsKeyWord = isContainKeyWord( feedEntry );
						boolean isSyntaxAllowed = isSyntaxAllowed( feedEntry );
						
						if ( isContainsKeyWord && isSyntaxAllowed ) {							
							eligibleEntryList.add( feedEntry );							
						}				
					}
					
				} else { 
					
					eligibleEntryList.clear();
					for (FeedEntry feedEntry : this.feedEntryList) {
														
						boolean isSyntaxAllowed = isSyntaxAllowed( feedEntry );						
						if( isSyntaxAllowed ){
							
							eligibleEntryList.add( feedEntry );		
						}														
					}							
				}
				dbManager.saveFeedEntryBatch( eligibleEntryList);
				System.out.println("Number of entries saved as a batch : " + eligibleEntryList.size() );
				
			} else {
				System.out.println( "****************** No urls were found for the scrapper job ******************");
			}
		} else {			
			System.out.println( "****************** No keywords were found for the scrapper job ******************");
		}
		System.out.println("*************** Scrapper Job Ended ****************\n\n");

		
	}
		
	private boolean isContainKeyWord(FeedEntry entry ) {

		try {			
			URL my_url = new URL(entry.getScrapperUrl());
			BufferedReader br = new BufferedReader(new InputStreamReader(
					my_url.openStream()));
			String strTemp = "";
			while (null != (strTemp = br.readLine())) {

				for (String keyword : ScrapperManager.keyWordList) {
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

	private boolean isSyntaxAllowed( FeedEntry entry ){
		
		for( String allowedSyntax : ScrapperManager.allowedSyntaxList ){
			
			if( entry.getSyntax().equalsIgnoreCase( allowedSyntax )){
				return true;
			}
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


	
}
