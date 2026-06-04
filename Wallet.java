import java.util.Scanner;
import java.util.ArrayList;
import java.security.SecureRandom;

public class Wallet {

    private ArrayList<User> allUsers;

    public Wallet(ArrayList<User> users){
        allUsers = users;
    }

    /*
        Below is a linear sign up process for the crypto wallet service as each user take turn to sign up.
    */


    public Wallet() {
        // Fix 1: Capitalized the "A" in ArrayList
        allUsers = new ArrayList<User>();
        System.out.println("Welcome to crypto wallet!");
        Scanner input = new Scanner(System.in);
        
        int count = 1;
        
        while (count <= 2){ // The value of 2 is responsible for the maximum number of users that can be signed up, which in this case is 2
            System.out.println("Please create user: " + "#" + count);
            System.out.print("Enter Username: ");
            String name = input.nextLine();
            System.out.print("Enter Password: ");
            String password = input.nextLine();


            System.out.print("Enter Private Key: ");
            String privateKey = input.nextLine();
            
            // Fix 2: Pass name, password, privateKey to addUser, which will add the user to the list if it's username is unique
            boolean success = addUser(name, password, privateKey);
            if (success == false){
                count--;   
                continue; 
            }

            count++;
        }
        input.close(); 
    }

    /*
        Below is a method that allows other new users that were not signed up initially to be added to the crypto wallet service.
    */

    public boolean addUser(String username, String password, String privateKey){
        boolean found = false;
        for (int i = 0; i < allUsers.size(); i++){
            if (allUsers.get(i).getUsername().equals(username)){
                found = true;
            }
        }
        if (found == true){
            System.out.println("User already exists. Please enter a different username.");
            return false;
        }
        else{
            try {
                // Generate the secure random key
                byte[] secretKey = new byte[32];
                new SecureRandom().nextBytes(secretKey);
                
                // Encrypt the private key immediately using the parameters passed to the method
                String cipherText = Crypto.encrypt(privateKey, username, secretKey);  

                // Create the user and assign the secured data
                User user = new User(username, password);
                user.setPrivateKey(cipherText);
                user.setSecretKey(secretKey);
                
                allUsers.add(user);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    /*
        Below is a method that allows existing users to be removed from the crypto wallet service if they verify their username and password.
    */

    public void removeUser(String username, String password){
        // Fix 5: Fixed capitalization from "allusers" to "allUsers"
        for (int i = allUsers.size() - 1; i >= 0; i--){ //Fix 11: I changed to backward traversal since I'm removing
            // Fix 6: Changed "[i]" to ".get(i)"
            if (allUsers.get(i).getUsername().equals(username) && allUsers.get(i).getPassword().equals(password)){
                User removed = allUsers.remove(i);
                System.out.println(removed.getUsername() + " has been removed.");
            }
        }
    }

    /*
        Below is a method that allows users to see their decrypted private key if they verify their username and password.
    */

    public void decryptKey(String username, String password) throws Exception{
        for (int i = 0; i < allUsers.size(); i++){
            // Fix 7: Changed "[i]" to ".get(i)"
            if (allUsers.get(i).getUsername().equals(username) && allUsers.get(i).getPassword().equals(password)){
                // Fix 8: Swapped out "user" for "allUsers.get(i)" to target the matching user found by the loop
                User targetUser = allUsers.get(i);
                String decryptedKey = Crypto.decrypt(targetUser.getPrivateKey(), targetUser.getUsername(), targetUser.getSecretKey());
                targetUser.setPrivateKey(decryptedKey);
                System.out.println("\n" + "Decrypted Output: " + decryptedKey);
                return; //Fix 12: I added a return statement to exit the method, or the last print statement will always be printed
            }
        }
        System.out.println("Invalid username or password.");
    }

    /*
        Below is the getter method of Wallet class
    */
    public ArrayList<User> getAllUsers(){
        return allUsers;
    }
}