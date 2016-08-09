package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.sound.midi.ControllerEventListener;

import gui.Window;
import logic.CreateTable;

public class EstablishConnection {
	private String username, password, serverType, host;
	private String server, email, port, table;
	private Connection conn;
	private boolean connectiosEstablished;
	private Window window;
	private Statement stmt;
	private ResultSet rst;
	private ResultSetMetaData resultSetData;
	private LinkedList<String[]> resultList;
	private String command;
	private DatabaseMetaData databaseData;
	private ArrayList<String> tableNames;
	private CreateTable createTable;
	
	public EstablishConnection(String username, String password, String serverType, Window window){
		this.username = username;
		this.password = password;
		this.serverType = serverType;
		host = null;
		server = "demo";
		port = "3306";
		table= "books";
		connectiosEstablished = false;
		this.window = window;
		resultList = new LinkedList<String[]>();
		establishConnection();
		tableNames = new ArrayList<String>();
	}
	
	public String establishDatabaseType(){
		if(server != null){
			switch (serverType) {
			case "MySQL":
				if(port != null){
					host = "jdbc:mysql://localhost:" + port + "/";
					host += server;
					connectiosEstablished = true;
					return host;
				}else{
					System.err.println("Hibásan megadott port!");
					break;
				}
			case "MS SQL":
				host = "jdbc:odbc:";
				host += server + "DSN";
				connectiosEstablished = true;
				return host;
			case "Oracle":
				/*if(email != null){
					host = "jdbc:oracle:";
					host += email;
					return host;
				}else{
					System.err.println("Hibásan megadott email cím!");
					break;
				}*/
				System.out.println("Oracle nem támogatott jelenleg.");
				break;
			default:
				System.err.println("Hibás adatbázis alkotás!");
				return null;
			}
		}else{
			System.err.println("Hibásan megadott tábla vagy host!");
			return null;
		}
		return null;
	}
	
	public void establishConnection(){
		try{
			conn = DriverManager.getConnection(establishDatabaseType(), username, password);
			System.out.println("Sikeres csatlakozás.");
		}catch(Exception e){
			if(window == null){
				window.message("Hibás ablak.");
			}else{
				window.message("Sikertelen csatlakozás. Hibásan megadott felhasználó név vagy jelszó.");
				
			}
			
		}
	}
	
	public void setTable(String table){
		this.table = table;
		setData();
	//	new CreateTable(window, );
	}
	
	public void setEmailForOracle(String email){
		email = email;
	}
	
	public void setPort(String port){
		port = port;
	}
	
	public Connection getConnection(){
		return conn;
	}
	
	public boolean isAlive() throws SQLException{
		return connectiosEstablished;
	}
	
	public void terminateConnection() throws SQLException{
		conn.close();
	}
	
	public void setStatement(String statement){
		this.command = statement;
	}
	
	public void setData(){
		String[] resultColumm;
		String[] collumNames = new String[0];
		command = "SELECT * FROM " + table;
		try{
			stmt = conn.createStatement();
			
			
			rst = stmt.executeQuery("select * from " + table);
			resultSetData = rst.getMetaData();
			
			int collumNumbers = resultSetData.getColumnCount();
			collumNames = new String[collumNumbers];
			if(collumNames.length > 0){
				window.resetTableList();
			}
			
			//get the Table names
			databaseData = conn.getMetaData();
			rst = databaseData.getTables(null, null, "%", null);
			while(rst.next()){
				System.out.println("Elkezdni beolvasni");
				window.setTables(rst.getString(3));
				tableNames.add(rst.getString(3));
			}
			
			for(int i = 0; i < collumNumbers; ++i){
				collumNames[i] = resultSetData.getColumnName(i+1);
			}
			//rst = databaseData.get //Innen folytatni
			while(rst.next()){
				System.out.println("Elkezdni beolvasni");
				if(resultSetData.getColumnCount() != 0){
					resultColumm = new String[resultSetData.getColumnCount()];
					for(int i = 0; i < 3; ++i){
						resultColumm[i] = rst.getString(i+1);
					}
					resultList.add(resultColumm);
					System.out.println("resultList mérete a Connection classban A: " + resultList.size());
				}else{
					window.message("Üres a tábla.");
				}	
			}	
		}catch(SQLException e){
			window.message("Hibás Statement.");
		}
		if(collumNames.length != 0){
			System.out.println("resultList mérete a Connection classban: " + resultList.size());
			createTable = new CreateTable(window, tableNames, this, resultList);
		}else{
			window.message("Tábla inicilaizálása sikertelen.");
		}
	}
	
	public void setTableNames(String tableName){
		tableNames.add(tableName);
	}
	
	public void changeTable(String table){
		this.table = table;
		
	}
}
