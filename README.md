import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class S2SOpportunityServiceImplTest {

    @InjectMocks
    private S2SOpportunityServiceImpl service;

    @Mock
    private OpportunityPitchEntityDataRepository optyPitchRepo;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ParamRepository paramRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper mapper;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testManageLeadData_Success_GET() throws Exception {
        // Mock Input Data
        Map<String, Object> request = new HashMap<>();
        request.put("headerData", "testHeader");

        int index = 12;
        String serviceUrl = "http://test.com";

        // Mock Dependencies
        HttpHeaders headers = new HttpHeaders();
        headers.add("s2sHeader", "testHeader");

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("key", "value");

        ResponseEntity<Map> mockResponse = new ResponseEntity<>(expectedResponse, HttpStatus.OK);

        when(service.getSales2ServiceUrl(index)).thenReturn(serviceUrl);
        when(restTemplate.exchange(
                eq(new URI(serviceUrl)),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Map.class)
        )).thenReturn(mockResponse);

        // Execute Method
        Map<String, Object> response = service.manageLeadData(request, index);

        // Verify
        assertNotNull(response);
        assertEquals("value", response.get("key"));
    }

    @Test
    public void testManageLeadData_Success_POST() throws Exception {
        // Mock Input Data
        Map<String, Object> request = new HashMap<>();
        request.put("key", "value");

        int index = 10;
        String serviceUrl = "http://test.com";

        // Mock Dependencies
        HttpHeaders headers = new HttpHeaders();
        headers.add("s2sHeader", "testHeader");

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("key", "value");

        ResponseEntity<Map> mockResponse = new ResponseEntity<>(expectedResponse, HttpStatus.OK);

        when(service.getSales2ServiceUrl(index)).thenReturn(serviceUrl);
        when(restTemplate.postForEntity(
                eq(serviceUrl),
                any(HttpEntity.class),
                eq(Map.class)
        )).thenReturn(mockResponse);

        // Execute Method
        Map<String, Object> response = service.manageLeadData(request, index);

        // Verify
        assertNotNull(response);
        assertEquals("value", response.get("key"));
    }

    @Test(expected = Sales2ServiceRuntimeException.class)
    public void testManageLeadData_ServiceException() throws Exception {
        // Mock Input Data
        Map<String, Object> request = new HashMap<>();
        request.put("headerData", "testHeader");

        int index = 12;
        String serviceUrl = "http://test.com";

        // Mock Dependencies
        when(service.getSales2ServiceUrl(index)).thenReturn(serviceUrl);
        when(restTemplate.exchange(
                eq(new URI(serviceUrl)),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Map.class)
        )).thenThrow(new RuntimeException("Mocked exception"));

        // Execute Method
        service.manageLeadData(request, index);
    }
}
