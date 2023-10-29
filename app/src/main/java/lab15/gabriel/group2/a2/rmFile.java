package lab15.gabriel.group2.a2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class rmFile {
    // This file is used to delete .db files after each run, this file will be ignored in the test.
    public static void main(String[] args) {
        deleteFile("src/main/java/lab15/gabriel/group2/a2/Database/UserDB.db");
        deleteFile("src/main/java/lab15/gabriel/group2/a2/Database/User_scroll_list.db");
        deleteFile("src/main/java/lab15/gabriel/group2/a2/Database/Scroll.db");

        deleteTxtFilesInFolder("src/main/java/lab15/gabriel/group2/a2/ScrollsInLibrary");
    }

    public static void deleteFile(String filePath) {
        Path path = Paths.get(filePath);

        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                System.err.println("Error occurred while deleting " + path.getFileName() + ": " + e.getMessage());
            }
        } else {
            System.out.println(path.getFileName() + " does not exist.");
        }
    }

    public static void deleteTxtFilesInFolder(String folderPath) {
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Provided path is not a valid directory.");
            return;
        }

        File[] listOfFiles = folder.listFiles();

        if (listOfFiles == null) {
            System.out.println("No files found in the directory.");
            return;
        }

        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                if (file.delete()) {
                    System.out.println("Deleted: " + file.getName());
                } else {
                    System.out.println("Failed to delete: " + file.getName());
                }
            }
        }
    }
}
