package com.epsilon.LeakHawk.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.epsilon.LeakHawk.scrapper.FeedEntry;

public class FileManager {

	
	public void saveEntryList( List<FeedEntry> entryList ){

		for (FeedEntry feedEntry : entryList) {
			
			saveEntryAsFile( feedEntry );
		}
	}
	
	
	private void saveEntryAsFile(FeedEntry entry) {
		
		BufferedReader reader = null;
		BufferedWriter writer = null;
		String fileName = "";
		try {
			
			fileName = entry.getKey();
			if( entry.getTitle() != null ) {
				fileName = fileName.concat("-").concat( entry.getTitle() );
			}
			
			entry.setEntryFileName(fileName);
						
		    reader = new BufferedReader(new InputStreamReader(
					entry.getEntryStream()));
		    File file = new File( "E:\\Mywork\\Pasbin\\", fileName );
		    writer = new BufferedWriter(new FileWriter(file));
			String line;
			while ((line = reader.readLine()) != null) {
				writer.write(line);
				writer.newLine();
			}
			
			writer.flush();
			System.out.println("Entry successfully saved to : " + file.getAbsolutePath() );
			
		} catch (IOException e) {
			System.out.println("Unable to create a file with the name  : " +  fileName);
		} catch (Exception fe) {
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
}
