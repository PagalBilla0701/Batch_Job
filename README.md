import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class S2SOpportunityServiceImplTest {

    @InjectMocks
    private S2SOpportunityServiceImpl service; // Ensure this service is non-static for proper testing

    @Mock
    private ObjectMapper mapper;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    public void testLogJson() {
        // Arrange
        Object request = new Object();
        Object payload = new Object();

        try {
            when(mapper.writeValueAsString(request)).thenReturn("{\"request\":\"value\"}");
            when(mapper.writeValueAsString(payload)).thenReturn("{\"payload\":\"value\"}");

            // Act
            service.logJson(request, payload);

            // Assert
            verify(mapper, times(1)).writeValueAsString(request);
            verify(mapper, times(1)).writeValueAsString(payload);

        } catch (JsonProcessingException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }
}
