import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

class OpportunityServiceTest {

    @Mock
    private OptyPitchRepo optyPitchRepo;

    @InjectMocks
    private OpportunityService opportunityService; // Replace with your actual service class name

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOpportunityListingPage() {
        // Mock request map
        Map<String, Object> request = new HashMap<>();
        request.put("queuetype", "Sales");
        request.put("countrycode", "US");

        // Mock repository response
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("opportunities", "Mocked Opportunity Data");

        // Define repository behavior
        when(optyPitchRepo.getOpportunityListingPage("Sales", "US")).thenReturn(mockResponse);

        // Call the method under test
        Map<String, Object> response = opportunityService.getOpportunityListingPage(request);

        // Verify the repository was called with correct parameters
        verify(optyPitchRepo, times(1)).getOpportunityListingPage("Sales", "US");

        // Assert the response
        assertNotNull(response);
        assertEquals("Mocked Opportunity Data", response.get("opportunities"));
    }
}
