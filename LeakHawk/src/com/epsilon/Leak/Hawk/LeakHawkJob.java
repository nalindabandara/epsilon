package com.epsilon.Leak.Hawk;

import java.util.ArrayList;
import java.util.List;

import com.epsilon.Leak.Hawk.filter.PreFilterComponent;
import com.epsilon.Leak.Hawk.model.FeedEntry;
import com.epsilon.LeakHawk.io.DBManager;
import com.epsilon.LeakHawk.io.FileManager;

public class LeakHawkJob extends Thread {

	private List<FeedEntry> feedEntryList = null;
	
	public void run(){
		
		if( feedEntryList != null && feedEntryList.size() > 0 ){
					
			//Applying Pre Filter
			PreFilterComponent preFilterComponent = new PreFilterComponent( feedEntryList );			
			preFilterComponent.applyPreFilter();			
			List<FeedEntry> preFilteredList = preFilterComponent.getFilteredEntryList();
			
			FileManager fileManager = new FileManager();
			
			DBManager dbManager = new DBManager();
			dbManager.saveFeedEntryBatch(preFilteredList);
			
			
		}
	}

	public List<FeedEntry> getFeedEntryList() {
		return feedEntryList;
	}

	public void setFeedEntryList(List<FeedEntry> feedEntryList) {
		this.feedEntryList = feedEntryList;
	}
	
	
	
	
}
