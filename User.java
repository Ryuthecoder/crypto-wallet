public class User {

    private String username;
    private String password;
    private String privateKey;
    private byte[] secretKey;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /*
        Below are the getters and setters for the User class
    */

    public String getUsername() { return username; }
    public String getPrivateKey() { return privateKey; }
    public byte[] getSecretKey() { return secretKey; }
    public void setPrivateKey(String privateKey) { this.privateKey = privateKey; }

    // Fix 9: Added missing getter to let Wallet check passwords
    public String getPassword() { 
        return password; 
    }

    // Fix 10: Added missing setter to let Wallet store the generated 
    public void setSecretKey(byte[] secretKey) { 
        this.secretKey = secretKey; 
    }
}