import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class CallActivityControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CallActivityController callActivityController;

    @Mock
    private CallActivityService callActivityService;

    @Mock
    private CallActivityAction callActivityAction;

    @Mock
    private CemsUiIntegrator cemsUiIntegrator;

    @Mock
    private AppMessageSourceHelper messageHelper;

    @Mock
    private UserAction userAction;

    @Mock
    private MenuAccessRepository menuAccessRepository;

    @Mock
    private com.scb.coms.service.MenuService menuService;

    @Mock
    private CallActivityEDMIService callActivityEDMIService;

    @Mock
    private ParamRepository paramRepository;

    @Mock
    private BLPCIDSSDAO blpCIDSSDAO;

    @Mock
    private SoftTokenPropertiesUtil softTokenPropertiesUtil;

    @Mock
    private TSYSKongCommonService tsysKongCommonService;

    @Mock
    private SectionDataResponse sectionResponse;

    private GenesysCallActivity genCall;
    private LoginBean login;
    private ModelMap model;
    private UserBean userBean;
    private CallActivity call;
    private Map<String, String> responseMap;
    private Map<String, String> headers;
    private GenesysRequestData genesysData;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(callActivityController).build();

        // Initialize test data
        genCall = new GenesysCallActivity();
        genCall.setAvailableAuth("OTP");
        genCall.setOnera("2+1");

        call = new CallActivity("23456789", "SGP");
        call.setCustId("A0198678");
        call.setAccountNoIVR("123456790");

        model = new ModelMap();
        login = new LoginBean();
        userBean = new UserBean();
        userBean.setCountryShortDesc("SGP");
        userBean.setCountryCode("SG");
        userBean.setInstanceCode("CB_5G");
        userBean.setFullname("XXXX");

        login.setUserBean(userBean);
        login.setUsername("1200046");

        responseMap = new HashMap<>();
        responseMap.put("RelationshipNo", "SG23456789");
        responseMap.put("Account", "123456790");
        responseMap.put("callRef", "SG23456789");
        responseMap.put("type", "ST");

        headers = new HashMap<>();
        headers.put("timestamp", String.valueOf(System.currentTimeMillis()));
        headers.put("responseStatus", "true");

        genesysData = new GenesysRequestData();
        genesysData.setAccountNo("123456790");
        genesysData.setRelId("A0198678");
    }

    @Test
    public void loadNewCallActivityGenesysTest() throws Exception {
        // Arrange
        Map<String, String> customerMap = new HashMap<>();
        customerMap.put("fullname", "XXXX");
        customerMap.put("gender", "Female");
        customerMap.put("staffCategoryCode", "01");
        customerMap.put("staffCategoryDesc", "STAFF");

        Map<String, String> accountMap = new HashMap<>();
        accountMap.put("custAcctProdType", "Savings Account");
        accountMap.put("custAcctIdentifier", "ACCT");
        accountMap.put("currencyCode", "SGD");

        when(callActivityService.getCustomerInfo("SG", "A0198678")).thenReturn(customerMap);
        when(callActivityService.isSoftTokenEnableForCountry("SG")).thenReturn("true");
        when(callActivityService.identifyAccountOrCustomerId("SG", "123456790")).thenReturn(accountMap);
        when(callActivityAction.saveCallActivity(any())).thenReturn("SG23456789");
        doNothing().when(callActivityService).addToRecentItem(call, login);
        when(callActivityService.renderCallInfo(call, false, "SG", login)).thenReturn(sectionResponse);

        // Act
        callActivityController.loadNewCallActivityGenesys(login, "OTP", model, genesysData);

        // Assert
        verify(callActivityService, times(1)).getCustomerInfo("SG", "A0198678");
        verify(callActivityService).isSoftTokenEnableForCountry("SG");
        verify(callActivityService).identifyAccountOrCustomerId("SG", "123456790");
        verify(callActivityAction).saveCallActivity(any());
        verify(callActivityService).renderCallInfo(call, false, "SG", login);

        // Additional assertions for expected outcomes if needed
        // Example: assertEquals(expectedValue, actualValue);
    }
}
