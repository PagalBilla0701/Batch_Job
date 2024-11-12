import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class IVRCtomResponseEntityServiceTest {

    @Mock
    private ParamRepository paramRepository;

    @Mock
    private CtomOAuthTokenGenerator ctomOAuthTokenGenerator;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private IVRCtomResponseEntityService ivrCtomResponseEntityService;

    private Map<String, Object> ctomRequestBody;
    private String idParam = "id123";
    private String xParamKey1 = "testKey1";
    private String xParamKey2 = "testKey2";
    private String countryCodeForParam = "US";

    @Before
    public void setUp() {
        ctomRequestBody = new HashMap<>();
        ctomRequestBody.put("countryCode", countryCodeForParam);
    }

    @Test
    public void testGetReponseEntityforCTOM() throws Exception {
        // Arrange
        Map<String, String> mockParamData = new HashMap<>();
        mockParamData.put("service_url", "http://example.com/api");

        // Mock the behavior of getCTOMEndPointURL to return mock data
        when(ivrCtomResponseEntityService.getCTOMEndPointURL(xParamKey1, xParamKey2, countryCodeForParam, idParam))
                .thenReturn(mockParamData);

        // Mock the behavior of CtomOAuthTokenGenerator to return a dummy access token
        when(ctomOAuthTokenGenerator.getAccessToken(mockParamData)).thenReturn("dummyAccessToken");

        // Mock RestTemplate to simulate an HTTP POST request and return a successful response
        ResponseEntity<String> mockResponseEntity = mock(ResponseEntity.class);
        when(mockResponseEntity.getBody()).thenReturn("Mocked response body");
        when(restTemplate.postForEntity(any(URI.class), any(HttpEntity.class), eq(String.class)))
                .thenReturn(mockResponseEntity);

        // Act
        ResponseEntity<String> response = ivrCtomResponseEntityService.getReponseEntityforCTOM(ctomRequestBody, idParam, xParamKey1, xParamKey2, countryCodeForParam);

        // Assert
        assertNotNull(response);
        assertEquals("Mocked response body", response.getBody());

        // Verify interactions
        verify(restTemplate, times(1)).postForEntity(any(URI.class), any(HttpEntity.class), eq(String.class));
        verify(ctomOAuthTokenGenerator, times(1)).getAccessToken(mockParamData);
    }
}
