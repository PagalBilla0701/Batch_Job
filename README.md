import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CallActivityServiceImplTest {

    private CallActivityServiceImpl callActivityService = new CallActivityServiceImpl();

    @Test
    public void testGetAuthenticationType() {
        // Test case 1: Multiple authentication types separated by '|'
        String auth1 = "AUTH_A|AUTH_B";
        assertEquals("AUTH_A", callActivityService.getAuthenticationType(auth1));

        // Test case 2: Single authentication type
        String auth2 = "AUTH_A";
        assertEquals("AUTH_A", callActivityService.getAuthenticationType(auth2));

        // Test case 3: Empty string as input
        String auth3 = "";
        assertEquals("", callActivityService.getAuthenticationType(auth3));

        // Test case 4: String without '|'
        String auth4 = "SINGLE_AUTH";
        assertEquals("SINGLE_AUTH", callActivityService.getAuthenticationType(auth4));
    }
}
