package dataManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


public class DBConnector implements QueryPerformer{
	
	 //Configuration parameters for the generation of the IAM Database Authentication token
    private static final String RDS_INSTANCE_HOSTNAME = "dbinfo3.ckadinof62hi.eu-west-3.rds.amazonaws.com";
    private static final int RDS_INSTANCE_PORT = 1521;
    //private static final String REGION_NAME = "ue-west-3";
    private static final String DB_USER = "gNOkUhDOCM";
    private static final String DB_PASSWORD = "A?T>Q_oBA=K]->gv+sTTZg?VJZkaF";
    @SuppressWarnings("unused")
	private static final String JDBC_URL = "jdbc:oracle://" + RDS_INSTANCE_HOSTNAME + ":" + RDS_INSTANCE_PORT;
	//jdbc:oracle:thin:@dbinfo3.ckadinof62hi.eu-west-3.rds.amazonaws.com:1521:dbinfo3", "gNOkUhDOCM", "A?T>Q_oBA=K]->gv+sTTZg?VJZkaF"
	
    //SID Servce Identifier : dbinfo3 
	
    //esempio di INSERT INTO
	//INSERT INTO Utente (Username,Nome,Cognome,DataNas,Password,ResidenzaLat,ResidenzaLong,RaggioAzione)  VALUES ('Pippo','a','a',TO_DATE('2018-11-20','YYYY-MM-DD'),'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa',1,1,1);
	
	
    private static DBConnector instance = null;
    private Connection conn;
    private Statement stmt;
    
    private DBConnector()  {
    	
    	try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
	        //get the connection
	        conn = getDBConnectionUsingIam();
	        //verify the connection is successful
	        stmt = conn.createStatement();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}  
    	
    	
        /*ResultSet rs=stmt.executeQuery("SELECT owner\n" + 
        		"  FROM all_tables\n" + 
        		" WHERE table_name = 'DUAL'");*/    
    }
    
	public static DBConnector getDBConnector() {
		if(instance == null)
			instance = new DBConnector();
		return instance;
		
	}
	
	@SuppressWarnings("unused")
	private void close() {
		//close the connection
        try {
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			instance = null;
		}
        // clearSslProperties();
	}
	
	/*public ResultSet executeQuery(String query) {
		try {
			return stmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}*/
	
	public ResultSet executeStatement(Statement stmt) {
		
		return null;
	}
	
	public PreparedStatement prepareStatement(String sql) {
		try {
			return conn.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	public void PROVA() throws SQLException {
		ResultSet rs = stmt.executeQuery("SELECT *  FROM UTENTE");
        
        while (rs.next()) {
        	String id = rs.getString(1);
            System.out.println(id);
        }
	}
	
	
	
	
	
	
	
	
	
	/**
     * This method returns a connection to the db instance authenticated using IAM Database Authentication
     * @return
     * @throws Exception
     */
    private static Connection getDBConnectionUsingIam() throws Exception {
       // setSslProperties();
        //return DriverManager.getConnection(JDBC_URL, setMySqlConnectionProperties());
        //jdbc:oracle:thin:@dbinfo3.ckadinof62hi.eu-west-3.rds.amazonaws.com:1521:dbinfo3", "gNOkUhDOCM", "A?T>Q_oBA=K]->gv+sTTZg?VJZkaF"
    	return DriverManager.getConnection("jdbc:oracle:thin:@dbinfo3.ckadinof62hi.eu-west-3.rds.amazonaws.com:1521:dbinfo3", "gNOkUhDOCM", "A?T>Q_oBA=K]->gv+sTTZg?VJZkaF");
    }
    
    
    /**
     * This method sets the mysql connection properties which includes the IAM Database Authentication token
     * as the password. It also specifies that SSL verification is required.
     * @return
     */
    @SuppressWarnings("unused")
	private static Properties setMySqlConnectionProperties() {
        Properties mysqlConnectionProperties = new Properties();
        //mysqlConnectionProperties.setProperty("verifyServerCertificate","true");
        //mysqlConnectionProperties.setProperty("useSSL", "true");
        mysqlConnectionProperties.setProperty("user",DB_USER);
        mysqlConnectionProperties.setProperty("password", DB_PASSWORD);
        return mysqlConnectionProperties;
    }



}
