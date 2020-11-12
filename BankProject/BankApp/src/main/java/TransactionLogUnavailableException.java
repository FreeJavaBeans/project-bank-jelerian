public class TransactionLogUnavailableException extends Exception { 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TransactionLogUnavailableException(String errorMessage) {
        super(errorMessage);
    }
}
