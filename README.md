import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class CallActivityControllerTest {

    @Mock
    private CallActivityService callActivityService;

    @Mock
    private CallActivityAction callActivityAction;

    @InjectMocks
    private CallActivityController callActivityController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(callActivityController).build();
    }

    @Test
    public void testGenesysTransfer() throws Exception {
        // Mock login bean
        LoginBean loginBean = new LoginBean();
        loginBean.setUserBean(new UserBean());
        loginBean.getUserBean().setCountryShortDesc("US");

        // Mock call activity
        CallActivity callActivity = new CallActivity();
        callActivity.setCustomerSegment("Premium");
        callActivity.setCallerId("123456");
        callActivity.setCustIdIVR("ABC123");
        callActivity.setOneFa("Verified");
        callActivity.setTwofa("Verified");

        // Mock action response
        when(callActivityAction.getCallActivityByRefNo("12345", "US")).thenReturn(callActivity);

        // Prepare expected form fields
        Map<String, Object> formFields = new HashMap<>();
        formFields.put("CTI_LANGUAGE", callActivity.getIang());
        formFields.put("CTI_SEGMENT", callActivity.getCustomerSegment());
        formFields.put("CTI_CALLERID", callActivity.getCallerId());
        formFields.put("CTI_RELATIONSHIPID", callActivity.getCustIdIVR());

        // Mock service response
        when(callActivityService.makeResponseWrapper(anyMap(), eq(true))).thenReturn(formFields);

        // Perform request and verify response
        mockMvc.perform(post("/call-activity/genesys-transfer.do")
                .sessionAttr("login", loginBean)
                .param("callRefNo", "12345")
                .param("customerId", "CUST123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.CTI_SEGMENT").value("Premium"))
                .andExpect(jsonPath("$.CTI_CALLERID").value("123456"))
                .andExpect(jsonPath("$.CTI_RELATIONSHIPID").value("ABC123"));

        // Verify interactions
        verify(callActivityAction).getCallActivityByRefNo("12345", "US");
        verify(callActivityService).makeResponseWrapper(anyMap(), eq(true));
    }
}
