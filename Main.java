import java.security.SecureRandom;
import java.util.Scanner;
import java.util.Arrays; 
import java.util.ArrayList;

public class Main {


    public static void main(String[] args) throws Exception {
        try {
            
           //Demo: 

            Wallet wallet = new Wallet();  //Starts the signup process for all the new users

            User bob = wallet.getAllUsers().get(0); //Gets the first user in the list 
            System.out.println("\n" + bob.getPrivateKey()); //Prints the decrypted private key of user above

            wallet.addUser("newUser","newPassword","newPrivateKey"); //Adds a new user with specified username and password to the list 
            wallet.decryptKey("newUser","newPassword"); //Decrypts the private key of the user (above) with specified username and password
            wallet.removeUser("newUser","newPassword"); //Removes the user (above) with specified username and password from the list and prints verification message
           
          
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 }