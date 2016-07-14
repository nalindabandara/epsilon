package com.epsilon.Leak.Hawk.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.epsilon.Leak.Hawk.LeakHawkManager;
import com.epsilon.Leak.Hawk.model.FeedEntry;
import com.epsilon.Leak.Hawk.utils.LeakHawkUtils;

public class PreFilterComponent {
	
	public static List<String> keyWordList = Arrays.asList( LeakHawkUtils.properties.getProperty("pre.filter.keyword.list").split(","));
	
	private List<FeedEntry> originalEntryList;
	
	private List<FeedEntry> filteredEntryList = new ArrayList<FeedEntry>();
	
	public PreFilterComponent( List<FeedEntry> list ){
		
		this.originalEntryList = list;
	}
	
	
	public void applyPreFilter(){
		
		if( LeakHawkManager.isApplyingPreFilter ){
			
			for( FeedEntry entry : originalEntryList ){	
				
				boolean keywordContains = isContainKeyWord(entry);
							
				boolean regExpsMatched = isRegExpsMatched( entry );
											
				if( !(keywordContains || regExpsMatched) ){
					filteredEntryList.add(entry);				
				}
			}		
		} else {
			if( originalEntryList != null && originalEntryList.size() > 0 ){
				filteredEntryList.addAll( originalEntryList );
			}			
		}
		
	}

	
	private boolean isContainKeyWord(FeedEntry entry ) {

		BufferedReader br = null;
		try {			
			URL my_url = new URL(entry.getScrapperUrl());
			br = new BufferedReader(new InputStreamReader(
					my_url.openStream()));
			String strTemp = "";
			while (null != (strTemp = br.readLine())) {

				for (String keyword : keyWordList ) {
					if (strTemp.toUpperCase().contains(keyword.toUpperCase())) {
						//exit after the first successful hit						
						return true;
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally{
			if( br != null ){
				try {
					br.close();
				} catch (IOException e) {					
					e.printStackTrace();
				}
			}
			
		}
		
		return false;
	}
	
	
	private boolean isRegExpsMatched( FeedEntry entry ){
		
		return false;
	}
	

	public List<FeedEntry> getOriginalEntryList() {
		return originalEntryList;
	}


	public void setOriginalEntryList(List<FeedEntry> originalEntryList) {
		this.originalEntryList = originalEntryList;
	}


	public List<FeedEntry> getFilteredEntryList() {
		return filteredEntryList;
	}


	public void setFilteredEntryList(List<FeedEntry> filteredEntryList) {
		this.filteredEntryList = filteredEntryList;
	}
	
	
}
