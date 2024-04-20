import java.util.ArrayList;
import java.io.*;

public class blockchain {

    public static ArrayList<Block> Blockchain = new ArrayList<Block>();
    public static int difficulty = 5;

    public ArrayList<Block> getBlocks() {
        return Blockchain;
    }

	public void addBlock(Transaction transaction) {
        String transactionData = serializeTransaction(transaction); // Convert transaction to string data
        String previousHash = getLastBlockHash(); // Get hash of the last block in the blockchain

        // Create a new block with the transaction data and previous hash
        Block newBlock = new Block(transactionData, previousHash);

        // Add the new block to the blockchain
        Blockchain.add(newBlock);
    }

    // Helper method to serialize transaction data (convert transaction object to string)
	public String serializeTransaction(Transaction transaction) {
        String serializedTransaction = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(transaction);
            objectOutputStream.close();
            serializedTransaction = byteArrayOutputStream.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serializedTransaction;
    }

    // Helper method to get the hash of the last block in the blockchain
    public String getLastBlockHash() {
        if (Blockchain.isEmpty()) {
            return "0"; // Return default hash for the genesis block (e.g., "0")
        }
        return Blockchain.get(Blockchain.size() - 1).calculateHash();
    }
	
	public static Boolean isChainValid() {
		Block currentBlock; 
		Block previousBlock;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		
		//loop through blockchain to check hashes:
		for(int i=1; i < Blockchain.size(); i++) {
			currentBlock = Blockchain.get(i);
			previousBlock = Blockchain.get(i-1);
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
