import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ModelMap;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

class GenesysControllerTest {

    @Mock
    private CallActivityAction callActivityAction;

    @Mock
    private CallActivityService callActivityService;

    @InjectMocks
    private GenesysController genesysController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(genesysController).build();
    }

    @Test
    void testGenesysTransfer() throws Exception {
        // Prepare test data
        String callRefNo = "REF12345";
        String customerId = "cust123";
        String refNo = callRefNo.substring(2); // Expected processed refNo
        String countryCode3 = "US";

        // Create login bean with necessary mock setup
        LoginBean login = new LoginBean();
        UserBean userBean = new UserBean();
        userBean.setCountryShortDesc(countryCode3);
        login.setUserBean(userBean);

        CallActivity mockCallActivity = mock(CallActivity.class);
        when(callActivityAction.getCallActivityByRefNo(refNo, countryCode3)).thenReturn(mockCallActivity);
        
        // Set up mock call activity values
        when(mockCallActivity.getLang()).thenReturn("EN");
        when(mockCallActivity.getCustomerSegment()).thenReturn("Premium");
        when(mockCallActivity.getCallerId()).thenReturn("1234567890");
        when(mockCallActivity.getOnis()).thenReturn("ONIS123");
        when(mockCallActivity.getIdntfTyp()).thenReturn("ID123");
        when(mockCallActivity.getIdBlockcode()).thenReturn("Block123");
        when(mockCallActivity.getAuthBlockCode()).thenReturn("AuthBlock");
        when(mockCallActivity.getSelfSrvcCode()).thenReturn("SelfService");
        when(mockCallActivity.getAvailableAuth()).thenReturn("AuthAvailable");
        when(mockCallActivity.getCustIdIVR()).thenReturn("IVR123");
        when(mockCallActivity.getLastMobno()).thenReturn("9876543210");
        when(mockCallActivity.getRmn()).thenReturn("RMN123");
        when(mockCallActivity.isOneFaverifed()).thenReturn(true);
        when(mockCallActivity.getOneFa()).thenReturn("1FA123");
        when(mockCallActivity.isTwofaVerified()).thenReturn(true);
        when(mockCallActivity.getTwofa()).thenReturn("2FA123");
        
        Map<String, String> transferPoints = new HashMap<>();
        transferPoints.put("point1", "TransferPoint1");
        when(callActivityService.getTransferPointsMap()).thenReturn(transferPoints);

        // Prepare form fields and expected response
        Map<String, Object> formFields = new HashMap<>();
        formFields.put("CTI_LANGUAGE", "EN");
        formFields.put("CTI_SEGMENT", "Premium");
        formFields.put("CTI_CALLERID", "1234567890");
        formFields.put("CTI_DNIS", "ONIS123");
        formFields.put("CTI_IDENTIFICATIONTYPE", "ID123");
        formFields.put("CTI_IDENT_BLOCKCODES", "Block123");
        formFields.put("CTI_AUTH_BLOCKCODES", "AuthBlock");
        formFields.put("CTI_SELF_SERVICE_BLOCKCODES", "SelfService");
        formFields.put("CTI_AVAILABLEAUTHENTICATION", "AuthAvailable");
        formFields.put("CTI_RELATIONSHIPID", "IVR123");
        formFields.put("CTI_MOBILE_NUMBER", "9876543210");
        formFields.put("CTI_RMN", "RMN123");
        formFields.put("transferPoints", transferPoints);
        
        String onefa = mockCallActivity.isOneFaverifed() ? mockCallActivity.getOneFa().concat("[S") : null;
        String twofa = mockCallActivity.isTwofaVerified() ? mockCallActivity.getTwofa().concat("/5") : null;
        
        formFields.put("CTI_VERI", genesysController.setFailedHistory(onefa, mockCallActivity.getFailedAuthone()));
        formFields.put("CTI_VER2", genesysController.setFailedHistory(twofa, mockCallActivity.getFailedAuthTwo()));

        // Define expected response wrapper
        Object expectedResponse = "Expected Response";
        when(callActivityService.makeResponseWrapper(formFields, true)).thenReturn(expectedResponse);

        // Execute the controller method
        ModelMap model = new ModelMap();
        Object result = genesysController.genesysTransfer(login, callRefNo, customerId, model);

        // Verify results
        assertEquals(expectedResponse, result);
        verify(callActivityAction).getCallActivityByRefNo(refNo, countryCode3);
        verify(callActivityService).makeResponseWrapper(formFields, true);
    }
}
