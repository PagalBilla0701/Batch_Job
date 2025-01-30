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
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class S2SOpportunityServiceImplTest {

    @InjectMocks
    @Spy
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
        service = spy(new S2SOpportunityServiceImpl()); // Ensure the service is spied
    }

    @Test
    public void testManageLeadData_SuccessScenario() throws Exception {
        // Mock request and response
        Map<String, Object> request = new HashMap<>();
        request.put("headerData", "testHeader");
        request.put("key1", "value1");

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("responseKey", "responseValue");

        // Mock the serviceUrl using reflection
        String serviceUrl = invokePrivateGetSales2ServiceUrl(1);

        // Stub RestTemplate response
        when(restTemplate.postForEntity(eq(serviceUrl), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(new ResponseEntity<>(expectedResponse, HttpStatus.OK));

        // Stub getHeaderString method
        doReturn("mockHeader").when(service).getHeaderString(any());

        // Execute
        Map<String, Object> actualResponse = service.manageLeadData(request, 1);

        // Verify
        assertNotNull(actualResponse);
        assertEquals("responseValue", actualResponse.get("responseKey"));
        verify(restTemplate, times(1)).postForEntity(eq(serviceUrl), any(HttpEntity.class), eq(Map.class));
    }

    // Utility method to invoke private method using reflection
    private String invokePrivateGetSales2ServiceUrl(int index) throws Exception {
        Method method = S2SOpportunityServiceImpl.class.getDeclaredMethod("getSales2ServiceUrl", int.class);
        method.setAccessible(true);
        return (String) method.invoke(service, index);
    }
}
