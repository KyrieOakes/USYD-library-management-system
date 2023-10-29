package lab15.gabriel.group2.a2;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
public class rmFile_test {
    private final Path userDbPath = Paths.get("src/main/java/lab15/gabriel/group2/a2/Database/UserDB.db");
    private final Path scrollDbPath = Paths.get("src/main/java/lab15/gabriel/group2/a2/Database/Scroll.db");

    @BeforeEach
    public void tearDown() throws IOException {
        // Clean up, just in case the test didn't work and the files are still present
        if (!(Files.exists(userDbPath)) ){
            Files.createFile(userDbPath);
        }
        if (!Files.exists(scrollDbPath)) {
            Files.createFile(scrollDbPath);
        }
    }

    @Test
    public void testRmFile() {
        // Before running rmFile, both files should exist
        assertTrue(Files.exists(userDbPath));
        assertTrue(Files.exists(scrollDbPath));

        rmFile.main(new String[]{});  // Execute the main method of rmFile class

        // After running rmFile, both files should not exist
        assertFalse(Files.exists(userDbPath));
        assertFalse(Files.exists(scrollDbPath));
    }
}
