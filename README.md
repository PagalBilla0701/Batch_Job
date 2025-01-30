import java.lang.reflect.Method;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class S2SOpportunityServiceImplTest {

    @Test
    public void testManageLeadData_SuccessfulResponse() throws Exception {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("headerData", "testHeader");

        int index = 12;

        // Create an instance of the service class
        S2SOpportunityServiceImpl service = new S2SOpportunityServiceImpl();

        // Use reflection to access the private method getSales2ServiceUrl
        Method method = S2SOpportunityServiceImpl.class.getDeclaredMethod("getSales2ServiceUrl", int.class);
        method.setAccessible(true); // Make the private method accessible

        // Mock the getSales2ServiceUrl method to return a dummy URL
        String serviceUrl = "http://example.com/service";
        when(method.invoke(service, index)).thenReturn(serviceUrl); // Mock invocation of the method

        // Mock the response entity
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("key", "value");

        ResponseEntity<Map> responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCodeValue()).thenReturn(200);
        when(responseEntity.getBody()).thenReturn(responseMap);

        // Mock RestTemplate behavior
        when(restTemplate.exchange(
                eq(new URI(serviceUrl)), // Ensure URI is correctly mocked
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Map.class))
        ).thenReturn(responseEntity);

        // Act
        Map<String, Object> response = service.manageLeadData(request, index);

        // Assert
        assertNotNull(response);
        assertEquals("value", response.get("key"));
    }
}
