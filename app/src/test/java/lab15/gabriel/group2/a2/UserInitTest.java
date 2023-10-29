package lab15.gabriel.group2.a2;
import lab15.gabriel.group2.a2.Database.UserDB;
import lab15.gabriel.group2.a2.user_accounts.User_init;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserInitTest {
    @Test
    public void testEncryptPassword() {
        // A known password and its corresponding SHA-256 encrypted value
        String password = "testPassword";
        String expectedEncryptedValue = "fd5cb51bafd60f6fdbedde6e62c473da6f247db271633e15919bab78a02ee9eb";

        String actualEncryptedValue = User_init.encryptPassword(password);

        assertEquals(expectedEncryptedValue, actualEncryptedValue, "Encrypted value did not match expected value.");
    }

}
