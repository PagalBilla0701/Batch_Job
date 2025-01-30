import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;
import java.util.*;

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
    public void testManageLeadData_SuccessScenario() throws Exception {
        // Mock request and response
        Map<String, Object> request = new HashMap<>();
        request.put("headerData", "testHeader");
        request.put("key1", "value1");

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("responseKey", "responseValue");

        String serviceUrl = invokePrivateGetSales2ServiceUrl(1);
        when(restTemplate.postForEntity(eq(serviceUrl), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(new ResponseEntity<>(expectedResponse, HttpStatus.OK));

        // Mock getHeaderString method
        doReturn("mockHeader").when(service).getHeaderString(any());

        // Execute
        Map<String, Object> actualResponse = service.manageLeadData(request, 1);

        // Verify
        assertNotNull(actualResponse);
        assertEquals("responseValue", actualResponse.get("responseKey"));
        verify(restTemplate, times(1)).postForEntity(eq(serviceUrl), any(HttpEntity.class), eq(Map.class));
    }

    @Test(expected = Sales2ServiceRuntimeException.class)
    public void testManageLeadData_InvalidResponseStatus() throws Exception {
        // Mock request and response
        Map<String, Object> request = new HashMap<>();
        request.put("headerData", "testHeader");

        String serviceUrl = invokePrivateGetSales2ServiceUrl(1);
        when(restTemplate.postForEntity(eq(serviceUrl), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));

        // Mock getHeaderString method
        doReturn("mockHeader").when(service).getHeaderString(any());

        // Execute
        service.manageLeadData(request, 1);
    }

    @Test(expected = Sales2ServiceRuntimeException.class)
    public void testManageLeadData_IOException() throws Exception {
        // Mock request
        Map<String, Object> request = new HashMap<>();
        request.put("headerData", "testHeader");

        // Simulate an IOException when calling private method
        doThrow(new IOException("IO Error")).when(service).getHeaderString(any());

        // Execute
        service.manageLeadData(request, 1);
    }

    @Test(expected = Sales2ServiceRuntimeException.class)
    public void testManageLeadData_GenericException() throws Exception {
        // Mock request
        Map<String, Object> request = new HashMap<>();
        request.put("headerData", "testHeader");

        // Simulate a generic exception when calling private method
        doThrow(new Exception("Generic Error")).when(service).getHeaderString(any());

        // Execute
        service.manageLeadData(request, 1);
    }

    @Test
    public void testPrivateGetSales2ServiceUrl() throws Exception {
        int index = 1;

        // Call private method using reflection
        String serviceUrl = invokePrivateGetSales2ServiceUrl(index);

        // Verify output
        assertNotNull(serviceUrl);
        System.out.println("Service URL: " + serviceUrl);
    }

    // Utility method to invoke the private method using reflection
    private String invokePrivateGetSales2ServiceUrl(int index) throws Exception {
        Method method = S2SOpportunityServiceImpl.class.getDeclaredMethod("getSales2ServiceUrl", int.class);
        method.setAccessible(true);
        return (String) method.invoke(service, index);
    }
}
