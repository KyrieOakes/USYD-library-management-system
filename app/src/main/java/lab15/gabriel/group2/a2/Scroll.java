package lab15.gabriel.group2.a2;

import lab15.gabriel.group2.a2.Database.ScrollDB;
import lab15.gabriel.group2.a2.Database.UserDB;

//import javax.annotation.processing.Generated;
import java.io.BufferedWriter;
import java.sql.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Scroll {
    private String scrollId;
    private String scrollName;
    private String scrollFilePath;
    private String userId; // the id of the user who uploaded the scroll
    private String type; // the scroll can be public or private
    private String scrollPassword;
    private String uploadDate;
    private String downloadNum;

    private String content;

    private static final String CURRENT_DIR = System.getProperty("user.dir");
    private static final String DB_PATH = CURRENT_DIR + "/src/main/java/lab15/gabriel/group2/a2/Database/Scroll.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;

    private static boolean filterCheck = false;

    private static boolean filteredResult = false;

    // try
    public Scroll(String scrollId, String scrollName, String scrollFilePath, String userId, String type,
                  String scrollPassword, String uploadDate, String content, String downloadNum) {
        this.scrollId = scrollId;
        this.scrollName = scrollName;
        this.scrollFilePath = scrollFilePath;
        this.userId = userId;
        this.type = type;
        this.scrollPassword = scrollPassword;
        this.uploadDate = uploadDate;
        this.content = content;
        this.downloadNum = downloadNum;
    }

    public void setScrollId(String scrollId) {
        this.scrollId = scrollId;
    }

    public String getScrollId() {
        return this.scrollId;
    }

    public void setScrollName(String scrollName) {
        this.scrollName = scrollName;
    }

    public String getScrollName() {
        return this.scrollName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getScrollFilePath() {
        return scrollFilePath;
    }

    public String getDownloadNum() {
        return downloadNum;
    }

    public void setScrollFilePath(String scrollFilePath) {
        this.scrollFilePath = scrollFilePath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScrollPassword() {
        return scrollPassword;
    }

    public void setScrollPassword(String scrollPassword) {
        this.scrollPassword = scrollPassword;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public String getContent() {
        return content;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public static Boolean addNewScroll(String scrollId, String scrollName, String userId,
                                       String type, String scrollPassword, String scrollContent, String downloadNum) {
        String CURRENT_DIR = System.getProperty("user.dir");
        //scrollName = scrollName + ".txt";
        Boolean alreadyExist = false;
        String scrollFilePath = CURRENT_DIR + "/src/main/java/lab15/gabriel/group2/a2/ScrollsInLibrary/" + scrollName; // Notice the added "/"
        // get current date and time
        Date currentDate = new Date();

        // format the date and time
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(currentDate);

        Scroll new_scroll = new Scroll(scrollId, scrollName, scrollFilePath, userId,
                type, scrollPassword, formattedDate, scrollContent.replace("\\n", System.lineSeparator()), downloadNum);
        // add the new scroll into user's scroll list
        User currentUser = UserDB.user_detail(userId);
        if(currentUser == null){
//            System.out.printf("User not found!\n");
            return false;
        }
        currentUser.addScrollToUser(new_scroll);

        // Check in DB if the scroll with same name exists
        String insertResult = ScrollDB.insert_data_Scroll(new_scroll);
        if (insertResult.startsWith("Already exist file")) {
            System.out.println("Scroll with the same name already exists in the database, please use another name!");
            alreadyExist = true;
        } else if (insertResult.startsWith("Already exist scrollId")) {
            System.out.println("Scroll with the same ScrollID already exists in the database, please use another ScrollID!");
            alreadyExist = true;
        }

        if (alreadyExist) {
            return false;
        }

        try {
            File file = new File(scrollFilePath);

            // Create parent directories if they don't exist
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }

            // create the file if the file does not exist
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(scrollContent.replace("\\n", System.lineSeparator()));
            fileWriter.close();

//            System.out.print("Scroll added successfully!\n");
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error when adding scroll：" + e.getMessage());
            return false;
        }
    }

    public static Boolean removeScrollBy_scrollName(User operate_user, Scroll sc) {
        String CURRENT_DIR = System.getProperty("user.dir");
        String DB_PATH = CURRENT_DIR + "/src/main/java/lab15/gabriel/group2/a2/Database/Scroll.db";
        String DB_URL = "jdbc:sqlite:" + DB_PATH;
        String SCROLLS_DIR = CURRENT_DIR + "/src/main/java/lab15/gabriel/group2/a2/ScrollsInLibrary/";

        String query = "DELETE FROM Scroll WHERE scrollName = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, sc.getScrollName());  // <-- Use sc.getName() here
            int deletedRows = pstmt.executeUpdate();

            if (deletedRows > 0) {
                // Now, delete the file from the filesystem
                File fileToDelete = new File(SCROLLS_DIR + sc.getScrollName());  // <-- Use sc.getName() here
                if (fileToDelete.exists()) {
                    if (fileToDelete.delete()) {
//                    System.out.println(sc.getName() + " file has been deleted.");  // <-- Use sc.getName() here
                    } else {
                        System.out.println("Failed to delete " + sc.getScrollName() + " file.");  // <-- Use sc.getName() here
                    }
                }
                System.out.println(sc.getScrollName() + " has been removed now\n");  // <-- Use sc.getName() here
                User currentUser = UserDB.user_detail(operate_user.getUserId());
                currentUser.deleteScrollToUser(sc);

                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




    // check the user is the uploader of the scroll or not
    private boolean isUploader(String userId) {
        return this.userId.equals(userId);
    }

    public boolean editScroll(String userId, String scrollID, String newContent){ // need the user input the newContent in the UI
        if (!isUploader(userId)){
            System.out.println("You are not the uploader of the scroll, only uploader has the permission to edit.");
            return false;
        }
        String CURRENT_DIR = System.getProperty("user.dir");
//        scrollName = scrollName + ".txt";
        String file = CURRENT_DIR + "/src/main/java/lab15/gabriel/group2/a2/ScrollsInLibrary/" + scrollName;
        File actualFile = new File(file);
        if (!actualFile.exists()) {// if the file does not exist, then print out an error message and return false
            System.out.println("Scroll does not exist.");
            return false;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(actualFile))) {
            writer.write(newContent.replace("\\n", System.lineSeparator()));
            ScrollDB.updateRow(scrollID,newContent.replace("\\n", System.lineSeparator()));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public boolean updateScrollName(String oldScrollName, String newScrollName, String userId) {

        // Check if the new scroll name already exists in the database
        String checkSQL = "SELECT COUNT(*) as nameCount FROM Scroll WHERE scrollName = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement checkStmt = conn.prepareStatement(checkSQL)) {

            checkStmt.setString(1, newScrollName);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt("nameCount") > 0) {
                System.out.println("The scroll name \"" + newScrollName + "\" already exists in the database. Please enter a unique name.");
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        String updateSQL = "UPDATE Scroll SET scrollName = ? WHERE scrollName = ? AND userId = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {

            updateStmt.setString(1, newScrollName);
            updateStmt.setString(2, oldScrollName);
            updateStmt.setString(3, userId);
            int updatedRows = updateStmt.executeUpdate();

            if (updatedRows == 0) {
                System.out.println("No scroll found with the specified name and user ID.");
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        String scrollFilePath = "src/main/java/lab15/gabriel/group2/a2/ScrollsInLibrary/";
        File oldFile = new File(scrollFilePath + oldScrollName);
        File newFile = new File(scrollFilePath + newScrollName);

        return oldFile.renameTo(newFile);

    }




    public static boolean downloadScroll(String scrollId, String userId){
        User currentUser = UserDB.user_detail(userId);
        Scanner scanner = new Scanner(System.in);
        String CURRENT_DIR = System.getProperty("user.dir");
        String downloadPath = CURRENT_DIR + "/src/main/java/lab15/gabriel/group2/a2/DownloadedScrolls/";
        Scroll scrollToBeDownloaded = ScrollDB.return_scroll_by_ID(scrollId);
        String fileContent = scrollToBeDownloaded.getContent();
        String fileName = scrollToBeDownloaded.getScrollName();

        if(currentUser.getUserType() == "guest"){
            System.out.printf("Guest users are not allowed to download scrolls!\n");
            return false;
        }
        else{
            // When the scroll is private, password is needed
            if(scrollToBeDownloaded.getType().equals("private")){
                System.out.printf("This scroll is private, please input the password to download:\n");
                String userInput = scanner.nextLine();
                if(userInput.equals(scrollToBeDownloaded.scrollPassword)){
                    System.out.printf("Password correct! Downloading...\n");
                    if(writeToFiles(downloadPath, fileName, fileContent)){
                        ScrollDB.updateDownloadCount(scrollId); // the download number of this scroll increase by 1
                        return true;
                    }
                    else {
                        return false;
                    }
                }
                else{
                    System.out.printf("Password incorrect!\n");
                    return false;
                }
            }
            // When the scroll is public, download without password
            else{
                if(writeToFiles(downloadPath, fileName, fileContent)){
                    ScrollDB.updateDownloadCount(scrollId); // the download number of this scroll increase by 1
                    return true;
                }
                else {
                    return false;
                }
            }
        }
    }
    public static boolean writeToFiles(String downloadPath, String fileName, String fileContent){
        File directory = new File(downloadPath);
        if (!directory.exists()) {
            directory.mkdirs();  // If the directory does not exist, create it
        }
        File file = new File( downloadPath + File.separator + fileName);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            // If the file does not exist, create it
            if (!file.exists()) {
                file.createNewFile();
            }

            bw.write(fileContent);  // write fi2le content
            System.out.println("Done writing to file: " + file.getAbsolutePath());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> filterAndDisplayScrolls() {
        List<String> scrolls = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Please choose the condition to filter the scroll:\n 1. UserID\n2. ScrollID\n3. ScrollName\n4. UploadDate");
            String choice = scanner.nextLine();

            String query = "SELECT * FROM Scroll WHERE ";
            List<String> params = new ArrayList<>();

            switch (choice) {
                case "1":
                    System.out.print("Please input the userId: ");
                    query += "userId = ?";
                    params.add(scanner.nextLine());
                    break;
                case "2":
                    System.out.print("Please input the scrollId: ");
                    query += "scrollId = ?";
                    params.add(scanner.nextLine());
                    break;
                case "3":
                    System.out.print("Please input the scrollName: ");
                    query += "scrollName = ?";
                    params.add(scanner.nextLine());
                    break;
                case "4":
                    System.out.print("Please input the uploadDate: ");
                    query += "uploadDate = ?";
                    params.add(scanner.nextLine());
                    break;
                default:
                    System.out.println("Invalid input.");
                    return scrolls;
            }

            PreparedStatement pstmt = conn.prepareStatement(query);
            for (int i = 0; i < params.size(); i++) {
                pstmt.setString(i + 1, params.get(i));
            }
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String scrollInfo = "Scroll ID: " + rs.getString("scrollId") + " | Scroll Name: " + rs.getString("scrollName") + rs.getString("uploadDate");
                scrolls.add(scrollInfo);
            }

            pstmt.close();
            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return scrolls;
    }



    public static void setFilterCheck() {
        filterCheck = true;
    }

    public static boolean getFilterCheck() {
        return filterCheck;
    }

    public static boolean getFilteredResult() {
        return filteredResult;
    }
}