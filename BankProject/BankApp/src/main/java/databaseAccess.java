import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.TimeZone;

public class databaseAccess {
	private static databaseAccess singleton = new databaseAccess();
	private Connection conn;

	public databaseAccess() {
		databaseAccessCreate();
	}
	private void databaseAccessCreate() {
            
            String password = System.getenv("DB_PASSWORD");
            String username = System.getenv("DB_USERNAME");
            String url = System.getenv("DB_URL");
            try {
                    this.conn = DriverManager.getConnection(url, username, password);
            }catch(SQLException e) {
                    System.out.println("Failed to Connect to DB");
                    System.out.println("Password: " + password);
                    System.out.println("Username: " + username);
                    System.out.println("Url: " + url);
                    e.printStackTrace();
            }
            try {
				conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			} catch (SQLException e) {
				e.printStackTrace();
			}
    }

    //public getter for the static reference
    public static databaseAccess getDatabaseAccess() {
            return singleton;
    }
    
    public Connection getConnection() {
        return conn;
    }

    public String validateUser(String userName, String password) {
    	String userLookup =  "select * from \"User\" where " +
    				"\"UserName\"=? and \"Password\"=?";
        try{
            PreparedStatement lookup = conn.prepareStatement(userLookup);
        	lookup.setString(1, userName);
        	lookup.setString(2, password);
        	lookup.executeQuery();
        	ResultSet rs = lookup.executeQuery();
        	if(rs.next())
        		return rs.getString("Employee");
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        return null;
    }
	public boolean createAccount(String userName, double amount) {
	   	String accountCreate =  "insert into \"Account\" (\"UserId\", \"Balance\") values (?, ?) ";
    	String userLookup =  "select \"UserId\" from \"User\" where \"UserName\"=?";
    	int userId = -1;

	    try{
	        PreparedStatement lookup = conn.prepareStatement(userLookup);
	    	lookup.setString(1, userName);
	    	ResultSet rs = lookup.executeQuery();
	    	if(rs.next())
	    		userId = rs.getInt("UserId");
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    	return false;
	    }
	    try{
	        PreparedStatement create = conn.prepareStatement(accountCreate);
	        create.setInt(1, userId);
	        create.setDouble(2, amount);
	    	return create.executeUpdate() == 1;
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    	return false;
	    }
	}
	public boolean addPerson(String firstName, String lastName, String address, String idType, String id, String state) {
	   	String personAdd =  "insert into \"Person\" (\"FirstName\", \"LastName\", \"Address\", \"StateIdType\", \"StateId\", \"State\") " +
	   			"values (?, ?, ?, ?::idtype, ?, ?) ";


	    try{
	        PreparedStatement add = conn.prepareStatement(personAdd);
	        add.setString(1, firstName);
	        add.setString(2, lastName);
	        add.setString(3, address);
	        add.setString(4, idType);
	        add.setString(5, id);
	        add.setString(6, state);
	        return add.executeUpdate() == 1;
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    	return false;
	    }
	}
	public boolean createUser(String userName, String password, boolean isEmployee) {
	   	String userAdd =  "insert into \"User\" (\"PersonId\", \"UserName\", \"Password\", \"Employee\") " +
	   			"values (1, ?, ?, ?) ";
	    try{
	        PreparedStatement add = conn.prepareStatement(userAdd);
	        add.setString(1, userName);
	        add.setString(2, password);
	        add.setBoolean(3, isEmployee);
	        return add.executeUpdate() == 1;
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    	return false;
	    }
	}
	
	public boolean deposit(int accountNumber, double amount) {
		
		boolean result = false;
	   	String balance =  "select \"Balance\" from \"Account\" where \"AccountId\" = ?;";
	   	String deposit =  "update \"Account\" set \"Balance\" = ? where \"AccountId\" = ?;";
	   	String transaction = "insert into \"Transaction\" (\"AccountId\", \"TransactionType\", \"Amount\", \"TransactionTime\") " +
	   			"values (?, ?::transactionKey, ?, ?) ";

	   	double currentBalance = 0.0;
	    try{
	        PreparedStatement getBalance = conn.prepareStatement(balance);
	        getBalance.setInt(1, accountNumber);
	    	ResultSet rs = getBalance.executeQuery();
	    	if(rs.next())
	    		currentBalance = rs.getDouble("Balance");
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    	return false;
	    }
	    try{
	        PreparedStatement trans = conn.prepareStatement(transaction);
	        trans.setInt(1, accountNumber);
	        trans.setString(2, "CREDIT");
	        trans.setDouble(3,  amount);

	        trans.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));

	        result = trans.executeUpdate() == 1;
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    	return false;
	    }
	    return result;

	}
}
