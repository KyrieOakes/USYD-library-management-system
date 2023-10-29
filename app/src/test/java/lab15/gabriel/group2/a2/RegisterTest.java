package lab15.gabriel.group2.a2;
import lab15.gabriel.group2.a2.Database.UserDB;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import lab15.gabriel.group2.a2.Login_Register.Register;
import lab15.gabriel.group2.a2.rmFile;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest {

    @BeforeEach
    public void setUp() throws Exception {
        String[] args = null;
        rmFile.main(args);
        // Initialize database before every test (Assuming that UserDB provides an initialization method)
        UserDB.init_db();
    }


    @Test
    public void testSuccessfulRegistration() {
        Boolean result = Register.register("1234567890", "test@example.com", "Test User", "userID1", "testUsername", "testPassword");
        assertTrue(result);
    }

    @Test
    public void testDuplicateRegistration() {
        // First registration should be successful
        Boolean firstResult = Register.register("1234567890", "test@example.com", "Test User", "userID1", "testUsername", "testPassword");
        assertTrue(firstResult);

        // Second registration with the same details should fail
        Boolean secondResult = Register.register("1234567890", "test@example.com", "Test User", "userID1", "testUsername", "testPassword");
        assertFalse(secondResult);
    }

    @Test
    public void testExceptionInRegistration() {
    Boolean result = Register.register(null, "test@example.com", "Test User", "userID1", "testUsername", "testPassword");
        assertTrue(result);
    }
}

