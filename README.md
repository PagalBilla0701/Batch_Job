import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ModelAndView;

@RunWith(SpringJUnit4ClassRunner.class)
public class CallActivityControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CallActivityController callActivityController;

    @Mock
    private CallActivityService callActivityService;

    @Mock
    private CallActivityAction callActivityAction;

    @Mock
    private LoginBean loginBean;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(callActivityController).build();
    }

    @Test
    public void testButtonClickInfo_Success() throws Exception {
        // Prepare mock data
        String callRefNo = "12345";
        String type = "type";
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");

        // Mock service behavior
        when(callActivityService.makeResponseWrapper(anyMap(), eq(true))).thenReturn(response);

        // Perform the POST request
        mockMvc.perform(post("/call-activity/genesys-button-click.do")
                .sessionAttr("login", loginBean)
                .param("type", type)
                .param("callActivityNo", callRefNo)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    public void testButtonClickInfo_Failure_InvalidType() throws Exception {
        // Test with an invalid type to verify exception handling
        String callRefNo = "12345";
        String type = "invalidType";

        mockMvc.perform(post("/call-activity/genesys-button-click.do")
                .sessionAttr("login", loginBean)
                .param("type", type)
                .param("callActivityNo", callRefNo)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGenesysTransfer_Success() throws Exception {
        String callRefNo = "12345";
        String customerId = "cust123";
        
        CallActivity mockCall = mock(CallActivity.class);
        when(callActivityAction.getCallActivityByRefNo(anyString(), anyString())).thenReturn(mockCall);
        when(mockCall.getLang()).thenReturn("EN");

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        when(callActivityService.makeResponseWrapper(anyMap(), eq(true))).thenReturn(response);

        mockMvc.perform(post("/call-activity/genesys-transfer.do")
                .sessionAttr("login", loginBean)
                .param("callRefNo", callRefNo)
                .param("customerId", customerId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    public void testGenesysTriggerAttachedData_Success() throws Exception {
        String callRefNo = "12345";
        String relId = "rel123";
        String type = "type";
        String customerName = "John Doe";
        String productType = "Personal Loan";
        String accountNumber = "acct123";

        CallActivity mockCall = mock(CallActivity.class);
        when(callActivityAction.getCallActivityByRefNo(anyString(), anyString())).thenReturn(mockCall);
        when(mockCall.isOneFaVerifed()).thenReturn(true);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        when(callActivityService.makeResponseWrapper(anyMap(), eq(true))).thenReturn(response);

        mockMvc.perform(post("/call-activity/genesys-trigger-verify.do")
                .sessionAttr("login", loginBean)
                .param("callActivityNo", callRefNo)
                .param("relationshipNo", relId)
                .param("type", type)
                .param("customerName", customerName)
                .param("productType", productType)
                .param("customerAccountNo", accountNumber)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }
}
