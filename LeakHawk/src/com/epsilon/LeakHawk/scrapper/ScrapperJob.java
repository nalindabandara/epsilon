package com.epsilon.LeakHawk.scrapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;


public class ScrapperJob extends Thread {

	private static String BASE_URL = "http://pastebin.com/api_scrape_item.php?i=";
	
	private List<String> keyList;
	
	private List<String> keywordList;
	
	public void run(){
		
		launch();
	}
	
	private void launch() {

		if( this.keywordList != null && this.keywordList.size() > 0 ){
			
			if( this.keyList != null && this.keyList.size() > 0 ){
				
				for (String key : this.keyList) {
					
					String url = getUrl( key );
					if (isContainKeyWord( url, this.keywordList )) {
						downloadPage(url, key);
					}
				}
			} else {
				System.out.println( "****************** No urls were found for the scrapper job ******************");
			}
		} else {			
			System.out.println( "****************** No keywords were found for the scrapper job ******************");
		}
		

		
	}
		
	private boolean isContainKeyWord(String url, List<String> keyWordList) {

		try {			
			URL my_url = new URL(url);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					my_url.openStream()));
			String strTemp = "";
			while (null != (strTemp = br.readLine())) {

				for (String keyword : keyWordList) {
					if (strTemp.toUpperCase().contains(keyword.toUpperCase())) {
						return true;
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	private void downloadPage(String url, String fileName ) {

		System.out.println("Downloading page at : " + url);	
		
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			
			URL urlObj = new URL(url);
		    reader = new BufferedReader(new InputStreamReader(
					urlObj.openStream()));
		    File file = new File( fileName );
		    writer = new BufferedWriter(new FileWriter(file));
			String line;
			while ((line = reader.readLine()) != null) {
				writer.write(line);
				writer.newLine();
			}
			
			writer.flush();
			System.out.println("Page successfully saved to : " + file.getAbsolutePath() );
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			
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
	

	public List<String> getKeyList() {
		return keyList;
	}

	public void setKeyList(List<String> keyList) {
		this.keyList = keyList;
	}

	public List<String> getKeywordList() {
		return keywordList;
	}

	public void setKeywordList(List<String> keywordList) {
		this.keywordList = keywordList;
	}
	
	
	private String getUrl( String key ){
		
		return BASE_URL.concat(key);
	}
	
}
