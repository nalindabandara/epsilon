package com.epsilon.Leak.Hawk.aggregator;

import java.util.ArrayList;
import java.util.List;

import com.epsilon.Leak.Hawk.model.FeedEntry;

public class PatbinAggrgator extends Aggregator {

	private static int BATCH_SIZE = 50;
	
	@Override
	public void addFeedEntry(FeedEntry entry) {
		
		try {
			this.queue.put(entry);
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
		
	}		

	@Override
	public List<FeedEntry> getFeedEntryList() {

		List<FeedEntry> list = new ArrayList<FeedEntry>(); 
		int count = 0;
		while( !(this.queue.isEmpty()) && (count < BATCH_SIZE) ){
			try {
				list.add( this.queue.take());
			} catch (InterruptedException e) {				
				e.printStackTrace();
			} finally {
				count++;
			}			
		}
		return list;
	}

	

}
