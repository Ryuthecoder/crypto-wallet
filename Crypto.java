import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Base64;

/*
    This class is responsible for the cryptographic process
 */

public class Crypto{
    private static final String ALGO = "AES/GCM/NoPadding";

    //This method encrypts the private key
    public static String encrypt(String privateKeyText, String username, byte[] masterKeyBytes) throws Exception {
        byte[] iv = new byte[12]; //Creates a 12-byte container for a random starting number
        new SecureRandom().nextBytes(iv); // Fills the container with secure, random bytes

        Cipher cipher = Cipher.getInstance(ALGO);  //Loads Java's crypto engine
        SecretKeySpec keySpec = new SecretKeySpec(masterKeyBytes, "AES"); //Turns raw secret key bytes into an offical AES key
        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv); //Pairs the random IV with a 128-bit secrety or authentication tag

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec); //Sets the engine to encrypt mode with the key and settings
        cipher.updateAAD(username.getBytes()); //Binds the username to the lock to prevent tampering

        byte[] cipherText = cipher.doFinal(privateKeyText.getBytes());  //Locks the secret message and seals it with an authentication tag

        ByteBuffer buffer = ByteBuffer.allocate(iv.length + cipherText.length); //Allocates exact space for both the IV and locked message
        buffer.put(iv); //Packages the IV at the very beginning
        buffer.put(cipherText); // Packages the locked message right after the IV

        return Base64.getEncoder().encodeToString(buffer.array()); //Converts the entire combined package into a base64 text
    }

    //This method decrypts the private key
    public static String decrypt(String encryptedBase64Text,String username, byte[] keyBytes) throws Exception {
        byte[] encryptedData = Base64.getDecoder().decode(encryptedBase64Text); //It turns the encrypted text into a byte array
        ByteBuffer buffer = ByteBuffer.wrap(encryptedData); //Wraps the data so we can slice it easily

        byte[] iv = new byte[12];//Creates 12-byte container for the IV
        buffer.get(iv); //Pulls the first 12 bytes out to use as the IV

        byte[] cipherText = new byte[buffer.remaining()]; //Creates a container for the rest of the data
        buffer.get(cipherText); //Pulls out remaining bytes as the locked message

        Cipher cipher = Cipher.getInstance(ALGO); //loads java's crypto engine
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES"); //turns raw bytes into offical AES key
        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv); //Pairs the IV with 128-bit security or authentication tag size
 
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec); //Sets the engine to decrypt mode with the key and IV
        cipher.updateAAD(username.getBytes()); //Uses the username to verify the data belongs to them

        byte[] decryptedKeyBytes = cipher.doFinal(cipherText); //Unlocks message and checks for tampering
        return new String(decryptedKeyBytes); // Converts the unlocked bytes back into readable text
    }
}
    