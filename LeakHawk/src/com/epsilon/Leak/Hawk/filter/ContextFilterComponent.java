package com.epsilon.Leak.Hawk.filter;

import java.io.BufferedReader;
import java.io.FileReader;
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
	
	public ContextFilterComponent(){
		
	}
	
	public static void main(String[] args ){
		
		ContextFilterComponent cf = new ContextFilterComponent();
		cf.regExpTestDrive();
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
				
				while ((line = reader.readLine()) != null) {								
					
					Matcher matcher1 = pattern1.matcher(line);
					if ( matcher1.find() ) {
						return true;
					}
					
					Matcher matcher2 = pattern2.matcher(line);
					if ( matcher2.find() ) {
						return true;
					}					
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

	
	private void regExpTestDrive(){
		
		BufferedReader reader = null;		
		try {
				
			String regEx1 = "^4[0-9]{12}(?:[0-9]{3})?$";
			
			//String regEx1 = "[0-9]*";
			
			String regEx2 = "^5[1-5][0-9]{14}$";

		    // Create a Pattern object
		    Pattern pattern1 = Pattern.compile(regEx1);		    
		    Pattern pattern2 = Pattern.compile(regEx2);
		    
		    // Now create matcher object.
		    		      
			
		    reader = new BufferedReader( new FileReader("E:\\myworksapce\\epsilon-nalinda\\LeakHawk\\src\\com\\epsilon\\LeakHawk\\test\\testing.txt") );			   
			String line;
			
			while ((line = reader.readLine()) != null) {	
				
				Matcher matcher1 = pattern1.matcher(line);
				if ( matcher1.find() ) {
					System.out.println("Found value: " + matcher1.group(0) );
				} else {
					System.out.println("No match found");
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
	}
	
	public List<FeedEntry> getFilteredEntryList() {
		return filteredEntryList;
	}

	public void setFilteredEntryList(List<FeedEntry> filteredEntryList) {
		this.filteredEntryList = filteredEntryList;
	}
	
	
}
