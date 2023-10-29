package lab15.gabriel.group2.a2;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import lab15.gabriel.group2.a2.Database.ScrollDB;
import lab15.gabriel.group2.a2.Database.UserDB;
import lab15.gabriel.group2.a2.Login_Register.LoginUI;

public class UserUI {


    public static void main(String[] args, Scanner scanner) {

        boolean loginCheck = LoginUI.loginCheck;
        String userID = LoginUI.userID; // Fixed a typo in variable name
        String userIdentity = LoginUI.userIdentity;

        String username = UserDB.getUsernameByUserId(userID);

        boolean loopCheck = true;

        if (loginCheck) {
            System.out.println("\nHi " + username + " (" + userIdentity + " user)\n");
        } else {
            System.out.println("\nHi Guest\n");
        }

        displayScrollOfTheDay();

        while (loopCheck) {


            int scrollId = 0;
            boolean validInput = false;

            while (!validInput) {



                System.out.println(" -= Scroll Library =- \n");

                List<String> displayList = extractScrollNames(ScrollDB.get_ScrollNameAndContent_By_UserId());
                for (String item : displayList) {
                    System.out.println(item);
                }

                System.out.println("\nPlease choose an option:");
                System.out.println("1. Preview Scroll");
                if (loginCheck) {
                    System.out.println("2. Download Scroll");
                }
                if (loginCheck) {
                    System.out.println("3. Upload Scroll");
                }
                if (loginCheck) {
                    System.out.println("4. Edit Scroll Content");
                }
                if (loginCheck){
                    System.out.println("5. Edit Scroll Name");
                }

                if (loginCheck) {
                    System.out.println("6. Update User Information");
                }
                System.out.println("7. Filter Scroll");
                System.out.println("8. Logout");
                if (loginCheck){
                    System.out.println("10. Remove Scroll");
                }

                System.out.print("Enter your choice: ");
                String input = scanner.nextLine().trim();

                if ("1".equals(input)) {
                    while (!validInput) {
                        System.out.print("\nEnter the scroll ID to preview content: ");
                        input = scanner.nextLine().trim();
                        if (input.isEmpty()) {
                            System.out.println("Please enter a scroll ID: ");
                            continue;
                        }

                        try {
                            // Scroll scrollToBePreviewed = ScrollDB.
                            scrollId = Integer.parseInt(input);
                            validInput = true;
                            previewScrollContent(ScrollDB.get_ScrollNameAndContent_By_UserId(), scrollId);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a valid scroll ID!");
                        }
                    }
                } else if ("2".equals(input) && loginCheck) {
                    while (!validInput) {
                        System.out.print("Enter the scroll ID to download (or 'b' to go back): ");
                        input = scanner.nextLine().trim();
                        if (input.isEmpty()) {
                            System.out.println("Please enter a scroll ID: ");
                            continue;
                        }

                        if ("b".equals(input)) {
                            validInput = true; // Go back
                        } else {
                            int scrollIdToDownload;
                            try {
                                scrollIdToDownload = Integer.parseInt(input);
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input. Please enter a valid scroll ID!");
                                continue;
                            }

                            if (validScrollID(scrollIdToDownload)) {
                                // Attempt to download the scroll with the entered scroll ID
                                boolean downloadSuccess = Scroll.downloadScroll(
                                        String.valueOf(scrollIdToDownload),
                                        LoginUI.userID
                                );
                                if (downloadSuccess) {
                                    System.out.println("Scroll downloaded successfully!\n");
                                }
                                validInput = true;
                            } else {
                                System.out.println("Invalid scroll ID. Please enter a valid scroll ID!");
                            }
                        }
                    }
                }

                else if ("3".equals(input) && loginCheck) {
                    while (!validInput) {
                        System.out.print("Enter the scroll name: ");
                        String scrollName = scanner.nextLine().trim();

                        // Test edge case: Empty scroll name
                        if (scrollName.isEmpty()) {
                            System.out.println("Scroll name cannot be empty. Please provide a valid name.");
                            continue;
                        }

                        System.out.print("Is this scroll public or private? (Enter 'public' or 'private'): ");
                        String scrollType = scanner.nextLine().trim().toLowerCase();

                        // Test edge case: Invalid scroll type
                        if (!("public".equals(scrollType))) {
                            if (!("private".equals(scrollType))) {
                                System.out.println("Invalid scroll type. Please enter 'public' or 'private'.");
                                continue;
                            }
                        }

                        // Check if it's a private scroll and ask for a password
                        String scrollPassword = "";
                        if ("private".equals(scrollType)) {
                            System.out.print("Enter a password for the private scroll: ");
                            scrollPassword = scanner.nextLine().trim();

                            // Test edge case: Empty password
                            if (scrollPassword.isEmpty()) {
                                System.out.println("Password cannot be empty for private scrolls. Please enter a password.");
                                continue;
                            }
                        }

                        System.out.println("Enter the scroll content (Binary Input only, use \\n for next line): ");
                        String scrollContentString = scanner.nextLine().trim();

                        String id = String.valueOf((ScrollDB.get_ScrollNameAndContent_By_UserId().size()/2)+1);

                        // Validate the scroll content
                        if (isValidContent(scrollContentString)) {
                            // Upload the scroll with the entered details
                            boolean uploadSuccess = Scroll.addNewScroll(
                                    id,
                                    scrollName,
                                    userID,
                                    scrollType,
                                    scrollPassword,
                                    scrollContentString,
                                    "0"
                            );

                            if (uploadSuccess) {
                                System.out.println("\nScroll uploaded successfully!");
                            } else {
                                System.out.println("\nUpload failed. Please try again.");
                            }
                            validInput = true;
                        } else {
                            System.out.println("Invalid content. Please enter '0' or '1' or '\\n' as the scroll content.");
                        }
                    }

                } else if ("4".equals(input) && loginCheck) {
                    while (!validInput) {
                        System.out.print("\nEnter the scroll ID to edit (or 'b' to go back): ");
                        input = scanner.nextLine().trim();
                        if (input.isEmpty()) {
                            System.out.println("\nPlease enter a scroll ID: ");
                            continue;
                        }

                        if ("b".equals(input)) {
                            validInput = true; // Go back
                        } else {
                            int scrollIdToEdit;
                            try {
                                scrollIdToEdit = Integer.parseInt(input);
                            } catch (NumberFormatException e) {
                                System.out.println("\nInvalid input. Please enter a valid scroll ID!");
                                continue;
                            }

                            if (validScrollID(scrollIdToEdit)) {
                                Scroll scrollToBeEdited = ScrollDB.return_scroll_by_ID(String.valueOf(scrollIdToEdit));

                                // Check if the logged-in user is the uploader of this scroll
                                if (!LoginUI.userID.equals(scrollToBeEdited.getUserId())) {
                                    System.out.println("\nYou are not the uploader of this scroll. You can't edit it!");
                                    continue;
                                }

                                System.out.println("\nEnter the new content: (Binary Input only, use \\n for next line)");
                                String newContent = scanner.nextLine().trim();
                                if (isValidContent(newContent)) {
                                    // Edit the scroll with the entered scroll ID and new content
                                    boolean editSuccess = scrollToBeEdited.editScroll(
                                            LoginUI.userID,
                                            String.valueOf(scrollIdToEdit),
                                            newContent
                                    );
                                    if (editSuccess) {
                                        System.out.println("\nScroll edited successfully!");
                                    } else {
                                        System.out.println("\nEdit failed. Scroll not found.");
                                    }
                                    validInput = true;
                                } else {
                                    System.out.println("Invalid content. Please enter '0' or '1' or '\\n' as the new content.");
                                }
                            } else {
                                System.out.println("Invalid scroll ID. Please enter a valid scroll ID!");
                            }
                        }
                    }
                }
                else if ("5".equals(input) && loginCheck) {
                    while (!validInput) {
                        System.out.print("\nEnter the scroll ID for which you want to edit the filename (or 'b' to go back): ");
                        input = scanner.nextLine().trim();

                        if (input.isEmpty()) {
                            System.out.println("\nPlease enter a scroll ID: ");
                            continue;
                        }

                        if ("b".equals(input)) {
                            validInput = true; // Go back
                        } else {
                            int scrollIdToEdit;
                            try {
                                scrollIdToEdit = Integer.parseInt(input);
                            } catch (NumberFormatException e) {
                                System.out.println("\nInvalid input. Please enter a valid scroll ID!");
                                continue;
                            }

                            if (validScrollID(scrollIdToEdit)) {
                                Scroll scrollToBeEdited = ScrollDB.return_scroll_by_ID(String.valueOf(scrollIdToEdit));

                                if (scrollToBeEdited == null) {
                                    System.out.println("\nScroll not found!");
                                    continue;
                                }

                                if (!LoginUI.userID.equals(scrollToBeEdited.getUserId())) {
                                    System.out.println("\nYou are not the uploader of this scroll. You can't edit its filename.");
                                    continue;
                                }

                                boolean editSuccess = false;
                                String newFilename = ""; // Initialize it here
                                while(!editSuccess) {
                                    String oldScrollName = scrollToBeEdited.getScrollName();
                                    System.out.print("\nEnter the new filename (or 'b' to go back): ");
                                    newFilename = scanner.nextLine().trim();

                                    if ("b".equals(newFilename)) {
                                        break; // Go back
                                    }

                                    if (isValidFilename(newFilename)) {
                                        editSuccess = scrollToBeEdited.updateScrollName(oldScrollName, newFilename, LoginUI.userID);
                                        if (editSuccess) {
                                            System.out.println("\nFilename edited successfully!");

                                        } else {
                                            System.out.println("\nEDIT FAILED. Filename might already exist. Please try again.");
                                        }
                                    } else {
                                        System.out.println("Invalid filename. Please ensure it does not have any special characters and is not too long.");
                                    }
                                }

                                if(editSuccess || "b".equals(newFilename)) {
                                    validInput = true;
                                }
                            } else {
                                System.out.println("Invalid scroll ID. Please enter a valid scroll ID!");
                            }
                        }
                    }
                }





                else if ("6".equals(input)) {
                    UpdateUI.main(args, scanner);
                } else if ("7".equals(input)) {

                    List<String> filterResult = Scroll.filterAndDisplayScrolls();
                    if (!filterResult.isEmpty()) {
                        System.out.println("Filtered Scroll:");
                        for (String result:filterResult) {
                            System.out.println(result);
                        }
                        System.out.println("");
                    }

                    while (!filterResult.isEmpty()) {
                        
                        System.out.println("Please choose an option:");
                        System.out.println("1. Preview Scroll");
                        if (loginCheck) {
                            System.out.println("2. Download Scroll");
                        }
                        System.out.println("3. Clear Filter");

                        System.out.print("Enter your choice: ");
                        String inputFilter = scanner.nextLine().trim();

                        if ("1".equals(inputFilter)) {
                            // Preview Scroll
                            while (true) {
                                System.out.print("\nEnter the scroll ID to preview content: ");
                                input = scanner.nextLine().trim();
                                if (input.isEmpty()) {
                                    System.out.println("Please enter a scroll ID: ");
                                    continue;
                                }

                                try {
                                    scrollId = Integer.parseInt(input);
                                    previewScrollContent(ScrollDB.get_ScrollNameAndContent_By_UserId(), scrollId);
                                    break;
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid input. Please enter a valid scroll ID!");
                                }
                            }
                        } else if ("2".equals(inputFilter) && loginCheck) {
                            // Download Scroll
                            while (true) {
                                System.out.print("Enter the scroll ID to download (or 'b' to go back): ");
                                input = scanner.nextLine().trim();
                                if (input.isEmpty()) {
                                    System.out.println("Please enter a scroll ID: ");
                                    continue;
                                }

                                if ("b".equals(input)) {
                                    break;
                                } else {
                                    int scrollIdToDownload;
                                    try {
                                        scrollIdToDownload = Integer.parseInt(input);
                                    } catch (NumberFormatException e) {
                                        System.out.println("Invalid input. Please enter a valid scroll ID!");
                                        continue;
                                    }

                                    if (validScrollID(scrollIdToDownload)) {
                                        // Attempt to download the scroll with the entered scroll ID
                                        boolean downloadSuccess = Scroll.downloadScroll(
                                                String.valueOf(scrollIdToDownload),
                                                LoginUI.userID
                                        );
                                        if (downloadSuccess) {
                                            System.out.println("Scroll downloaded successfully!\n");
                                            break;
                                        }
                                        
                                    } else {
                                        System.out.println("Invalid scroll ID. Please enter a valid scroll ID!");
                                    }
                                }
                            }
                        } else if ("3".equals(inputFilter)) {
                            break; // Exit the filter loop
                        } else {
                            System.out.println("\nInvalid choice. Please choose a valid option.");
                        }
                    }

                } else if ("8".equals(input)) {
                    loopCheck = false;
                    validInput = false;
                    LoginUI.loginCheck = false;

                    LoginUI.userIdentity = "guest";

                    LoginUI.userID = "";
                    break;
                } else if ("10".equals(input) && loginCheck) {
                    while (!validInput) {
                        System.out.print("Enter the scroll ID to Remove (or 'b' to go back): ");
                        input = scanner.nextLine().trim();
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


                    }
                }else {
                    System.out.println("\nInvalid choice. Please choose a valid option.");
                }
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
            numberedScrollNames.add("Scroll ID: " + count + " | " + "Scroll Name: " + name);
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
        System.out.println("Scroll not found.");
    }

    public static boolean validScrollID(int scrollID) {
        if (scrollID <= ScrollDB.get_ScrollNameAndContent_By_UserId().size()/2) {
            return true;
        } else {return false;}
    }

    public static boolean isValidContent(String content) {
        // Validate the content using a regular expression pattern
        String check1 = content.replace("\\n", "");
        boolean onlyLineBreak = check1.isEmpty();
        String check2 = check1.replace("1", "");
        String chcek3 = check2.replace("0","");
        return (!onlyLineBreak && chcek3.isEmpty());
    }

    public static boolean isValidFilename(String filename) {
        // Check the filename length (you can adjust the range as needed)
        if (filename.length() < 1 || filename.length() > 255) {
            return false;
        }
        // Define a pattern for invalid characters (e.g., \, /, :, *, ?, ", <, >, |)
        String regex = "[^<>:\"/\\\\|?*]+";
        return filename.matches(regex);
    }

    private static void displayScrollOfTheDay() {
        List<String> scrollLibrary = ScrollDB.get_ScrollNameAndContent_By_UserId();

        if (!scrollLibrary.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(scrollLibrary.size() / 2); // Select a random scroll

            String scrollName = scrollLibrary.get(randomIndex * 2);
            String scrollContent = scrollLibrary.get(randomIndex * 2 + 1);

            System.out.println(" -= Scroll of the Day =- ");
            System.out.println("Scroll Name: " + scrollName.replace(".txt", ""));
            System.out.println("Preview: ");
            String[] lines = scrollContent.split("\n");
            for (int i = 0; i < Math.min(3, lines.length); i++) {
                System.out.println(lines[i]);
            }
            System.out.println();
        } else {
            System.out.println("No scrolls available in the library.");
        }
    }


}