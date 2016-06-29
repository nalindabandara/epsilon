package com.epsilon.LeakHawk.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
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
				
		String fileName = entry.getKey();
		
		String entryTitle = entry.getTitle();
		
		if( entryTitle != null && entryTitle.length() > 0 ){
			
			System.out.println("Entry Title : " + entryTitle );
			
			if( isValidName( entryTitle ) ){
				fileName = fileName.concat("-").concat( entryTitle );
			}
		}		    
	    return fileName;
	}
	
	public static boolean isValidName(String text) {
	    Pattern pattern = Pattern.compile(
	        "# Match a valid Windows filename (unspecified file system).          \n" +
	        "^                                # Anchor to start of string.        \n" +
	        "(?!                              # Assert filename is not: CON, PRN, \n" +
	        "  (?:                            # AUX, NUL, COM1, COM2, COM3, COM4, \n" +
	        "    CON|PRN|AUX|NUL|             # COM5, COM6, COM7, COM8, COM9,     \n" +
	        "    COM[1-9]|LPT[1-9]            # LPT1, LPT2, LPT3, LPT4, LPT5,     \n" +
	        "  )                              # LPT6, LPT7, LPT8, and LPT9...     \n" +
	        "  (?:\\.[^.]*)?                  # followed by optional extension    \n" +
	        "  $                              # and end of string                 \n" +
	        ")                                # End negative lookahead assertion. \n" +
	        "[^<>:\"/\\\\|?*\\x00-\\x1F]*     # Zero or more valid filename chars.\n" +
	        "[^<>:\"/\\\\|?*\\x00-\\x1F\\ .]  # Last char is not a space or dot.  \n" +
	        "$                                # Anchor to end of string.            ", 
	        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE | Pattern.COMMENTS);
	    Matcher matcher = pattern.matcher(text);
	    boolean isMatch = matcher.matches();
	    return isMatch;
	}
	
}
