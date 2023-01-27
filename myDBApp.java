import java.io.*;
import java.sql.*;
import java.util.*;

public class myDBApp {
	
	// NOTE: You will need to change some variables from START to END.
	public static void main(String[] argv) throws SQLException {
		
		String user_username;
		String user_password;
		Scanner myObj = new Scanner(System.in);
		System.out.println("Please enter your username");
		user_username = myObj.nextLine();
		System.out.println("Please enter your password");
		user_password = myObj.nextLine();
		
		// START
		// Enter your username.
		String user = user_username ;
		// Enter your database password, NOT your university password.
		String password = user_password;
		
		/** IMPORTANT: If you are using NoMachine, you can leave this as it is.
		 * 
	 	 *  Otherwise, if you are using your OWN COMPUTER with TUNNELLING:
	 	 * 		1) Delete the original database string and 
	 	 * 		2) Remove the '//' in front of the second database string.
	 	 */
		String database = "teachdb.cs.rhul.ac.uk";
		//String database = "localhost";
		// END
		
		Connection connection = connectToDatabase(user, password, database);
		if (connection != null) {
			System.out.println("SUCCESS: You made it!"
					+ "\n\t You can now take control of your database!\n");
		} else {
			System.out.println("ERROR: \tFailed to make connection!");
			System.exit(1);
		}
		// Now we're ready to use the DB. You may add your code below this line.
		
		String query = "DROP TABLE IF EXISTS DelayedFlights";
		String query2 = "DROP TABLE IF EXISTS Airport";
		ResultSet rs;
			
		rs = executeQuery(connection, query);
		rs = executeQuery(connection, query2);
		
		
		
		
		//Creating 2 new tables
		String create_DelayedFlightsTable = "CREATE TABLE DelayedFlights (ID_of_Delayed_Flight varchar(15), Month varchar(15), DayofMonth varchar(15), DayOfWeek varchar(15), "
				+ "DepTime varchar(15), ScheduledDepTime varchar(15), ArrTime varchar(15), ScheduledArrTime varchar(15), UniqueCarrier varchar(15),"
				+ "FlightNum varchar(15), ActualFlightTime varchar(15), scheduledFlightTime varchar(15), AirTime varchar(15), ArrDelay int,"
				+ "DepDelay varchar(15), Orig varchar(15), Dest varchar(15), Distance varchar(15), primary key (ID_of_Delayed_Flight), FOREIGN KEY (Orig) REFERENCES Airport(airportCode), FOREIGN KEY (Dest) REFERENCES Airport(airportCode) );";
		
		
		String create_AirportTable = "CREATE TABLE Airport (airportCode varchar(15), airportName varchar(100), "
				+ "City varchar(100), State varchar(100), primary key (airportCode));";
		
		
		rs = executeQuery(connection, create_AirportTable);
		insertIntoTableFromFile(connection, "Airport", "src/resources/airport");
		rs = executeQuery(connection, create_DelayedFlightsTable);
		insertIntoTableFromFile(connection, "DelayedFlights", "src/resources/delayedFlights");
		
		
			
		
		
		
		
		
		System.out.println("########### 1st Query ###########");
		String first_question = "SELECT DISTINCT UniqueCarrier, COUNT(UniqueCarrier) FROM delayedFlights GROUP BY UniqueCarrier ORDER BY COUNT(UniqueCarrier) DESC LIMIT 5;";
		
		rs = executeQuery(connection, first_question);
		try {
			while(rs.next()) {
				System.out.println(rs.getString(1)+" "+rs.getString(2));
			}
			
					
		} catch (SQLException e) {
			e.printStackTrace();
		}
		rs.close();
		
		
		System.out.println("########### 2nd Query ###########");
		String second_question = "SELECT DISTINCT City, COUNT(Orig) FROM delayedFlights df INNER JOIN Airport a ON df.orig = a. airportCode GROUP BY a.City ORDER BY COUNT(Orig) DESC LIMIT 5;";
		
		rs = executeQuery(connection, second_question);
		try {
			while(rs.next()) {
				System.out.println(rs.getString(1)+" "+rs.getString(2));
			}
			
					
		} catch (SQLException e) {
			e.printStackTrace();
		}
		rs.close();

		
		System.out.println("########### 3rd Query ###########");
		String third_question = "SELECT DISTINCT Dest, SUM(ArrDelay) FROM delayedFlights GROUP BY Dest ORDER BY SUM(ArrDelay) DESC LIMIT 5 OFFSET 1 ROWS;";
		
		rs = executeQuery(connection, third_question);
		try {
			
			while(rs.next()) {
				System.out.println(rs.getString(1)+" "+rs.getString(2));
			}
			
					
		} catch (SQLException e) {
			e.printStackTrace();
		}
		rs.close();
		
		System.out.println("########### 4th Query ###########");
		String fourth_question = "SELECT DISTINCT State, COUNT(State) FROM Airport GROUP BY State HAVING COUNT(State) > 9 ORDER BY COUNT(State);";
		
		rs = executeQuery(connection, fourth_question);
		try {
			
			while(rs.next()) {
				System.out.println(rs.getString(1)+" "+rs.getString(2));
			}
			
					
		} catch (SQLException e) {
			e.printStackTrace();
		}
		rs.close();
		
		System.out.println("########### 5th Query ###########");
		String fifth_question = "SELECT DISTINCT State, Count(State) FROM airport a INNER JOIN delayedFlights df ON a.airportcode = df.orig "
				+ "WHERE (SELECT DISTINCT State FROM airport WHERE airportCode = df.dest) = (SELECT DISTINCT State FROM airport WHERE airportCode = df.orig) GROUP BY a.State ORDER BY COUNT(State) DESC LIMIT 5 ;"; 
	
		
		rs = executeQuery(connection, fifth_question);
		try {
			
			while(rs.next()) {
				System.out.println(rs.getString(1)+" "+rs.getString(2));
			}
			
					
		} catch (SQLException e) {
			e.printStackTrace();
		}
		rs.close();
		
		


		
		
		
		
		
	}
	
	// You can write your new methods here.
	
	public static int insertIntoTableFromFile(Connection connection,
			String table, String file) {//CODE FROM LAB

		

		BufferedReader br = null;
		int numberRows = 0;
		try {
			Statement statement = connection.createStatement();
			String sCurrent, broken[], composed = "";
			br = new BufferedReader(new FileReader(file));

			while ((sCurrent = br.readLine()) != null) {
				// Insert each line to the DB
				broken = sCurrent.split(",");
				composed = "INSERT INTO " + table + " VALUES (";
				int i;
				for (i = 0; i < broken.length - 1; i++) {
					composed += "'" + broken[i] + "',";
				}
				composed += "'" + broken[i] + "');";
				//System.out.println(composedLine);
				numberRows = statement.executeUpdate(composed);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return numberRows;
	}
	/*
	public static int insertIntoTableFromFile(Connection connection, String table,
            String filename) {
		int numRows = 0;
		String currentLine = null;
		try {
		    BufferedReader br = new BufferedReader(new FileReader(filename));
		    Statement st = connection.createStatement();
		    // Read in each line of the file until we reach the end.
		    while ((currentLine = br.readLine()) != null) {
		        String[] values = currentLine.split(",");
		        String value_string = "\"" + values[0] + "\"" ;
		        
		        
		        for(int i=1; i<values.length; i++){
		        	
		        	//System.out.println(values);
		            value_string = value_string + ", " + "\"" + values[i] + "\"";
		            // 
		        }
		        
		        
		       //String composedLine = "INSERT INTO " + table + " VALUES ("+value_string+");";
		        String composedLine = "INSERT INTO DelayedFlights VALUES (\"0\", \"1\", \"3\", \"4\", \"2003\", \"1955\", \"2211\", \"2225\", \"WN\", \"335\", \"128\", \"150\", \"116\", \"-14\", \"8\", \"TLH\", \"TPA\", \"810\");";
		        System.out.println(composedLine);
		        // Finally, execute the entire composed line.
		        numRows = st.executeUpdate(composedLine);
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return numRows;
				
		
	}
	*/
	
	//(ID_of_Delayed_Flight, Month, DayofMonth, DayOfWeek, DepTime, ScheduledDepTime, ArrTime, ScheduledArrTime, UniqueCarrier, FlightNum, ActualFlightTime, scheduledFlightTime, AirTime, ArrDelay, DepDelay, Orig, Dest, Distance)
	
	
	public static ResultSet executeQuery(Connection connection, String query) {
	    System.out.println("DEBUG: Executing query...");
	    try {
	        Statement st = connection.createStatement();
	        ResultSet rs = st.executeQuery(query);
	        return rs;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	    
	    
	
	public static void createTable(Connection connection,
			String tableDescription) {
		Statement st = null;
		try {
			st = connection.createStatement();
			st.execute("CREATE TABLE " + tableDescription);
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	
	    
	// ADVANCED: This method is for advanced users only. You should not need to change this!
	public static Connection connectToDatabase(String user, String password, String database) {
		System.out.println("------ Testing PostgreSQL JDBC Connection ------");
		Connection connection = null;
		try {
			String protocol = "jdbc:postgresql://";
			String dbName = "/CS2855/";
			String fullURL = protocol + database + dbName + user;
			connection = DriverManager.getConnection(fullURL, user, password);
		} catch (SQLException e) {
			String errorMsg = e.getMessage();
			if (errorMsg.contains("authentication failed")) {
				System.out.println("ERROR: \tDatabase password is incorrect. Have you changed the password string above?");
				System.out.println("\n\tMake sure you are NOT using your university password.\n"
						+ "\tYou need to use the password that was emailed to you!");
			} else {
				System.out.println("Connection failed! Check output console.");
				e.printStackTrace();
			}
		}
		return connection;
	}
}