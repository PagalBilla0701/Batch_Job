import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

public class CallActivityControllerTest {

    @InjectMocks
    private CallActivityController callActivityController; // Replace with your actual controller class name

    @Mock
    private CallActivityAction callActivityAction;

    @Mock
    private CallActivityService callActivityService;

    @Mock
    private LoginBean login;

    @Mock
    private CallActivity call;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(login.getUserBean().getCountryShortDesc()).thenReturn("CountryCode");
    }

    @Test
    public void testGenesysTransfer_SuccessfulCall() throws Exception {
        String callRefNo = "AB12345";
        String expectedRefNo = "12345";
        String customerId = "customer123";

        // Mock CallActivity object and its behavior
        when(callActivityAction.getCallActivityByRefNo(expectedRefNo, "CountryCode")).thenReturn(call);
        when(call.getLang()).thenReturn("EN");
        when(call.getCustomerSegment()).thenReturn("Segment");
        when(call.getCallerId()).thenReturn("CallerId");
        when(call.getDnis()).thenReturn("Dnis");
        when(call.getIdntfTyp()).thenReturn("IDType");
        when(call.getIdBlockcode()).thenReturn("BlockCode");
        when(call.getAuthBlockCode()).thenReturn("AuthBlockCode");
        when(call.getSelfSrvcCode()).thenReturn("SelfServiceCode");
        when(call.getAvailableAuth()).thenReturn("AvailableAuth");
        when(call.getCustIdIVR()).thenReturn("CustIdIVR");
        when(call.getLastMobno()).thenReturn("MobileNumber");
        when(call.getRmn()).thenReturn("RMN");

        Map<String, String> transferPoints = new HashMap<>();
        transferPoints.put("Point1", "TransferPoint1");
        transferPoints.put("Point2", "TransferPoint2");

        when(callActivityService.getTransferPointsMap()).thenReturn(transferPoints);
        when(call.isOneFaVerified()).thenReturn(true);
        when(call.getOneFa()).thenReturn("OneFA");
        when(call.isTwofaVerified()).thenReturn(false);
        when(call.getTwofa()).thenReturn("TwoFA");

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", "success");

        when(callActivityService.makeResponseWrapper(anyMap(), eq(true))).thenReturn(responseMap);

        // Act
        Object result = callActivityController.genesysTransfer(login, callRefNo, customerId, new ModelMap());

        // Assert
        assertEquals(responseMap, result);
        verify(callActivityService).makeResponseWrapper(anyMap(), eq(true));
    }

    @Test
    public void testGenesysTransfer_InvalidCallRefNo() {
        String invalidCallRefNo = "A"; // Less than expected length for substring operation
        String customerId = "customer123";

        assertThrows(StringIndexOutOfBoundsException.class, () -> {
            callActivityController.genesysTransfer(login, invalidCallRefNo, customerId, new ModelMap());
        });
    }

    @Test
    public void testGenesysTransfer_CallActivityNotFound() throws Exception {
        String callRefNo = "AB12345";
        String expectedRefNo = "12345";
        String customerId = "customer123";

        when(callActivityAction.getCallActivityByRefNo(expectedRefNo, "CountryCode")).thenThrow(new CallActivityRuntimeException("Call Activity not found"));

        assertThrows(CallActivityRuntimeException.class, () -> {
            callActivityController.genesysTransfer(login, callRefNo, customerId, new ModelMap());
        });
    }
}
