package com.epsilon.LeakHawk.db;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.epsilon.LeakHawk.scrapper.FeedEntry;

public class DBManager {

	
	public void saveFeedEntryBatch( List<FeedEntry> entryList ){
		
		Connection con = null;
		PreparedStatement statement = null;
		try {
			con = DBConnector.getConnection();
			
            StringBuilder sb = new StringBuilder("INSERT INTO feed_entry ( ");                       
            sb.append("entry_key, "); 
            sb.append("entry_url, ");
            sb.append("entry_title, ");
            sb.append("entry_file, ");
            sb.append("entry_matchingKeyword, ");
            sb.append("entry_user ) VALUES ( ?, ?, ?, ?, ?, ? ); ");
            statement = con.prepareStatement( sb.toString() );
            
            for( FeedEntry feedEntry : entryList){
            	insertFeedEntryBatch( feedEntry, statement );
            }
            
            statement.executeBatch();            
            
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				DBConnector.closeStatement(statement);
				DBConnector.closeConnection(con);				
			} catch (SQLException e) {				
				e.printStackTrace();
			}			
		}
	}
	
	private void insertFeedEntryBatch( FeedEntry feedEntry, PreparedStatement statement){
		
		 int count = 0;           
         
		 try {
			 statement.setString( ++count, feedEntry.getKey() );
			 statement.setString( ++count, feedEntry.getScrapperUrl() ); 
			 statement.setString( ++count, feedEntry.getTitle() ); 
			 statement.setBlob(++count, feedEntry.getEntryStream());
			 statement.setString( ++count, feedEntry.getMatchingKeyword() ); 
			 statement.setString( ++count, feedEntry.getUser() ); 
			 statement.addBatch();
		} catch (SQLException e) {			
			e.printStackTrace();
		} 		
	}
	
	public void updateFeedEntryBatch( List<FeedEntry> entryList ){
		
		Connection con = null;
		PreparedStatement statement = null;
		try {
			con = DBConnector.getConnection();
			
            StringBuilder sb = new StringBuilder("UPDATE feed_entry SET entry_processed = ? WHERE entry_id = ?");                                   
            statement = con.prepareStatement( sb.toString() );
            
            for( FeedEntry feedEntry : entryList){
            	
            	int count = 0;           
            	
            	statement.setBoolean( ++count, true );
    			statement.setInt( ++count, feedEntry.getEntryId() ); 
    			statement.addBatch();
            }
            
            statement.executeBatch();            
            
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				DBConnector.closeStatement(statement);
				DBConnector.closeConnection(con);				
			} catch (SQLException e) {				
				e.printStackTrace();
			}			
		}
	}
	
	
	public List<FeedEntry> loadFeedEntryTobeProcessed(){
		
		List<FeedEntry> entryList = new ArrayList<FeedEntry>();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try {
			con = DBConnector.getConnection();
			
            StringBuilder sb = new StringBuilder("SELECT * FROM feed_entry WHERE entry_processed = 0");                                   
            statement = con.prepareStatement( sb.toString() );            
            rs = statement.executeQuery();
            while( rs.next() ) {
            	
            	FeedEntry entry = new FeedEntry();
            	entry.loadFromDatabase(con, rs);   
            	entryList.add(entry);
            }
            
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				DBConnector.closeStatement(statement);
				DBConnector.closeConnection(con);				
			} catch (SQLException e) {				
				e.printStackTrace();
			}			
		}
		return entryList;
	}
}