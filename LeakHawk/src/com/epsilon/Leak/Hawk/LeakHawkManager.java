package com.epsilon.Leak.Hawk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.epsilon.Leak.Hawk.aggregator.PatbinAggrgator;
import com.epsilon.Leak.Hawk.model.FeedEntry;
import com.epsilon.Leak.Hawk.sensor.PastbinSensor;
import com.epsilon.Leak.Hawk.utils.LeakHawkUtils;

public class LeakHawkManager {

	public static boolean isApplyingPreFilter;
	
	public static boolean isApplyingContextFilter;
	
	public static void main( String[] args ){
		
		LeakHawkUtils.readConfigFile();
		
		System.out.print("Do you want to apply the Pre Filter ? [y/n] : ");		
		try{
		    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		    String s = bufferRead.readLine();		      		    
		    if( s.equalsIgnoreCase("y")){
		    	isApplyingPreFilter = true;		    	
		    } else {
		    	isApplyingPreFilter = false;	    	
		    }		    
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		System.out.print("Do you want to apply the Context Filter ? [y/n] : ");		
		try{
		    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		    String s = bufferRead.readLine();		      		    
		    if( s.equalsIgnoreCase("y")){
		    	isApplyingContextFilter = true;		    	
		    } else {
		    	isApplyingContextFilter = false;	    	
		    }		    
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		
		PastbinSensor pastebinSensor = new PastbinSensor();	
		
		PatbinAggrgator pastebinAggrgator = new PatbinAggrgator();
		
		pastebinSensor.setAggregator( pastebinAggrgator );		
		pastebinSensor.start();
		
		while( true ){
			
			try {
				Thread.sleep( 10000 );
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
			
			List<FeedEntry> entryList = pastebinAggrgator.getFeedEntryList();
			
			LeakHawkJob leakHawkJob = new LeakHawkJob();
			leakHawkJob.setFeedEntryList(entryList);
			leakHawkJob.start();			
		}
	}
	
	
	
}
