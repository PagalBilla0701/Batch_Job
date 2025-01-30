import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import java.lang.reflect.Method;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
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
    public void testGetSales2ServiceUrl() throws Exception {
        // Mock Param object
        Param mockParam = new Param("L9993");
        mockParam.setKeys(new String[] {
            "SALES_OPPORTUNITY_URL",
            "SALES_PITCHING_URL",
            "SALES_CALL_SUMMARY_URL"
        });
        mockParam.setData(new String[] {
            "http://opportunity.com",
            "http://pitching.com",
            "http://summary.com"
        });

        // Mock repository response
        when(paramRepository.getParam(any(Param.class))).thenReturn(mockParam);

        // Use reflection to access the private method
        Method method = S2SOpportunityServiceImpl.class.getDeclaredMethod("getSales2ServiceUrl", int.class);
        method.setAccessible(true);

        // Call the private method with reflection
        String url = (String) method.invoke(service, 0); // Passing 0 as the index

        // Verify
        assertNotNull(url);
        assertEquals("http://opportunity.com", url);
    }
}
