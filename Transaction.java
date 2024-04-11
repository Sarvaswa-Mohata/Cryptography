public class Transaction {
    private double amount;
    private String recipient;
    private String purpose;

    public Transaction(double amount,String recipient, String purpose) {
		this.amount = amount;
		this.recipient = recipient;
		this.purpose = purpose;
	}
}
