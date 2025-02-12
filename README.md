import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class YourServiceTest {

    @Mock
    private ParamRepository paramRepository;  // Mocked dependency

    @InjectMocks
    private YourService yourService;  // Replace 'YourService' with actual class

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetLMSPageSize_HappyPath_And_NullResults() {
        // Mock valid response
        Param mockResult = mock(Param.class);
        when(paramRepository.getParam(any(Param.class))).thenReturn(mockResult);
        when(mockResult.getData()).thenReturn(new String[]{"10", "20", "30"});  // Simulated values

        // Test valid index case
        assertEquals(20, yourService.getLMSPageSize(1));  // Should return "20"

        // Test null results scenario (default value should be returned)
        when(paramRepository.getParam(any(Param.class))).thenReturn(null);
        assertEquals(15, yourService.getLMSPageSize(0));  // Default value when results are null
    }
}
