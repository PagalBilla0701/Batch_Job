import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Method;

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
        // Create and initialize the `Param` object with proper arrays
        Param mockParam = mock(Param.class);
        when(mockParam.getKeys()).thenReturn(new String[] {
            "SALES_OPPORTUNITY_URL",
            "SALES_PITCHING_URL",
            "SALES_CALL_SUMMARY_URL"
        });
        when(mockParam.getData()).thenReturn(new String[] {
            "http://opportunity.com",
            "http://pitching.com",
            "http://summary.com"
        });

        // Mock the `paramRepository.getParam()` method
        when(paramRepository.getParam(any(Param.class))).thenReturn(mockParam);

        // Use reflection to access the private method
        Method method = S2SOpportunityServiceImpl.class.getDeclaredMethod("getSales2ServiceUrl", int.class);
        method.setAccessible(true);

        // Test for index 0
        String url = (String) method.invoke(service, 0);
        assertNotNull(url);
        assertEquals("http://opportunity.com", url);

        // Test for index 1
        url = (String) method.invoke(service, 1);
        assertNotNull(url);
        assertEquals("http://pitching.com", url);

        // Test for index 2
        url = (String) method.invoke(service, 2);
        assertNotNull(url);
        assertEquals("http://summary.com", url);

        // Verify that `paramRepository.getParam()` was called once
        verify(paramRepository, times(1)).getParam(any(Param.class));
    }
}
