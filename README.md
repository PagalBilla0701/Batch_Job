import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService; // Replace with your actual class name containing the method

    @Mock
    private CallActivityEDMIService callActivityEDMIService; // Mocking the dependency

    @Mock
    private IEProxy IEProxy; // Mocking the dependency

    @Mock
    private Proxy proxy; // Mocking the dependency

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIdentifyAccountOrCustomerId() {
        // Mock inputs
        String countryCodeSG = "SG";
        String countryCodeIN = "IN";
        String countryCodeAE = "AE";
        String accountNumber = "123456";

        // Mock outputs
        Map<String, Object> mockResponseSG = new HashMap<>();
        Map<String, String> respMapSG = new HashMap<>();
        respMapSG.put("customerId", "CUST123");
        mockResponseSG.put("resp", respMapSG);

        Map<String, Object> mockResponseAE = new HashMap<>();
        Map<String, String> respMapAE = new HashMap<>();
        respMapAE.put("customerId", "CUST456");
        mockResponseAE.put("resp", respMapAE);

        // Mock behavior for SG and IN
        when(callActivityEDMIService.getCustomerAcctRowID(countryCodeSG, accountNumber)).thenReturn(mockResponseSG);
        when(callActivityEDMIService.getCustomerAcctRowID(countryCodeIN, accountNumber)).thenReturn(mockResponseSG);

        // Mock behavior for AE
        when(IEProxy.getAnswer(eq(countryCodeAE), eq("ACCTINFO"), any(String[].class))).thenReturn(mockResponseAE);

        // Mock behavior for other countries
        when(proxy.getAnswer(eq("US"), eq("ACCTINFO"), any(String[].class))).thenReturn(null);

        // Test for SG
        Map<String, String> resultSG = accountService.identifyAccountOrCustomerId(countryCodeSG, accountNumber);
        assertNotNull(resultSG);
        assertEquals("CUST123", resultSG.get("customerId"));

        // Test for AE
        Map<String, String> resultAE = accountService.identifyAccountOrCustomerId(countryCodeAE, accountNumber);
        assertNotNull(resultAE);
        assertEquals("CUST456", resultAE.get("customerId"));

        // Test for other countries
        Map<String, String> resultOther = accountService.identifyAccountOrCustomerId("US", accountNumber);
        assertNull(resultOther);
    }
}
