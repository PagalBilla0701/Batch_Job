import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

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
    public void testManageLeadData_SuccessfulResponse() throws Exception {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("headerData", "testHeader");

        int index = 12;
        String serviceUrl = "http://example.com/service";

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("key", "value");

        ResponseEntity<Map> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCodeValue()).thenReturn(200);
        when(responseEntity.getBody()).thenReturn(responseMap);

        when(restTemplate.exchange(
                any(URI.class), 
                eq(HttpMethod.GET), 
                any(HttpEntity.class), 
                eq(Map.class)))
            .thenReturn(responseEntity);

        // Act
        Map<String, Object> response = service.manageLeadData(request, index);

        // Assert
        assertNotNull(response);
        assertEquals("value", response.get("key"));
    }

    @Test(expected = Sales2ServiceRuntimeException.class)
    public void testManageLeadData_InvalidResponse() throws Exception {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        int index = 12;
        String serviceUrl = "http://example.com/service";

        ResponseEntity<Map> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCodeValue()).thenReturn(500);

        when(restTemplate.exchange(
                any(URI.class), 
                eq(HttpMethod.GET), 
                any(HttpEntity.class), 
                eq(Map.class)))
            .thenReturn(responseEntity);

        // Act
        service.manageLeadData(request, index);
    }

    @Test(expected = Sales2ServiceRuntimeException.class)
    public void testManageLeadData_IOException() throws Exception {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        int index = 12;

        when(restTemplate.exchange(
                any(URI.class), 
                eq(HttpMethod.GET), 
                any(HttpEntity.class), 
                eq(Map.class)))
            .thenThrow(new IOException("Test IOException"));

        // Act
        service.manageLeadData(request, index);
    }

    @Test(expected = Sales2ServiceRuntimeException.class)
    public void testManageLeadData_GenericException() throws Exception {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        int index = 12;

        when(restTemplate.exchange(
                any(URI.class), 
                eq(HttpMethod.GET), 
                any(HttpEntity.class), 
                eq(Map.class)))
            .thenThrow(new Exception("Test Exception"));

        // Act
        service.manageLeadData(request, index);
    }
}
