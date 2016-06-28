package com.epsilon.Leak.Hawk.sensor;

import java.util.List;

import com.epsilon.Leak.Hawk.aggregator.Aggregator;
import com.epsilon.Leak.Hawk.model.FeedEntry;

public abstract class SensorJob extends Thread {

	protected int feedEntriesPerHit;
	
	protected String connectingUrl;
	
	protected Aggregator aggregator;
	
	public Aggregator getAggregator() {
		return aggregator;
	}

	public void setAggregator(Aggregator aggregator) {
		this.aggregator = aggregator;
	}

	protected abstract void manageDownloadProcess();
}
