import java.util.ArrayList;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

public class blockchain {

    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static int difficulty = 5;

	public void addBlock(Transaction transaction) {
        String transactionData = serializeTransaction(transaction); // Convert transaction to string data
        String previousHash = getLastBlockHash(); // Get hash of the last block in the blockchain

        // Create a new block with the transaction data and previous hash
        Block newBlock = new Block(transactionData, previousHash);

        // Add the new block to the blockchain
        blockchain.add(newBlock);
    }

    // Helper method to serialize transaction data (convert transaction object to string)
    public String serializeTransaction(Transaction transaction) {
        // Create a Gson instance
        Gson gson = new Gson();

        // Convert the Transaction object to a JSON string
        String jsonTransaction = gson.toJson(transaction);

        return jsonTransaction;
    }

    // Helper method to get the hash of the last block in the blockchain
    public String getLastBlockHash() {
        if (blockchain.isEmpty()) {
            return "0"; // Return default hash for the genesis block (e.g., "0")
        }
        return blockchain.get(blockchain.size() - 1).calculateHash();
    }
	
	public static Boolean isChainValid() {
		Block currentBlock; 
		Block previousBlock;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		
		//loop through blockchain to check hashes:
		for(int i=1; i < blockchain.size(); i++) {
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i-1);
			//compare registered hash and calculated hash:
			if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
				System.out.println("Current Hashes not equal");			
				return false;
			}
			//compare previous hash and registered previous hash
			if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
				System.out.println("Previous Hashes not equal");
				return false;
			}
			//check if hash is solved
			if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
				System.out.println("This block hasn't been mined");
				return false;
			}
		}
		return true;
	}
}
