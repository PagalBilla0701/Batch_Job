import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CallActivityTest {

    @Test
    public void testSetFailedHistory_WithSuccessAndFailed() {
        // Arrange
        String success = "SUCCESS_AUTH";
        String failed = "FAILED_AUTH1|FAILED_AUTH2";

        // Act
        String result = new CallActivityController().setFailedHistory(success, failed);

        // Assert
        assertEquals("SUCCESS_AUTH,FAILED_AUTH1|F,FAILED_AUTH2|F", result);
    }

    @Test
    public void testSetFailedHistory_OnlySuccess() {
        // Arrange
        String success = "SUCCESS_AUTH";
        String failed = null;

        // Act
        String result = new CallActivityController().setFailedHistory(success, failed);

        // Assert
        assertEquals("SUCCESS_AUTH", result);
    }

    @Test
    public void testSetFailedHistory_OnlyFailed() {
        // Arrange
        String success = null;
        String failed = "FAILED_AUTH1|FAILED_AUTH2";

        // Act
        String result = new CallActivityController().setFailedHistory(success, failed);

        // Assert
        assertEquals("FAILED_AUTH1|F,FAILED_AUTH2|F", result);
    }

    @Test
    public void testSetFailedHistory_EmptyFailed() {
        // Arrange
        String success = "SUCCESS_AUTH";
        String failed = "";

        // Act
        String result = new CallActivityController().setFailedHistory(success, failed);

        // Assert
        assertEquals("SUCCESS_AUTH", result);
    }

    @Test
    public void testSetFailedHistory_EmptySuccessAndFailed() {
        // Arrange
        String success = null;
        String failed = "";

        // Act
        String result = new CallActivityController().setFailedHistory(success, failed);

        // Assert
        assertEquals(null, result);
    }
}
