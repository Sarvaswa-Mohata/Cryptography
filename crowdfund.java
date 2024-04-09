import java.io.*;
import java.util.*;

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
    public static void main(String[] args){
        System.out.print("Choose :\n");
        String filePath = "log.txt"; // Specify the path to your file
        readfile(filePath);
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();
        if(choice == 1){
            //Raise money
            System.out.println("Enter the cause you wanna raise money for :");
            String cause = scanner.nextLine();
            System.out.println("Enter the amount of money you want to raise (in Rs.) :");
            int money = scanner.nextInt();
            scanner.nextLine();
            String filePath2 = "catalog.txt"; // Specify the path to the output file

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath2,true))) {
                // Write sentences to the file
                writer.write(cause+" - Rs."+money);
                writer.newLine(); // Move to the next line
            } catch (IOException e) {
                System.err.println("Error writing to the file: " + e.getMessage());
            }
        }
        else if(choice==2){
            //Donate money
            System.out.println("Choose :");
            readfile("catalog.txt");

        }

        scanner.close();
    }
}
