import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

class ButtonServiceTest {

    @InjectMocks
    private ButtonService buttonService; // Assuming your method belongs to this service class

    @Mock
    private CallActivity callActivity;

    @Test
    void testGetButtons_withOneFaAndTwoFaVerified() {
        MockitoAnnotations.openMocks(this);

        // Mock callActivity behavior
        CallActivity.Id callId = mock(CallActivity.Id.class);
        when(callId.getCountryCode()).thenReturn("US");
        when(callActivity.getId()).thenReturn(callId);
        when(callActivity.isOneFaVerifed()).thenReturn(true);
        when(callActivity.isTwoFaVerified()).thenReturn(true);
        when(callActivity.getOneFa()).thenReturn("2+1");
        when(callActivity.getTwoFa()).thenReturn("OTHER");

        // Call the method under test
        Map<String, String> buttons = buttonService.getButtons(callActivity);

        // Verify the result
        assertEquals("Success", buttons.get("twoPlusOne"));
        assertEquals("Success", buttons.get("OTHER"));
    }

    @Test
    void testGetButtons_withOnlyOneFaVerified() {
        MockitoAnnotations.openMocks(this);

        // Mock callActivity behavior
        CallActivity.Id callId = mock(CallActivity.Id.class);
        when(callId.getCountryCode()).thenReturn("US");
        when(callActivity.getId()).thenReturn(callId);
        when(callActivity.isOneFaVerifed()).thenReturn(true);
        when(callActivity.isTwoFaVerified()).thenReturn(false);
        when(callActivity.getOneFa()).thenReturn("OTHER");

        // Call the method under test
        Map<String, String> buttons = buttonService.getButtons(callActivity);

        // Verify the result
        assertEquals("Success", buttons.get("OTHER"));
        assertEquals(null, buttons.get("twoPlusOne")); // Should not be set
    }

    @Test
    void testGetButtons_withNoFaVerified() {
        MockitoAnnotations.openMocks(this);

        // Mock callActivity behavior
        CallActivity.Id callId = mock(CallActivity.Id.class);
        when(callId.getCountryCode()).thenReturn("US");
        when(callActivity.getId()).thenReturn(callId);
        when(callActivity.isOneFaVerifed()).thenReturn(false);
        when(callActivity.isTwoFaVerified()).thenReturn(false);

        // Call the method under test
        Map<String, String> buttons = buttonService.getButtons(callActivity);

        // Verify the result
        assertEquals(null, buttons.get("twoPlusOne")); // Should not be set
        assertEquals(null, buttons.get("OTHER")); // Should not be set
    }
}
