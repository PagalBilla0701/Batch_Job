import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.*;
import java.util.stream.Collectors;

public class CallActivityServiceImplTest {

    @InjectMocks
    private CallActivityServiceImpl callActivityService;

    @Mock
    private CemsSectionDataRegAction cemsSecDataReqAction;

    @Mock
    private SectionDataResponse sectionDataResponse;

    @Mock
    private Map<String, Object> values;

    @Mock
    private CallActivity callActivity;

    @Mock
    private LoginBean loginBean;

    @Mock
    private UserBean userBean;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        // Setup mock behavior for loginBean and userBean
        when(loginBean.getUserBean()).thenReturn(userBean);
        when(userBean.getCountryCode()).thenReturn("US");
        when(userBean.getCountryShortDesc()).thenReturn("USA");
        when(userBean.getUserLanguage()).thenReturn("en");
        when(userBean.getUserId()).thenReturn(12345L);
        when(userBean.getPeoplewiseId()).thenReturn("PW123");
        when(userBean.getInstanceCode()).thenReturn("IC001");
        when(userBean.isSalesObjectAccess()).thenReturn(true);
        when(userBean.getUserRoleId()).thenReturn("ROLE_USER");
        
        // Setup mock behavior for cemsSecDataReqAction and sectionDataResponse
        when(cemsSecDataReqAction.getSectionDataResponse(anyMap(), eq(loginBean))).thenReturn(sectionDataResponse);
        
        // Setup mock for sectionDataResponse.getSections()
        List<Section> sectionList = new ArrayList<>();
        Section section = mock(Section.class);
        Map<String, Object> mockValues = new HashMap<>();
        mockValues.put("ibankStatus", "active");
        mockValues.put("lastLogin", "2023-12-12");
        mockValues.put("creditLimit", "5000");
        mockValues.put("riskCode", "low");
        mockValues.put("kycStatus", "completed");
        mockValues.put("outstanding Balance", "200");
        mockValues.put("currentDue Date", "2023-12-15");
        mockValues.put("approvedAmount", "10000");
        mockValues.put("currentInstallment", "500");
        mockValues.put("tenure", "12");
        mockValues.put("accountType", "savings");
        mockValues.put("openComplaintsCount", 1);
        mockValues.put("openSRCount", 2);
        mockValues.put("sensitiveCust", "yes");
        
        // Return the mock values
        when(section.getKeyValGridDataMap()).thenReturn(mockValues);
        sectionList.add(section);
        
        // Simulate that getSections() returns a list containing the mock section
        when(sectionDataResponse.getSections()).thenReturn(sectionList);
    }

    @Test
    public void testAddTransients_success() {
        // Call the method
        callActivityService.addTransients(callActivity, loginBean);
        
        // Verify if the values are correctly set
        verify(callActivity).setIbankStatus("active");
        verify(callActivity).setLastLogin("2023-12-12");
        verify(callActivity).setCreditLimit("5000");
        verify(callActivity).setFraudRiskCode("low");
        verify(callActivity).setkyc("completed");
        verify(callActivity).setLoanBal("200");
        verify(callActivity).setEmiDate("2023-12-15");
        verify(callActivity).setPrincipalLoanAmt("10000");
        verify(callActivity).setEmiAmount("500");
        verify(callActivity).setTenure("12");
        verify(callActivity).setAccType("savings");
        verify(callActivity).setOpenSr("3");
        verify(callActivity).setSenstvClient("yes");
    }

    @Test(expected = NullPointerException.class)
    public void testAddTransients_nullValues() {
        // Simulate null response from the mock
        when(sectionDataResponse.getSections()).thenReturn(null);
        
        // Call the method with null section data
        callActivityService.addTransients(callActivity, loginBean);
    }
}
