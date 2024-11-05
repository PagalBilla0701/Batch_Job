import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.ModelMap;

public class CallActivityControllerTest {

    @Mock
    private GridDataAction gridDataAction;

    @Mock
    private CallActivityService callActivityService;

    @Mock
    private CallActivityAction callActivityAction;

    @Mock
    @Qualifier("CemsSectionDataIntegrator")
    private CemsUiIntegrator cemsUiIntegrator;

    @Mock
    private CVQuestionAction cvQuestionAction;

    @Mock
    private AppMessageSourceHelper messageHelper;

    @Mock
    private UserAction userAction;

    @Mock
    @Qualifier("menuAccessRepository")
    private MenuAccessRepository menuAccessRepository;

    @Mock
    private MenuService menuService;

    @Mock
    private OpportunityService opportunityService;

    @Mock
    private CallActivityEDMIService callActivityEDMIService;

    @Mock
    private ComsUtil comsUtil;

    @Mock
    private OpportunityListingFactory opptyListingFactory;

    @Mock
    private ParamRepository paramRepository;

    @Mock
    private BLPCIDSSDAO bLPCIDSSDAO;

    @Mock
    private SoftTokenPropertiesUtil softTokenPropertiesUtil;

    // TSYS implementation-related mock
    @Mock
    private ODSCpanTpanService odsCpanTpanService;

    @Mock
    private TSYSKongCommonService tsysKongCommonService;

    @Mock
    private SectionDataResponse sectionResponse;

    @InjectMocks
    private CallActivityController controller;

    private GenesysCallActivity gencall;
    private LoginBean login;
    private ModelMap model;
    private UserBean userBean;
    private CallActivity call;
    private Map<String, String> responseMap;
    private Map<String, Object> finalMap;
    private GenesysRequestData genesysData;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        gencall = new GenesysCallActivity();
        gencall.setAvailableAuth("CTP|ST");
        gencall.setOneFa("2+1");

        call = new CallActivity();
        model = new ModelMap();

        login = new LoginBean();
        userBean = new UserBean();
        userBean.setCountryShortDesc("SGP");
        userBean.setCountryCode("SG");
        userBean.setInstanceCode("CB SG");
        userBean.setFullname("XXXX");

        login.setUserBean(userBean);
        login.setUsername("1200046");

        call = new CallActivity("23456789", "SGP");
        call.setCustId("A0198678");
        call.setAccountNoIVR("123456790");

        responseMap = new HashMap<>();
        responseMap.put("RelationshipNo", call.getCustId());
        responseMap.put("AccountNo", call.getAccountNoIVR());
        responseMap.put("callRefNo", "5623456789");
        responseMap.put("type", "ST");

        finalMap = new HashMap<>();
        finalMap.put("header", responseMap);
        finalMap.put("statusCode", "OK");

        genesysData = new GenesysRequestData();
        genesysData.setAccountNo("123456790");
        genesysData.setRelId("A0198678");
    }

    @Test
    public void loadNewCallActivityGenesys_test() throws Exception {
        Map<String, String> customerMap = new HashMap<>();
        customerMap.put("fullName", "xxxx");
        customerMap.put("gender", "F");
        customerMap.put("genderDesc", "Female");
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
        when(callActivityAction.getCallActivityByRefNo("5623456789", "SGP")).thenReturn(call);
        doNothing().when(callActivityService).addToRecentItem(call, login);
        when(callActivityService.renderCallInfo(call, false, "SG", login)).thenReturn(sectionResponse);

        controller.loadNewCallActivityGenesys(login, "TPIN", model, genesysData);

        verify(callActivityService, times(2)).getCustomerInfo("SG", "A0198678");
        verify(callActivityService).isSoftTokenEnableForCountry("SG");
        verify(callActivityService, atLeast(1)).identifyAccountOrCustomerId("SG", "123456790");
        verify(callActivityAction).saveCallActivity(any());
        verify(callActivityAction).getCallActivityByRefNo("5623456789", "SGP");
        verify(callActivityService).addToRecentItem(call, login);
        verify(callActivityService).renderCallInfo(call, false, "SG", login);
    }
}
