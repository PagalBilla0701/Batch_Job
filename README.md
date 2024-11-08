import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

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
import org.springframework.test.util.ReflectionTestUtils;

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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveGenCallActivity() throws Exception {
        GenesysCallActivity mockGenCall = new GenesysCallActivity();
        when(callActivityRepository.saveGenesysCallActivity(mockGenCall)).thenReturn(mockGenCall);

        GenesysCallActivity result = callActivityAction.saveGenCallActivity(mockGenCall);

        assertNotNull(result);
        verify(callActivityRepository, times(1)).saveGenesysCallActivity(mockGenCall);
    }

    @Test
    public void testRemoveFromListOfAuthentications() {
        CallActivity call = new CallActivity();
        call.setAvailableAuth("auth1|auth2|auth3");
        call.setTwoFaVerified(true);

        String authMethod = "auth2";

        callActivityAction.removeFromListOfAuthentications(call, authMethod);

        List<String> expectedAuthList = Arrays.asList("auth1", "auth3");
        assertEquals(String.join("|", expectedAuthList), call.getAvailableAuth());
    }

    @Test
    public void testRemoveFromListOfAuthentications_NotVerified() {
        CallActivity call = new CallActivity();
        call.setAvailableAuth("auth1|auth2");
        call.setTwoFaVerified(false);

        String authMethod = "auth2";

        callActivityAction.removeFromListOfAuthentications(call, authMethod);

        assertEquals("auth1|auth2", call.getAvailableAuth());
    }

    @Test
    public void testGetGenesysCallActivityByRelId() throws Exception {
        String relId = "relId1";

        GenesysCallActivity mockGenCall = new GenesysCallActivity();
        when(callActivityRepository.getGenesysCallActivityByRelId(relId)).thenReturn(mockGenCall);

        GenesysCallActivity result = callActivityAction.getGenesysCallActivityByRelId(relId);

        assertNotNull(result);
        verify(callActivityRepository, times(1)).getGenesysCallActivityByRelId(relId);
    }

    @Test
    public void testSensitiveCustomerFlag() {
        String relId = "relId1";
        String countryCode = "US";
        String expectedFlag = "Y";

        when(callActivityRepository.getSenstiveCustomer(relId, countryCode)).thenReturn(expectedFlag);

        String result = callActivityAction.senstiveCustomerFlag(relId, countryCode);

        assertEquals(expectedFlag, result);
        verify(callActivityRepository, times(1)).getSenstiveCustomer(relId, countryCode);
    }
}
