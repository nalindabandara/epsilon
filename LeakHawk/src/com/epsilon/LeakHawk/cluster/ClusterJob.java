package com.epsilon.LeakHawk.cluster;

import java.util.List;

import com.epsilon.LeakHawk.db.DBManager;
import com.epsilon.LeakHawk.scrapper.FeedEntry;

public class ClusterJob extends Thread {

	public void run(){
		
		launch();
	}
	
	private void launch() {
		
		DBManager dbManager = new DBManager();
		
		while ( true ){
			
			List<FeedEntry> feedEntryList = dbManager.loadFeedEntryTobeProcessed();
			
			for( FeedEntry entry : feedEntryList){			
				
				applyClusterScripts();
							
			}
			
			dbManager.updateFeedEntryBatch(feedEntryList);
			System.out.println( feedEntryList.size() + " Feed entries were successfully processed.");
			try {
				Thread.sleep( 60000 );
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
		}		
	}
	
	
	// All the script related code would go here.
	private void applyClusterScripts(){
		
		
	}
}
