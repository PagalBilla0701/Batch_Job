import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

public class CallActivityServiceImplTest {

    @InjectMocks
    private CallActivityServiceImpl callActivityService;

    @Mock
    private VerificationScriptProxy proxy;

    @Mock
    private VerificationScriptProxyIE IEProxy;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCustomerInfo() {
        // Mock data for "AE" countryCode
        Map<String, Object> aeResponse = new HashMap<>();
        aeResponse.put("resp", new HashMap<String, String>() {{
            put("key", "valueAE");
        }});

        when(IEProxy.getAnswer(eq("AE"), eq("CUSTINFO"), any(String[].class)))
                .thenReturn(aeResponse);

        // Mock data for "MY" countryCode
        Map<String, Object> myResponse = new HashMap<>();
        myResponse.put("resp", new HashMap<String, String>() {{
            put("key", "valueMY");
        }});

        when(proxy.getAnswer(eq("MY"), eq("CUSTINFOMY"), any(String[].class)))
                .thenReturn(myResponse);

        // Mock data for other country codes
        Map<String, Object> otherResponse = new HashMap<>();
        otherResponse.put("resp", new HashMap<String, String>() {{
            put("key", "valueOther");
        }});

        when(proxy.getAnswer(eq("IN"), eq("CUSTINFO"), any(String[].class)))
                .thenReturn(otherResponse);

        // Test case for "AE"
        Map<String, String> resultAE = callActivityService.getCustomerInfo("AE", "12345");
        assertNotNull(resultAE);
        assertEquals("valueAE", resultAE.get("key"));

        // Test case for "MY"
        Map<String, String> resultMY = callActivityService.getCustomerInfo("MY", "67890");
        assertNotNull(resultMY);
        assertEquals("valueMY", resultMY.get("key"));

        // Test case for other country code "IN"
        Map<String, String> resultOther = callActivityService.getCustomerInfo("IN", "99999");
        assertNotNull(resultOther);
        assertEquals("valueOther", resultOther.get("key"));

        // Test case for null response
        when(proxy.getAnswer(eq("IN"), eq("CUSTINFO"), any(String[].class)))
                .thenReturn(null);
        Map<String, String> resultNull = callActivityService.getCustomerInfo("IN", "11111");
        assertNull(resultNull);

        // Test case for response with no "resp" key
        Map<String, Object> emptyResponse = new HashMap<>();
        when(proxy.getAnswer(eq("IN"), eq("CUSTINFO"), any(String[].class)))
                .thenReturn(emptyResponse);
        Map<String, String> resultEmpty = callActivityService.getCustomerInfo("IN", "22222");
        assertNull(resultEmpty);
    }
}
