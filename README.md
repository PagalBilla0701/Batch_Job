import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ModelMap;

import java.util.HashMap;
import java.util.Map;

public class CallActivityControllerTest {

    @Mock
    private CallActivityService callActivityService;

    @InjectMocks
    private CallActivityController callActivityController; // Assuming this is where getMobileNumber is called from

    @Mock
    private Logger logger; // If you want to mock logging as well, otherwise remove this mock

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    public void testGetMobileNumber_withValidCustomerIdAndCountryCode() {
        // Arrange
        String customerId = "12345";
        String countryCode = "SG";
        Map<String, String> customerInfo = new HashMap<>();
        customerInfo.put("mobilePhoneNumber", "123-456-7890");
        
        when(callActivityService.getCustomerInfo(countryCode, customerId)).thenReturn(customerInfo);

        // Act
        String result = callActivityController.getMobileNumber(customerId, countryCode);

        // Assert
        assertEquals("123-456-7890", result);
    }

    @Test
    public void testGetMobileNumber_withCustomerNotFound() {
        // Arrange
        String customerId = "12345";
        String countryCode = "SG";
        
        when(callActivityService.getCustomerInfo(countryCode, customerId)).thenReturn(null);

        // Act
        String result = callActivityController.getMobileNumber(customerId, countryCode);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetMobileNumber_withEmptyCustomerId() {
        // Arrange
        String customerId = "";
        String countryCode = "SG";

        // Act
        String result = callActivityController.getMobileNumber(customerId, countryCode);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetMobileNumber_withNullCustomerId() {
        // Arrange
        String customerId = null;
        String countryCode = "SG";

        // Act
        String result = callActivityController.getMobileNumber(customerId, countryCode);

        // Assert
        assertNull(result);
    }
}
