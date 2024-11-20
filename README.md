import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CallActivityControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CallActivityController callActivityController;

    @Mock
    private CallActivityService callActivityService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(callActivityController).build();
    }

    @Test
    public void testGetMobileNumberForotp_Success() throws Exception {
        // Arrange
        String customerId = "12345";
        String countryCode = "91";
        String expectedMobileNumber = "+911234567890";

        // Mocking the service layer
        Map<String, String> customerInfo = new HashMap<>();
        customerInfo.put("mobilePhoneNumber", expectedMobileNumber);
        Mockito.when(callActivityService.getCustomerInfo(countryCode, customerId))
               .thenReturn(customerInfo);

        // Act & Assert
        mockMvc.perform(post("/genesys-get-mobileNumber.do")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("customerId", customerId)
                        .param("countryCode", countryCode))
               .andExpect(status().isOk())
               .andExpect(content().string(expectedMobileNumber));
    }

    @Test
    public void testGetMobileNumberForotp_CustomerNotFound() throws Exception {
        // Arrange
        String customerId = "12345";
        String countryCode = "91";

        // Mocking the service layer to return null
        Mockito.when(callActivityService.getCustomerInfo(countryCode, customerId))
               .thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/genesys-get-mobileNumber.do")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("customerId", customerId)
                        .param("countryCode", countryCode))
               .andExpect(status().isOk())
               .andExpect(content().string(""));
    }

    @Test
    public void testGetMobileNumberForotp_InvalidCustomerId() throws Exception {
        // Arrange
        String customerId = "";
        String countryCode = "91";

        // Act & Assert
        mockMvc.perform(post("/genesys-get-mobileNumber.do")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("customerId", customerId)
                        .param("countryCode", countryCode))
               .andExpect(status().isOk())
               .andExpect(content().string(""));
    }
}
