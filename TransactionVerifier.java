import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

public class TransactionVerifier {

    private static final String SECRET_KEY = "your_secret_key";

    public boolean verifyTransaction(String receivedChallenge, byte[] receivedHmac) {
        try {
            byte[] secretKey = SECRET_KEY.getBytes();
            byte[] challenge = receivedChallenge.getBytes();

            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "HmacSHA256");
            hmac.init(secretKeySpec);
            byte[] computedHmac = hmac.doFinal(challenge);

            // Compare computed HMAC with received HMAC
            return MessageDigest.isEqual(computedHmac, receivedHmac);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return false;
        }
    }
}
