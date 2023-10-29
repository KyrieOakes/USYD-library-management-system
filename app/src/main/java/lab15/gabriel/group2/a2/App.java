package lab15.gabriel.group2.a2;
import lab15.gabriel.group2.a2.Database.ScrollDB;
import lab15.gabriel.group2.a2.Database.User_Scroll_DB;
import lab15.gabriel.group2.a2.user_accounts.User_init;
import lab15.gabriel.group2.a2.Database.UserDB;
import lab15.gabriel.group2.a2.Login_Register.LoginUI;
import lab15.gabriel.group2.a2.Login_Register.RegisterUI;
import java.util.Scanner;
public class App {

    public static void main(String[] args) {

        UserDB.main(args);
        User_init.main(args);
        ScrollDB.main(args);
        ScrollDB.synchronization();
        User_Scroll_DB.main(args);



        Scanner scanner = new Scanner(System.in);

        while (true) {

            WelcomePage.main(args);

            System.out.print("\nPlease choose an option: ");
            String userInput = scanner.nextLine();

            if ("1".equals(userInput)) {
                UserUI.main(args, scanner);
            } else if ("2".equals(userInput)){
                LoginUI.main(args);
                if (LoginUI.loginCheck) {
                    UserUI.main(args, scanner);
                }
            } else if ("3".equals(userInput)) {
                LoginUI.main(args);
                if (LoginUI.loginCheck) {
                    AdminUI.main(args, scanner);
                }
            } else if ("4".equals(userInput)) {
                RegisterUI.main(args, scanner);
            } else if ("5".equals(userInput)) {
                break;
            } else {
                System.out.println("Invalid Choice");
            }
        }

        rmFile.main(args);
    }
}
