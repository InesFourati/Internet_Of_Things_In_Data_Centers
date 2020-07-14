package application.IoT_DS;


import java.sql.SQLException;


public class Testing {
public static void main(String[] args) throws SQLException {
	Database.connectDatabase();
	String sqlCreate = "DELETE FROM xyz";
	Database.updateTable(sqlCreate);
	
	String sqlQuery = "DELETE FROM cpu_test;";
	
	//Update the corresponding Table with the payload values
	Database.updateTable(sqlQuery);

	
	String querySQL = "SELECT host,AVG(value) AS avg ,MIN(value) AS min,MAX(value) AS max\n"
			+"FROM "+"cpu_test"+"\n"
			+"GROUP BY host";
	 String deleteSQL ="DELETE FROM "+"cpu_test"+";";
	String rs = Database.queryPayload(querySQL,"cpu_test");
	System.out.println(rs);
	for (int i = 0; i < 10; i++) {
		System.out.println(i);
		if (i==3) {
			break;
		}
	}
System.out.println("ok");
	
	
	//delete the values used for the resultset
	//Database.updateTable(deleteSQL);
	
	//construct an MQTTmessage from the arrays
	/*String topicPayload = Message.constructPayload("cpu_test",rs);
	System.out.println(topicPayload);*/


}}