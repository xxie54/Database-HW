import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PersonsBeen {
	
	//Task[] taskList = new Task[100];
	
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	public void connectToDB() throws Exception {
		try  {
			// Step 1. This will load the MySQL driver. Each DB has its own driver.
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			// Step 2. Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/contactsdb.contacts?"
							+ "root" +
							"helloworld"); 
			readAllPerson();
			insertPerson("Peter");
			insertPerson("Rachel");
			insertPerson("Michael");
			deletePerson("John");
			deletePerson("Bob");
			deletePerson("Shirley");
			readAllPerson();
		} catch(Exception e) {
			throw e;
		} finally {
			close();
		}
	}
	public void readAllPerson() throws Exception{
		try {
			// Step 3. Statements allows us to send SQL queries to the DB.
			statement = connect.createStatement();
			resultSet = statement.executeQuery("select * from contactsdb.contacts");
			writeResultSet(resultSet);
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void deletePerson(String id) throws Exception {
		//do on your own
		try {
			// Use PreparedStatement when inserting with variables. It's very efficient.
			preparedStatement =  connect
					.prepareStatement("delete from contactsdb.contacts where id=?");
			preparedStatement.setString(1, id);
			preparedStatement.executeUpdate();
					
		} catch (Exception e) {
			throw e;
		}
		//;
	}
	
	private void writeMetaData(ResultSet resultSet) throws SQLException {
		System.out.println("The columns in this table are : ");
		System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
		for( int i=1; i<= resultSet.getMetaData().getColumnCount(); i++) {
			System.out.println("Column " + i +  " " + 
					resultSet.getMetaData().getColumnName(i));
		}
	}
	
	public void insertPerson(String task) throws Exception {
		try {
			// Use PreparedStatement when inserting with variables. It's very efficient.
			preparedStatement =  connect
					.prepareStatement("insert into contactsdb.contacts values (default, ?)");
			preparedStatement.setString(1, task);
			preparedStatement.executeUpdate();
					
		} catch (Exception e) {
			throw e;
		}
	}
	

	private void writeResultSet(ResultSet resultSet) throws SQLException {
		// ResultSet holds our results. It
		System.out.println("---------------------------------");
		writeMetaData(resultSet);
		
		while(resultSet.next()) {
			String tDescription = resultSet.getString("description");
			String id = resultSet.getString("id");
			
			//Boot up the Task object
			 
			System.out.print("ID : "+id);
			System.out.print("\t--> ");
			System.out.println("Description: " + tDescription);
		}
	}
	private void close() {
		try {
			if(connect != null) {
				connect.close();
			}
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public static void main(String[] args) throws Exception {
		PersonsBeen taskBean = new PersonsBeen();
		taskBean.connectToDB();
	}
}