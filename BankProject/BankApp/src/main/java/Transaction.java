import java.util.Date;

public class Transaction {
	int tranasactionId;
	int accountId;
	String tranasactionType;
	double amount;
	@Override
	public String toString() {
		return "Transaction [tranasactionId=" + tranasactionId + ", accountId=" + accountId + ", tranasactionType="
				+ tranasactionType + ", amount=" + amount + ", targetAccount=" + targetAccount + ", transactionTime="
				+ transactionTime + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	int targetAccount;
	Date transactionTime;
}
