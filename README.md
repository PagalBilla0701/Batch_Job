import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

@ExtendWith(MockitoExtension.class)
public class CallActivityActionImplTest {

    @InjectMocks
    private CallActivityActionImpl callActivityAction;

    @Mock
    private CallActivityRepository callActivityRepository;

    @Mock
    private VerificationScriptProxy proxy;

    @Mock
    private VerificationScriptProxyIE IEProxy;

    private GenesysCallActivity genCall;
    private CallActivity call;

    @BeforeEach
    public void setUp() {
        genCall = new GenesysCallActivity();
        call = new CallActivity();
    }

    @Test
    public void testSaveGenCallActivity() throws Exception {
        when(callActivityRepository.saveGenesysCallActivity(genCall)).thenReturn(genCall);

        GenesysCallActivity result = callActivityAction.saveGenCallActivity(genCall);

        assertNotNull(result);
        verify(callActivityRepository, times(1)).saveGenesysCallActivity(genCall);
    }

    @Test
    public void testRemoveFromListOfAuthentications() {
        call.setAvailableAuth("0005|Q011|Q013");
        String authMethod = "Q011";
        call.setTwoFaVerified(true);

        callActivityAction.removeFromListOfAuthentications(call, authMethod);

        assertEquals("0005|Q013", call.getAvailableAuth());
    }

    @Test
    public void testRemoveFromListOfAuthentications_NoAuthMethodFound() {
        call.setAvailableAuth("Q828");
        String authMethod = "Q021";
        call.setTwoFaVerified(true);

        callActivityAction.removeFromListOfAuthentications(call, authMethod);

        assertEquals("Q828", call.getAvailableAuth());
    }

    @Test
    public void testGetGenesysCallActivityByRelId() {
        String relId = "12345";

        GenesysCallActivity expectedGenCall = new GenesysCallActivity();
        when(callActivityRepository.getGenesysCallActivityByRelId(relId)).thenReturn(expectedGenCall);

        GenesysCallActivity result = callActivityAction.getGenesysCallActivityByRelId(relId);

        assertEquals(expectedGenCall, result);
        verify(callActivityRepository, times(1)).getGenesysCallActivityByRelId(relId);
    }

    @Test
    public void testSenstiveCustomerFlag() {
        String relId = "12345";
        String countryCode = "US";
        String expectedFlag = "Y";

        when(callActivityRepository.getSenstiveCustomer(relId, countryCode)).thenReturn(expectedFlag);

        String result = callActivityAction.senstiveCustomerFlag(relId, countryCode);

        assertEquals(expectedFlag, result);
        verify(callActivityRepository, times(1)).getSenstiveCustomer(relId, countryCode);
    }
}
