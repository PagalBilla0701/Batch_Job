import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

@RunWith(MockitoJUnitRunner.class)
public class CallActivityActionImplTest {

    @Mock
    private CallActivityRepository callActivityRepository;

    @Mock
    private VerificationScriptProxy proxy;

    @Mock
    private VerificationScriptProxyIE IEProxy;

    @InjectMocks
    private CallActivityActionImpl callActivityAction;

    private GenesysCallActivity genCall;

    private CallActivity call;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        genCall = new GenesysCallActivity();
        genCall.setCallRefNo("123456789");  // Initialize with test data

        call = new CallActivity();
        call.setAvailableAuth("auth1|auth2|auth3");  // Set up available authentication methods
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
        // Assuming we are removing "auth2" from the list
        String authMethod = "auth2";
        call.setTwoFaVerified(true);

        callActivityAction.removeFromListOfAuthentications(call, authMethod);

        // Verify if "auth2" was removed from the list
        assertFalse(call.getAvailableAuth().contains(authMethod));
    }

    @Test
    public void testGetGenesysCallActivityByRelId() throws Exception {
        String relId = "12345";
        GenesysCallActivity expectedCallActivity = new GenesysCallActivity();
        when(callActivityRepository.findByRelId(relId)).thenReturn(expectedCallActivity);

        GenesysCallActivity result = callActivityAction.getGenesysCallActivityByRelId(relId);
        assertEquals(expectedCallActivity, result);
        verify(callActivityRepository, times(1)).findByRelId(relId);
    }

    @Test
    public void testSensitiveCustomerFlag() {
        String relId = "12345";
        String countryCode = "IN";
        String expectedFlag = "Y";
        when(callActivityRepository.getSenstiveCustomer(relId, countryCode)).thenReturn(expectedFlag);

        String result = callActivityAction.senstiveCustomerFlag(relId, countryCode);
        assertEquals(expectedFlag, result);
        verify(callActivityRepository, times(1)).getSenstiveCustomer(relId, countryCode);
    }
}
