package com.epsilon.Leak.Hawk.model;

import java.io.InputStream;

import org.json.simple.JSONObject;

import com.epsilon.LeakHawk.io.SavableObject;

public class FeedEntry {
	
	
	private String scrapperUrl;
	
	private String date;
	
	private String key;		
		
	private String title;		
	
	private String user;
	
	private String syntax;
	
	private InputStream entryStream;
	
	private String entryFileName;

	
	public FeedEntry( JSONObject jsonObj ){
		
		this.key = (String) jsonObj.get("key");
		this.user = (String) jsonObj.get("user");
		this.scrapperUrl = (String) jsonObj.get("scrape_url");		
		this.title = (String) jsonObj.get("title");
		this.syntax = (String) jsonObj.get("syntax");		
	}
	

	public String getScrapperUrl() {
		return scrapperUrl;
	}

	public void setScrapperUrl(String scrapperUrl) {
		this.scrapperUrl = scrapperUrl;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getSyntax() {
		return syntax;
	}

	public void setSyntax(String syntax) {
		this.syntax = syntax;
	}

	public InputStream getEntryStream() {
		return entryStream;
	}

	public void setEntryStream(InputStream entryStream) {
		this.entryStream = entryStream;
	}

	public String getEntryFileName() {
		return entryFileName;
	}

	public void setEntryFileName(String entryFileName) {
		this.entryFileName = entryFileName;
	}


	@Override
	public String toString() {
		return "FeedEntry [scrapperUrl=" + scrapperUrl + ", key=" + key
				+ ", title=" + title + ", user=" + user + ", syntax=" + syntax
				+ "]";
	}
	
	
	
}
