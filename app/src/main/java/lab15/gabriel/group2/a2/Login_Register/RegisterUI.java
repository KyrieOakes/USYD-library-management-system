package lab15.gabriel.group2.a2.Login_Register;

import java.util.Scanner;
import java.util.regex.Pattern;

import lab15.gabriel.group2.a2.Login_Register.Register;
import lab15.gabriel.group2.a2.user_accounts.User_init;

public class RegisterUI {
    public static void main(String[] args, Scanner scanner) {

        while (true) {
            System.out.println("\n// You can enter b to return to the previous page //");

            // Validate and obtain phone number
            String phoneNumber;
            while (true) {
                System.out.print("Please enter your phone number: ");
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
                System.out.print("Please enter your email address: ");
                emailAddress = scanner.nextLine().trim();
                if (isValidEmail(emailAddress)) {
                    break;
                } else {
                    System.out.println("\nInvalid email address format. Please enter a valid email address.\n");
                }
            }

            System.out.print("Please enter your full name: ");
            String fullName = scanner.nextLine().trim();

             // Validate and obtain user ID
             String userId;
             while (true) {
                 System.out.print("Please enter your user ID (Maximum of 8 digits): ");
                 userId = scanner.nextLine().trim();
                 if (userId.matches("\\d{1,8}")) {
                     break;
                 } else {
                     System.out.println("\nInvalid user ID format. Please enter 1-8 digit numeric input.\n");
                 }
             }
 

            System.out.print("Please enter your username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Please enter your password: ");
            String password = scanner.nextLine().trim();

            

            if (phoneNumber.isEmpty() || emailAddress.isEmpty() || fullName.isEmpty() ||
                userId.isEmpty() || username.isEmpty() || password.isEmpty()) {
                System.out.println("Please enter valid information. Fields cannot be empty or contain only spaces.");
            } else {
                if (Register.register(phoneNumber, emailAddress, fullName, userId, username, User_init.encryptPassword(password))) {
                    System.out.println("Register success");
                    System.out.println("Redirecting you to Welcome Page");
                    break;
                } else {
                    System.out.println("\nRegister fail. Some of your information has already been registered\n");
                    continue;
                }
            }
        }
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9]+\\.[A-Za-z0-9]+)$";
        return Pattern.matches(emailRegex, email);
    }

}