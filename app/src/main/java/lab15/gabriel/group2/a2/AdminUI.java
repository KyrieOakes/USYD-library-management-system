package lab15.gabriel.group2.a2;
//package lab15.gabriel.group2.a2.Login_Register;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;


import lab15.gabriel.group2.a2.Database.ScrollDB;
import lab15.gabriel.group2.a2.Login_Register.LoginUI;
import lab15.gabriel.group2.a2.user_accounts.User_init;
import lab15.gabriel.group2.a2.Database.UserDB;
import lab15.gabriel.group2.a2.UserUI;



public class AdminUI {
    private Admin admin;
    private String email;
    private String email_Update;


    public AdminUI(Admin admin) {
        this.admin = admin;
    }
    public void showMenu_Admin(Scanner scanner) {
        boolean loginCheck = LoginUI.loginCheck;
        String username = LoginUI.userID;
        String userIdentity = LoginUI.userIdentity;
        boolean loopCheck = true;

        if (loginCheck) {
            System.out.println("\nHi " + username + " (" + userIdentity + " user)\n");
        }

        while (loopCheck) {
            System.out.println("\n--------- Admin Panel ---------");
            System.out.println("1. View all Registered Users");
            System.out.println("2. Manage Registered Users (Add/Delete)");
            System.out.println("3. View stats of Scrolls");
            System.out.println("4. View Scrolls");
            System.out.println("5. Update profiles.");
            System.out.println("6. Exit");
            System.out.println("7. Remove Scroll");
            System.out.print("---------------------------------\n\n");
            System.out.print("Choose an option: ");

            while (!scanner.hasNextInt()) {  // While the input is not an integer
                String input = scanner.next();
                if (input.equalsIgnoreCase("b")) {
                    showMenu_Admin(scanner);  // Return to the admin's main menu
                    return;
                }
                System.out.println("Invalid input. Please enter a number or 'b' to go back to the main menu.");
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    List<User> allUsers = UserDB.getAllUsers();
                    admin.viewAllUsers(allUsers);
                    break;
                case 2:
                    loopCheck = showUserManagementMenu(scanner);
                    break;
                case 3:
                    // show every scrolls downloading number
                    Map<String, Integer> downloadCounts = admin.getAllScrollsDownloadCount();
                    System.out.println("----------------------");
                    System.out.println("\nDownload counts for scrolls:");
                    for (Map.Entry<String, Integer> entry : downloadCounts.entrySet()) {
                        System.out.println(entry.getKey() + ": " + entry.getValue());
                    }
                    System.out.println("----------------------\n");

                    // show scrolls uploaded by users
                    System.out.println("\nScrolls uploaded by users:");
                    admin.viewAllScrollsOfUsers();
                    break;
                case 4:
                    viewScrolls(scanner);
                    break;
                case 5:
//                    System.out.println("Please enter the Userid: ");
//                    String UserID = scanner.nextLine();
                    String UserID = this.admin.getUserId();
                    int ID = UserDB.getUserIdByUsername(UserID);

                    System.out.print("Enter phone number: ");
                    String phone_number = scanner.nextLine();
                    while(!phone_number.matches("\\d+")) {
                        System.out.println("Invalid phone number. Only digits are allowed.");
                        System.out.print("Enter phone number again or enter 'b' to go back: ");
                        phone_number = scanner.nextLine();
                        if(phone_number.equalsIgnoreCase("b")) {
                            return; // return to the previous menu/screen
                        }
                    }

                    boolean validEmail = false;
                    while (!validEmail) {
                        System.out.print("Enter user email: ");
                        email_Update = scanner.nextLine(); // changed from next() to nextLine()
                        if (isValidEmail(email_Update)) {
                            validEmail = true; // break out of the loop since the email is valid
                        } else if (email_Update.equalsIgnoreCase("b")) {
                            // Handle the logic to return to the previous menu/screen here
                            return;
                        } else {
                            System.out.println("Invalid email format. Try again or enter 'b' to go back.");
                        }
                    }

                    System.out.print("Enter new full name (or enter 'same' to keep unchanged): ");
                    String full_name = scanner.nextLine();

                    System.out.print("Enter new username (or enter 'same' to keep unchanged): ");
                    username = scanner.nextLine();

                    System.out.print("Enter new password (or enter 'same' to keep unchanged): ");
                    String password = scanner.nextLine();

                    String userId = "";
                    while (true) {
                        System.out.print("Enter new userId (or enter 'same' to keep unchanged, or 'b' to go back): ");
                        userId = scanner.nextLine();

                        if (userId.equals("same") || userId.matches("\\d+")) {
                            System.out.println("Valid input.");
                            break;
                        } else if (userId.equals("b")) {
                            System.out.println("Returning to the previous menu.");
                            break;
                        } else {
                            System.out.println("Invalid input. Please enter a numeric userId, 'same', or 'b' to go back.");
                        }
                    }


                    String result = UserDB.updateUser(ID, phone_number, full_name, email_Update, username, password, userId);
                    System.out.println(result);
                    break;
                case 6:
                    System.out.println("Exiting...");
                    LoginUI.loginCheck = false;

                    LoginUI.userIdentity = "guest";

                    LoginUI.userID = "";
                    return;

                case 7:
                        System.out.print("Enter the scroll ID to Remove (or 'b' to go back): ");
                        String input = scanner.nextLine().trim();
                        if (input.isEmpty()) {
                            System.out.println("Please enter a scroll ID: ");
                            continue;
                        }

                        String user_current_ID = LoginUI.userID;
                        String scroll_ID = input;

                        if(!ScrollDB.deleteScrollByScrollId(scroll_ID,user_current_ID)){
                            break;
                        }else {
                            System.out.println("\n");
                            System.out.println("Scroll ID : "+scroll_ID+" has been removed.\n");
                            break;
                        }



                default:
                    System.out.println("Invalid option. Please choose again.");
            }
        }
    }



    private boolean showUserManagementMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n--------- User Management ---------");
            System.out.println("1. Add a user");
            System.out.println("2. Delete a user");
            System.out.println("3. Modified the type of a user by UserId");
            System.out.println("4. Return to Admin Panel");
            System.out.print("Choose an option: ");
            int choice_Management = scanner.nextInt();
            scanner.nextLine();

            switch (choice_Management) {
                case 1:
                    System.out.println("Enter user details to add:\n");

                    System.out.print("Enter phone number: ");
                    String phone_number = scanner.nextLine();
                    while(!phone_number.matches("\\d+")) {
                        System.out.println("Invalid phone number. Only digits are allowed.");
                        System.out.print("Enter phone number again or enter 'b' to go back: ");
                        phone_number = scanner.nextLine();
                        if(phone_number.equalsIgnoreCase("b")) {
                            return true; // return to the previous menu/screen
                        }
                    }

                    boolean validEmail = false;
                    while (!validEmail) {
                        System.out.print("Enter user email: ");
                        email = scanner.nextLine(); // changed from next() to nextLine()
                        if (isValidEmail(email)) {
                            validEmail = true; // break out of the loop since the email is valid
                        } else if (email.equalsIgnoreCase("b")) {
                            // Handle the logic to return to the previous menu/screen here
                            return true;
                        } else {
                            System.out.println("Invalid email format. Try again or enter 'b' to go back.");
                        }
                    }

                    System.out.print("Enter full name: ");
                    String full_name = scanner.nextLine(); // Now it will wait for the new input correctly


                    System.out.print("Enter userId: ");
                    String userId = scanner.nextLine();

                    // Check for duplicate user ID
                    List<String> existingUserIds = UserDB.getAllUserIds(); // Assuming you have imported the UserDB class.
                    while (existingUserIds.contains(userId)) {
                        System.out.println("User ID already exists. Please enter a different one or enter 'b' to go back to admin panel.");
                        userId = scanner.nextLine();
                        if (userId.equalsIgnoreCase("b")) {
                            return true; // Assuming you want to stay in this method when returning to the admin panel.
                        }
                    }

                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();

                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();

                    System.out.print("Enter user type (e.g. general (general user)/ admin (admin user) ): ");
                    String userType = scanner.nextLine();

                    User newUser = new User(phone_number, email, full_name, userId, username, User_init.encryptPassword(password), userType);

                    String response = UserDB.insert_data(newUser);
                    System.out.println(response);
                    break;

                case 2:
                    System.out.print("Enter userID to delete: ");
                    String userID = scanner.next();
                    List<String> admins = UserDB.getAdminID();

                    Boolean sel_account = false;
                    for (int i = 0; i < admins.size(); i++) {
                        if (admins.get(i) .equals(userID) ){
                            sel_account = true;
                            break;
                        }
                    }
                    if (sel_account){
                        System.out.println("You can't delete yourself or Other admin!!!");
                        break;
                    }


                    String result = admin.deleteUserByUserID(userID);
                    System.out.println("");
                    System.out.println(result); // Display the result to the admin user
                    break;

                case 3:
                    //admin modify the type of other users
                    System.out.println("Enter userID to modify: ");
                    String userID_Modi = scanner.next();
//                    String userID = scanner.nextLine();
                    while(!userID_Modi.matches("\\d+")) {
                        System.out.println("Invalid userID. Only digits are allowed.");
                        System.out.print("Enter a valid userID or enter 'b' to go back: ");
                        userID_Modi = scanner.nextLine();
                        if(userID_Modi.equalsIgnoreCase("b")) {
                            return true; // return to the previous menu/screen
                        }
                    }

                    System.out.println("Enter the type of the user: ");
//                    String Type = scanner.nextLine();
                    String Type = scanner.next();
                    while(!Type.equals("admin") && !Type.equals("general user")) {
                        System.out.println("Invalid User type.");
                        System.out.print("Enter a valid user type or enter 'b' to go back: ");
                        Type = scanner.nextLine();
                        if(Type.equalsIgnoreCase("b")) {
                            return true; // return to the previous menu/screen
                        }
                    }

                    admin.changeUserRole(userID_Modi, Type);




                case 4:
                    return true;

                default:
                    System.out.println("Invalid option. Please choose again.");
            }
        }
    }

    public static List<String> extractScrollNames(List<String> inputList) {
        List<String> numberedScrollNames = new ArrayList<>();
        for (int i = 0, count = 1; i < inputList.size(); i += 2, count++) {
            String name = inputList.get(i);
            if (name.endsWith(".txt")) {
                // Remove the ".txt" extension from the name
                name = name.substring(0, name.length() - 4);
            }
            numberedScrollNames.add(count + ". " + name);
        }
        return numberedScrollNames;
    }

    public static void previewScrollContent(List<String> inputList, int scrollId) {
        if (scrollId <= inputList.size()/2) {
            String content = inputList.get(scrollId * 2 - 1);
            String[] lines = content.split("\n");
            System.out.println("\nPreview:");
            for (int j = 0; j < Math.min(3, lines.length); j++) {
                System.out.println(lines[j]);
            }
            System.out.println("");
            return;
        }
        System.out.println("Scroll not found.\n");
    }

    private void viewScrolls(Scanner scanner) {
        List<String> displayList = extractScrollNames(ScrollDB.get_ScrollNameAndContent_By_UserId());
        for (String item : displayList) {
            System.out.println(item);
        }

        int scrollId = 0;
        boolean validInput = false;

        while (!validInput) {
            System.out.print("\nEnter the scroll ID to preview content (Enter b to return to Admin Panel): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Please enter a scroll ID (Enter b to return to Admin Panel): ");
                continue;
            }

            if ("b".equals(input)) {
                return;
            }

            try {
                scrollId = Integer.parseInt(input);
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid scroll ID!");
            }
        }

        previewScrollContent(ScrollDB.get_ScrollNameAndContent_By_UserId(), scrollId);
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9]+\\.[A-Za-z0-9]+)$";
        return Pattern.matches(emailRegex, email);
    }
    public static void main(String[] args, Scanner scanner) {

        User userFromDB = UserDB.user_detail("20");
        System.out.println(userFromDB.getClass().getName());
        if (userFromDB == null) {
            System.out.println("Error: User not found.");
            return;
        }

        if (!(userFromDB instanceof Admin)) {
            System.out.println("The user is not an admin.");
            return;
        }


        Admin existingAdmin = (Admin) userFromDB;
        System.out.println(userFromDB.getClass().getName());
        AdminUI adminUIInstance = new AdminUI(existingAdmin);
        adminUIInstance.showMenu_Admin(scanner);
    }
}
