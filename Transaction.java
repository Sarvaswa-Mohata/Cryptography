import java.io.Serializable;

public class Transaction implements Serializable{
    private double amount;
    private String recipient;
    private String purpose;

    public Transaction(double amount,String recipient, String purpose) {
		  this.amount = amount;
		  this.recipient = recipient;
		  this.purpose = purpose;
	  }

    public double getAmount() {
      return amount;
    }

    public String getRecipient() {
      return recipient;
    }

}
