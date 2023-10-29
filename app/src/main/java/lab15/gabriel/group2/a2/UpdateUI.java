package lab15.gabriel.group2.a2;

import java.util.Scanner;
import java.util.regex.Pattern;

import lab15.gabriel.group2.a2.Database.UserDB;
import lab15.gabriel.group2.a2.Login_Register.LoginUI;
import lab15.gabriel.group2.a2.user_accounts.User_init;

public class UpdateUI {
    public static void main(String[] args, Scanner scanner) {

        while (true) {
            System.out.println("\n// You can enter b to return to the previous page //");

            String userID;
            while (true) {
                System.out.print("Please enter your user ID (Maximum of 8 digits): ");
                userID = scanner.nextLine().trim();
                if (userID.matches("\\d{1,8}")) {
                    if (!UserDB.doesUserIdExist(userID) || userID.equals(LoginUI.userID)) {
                        break;
                    } else {
                        System.out.println("\nUserID exist. Please enter a new UserID");
                    }
                    
                } else {
                    System.out.println("\nInvalid user ID format. Please enter 1-8 digit numeric input.\n");
                }
            }

            // Validate and obtain phone number
            String phoneNumber;
            while (true) {
                System.out.print("Please enter your updated phone number: ");
                phoneNumber = scanner.nextLine().trim();
                if (phoneNumber.matches("\\d+") | "b".equals(phoneNumber)) {
                    break;
                } else {
                    System.out.println("\nInvalid phone number format. Please enter numeric input only.\n");
                }
            }

            if ("b".equals(phoneNumber)) {
                break;
            }

            // Validate and obtain email address
            String emailAddress;
            while (true) {
                System.out.print("Please enter your updated email address: ");
                emailAddress = scanner.nextLine().trim();
                if (isValidEmail(emailAddress)) {
                    break;
                } else {
                    System.out.println("\nInvalid email address format. Please enter a valid email address.\n");
                }
            }

            System.out.print("Please enter your updated full name: ");
            String fullName = scanner.nextLine().trim();
        

            System.out.print("Please enter your updated username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Please enter your updated password: ");
            String password = scanner.nextLine().trim();


            if (userID.isEmpty() || phoneNumber.isEmpty() || emailAddress.isEmpty() || fullName.isEmpty() ||
                 username.isEmpty() || password.isEmpty()) {
                System.out.println("Please enter valid information. Fields cannot be empty or contain only spaces.");
            } else {
                int unique_ID = UserDB.getUserIdByUsername(LoginUI.userID);
                password = User_init.encryptPassword(password);
                System.out.println(UserDB.updateUser(unique_ID,phoneNumber,fullName,emailAddress,username,password,userID)+"\n");
                break;
            }
        }
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9]+\\.[A-Za-z0-9]+)$";
        return Pattern.matches(emailRegex, email);
    }

}