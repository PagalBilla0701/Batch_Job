import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.Map;

public class TransferPointsTest {

    @Test
    public void testGetTransferPointsMap() {
        // Arrange
        TransferPoints transferPoints = new TransferPoints(); // Assuming your class is named TransferPoints

        // Act
        Map<String, String> result = transferPoints.getTransferPointsMap();

        // Assert
        Map<String, String> expected = new HashMap<String, String>();
        expected.put("Start", "Start Menu");
        expected.put("MM", "Main menu");
        expected.put("ARE", "Account related enquiries");
        expected.put("PBM", "Phone banking menu");
        expected.put("BE", "Balance Enquiry");
        expected.put("CASA", "CASA menu");
        expected.put("CCPL", "CCPL Menu");

        assertEquals("The transfer points map does not match the expected values", expected, result);
    }
}



import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.List;

public class TransferPointsTest {

    @Test
    public void testGetTransferPoints() {
        // Arrange
        TransferPoints transferPoints = new TransferPoints(); // Assuming your class is named TransferPoints

        // Act
        List<String> result = transferPoints.getTransferPoints();

        // Assert
        List<String> expected = Arrays.asList("START", "MM", "ARE", "PBM", "BE", "CASA", "CCPL");
        assertEquals("The transfer points list does not match the expected values", expected, result);
    }
}
