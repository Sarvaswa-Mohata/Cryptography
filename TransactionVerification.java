import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class TransactionVerification {

    private static final String SECRET_KEY = "MySecretKey123"; // Shared secret key

    // Function to verify a donation transaction based on the provided amount
    public static boolean verifyTransaction(double transactionAmount) {
        // Step 1: Generate a random challenge
        String challenge = generateChallenge();
        System.out.println("Challenge generated: " + challenge);

        // Step 2: Calculate HMAC response using challenge and transaction amount
        String hmacResponse = calculateHmacResponse(challenge, transactionAmount);
        System.out.println("HMAC Response calculated: " + hmacResponse);

        // Step 3: Verify the HMAC response
        boolean isValid = verifyHmacResponse(challenge, transactionAmount, hmacResponse);

        return isValid;
    }

    // Method to generate a random challenge
    private static String generateChallenge() {
        SecureRandom random = new SecureRandom();
        byte[] challengeBytes = new byte[16];
        random.nextBytes(challengeBytes);
        return bytesToHexString(challengeBytes);
    }

    // Method to calculate HMAC response using challenge and transaction amount
    private static String calculateHmacResponse(String challenge, double transactionAmount) {
        String message = challenge + Double.toString(transactionAmount);
        return calculateHmac(message, SECRET_KEY);
    }

    // Method to verify the HMAC response
    private static boolean verifyHmacResponse(String challenge, double transactionAmount, String receivedHmacResponse) {
        String expectedHmacResponse = calculateHmac(challenge + Double.toString(transactionAmount), SECRET_KEY);
        return receivedHmacResponse.equals(expectedHmacResponse);
    }

    // Helper method to calculate HMAC using HMAC-SHA256 algorithm
    private static String calculateHmac(String message, String secretKey) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            hmac.init(secretKeySpec);
            byte[] hmacBytes = hmac.doFinal(message.getBytes());
            return bytesToHexString(hmacBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Helper method to convert byte array to hexadecimal string
    private static String bytesToHexString(byte[] bytes) {
        StringBuilder hexStringBuilder = new StringBuilder();
        for (byte b : bytes) {
            hexStringBuilder.append(String.format("%02x", b));
        }
        return hexStringBuilder.toString();
    }
}
