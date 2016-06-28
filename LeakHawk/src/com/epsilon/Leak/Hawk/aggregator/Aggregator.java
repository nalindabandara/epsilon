package com.epsilon.Leak.Hawk.aggregator;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.epsilon.Leak.Hawk.model.FeedEntry;

public abstract class Aggregator {

	protected BlockingQueue<FeedEntry> queue = new ArrayBlockingQueue<FeedEntry>(1024);

	public BlockingQueue<FeedEntry> getQueue() {
		return queue;
	}

	public void setQueue(BlockingQueue<FeedEntry> queue) {
		this.queue = queue;
	}
	
	
	public abstract void addFeedEntry( FeedEntry entry);
	
	public abstract List<FeedEntry> getFeedEntryList();
	
}
