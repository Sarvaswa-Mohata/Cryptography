import java.io.*;
import java.util.*;
import java.util.regex.*;

public class crowdfund {
    public static void readfile(String filePath){
        int lineNumber = 1; // Start line numbering from 1

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Prepend line number followed by colon to the beginning of each line
                String numberedLine = lineNumber + ": " + line;
                System.out.println(numberedLine); // Output the numbered line
                lineNumber++; // Increment line number for the next line
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    public static Transaction extractTransactionDetails(String fileName, int lineNumber, double money) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int currentLine = 0;

            // Read lines until reaching the specified line number
            while ((line = reader.readLine()) != null) {
                currentLine++;
                if (currentLine == lineNumber) {
                    // Regex pattern to match the line format and extract details
                    Pattern pattern = Pattern.compile("^(.*?) - (.*?) - Rs\\. (\\d+)$");
                    Matcher matcher = pattern.matcher(line);

                    // Check if the line matches the expected format
                    if (matcher.matches()) {
                        String recipient = matcher.group(1); // Extract recipient
                        String cause = matcher.group(2); // Extract cause

                        // Create and return a new Transaction object
                        return new Transaction(money, recipient, cause);
                    } else {
                        System.out.println("Line does not match expected format.");
                        return null; // Return null if line format does not match
                    }
                }
            }

            // Handle case where specified line number is out of range
            System.out.println("Specified line number is out of range.");
            return null;
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return null; // Return null in case of file reading error
        }
    }

    public static void updateAmount(String fileName, int lineNumber, int donationAmount) {
        try {
            // Read the file content into a StringBuilder to modify it
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            StringBuilder fileContent = new StringBuilder();
            String line;
            int currentLine = 0;

            // Read lines until reaching the specified line number
            while ((line = reader.readLine()) != null) {
                currentLine++;
                if (currentLine == lineNumber) {
                    // Regex pattern to match the line containing the original amount (e.g., "Rs. 5000")
                    Pattern pattern = Pattern.compile("Rs\\. (\\d+)$");
                    Matcher matcher = pattern.matcher(line);

                    if (matcher.find()) {
                        String originalAmountStr = matcher.group(1); // Extract original amount as string
                        int originalAmount = Integer.parseInt(originalAmountStr); // Convert to integer

                        // Calculate new amount after subtracting donationAmount
                        int remainingAmount = originalAmount - donationAmount;

                        // Ensure remainingAmount is non-negative
                        remainingAmount = Math.max(0, remainingAmount);

                        // Replace the original amount in the line with the new amount
                        String updatedLine = line.replaceFirst("Rs\\. \\d+$", "Rs. " + remainingAmount);
                        fileContent.append(updatedLine).append("\n");
                    } else {
                        System.out.println("Original amount not found in the specified line.");
                        fileContent.append(line).append("\n"); // Keep the original line
                    }
                } else {
                    fileContent.append(line).append("\n"); // Keep lines other than the specified line
                }
            }
            reader.close();

            // Write the updated content back to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(fileContent.toString());
            writer.close();

            System.out.println("Amount updated successfully! ");
        } catch (IOException e) {
            System.err.println("Error updating amount in file: " + e.getMessage());
        }
    }

    public static void main(String[] args){
        blockchain blockchain = new blockchain(); // Instantiate your blockchain
        while(true){
            System.out.print("Choose :\n");
            String filePath = "log.txt"; // Specify the path to your file
            readfile(filePath);
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            scanner.nextLine();
            if(choice == 1){
                //Raise money
                System.out.println("Enter the recipient's name :");
                String recipient = scanner.nextLine();
                System.out.println("Enter the cause you wanna raise money for :");
                String cause = scanner.nextLine();
                System.out.println("Enter the amount of money you want to raise (in Rs.) :");
                int money = scanner.nextInt();
                scanner.nextLine();
                String filePath2 = "catalog.txt"; // Specify the path to the output file

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath2,true))) {
                    // Write sentences to the file
                    writer.write(recipient + " - "+ cause +" - Rs. "+money);
                    writer.newLine(); // Move to the next line
                } catch (IOException e) {
                    System.err.println("Error writing to the file: " + e.getMessage());
                }
            }
            else if(choice==2){
                //Donate money
                System.out.println("Choose :");
                readfile("catalog.txt");
                int option = scanner.nextInt();
                System.out.println("Enter the amount of money you would want to donate :");
                double donationAmount = scanner.nextDouble();
                
                boolean isTransactionVerified = TransactionVerification.verifyTransaction(donationAmount);

                if (isTransactionVerified) {
                    System.out.println("Donation transaction verified successfully!");
                    // Create a new transaction object (replace with actual transaction data)
                    Transaction transaction = extractTransactionDetails("catalog.txt",option,donationAmount);
                    // Create a new block with the serialized transaction data
                    blockchain.addBlock(transaction);

                    System.out.println("Transaction added to blockchain successfully!");
                    
                    updateAmount("catalog.txt", option, (int)donationAmount);

                } else {
                    System.out.println("Donation transaction verification failed. Invalid HMAC response.");
                }
            }
            else if(choice==3){
                System.out.println("Enter the recipient's name whose transaction you want to view:");
                String name = scanner.nextLine();
                // Call the viewUser method with the recipient's name
                List<Transaction> transactions = new ArrayList<>(); // Assuming you have a list of transactions
                ViewTransactions viewTransactions = new ViewTransactions(transactions);
                viewTransactions.viewUser(name);                
            }
            else if(choice==4){
                scanner.close();
                return;
            }
            else{
                System.out.println("You entered an invalid choice! Please choose again...");
            }
        }
    }
}
