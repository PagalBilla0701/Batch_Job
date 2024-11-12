import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.CallActivityRepository;
import com.example.VerificationScriptProxy;
import com.example.VerificationScriptProxyIE;
import com.example.CallActivityActionImpl;
import com.example.GenesysCallActivity;
import com.example.CallActivity;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
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

    @BeforeEach
    void setUp() {
        genCall = new GenesysCallActivity(); // Initialize with sample data as needed
        call = new CallActivity(); // Initialize with sample data as needed
    }

    @Test
    public void testSaveGenCallActivity() throws Exception {
        when(callActivityRepository.saveGenesysCallActivity(genCall)).thenReturn(genCall);
        GenesysCallActivity result = callActivityAction.saveGenCallActivity(genCall);
        assertEquals(genCall, result);
        verify(callActivityRepository).saveGenesysCallActivity(genCall);
    }

    @Test
    public void testRemoveFromListOfAuthentications() {
        String authMethod = "authMethod";
        call.setAvailableAuth("authMethod|anotherMethod");
        call.setTwoFaVerified(true);
        
        callActivityAction.removeFromListOfAuthentications(call, authMethod);

        assertEquals("anotherMethod", call.getAvailableAuth());
    }

    @Test
    public void testGetGenesysCallActivityByRelId() throws Exception {
        String relId = "12345";
        GenesysCallActivity mockGenesysCallActivity = new GenesysCallActivity();
        when(callActivityRepository.findByRelId(relId)).thenReturn(mockGenesysCallActivity);

        GenesysCallActivity result = callActivityAction.getGenesysCallActivityByRelId(relId);
        assertEquals(mockGenesysCallActivity, result);
        verify(callActivityRepository).findByRelId(relId);
    }

    @Test
    public void testSensitiveCustomerFlag() {
        String relId = "12345";
        String countryCode = "US";
        String expectedFlag = "Sensitive";
        
        when(callActivityRepository.getSenstiveCustomer(relId, countryCode)).thenReturn(expectedFlag);
        
        String result = callActivityAction.senstiveCustomerFlag(relId, countryCode);
        assertEquals(expectedFlag, result);
        verify(callActivityRepository).getSenstiveCustomer(relId, countryCode);
    }
}
