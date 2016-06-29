package com.epsilon.LeakHawk.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;

import com.epsilon.Leak.Hawk.model.FeedEntry;
import com.epsilon.Leak.Hawk.utils.LeakHawkUtils;


public class FileManager {
	
	private static String originalFilePath = LeakHawkUtils.properties.getProperty("original.file.path");
	
	private static String contextFilePath = LeakHawkUtils.properties.getProperty("context.file.path");
	
		
	public void saveEntryList( List<FeedEntry> entryList, String filterType ){

		for (FeedEntry feedEntry : entryList) {
			
			if( filterType.equals("PRE_FILTER")){
				saveEntryAsFile( feedEntry, originalFilePath );
			}
			if( filterType.equals("CONTEXT_FILTER")){
				saveEntryAsFile( feedEntry, contextFilePath );
			}			
		}
	}
	
	
	private void saveEntryAsFile(FeedEntry entry, String filePath ) {
		
		BufferedReader reader = null;
		BufferedWriter writer = null;
		String fileName = "";
		try {
			
			fileName = getValidFileName( entry );
			
			entry.setEntryFileName(fileName);
			URL urlObj = new URL( entry.getScrapperUrl() );
			entry.setEntryStream( urlObj.openStream() );
			
			if( entry.getEntryStream() != null ){
			    reader = new BufferedReader(new InputStreamReader( entry.getEntryStream() ));
			    File file = new File( filePath, fileName );
			    writer = new BufferedWriter(new FileWriter(file));
				String line;
				while ((line = reader.readLine()) != null) {
					writer.write(line);
					writer.newLine();
				}
				
				writer.flush();
				System.out.println("Entry successfully saved to : " + file.getAbsolutePath() );
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Unable to create a file with the name  : " +  fileName);
		} catch (Exception fe) {
			fe.printStackTrace();
			System.out.println("Unable to create a file with the name  : " +  fileName);
		}
		
		finally {
			
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
	
	public String getValidFileName( FeedEntry entry ) {
		
		Pattern pattern = Pattern.compile("[^/./\\:*?\"<>|]");
		String fileName = entry.getKey();
		
		String entryTitle = entry.getTitle();
		
		if( entryTitle != null && entryTitle.length() > 0 ){
			
			System.out.println("Entry Title : " + entryTitle );
			
			if( !pattern.matcher( entryTitle ).find() ){
				fileName = fileName.concat("-").concat( entryTitle );
			}
		}		    
	    return fileName;
	}
	
}
