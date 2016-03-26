package com.epsilon.LeakHawk.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public abstract class SavableObject {

	public static int NEW = 1;

	public static int LOADED = 2;

	public static int MODIFIED = 3;

	public static int DELETED = 4;


	protected String key;

	protected int status;

    public DBTransaction save( )
    {
        DBTransaction dbTransaction = null;
        Connection con = null;
        try
        {
            con = DBConnector.getConnection();
            if( this.status == SavableObject.NEW )
            {
                dbTransaction = insertToDatabase( con );
            }
            if( this.status == SavableObject.MODIFIED )
            {
                dbTransaction = updateToDatabase( con );
            }
            if( this.status == SavableObject.DELETED )
            {
                dbTransaction = deleteFromDatabase( con );
            }
        }
        catch ( SQLException se )
        {
            se.printStackTrace();
            dbTransaction = new DBTransaction( DBTransaction.FAILED, se.getMessage() );
        } catch (Exception e) {			
			e.printStackTrace();
			dbTransaction = new DBTransaction( DBTransaction.FAILED, "Error Occured" );
		}
        finally
        {
            try
            {
                DBConnector.closeConnection( con );
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return dbTransaction;
    }
    

    public abstract DBTransaction insertToDatabase( Connection con ) throws SQLException, Exception;    
    
    public abstract DBTransaction updateToDatabase( Connection con ) throws SQLException, Exception; 

    public abstract DBTransaction deleteFromDatabase( Connection con ) throws SQLException;

    public abstract void loadFromDatabase( Connection con, ResultSet rs ) throws SQLException;

    public abstract int getNextId( Connection con )throws SQLException;

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }	
	
	
}
