import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import java.lang.reflect.Method;
import java.util.Map;

public class S2SOpportunityServiceImplTest {

    @InjectMocks
    private S2SOpportunityServiceImpl service;

    @Mock
    private ParamRepository paramRepository;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetLMSEndPointeUrl() throws Exception {
        // Arrange
        String paramKey = "testKey";

        // Mock the ParamRepository to return a Param object
        Param mockParam = mock(Param.class);
        when(mockParam.getKeys()).thenReturn(new String[]{paramKey});
        when(paramRepository.getParam(any(Param.class))).thenReturn(mockParam);
        String[] mockData = new String[]{"GET", "http://example.com", "/path", "/more", "end", "params", "and", "more", "", "", ""};
        when(mockParam.getData()).thenReturn(mockData);

        // Using reflection to access the private method
        Method method = S2SOpportunityServiceImpl.class.getDeclaredMethod("getLMSEndPointeUrl", String.class);
        method.setAccessible(true);

        // Act
        Map<String, String> result = (Map<String, String>) method.invoke(service, paramKey);

        // Assert
        assertNotNull(result);
        assertEquals("httpMethod", result.get("httpMethod"));
        assertEquals("GET", result.get("httpMethod"));
        assertTrue(result.get("serviceUrl").startsWith("http://example.com"));
    }
}
