package com.epsilon.LeakHawk.scrapper;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONObject;

import com.epsilon.LeakHawk.db.DBConnector;
import com.epsilon.LeakHawk.db.DBTransaction;
import com.epsilon.LeakHawk.db.SavableObject;



public class FeedEntry extends SavableObject {


	private String scrapperUrl;
	
	private String date;
	
	private String key;
	
	private String size;
		
	private String title;
	
	private String matchingKeyword;
	
	private String user;
	
	

	
	public FeedEntry( JSONObject jsonObj ){
		
		this.key = (String) jsonObj.get("key");
		this.user = (String) jsonObj.get("user");
		this.scrapperUrl = (String) jsonObj.get("scrape_url");
		this.status = SavableObject.NEW;
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

	public String getMatchingKeyword() {
		return matchingKeyword;
	}


	public void setMatchingKeyword(String matchingKeyword) {
		this.matchingKeyword = matchingKeyword;
	}
	

	@Override
	public String toString() {
		return "FeedEntry [scrapperUrl=" + scrapperUrl + ", date=" + date
				+ ", key=" + key + ", size=" + size + ", title=" + title
				+ ", user=" + user + "]";
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FeedEntry other = (FeedEntry) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	private InputStream getEntryStream() throws MalformedURLException, IOException{
		
		if( this.scrapperUrl != null ){
			URL urlObj = new URL(scrapperUrl);
			return urlObj.openStream();
		} 
		return null;
	}

	@Override
	public DBTransaction<FeedEntry> insertToDatabase(Connection con) throws SQLException,
			Exception {
	    PreparedStatement st = null;
        try
        {
            StringBuilder sb = new StringBuilder("INSERT INTO feed_entry ( ");                       
            sb.append("entry_key, "); 
            sb.append("entry_url, ");
            sb.append("entry_title, ");
            sb.append("entry_file, ");
            sb.append("entry_matchingKeyword, ");
            sb.append("entry_user ) VALUES ( ?, ?, ?, ?, ?, ? ); ");
            st = con.prepareStatement( sb.toString() );

            int count = 0;           
            
            st.setString( ++count, getKey() ); 
            st.setString( ++count, getScrapperUrl() ); 
            st.setString( ++count, getTitle() ); 
            st.setBlob(++count, getEntryStream());
            st.setString( ++count, getMatchingKeyword() ); 
            st.setString( ++count, getUser() ); 
                        
            st.execute();
            System.out.println( "Feed entry was successfully saved to database - key : " + getKey() + "  " + getMatchingKeyword());
            return new DBTransaction<FeedEntry>( DBTransaction.SUCCESS, "Feed entry was successfully saved to database" );
        } catch (Exception e ){
        	throw e;
        } finally {
            DBConnector.closeStatement(st);
        }		
	}


	@Override
	public DBTransaction updateToDatabase(Connection con) throws SQLException,
			Exception {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public DBTransaction deleteFromDatabase(Connection con) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void loadFromDatabase(Connection con, ResultSet rs)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public int getNextId(Connection con) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
