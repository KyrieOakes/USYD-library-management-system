package lab15.gabriel.group2.a2.Login_Register;

import java.util.Scanner;

import lab15.gabriel.group2.a2.Database.UserDB;
import lab15.gabriel.group2.a2.user_accounts.User_init;

public class LoginUI {

    public static boolean loginCheck = false;

    public static String userIdentity = "guest";

    public static String userID = "";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter userID (or b to return): ");
            String loginUsername = scanner.nextLine();

            if ("b".equals(loginUsername)) {
                break;
            }

            System.out.print("Enter password: ");
            String loginPassword = scanner.nextLine();

            String encryptedPW = User_init.encryptPassword(loginPassword);

            try {
                String DBpassword = UserDB.passwordCheck(loginUsername);
                if (DBpassword.equals(encryptedPW)) {
                    loginCheck = true;
                    try {

                        userIdentity = (UserDB.user_detail(loginUsername)).getUserType();
                        userID = loginUsername;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                } else {
                    System.out.println("Invalid login details");;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    } 
}
