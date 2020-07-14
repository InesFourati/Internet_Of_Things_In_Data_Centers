package application.IoT_DS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;



public class Database {
	//A pre-initialized DB containing all the tables and columns of the topics
	//TODO: framework input of the DB URL in the IoT device
	static String databaseURL ="jdbc:sqlite:/Users/iness/eclipse-workspace/IoT_DS/lib/EdgeNodeDB.db"; 

	
	 public static void connectDatabase() {
		 Connection connection = null;
	        try {
	            // create a connection to the database
	            connection = DriverManager.getConnection(databaseURL);
	            
	            System.out.println("Connection to SQLite has been established.");
	            
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        } finally {
	            try {
	                if (connection != null) {
	                    connection.close();
	                }
	            } catch (SQLException ex) {
	                System.out.println(ex.getMessage());
	            }
	        }}
	 
	 public static void updateTable(String sqlCreate) {
		 try ( Connection c =DriverManager.getConnection(databaseURL);
				 Statement stmt = c.createStatement()) {
	            // create a new table
	            stmt.execute(sqlCreate);
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
	    }
	 

	 public static String queryPayload (String query,String topicName) {
		 ResultSet rs = null;
		 String payload=null;
		 try (Connection c =DriverManager.getConnection(databaseURL);
				 Statement stmt = c.createStatement() ){
			 	rs = stmt.executeQuery(query);
			 		if(rs.next()) {
			 		payload=topicName+",";
					ResultSetMetaData md = rs.getMetaData();
					int n=md.getColumnCount();
				
				//Converting the SQL query result table to a String conform to the Telegraf messaging format "key=value,"
				for (int i = 1; i < n ; i++) {
							payload = payload+md.getColumnLabel(i).toString()+"="+rs.getObject(i).toString()+",";
				}
				
				//Last entry without an , at the end
				payload = payload+md.getColumnLabel(n).toString()+"="+rs.getObject(n).toString();
			 		}}catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
		return payload;
	 }
	 

	//TODO: Close Database when connection lost
	 
	 /*public static boolean isTableEmpty (String tableName) {
		 boolean empty = false;
		 ResultSet r = querySQL("SELECT * FROM "+tableName);
		 try {
			 if (r.next() == false) {
				 empty = true;
			 }
		} catch (SQLException  e) {
			System.out.println("DB:"+e.getMessage());
		}
		 return empty;
	 }*/
	 
}









