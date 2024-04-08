#include <iostream>
#include <vector>
#include <string>
#include <ctime>
#include <sstream>
#include <iomanip>
#include <openssl/hmac.h>

using namespace std;

// Block structure
struct Block {
    int index;
    string previousHash;
    vector<string> data;
    int nonce;
    string hash;

    Block(int idx, const string& prevHash, const vector<string>& d)
        : index(idx), previousHash(prevHash), data(d), nonce(0), hash("") {}
};

// Transaction structure
struct Transaction {
    string sender;
    string recipient;
    double amount;
    bool verified;

    Transaction(const string& from, const string& to, double amt)
        : sender(from), recipient(to), amount(amt), verified(false) {}
};

// Blockchain class
class Blockchain {
private:
    vector<Block> chain;
    vector<Transaction> transactions;
    vector<unsigned char> secretKey; // Secret key for HMAC

public:
    Blockchain(const vector<unsigned char>& key) : secretKey(key) {
        // Create the genesis block
        vector<string> data = {"Genesis Block"};
        chain.emplace_back(0, "0", data);
    }

    // Create a new block with verified transactions
    void createBlock() {
        vector<Transaction> verifiedTransactions = getVerifiedTransactions();
        int index = chain.size();
        string previousHash = chain.back().hash;
        vector<string> data;

        for (const auto& tx : verifiedTransactions) {
            stringstream ss;
            ss << "From: " << tx.sender << " To: " << tx.recipient << " Amount: " << tx.amount;
            data.push_back(ss.str());
        }

        chain.emplace_back(index, previousHash, data);
        transactions.clear(); // Clear transactions after adding to a block
    }

    // Verify transactions using HMAC (placeholder)
    bool verifyTransaction(const Transaction& tx) {
        // Implement transaction verification using HMAC (e.g., verify sender's authenticity)
        vector<unsigned char> dataToHmac(tx.sender.begin(), tx.sender.end());
        dataToHmac.push_back(tx.amount);

        vector<unsigned char> hmac = createHmac(dataToHmac);
        bool isValid = verifyHmac(hmac); // Placeholder for HMAC verification

        return isValid;
    }

    // Mine a new block using proof-of-work (similar to mineBlock in the previous example)
    void mineBlock(int difficulty) {
        // Similar to previous implementation
        string target(difficulty, '0');
        Block& block = chain.back();

        while (block.hash.substr(0, difficulty) != target) {
            block.nonce++;
            block.hash = calculateHash(block);
        }

        cout << "Block mined: " << block.hash << endl;
    }

    // View all successful transactions against a user
    void viewUser(const string& user) {
        cout << "Transactions for User: " << user << endl;
        for (const auto& tx : transactions) {
            if (tx.recipient == user && tx.verified) {
                cout << "- From: " << tx.sender << ", Amount: " << tx.amount << endl;
            }
        }
    }

    // Add a new transaction to the pending list
    void addTransaction(const string& sender, const string& recipient, double amount) {
        Transaction tx(sender, recipient, amount);
        if (verifyTransaction(tx)) {
            transactions.push_back(tx);
        } else {
            cout << "Transaction verification failed." << endl;
        }
    }

private:
    // Get verified transactions (e.g., filter by sender authenticity)
    vector<Transaction> getVerifiedTransactions() {
        vector<Transaction> verifiedTxs;
        for (const auto& tx : transactions) {
            if (verifyTransaction(tx)) {
                verifiedTxs.push_back(tx);
            }
        }
        return verifiedTxs;
    }

    // Calculate block hash (similar to previous implementation)
    string calculateHash(const Block& block) {
        // Implement hash calculation
        stringstream ss;
        ss << block.index << block.previousHash;

        for (const auto& data : block.data) {
            ss << data;
        }

        ss << block.nonce;

        string input = ss.str();
        unsigned char hash[SHA256_DIGEST_LENGTH];
        SHA256(reinterpret_cast<const unsigned char*>(input.c_str()), input.length(), hash);

        stringstream hashStream;
        for (int i = 0; i < SHA256_DIGEST_LENGTH; ++i) {
            hashStream << hex << setw(2) << setfill('0') << static_cast<int>(hash[i]);
        }

        return hashStream.str();
    }

    // Create HMAC for transaction data
    vector<unsigned char> createHmac(const vector<unsigned char>& data) {
        unsigned int digest_len = EVP_MAX_MD_SIZE; // Maximum digest length
        vector<unsigned char> hmac(digest_len);

        HMAC_CTX* ctx = HMAC_CTX_new();
        HMAC_Init_ex(ctx, secretKey.data(), secretKey.size(), EVP_sha256(), NULL);
        HMAC_Update(ctx, data.data(), data.size());
        HMAC_Final(ctx, hmac.data(), &digest_len);
        HMAC_CTX_free(ctx);

        return hmac;
    }

    // Placeholder for HMAC verification (always return true for now)
    bool verifyHmac(const vector<unsigned char>& hmac) {
        // Placeholder for HMAC verification logic
        return true;
    }
};

int main() {
    // Initialize the secret key for HMAC (should be securely stored)
    vector<unsigned char> secretKey = { /* Your secret key bytes */ };

    // Create blockchain instance
    Blockchain blockchain(secretKey);

    // Add transactions (e.g., crowdfunding pledges)
    blockchain.addTransaction("Alice", "CrowdfundingCampaign", 100.0);
    blockchain.addTransaction("Bob", "CrowdfundingCampaign", 50.0);

    // Mine a new block with verified transactions
    blockchain.mineBlock(2);

    // View transactions for a user
    blockchain.viewUser("CrowdfundingCampaign");

    return 0;
}
