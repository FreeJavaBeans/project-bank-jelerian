import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
	
	public boolean deposit(int accountNumber, double amount) throws TransactionLogUnavailableException {
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
	        PreparedStatement doDeposit = conn.prepareStatement(deposit);
	        doDeposit.setDouble(1, currentBalance + amount);
	        doDeposit.setInt(2, accountNumber);
	    	result  = doDeposit.executeUpdate() == 1;
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    	return false;
	    }

	    if(!result)
	    	return result;
	    try{
	        PreparedStatement trans = conn.prepareStatement(transaction);
	        trans.setInt(1, accountNumber);
	        trans.setString(2, "CREDIT");
	        trans.setDouble(3,  amount);

	        trans.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));

	        result = trans.executeUpdate() == 1;
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    	throw new TransactionLogUnavailableException("Transaction log unavailable. Die Die Die...");
	    }
	    return result;
	}

	public boolean withdraw(int accountNumber, double amount) throws TransactionLogUnavailableException {
		boolean result = false;
	   	String balance =  "select \"Balance\" from \"Account\" where \"AccountId\" = ?;";
	   	String withdraw =  "update \"Account\" set \"Balance\" = ? where \"AccountId\" = ?;";
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
	    
	    if(currentBalance - amount < 0) {
	    	System.out.println("Can't withdraw more than what's available.");
	    	return false;
	    }
	    
	    try{
	        PreparedStatement doWithdraw = conn.prepareStatement(withdraw);
	        doWithdraw.setDouble(1, currentBalance - amount);
	        doWithdraw.setInt(2, accountNumber);
	    	result  = doWithdraw.executeUpdate() == 1;
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    	return false;
	    }

	    if(!result)
	    	return result;
	    try{
	        PreparedStatement trans = conn.prepareStatement(transaction);
	        trans.setInt(1, accountNumber);
	        trans.setString(2, "DEBIT");
	        trans.setDouble(3,  amount);

	        trans.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));

	        result = trans.executeUpdate() == 1;
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    	throw new TransactionLogUnavailableException("Transaction log unavailable. Die Die Die...");
	    }
	    return result;
	}
	public boolean isEmployee(String userName) {
    	String userLookup =  "select \"Employee\" from \"User\" where \"UserName\"=?";
	    try{
	        PreparedStatement lookup = conn.prepareStatement(userLookup);
	    	lookup.setString(1, userName);
	    	ResultSet rs = lookup.executeQuery();
	    	if(rs.next())
	    		return rs.getBoolean("Employee");
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    }
	    return false;
	}
	@SuppressWarnings("null")
	public List<Transaction> getAllTransactions() {
    	String userLookup =  "select * from \"Transaction\"";
    	List<Transaction> result = new ArrayList<Transaction>();
	    try{
	        PreparedStatement lookup = conn.prepareStatement(userLookup);
	    	ResultSet rs = lookup.executeQuery();
	    	while(rs.next()){
	    		Transaction t = new Transaction();
	    		t.tranasactionId = rs.getInt("TransactionId");
	    		t.accountId = rs.getInt("AccountId");
	    		t.tranasactionType = rs.getString("TransactionType");
	    		t.amount = rs.getDouble("Amount");
	    		t.targetAccount = rs.getInt("TargetAccount");
	    		t.transactionTime = rs.getDate("TransactionTime");
	    		result.add(t);
	    	}
	    } catch (SQLException e) {
	    	e.printStackTrace();
		    return result;
	    }
	    return result;
	}
	public boolean transfer(int accountNumber, int targetAccountNumber, double amount) throws TransactionLogUnavailableException {
		boolean result = false;
	   	String balance =  "select \"Balance\" from \"Account\" where \"AccountId\" = ?;";
	   	String withdraw =  "update \"Account\" set \"Balance\" = ? where \"AccountId\" = ?;";
	   	String transaction = "insert into \"Transaction\" (\"AccountId\", \"TransactionType\", \"Amount\", \"TargetAccount\", \"TransactionTime\") " +
	   			"values (?, ?::transactionKey, ?, ?, ?) ";

	   	double currentBalanceSource = 0.0;
	   	double currentBalanceTarget = 0.0;
	    try{
	        PreparedStatement getBalance = conn.prepareStatement(balance);
	        getBalance.setInt(1, accountNumber);
	    	ResultSet rs = getBalance.executeQuery();
	    	if(rs.next())
	    		currentBalanceSource = rs.getDouble("Balance");
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    	return false;
	    }
	    
	    if(currentBalanceSource - amount < 0) {
	    	System.out.println("Can't transfer more than what's available.");
	    	return false;
	    }
	    
	    try{
	        PreparedStatement getBalance = conn.prepareStatement(balance);
	        getBalance.setInt(1, targetAccountNumber);
	    	ResultSet rs = getBalance.executeQuery();
	    	if(rs.next())
	    		currentBalanceTarget = rs.getDouble("Balance");
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    	return false;
	    }

	    try{
	        PreparedStatement doWithdraw = conn.prepareStatement(withdraw);
	        doWithdraw.setDouble(1, currentBalanceSource - amount);
	        doWithdraw.setInt(2, accountNumber);
	    	result  = doWithdraw.executeUpdate() == 1;
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    	return false;
	    }

	    try{
	        PreparedStatement doDeposit = conn.prepareStatement(withdraw);
	        doDeposit.setDouble(1, currentBalanceTarget + amount);
	        doDeposit.setInt(2, targetAccountNumber);
	    	result  = doDeposit.executeUpdate() == 1;
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    	return false;
	    }

	    if(!result)
	    	return result;
	    try{
	        PreparedStatement trans = conn.prepareStatement(transaction);
	        trans.setInt(1, accountNumber);
	        trans.setString(2, "TRANSFER");
	        trans.setDouble(3,  amount);
	        trans.setInt(4,  targetAccountNumber);

	        trans.setTimestamp(5, new java.sql.Timestamp(new java.util.Date().getTime()));

	        result = trans.executeUpdate() == 1;
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    	throw new TransactionLogUnavailableException("Transaction log unavailable. Die Die Die...");
	    }
	    return result;
	}

}
