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

import com.epsilon.LeakHawk.io.DBConnector;
import com.epsilon.LeakHawk.io.DBTransaction;
import com.epsilon.LeakHawk.io.SavableObject;



public class FeedEntry extends SavableObject {

	private int entryId;
	
	private String scrapperUrl;
	
	private String date;
	
	private String key;
	
	private String size;
		
	private String title;
	
	private String matchingKeyword;
	
	private String user;
	
	private String syntax;
	
	private InputStream entryStream;
	
	private String entryFileName;
	
	public FeedEntry(){
		
	}
	
	public FeedEntry( JSONObject jsonObj ){
		
		this.key = (String) jsonObj.get("key");
		this.user = (String) jsonObj.get("user");
		this.scrapperUrl = (String) jsonObj.get("scrape_url");		
		this.title = (String) jsonObj.get("title");
		this.syntax = (String) jsonObj.get("syntax");
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
		
	public String getSyntax() {
		return syntax;
	}


	public void setSyntax(String syntax) {
		this.syntax = syntax;
	}

	public int getEntryId() {
		return entryId;
	}

	public void setEntryId(int entryId) {
		this.entryId = entryId;
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
		return "FeedEntry [Scrapper Url=" + scrapperUrl + ", Key=" + key
				+ ", Title=" + title + ", Matching Keyword=" + matchingKeyword
				+ ", Syntax=" + syntax + "]";
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

	public InputStream _getEntryStream() throws MalformedURLException, IOException{
		
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
            st.setBlob(++count, _getEntryStream());
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
		
		this.status = SavableObject.LOADED;
		this.entryId = rs.getInt("entry_id");
		this.key = rs.getString("entry_key");
		this.title = rs.getString("entry_title");
		this.entryStream = rs.getBinaryStream("entry_file");
	}


	@Override
	public int getNextId(Connection con) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
