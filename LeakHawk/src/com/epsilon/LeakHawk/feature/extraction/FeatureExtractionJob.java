package com.epsilon.LeakHawk.feature.extraction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;

import com.epsilon.LeakHawk.io.DBManager;
import com.epsilon.LeakHawk.scrapper.FeedEntry;

public class FeatureExtractionJob extends Thread {

	public void run(){
		
		launch();
	}
	
	private void launch() {
		
		/*System.out.println("********************************************************************");		
		System.out.println("**************   Feature Extraction Job started    *****************");		
		System.out.println("********************************************************************");
		
		DBManager dbManager = new DBManager();
		
		while ( true ){
			
			System.out.println("**************   Feature Extraction cycle started  *****************");		
			List<FeedEntry> feedEntryList = dbManager.loadFeedEntryTobeProcessed();
				
			if( feedEntryList.size() > 0 ){		
				
				for( FeedEntry entry : feedEntryList){								
					saveEntryAsFile( entry );
					applyScripts( entry );
								
				}
				dbManager.updateFeedEntryBatch(feedEntryList);
			}
						
			System.out.println( feedEntryList.size() + " Feed entries were successfully processed.");
			System.out.println("**************   Feature Extraction cycle ended  ******************\n");	
			try {
				Thread.sleep( 30000 );
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}
		}		*/
	}
	
	
	// All the script related code would go here.
	private void applyScripts( FeedEntry entry ){
		
        try {
            ProcessBuilder pb = new ProcessBuilder("/bin/bash", "count_word.sh", entry.getEntryFileName());
            final Process process = pb.start();

            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            PrintWriter pw = new PrintWriter(process.getOutputStream());
            String line;

            while ((line = br.readLine()) != null) {
                System.out.println( "Script Output : " + line);
                pw.flush();
            }
        } catch(Exception e) {
            e.printStackTrace();
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
