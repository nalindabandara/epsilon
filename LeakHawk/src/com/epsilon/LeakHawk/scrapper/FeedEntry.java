package com.epsilon.LeakHawk.scrapper;

import org.json.simple.JSONObject;

public class FeedEntry {

	private String scrapperUrl;
	
	private String date;
	
	private String key;
	
	private String size;
		
	private String title;
	
	private String user;

	
	public FeedEntry( JSONObject jsonObj ){
		
		this.key = (String) jsonObj.get("key");
		this.user = (String) jsonObj.get("user");
		this.scrapperUrl = (String) jsonObj.get("scrape_url");
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

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
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


	@Override
	public String toString() {
		return "FeedEntry [scrapperUrl=" + scrapperUrl + ", date=" + date
				+ ", key=" + key + ", size=" + size + ", title=" + title
				+ ", user=" + user + "]";
	}
	
	
	
	
}
