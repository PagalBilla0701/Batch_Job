import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UpdatePreferredLanguageTest {

    @InjectMocks
    private YourServiceClass yourService; // Replace with your actual class name

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ParamRepository paramRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUpdatePreferredLanguage_Success() throws Exception {
        // Mock input
        String countryCode = "US";
        String custId = "123456";
        String userId = "testUser";

        // Mock Param response
        Param mockParam = new Param("IVR01");
        mockParam.setCountryCode(countryCode);
        when(paramRepository.getParam(any(Param.class))).thenReturn(new Param("IVR01", new String[] {
            null, null, null, null, null, null, "http://mock-api.com", "/path"
        }));

        // Mock API response
        Map<String, Object> mockResponseBody = new HashMap<>();
        mockResponseBody.put("status", "Success");

        ResponseEntity<Map> mockResponse = new ResponseEntity<>(mockResponseBody, HttpStatus.OK);
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.DELETE), any(), eq(Map.class)))
            .thenReturn(mockResponse);

        // Call the method
        String result = yourService.updatePreferredLanguage(countryCode, custId, userId);

        // Verify
        assertEquals("Success", result);
        verify(restTemplate, times(1))
            .exchange(any(URI.class), eq(HttpMethod.DELETE), any(), eq(Map.class));
    }

    @Test(expected = Exception.class)
    public void testUpdatePreferredLanguage_Exception() throws Exception {
        // Mock input
        String countryCode = "US";
        String custId = "123456";
        String userId = "testUser";

        // Mock Param response
        Param mockParam = new Param("IVR01");
        mockParam.setCountryCode(countryCode);
        when(paramRepository.getParam(any(Param.class))).thenReturn(new Param("IVR01", new String[] {
            null, null, null, null, null, null, "http://mock-api.com", "/path"
        }));

        // Mock API exception
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.DELETE), any(), eq(Map.class)))
            .thenThrow(new RuntimeException("API Error"));

        // Call the method
        yourService.updatePreferredLanguage(countryCode, custId, userId);
    }
}
