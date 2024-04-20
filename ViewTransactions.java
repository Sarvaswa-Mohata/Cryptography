import java.util.ArrayList;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ViewTransactions {
    private List<Transaction> transactions;

    public ViewTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void viewUser(String recipientName) {
        boolean found = false;
        blockchain Blockchain = new blockchain(); 
        for (Block block : Blockchain.getBlocks()) {
            String serializedData = block.getData(); // Assuming the data is stored as a serialized string
            Transaction transaction = deserializeTransaction(serializedData); // Deserialize the transaction data
            
            if (transaction.getRecipient().equals(recipientName)) {
                System.out.println("Block Hash: " + block.getHash());
                // System.out.println("Transaction ID: " + transaction.getTransactionId());
                System.out.println("Amount: Rs. " + transaction.getAmount());
                System.out.println();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No transactions found for recipient: " + recipientName);
        }
    }
    
    private Transaction deserializeTransaction(String serializedData) {
        Transaction deserializedTransaction = null;
        try {
            byte[] byteArray = serializedData.getBytes();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            deserializedTransaction = (Transaction) objectInputStream.readObject();
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return deserializedTransaction;
    }
    
}
