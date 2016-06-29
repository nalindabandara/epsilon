package com.epsilon.Leak.Hawk.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epsilon.Leak.Hawk.LeakHawkManager;
import com.epsilon.Leak.Hawk.model.FeedEntry;

public class ContextFilterComponent {

	private List<FeedEntry> originalEntryList;

	private List<FeedEntry> filteredEntryList = new ArrayList<FeedEntry>();

	public ContextFilterComponent(List<FeedEntry> list) {

		this.originalEntryList = list;
	}
	
	public void applyContextFilter(){
		
		if( LeakHawkManager.isApplyingContextFilter){
			
			for( FeedEntry entry : originalEntryList ){	
								
				if( regExpressionMatched( entry ) ){
					
					filteredEntryList.add(entry);
				}
			}

		} else {
			if( originalEntryList != null && originalEntryList.size() > 0 ){
				filteredEntryList.addAll( originalEntryList );
			}	
		}
	}
	
	private boolean regExpressionMatched( FeedEntry entry ){
				
		BufferedReader reader = null;		
		try {
				
			String regEx1 = "^4[0-9]{12}(?:[0-9]{3})?$";
			
			String regEx2 = "^5[1-5][0-9]{14}$";

		    // Create a Pattern object
		    Pattern pattern1 = Pattern.compile(regEx1);		    
		    Pattern pattern2 = Pattern.compile(regEx2);
		    
		    // Now create matcher object.
		    		      
			if( entry.getEntryStream() != null ){
			    reader = new BufferedReader(new InputStreamReader( entry.getEntryStream() ));			   
				String line;
				StringBuilder builder = new StringBuilder();
				while ((line = reader.readLine()) != null) {								
					builder.append(line);					
				}	
				
				Matcher matcher1 = pattern1.matcher(builder);
				if ( matcher1.find() ) {
					return true;
				}
				
				Matcher matcher2 = pattern2.matcher(builder);
				if ( matcher2.find() ) {
					return true;
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} catch (Exception fe) {
			fe.printStackTrace();
			
		}		
		finally {			
			try {
				
				if( reader != null ){
					reader.close();
				}

			} catch (IOException e) {				
				e.printStackTrace();
			}			
		}			      
		return false;
	}

	public List<FeedEntry> getFilteredEntryList() {
		return filteredEntryList;
	}

	public void setFilteredEntryList(List<FeedEntry> filteredEntryList) {
		this.filteredEntryList = filteredEntryList;
	}
	
	
}
