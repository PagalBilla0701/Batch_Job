import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import java.util.*;

public class CallActivityServiceImplTest {

    @InjectMocks
    private CallActivityServiceImpl callActivityService;

    @Mock
    private CemsSectionDataRegAction cemsSecDataReqAction;

    @Mock
    private CallActivity callActivity;

    @Mock
    private LoginBean loginBean;

    @Mock
    private SectionDataResponse sectionDataResponse;

    @Mock
    private UserBean userBean;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddTransients() {
        // Mock CallActivity and LoginBean fields
        when(callActivity.getCustId()).thenReturn("12345");
        when(callActivity.getAccountNo()).thenReturn("987654321");

        when(loginBean.getUserBean()).thenReturn(userBean);
        when(userBean.getCountryCode()).thenReturn("IN");
        when(userBean.getUserLanguage()).thenReturn("EN");
        when(userBean.getUserId()).thenReturn(12345L);
        when(userBean.getPeoplewiseId()).thenReturn("P123");
        when(userBean.getCountryShortDesc()).thenReturn("India");
        when(userBean.getInstanceCode()).thenReturn("INST1");
        when(userBean.isSalesObjectAccess()).thenReturn(true);
        when(userBean.getUserRoleId()).thenReturn("ADMIN");

        // Mock SectionDataResponse and its data
        Map<String, Object> mockKeyValData = new HashMap<>();
        mockKeyValData.put("ibankStatus", "Active");
        mockKeyValData.put("lastLogin", "2024-12-16");
        mockKeyValData.put("creditLimit", "50000");
        mockKeyValData.put("riskCode", "LOW");
        mockKeyValData.put("kycStatus", "Completed");
        mockKeyValData.put("outstanding Balance", "25000");
        mockKeyValData.put("currentDue Date", "2024-12-25");
        mockKeyValData.put("approvedAmount", "30000");
        mockKeyValData.put("currentInstallment", "3000");
        mockKeyValData.put("tenure", "36");
        mockKeyValData.put("accountType", "Savings");
        mockKeyValData.put("openComplaintsCount", 2);
        mockKeyValData.put("openSRCount", 3);
        mockKeyValData.put("sensitiveCust", "Yes");

        when(cemsSecDataReqAction.getSectionDataResponse(anyMap(), eq(loginBean)))
            .thenReturn(sectionDataResponse);
        when(sectionDataResponse.getSections())
            .thenReturn(Collections.singletonList(new SectionData("key1", mockKeyValData)));

        // Call the method
        callActivityService.addTransients(callActivity, loginBean);

        // Verify interactions and data set on CallActivity
        verify(callActivity).setIbankStatus("Active");
        verify(callActivity).setLastLogin("2024-12-16");
        verify(callActivity).setCreditLimit("50000");
        verify(callActivity).setFraudRiskCode("LOW");
        verify(callActivity).setkyc("Completed");
        verify(callActivity).setLoanBal("25000");
        verify(callActivity).setEmiDate("2024-12-25");
        verify(callActivity).setPrincipalLoanAmt("30000");
        verify(callActivity).setEmiAmount("3000");
        verify(callActivity).setTenure("36");
        verify(callActivity).setAccType("Savings");
        verify(callActivity).setOpenSr("5"); // 2 complaints + 3 SR
        verify(callActivity).setSenstvClient("Yes");
    }
}
