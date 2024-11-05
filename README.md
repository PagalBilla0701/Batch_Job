import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;
import com.example.CallActivityController;
import com.example.model.CallActivity;
import com.example.model.LoginBean;
import com.example.service.CallActivityService;

class CallActivityControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CallActivityController callActivityController;

    @Mock
    private CallActivityAction callActivityAction;

    @Mock
    private CallActivityService callActivityService;

    @Mock
    private LoginBean loginBean;

    private ModelMap model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(callActivityController).build();
        model = new ModelMap();
    }

    @Test
    void testButtonClickInfo_ValidType() throws Exception {
        // Set up test data
        String type = "1";
        String callRefNo = "IN123456";
        String refNo = callRefNo.substring(2);
        String countryCode = "IN";

        CallActivity callActivity = new CallActivity();
        callActivity.setCustId("12345");
        callActivity.setAccountNoIVR("987654321");

        // Mock the LoginBean to return country code
        when(loginBean.getUserBean().getCountryShortDesc()).thenReturn(countryCode);

        // Mock the callActivityAction to return CallActivity
        when(callActivityAction.getCallActivityByRefNo(refNo, countryCode)).thenReturn(callActivity);

        // Prepare response data
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("RelationshipNo", callActivity.getCustId());
        responseMap.put("AccountNo", callActivity.getAccountNoIVR());
        responseMap.put("callRefNo", callRefNo);
        responseMap.put("type", type);

        when(callActivityService.makeResponseWrapper(responseMap, true)).thenReturn(responseMap);

        // Perform test
        mockMvc.perform(post("/genesys-button-click.do")
                .param("type", type)
                .param("callActivityNo", callRefNo)
                .sessionAttr("login", loginBean))
                .andExpect(status().isOk());

        // Verify interactions
        verify(callActivityAction).getCallActivityByRefNo(refNo, countryCode);
        verify(callActivityService).makeResponseWrapper(responseMap, true);
    }

    @Test
    void testButtonClickInfo_InvalidType() {
        String invalidType = "invalidType";

        Exception exception = assertThrows(CallActivityAccessException.class, () -> {
            callActivityController.buttonClickInfo(loginBean, invalidType, "IN123456", model);
        });

        assertEquals("Invalid button type", exception.getMessage());
    }

    @Test
    void testButtonClickInfo_CallActivityNotFound() {
        String type = "1";
        String callRefNo = "IN123456";
        String refNo = callRefNo.substring(2);
        String countryCode = "IN";

        when(loginBean.getUserBean().getCountryShortDesc()).thenReturn(countryCode);
        when(callActivityAction.getCallActivityByRefNo(refNo, countryCode)).thenReturn(null);

        Exception exception = assertThrows(CallActivityAccessException.class, () -> {
            callActivityController.buttonClickInfo(loginBean, type, callRefNo, model);
        });

        assertTrue(exception.getMessage().contains("Unable to retrieve call activity"));
    }
}
