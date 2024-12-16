import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;

@RunWith(MockitoJUnitRunner.class)
public class CallActivityServiceImplTest {

    @InjectMocks
    private CallActivityServiceImpl callActivityServiceImpl;

    @Mock
    private CemsSectionDataRegAction cemsSecDataReqAction;

    @Mock
    private Logger logger;

    private CallActivity callActivity;
    private LoginBean loginBean;
    private Map<String, Object> responseValues;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        callActivity = new CallActivity();
        loginBean = new LoginBean();

        // Mocking loginBean setup
        loginBean.setUserBean(new UserBean());
        loginBean.getUserBean().setCountryCode("US");
        loginBean.getUserBean().setUserLanguage("EN");
        loginBean.getUserBean().setUserId(12345L);
        loginBean.getUserBean().setPeoplewiseId("PW123");
        loginBean.getUserBean().setCountryShortDesc("United States");
        loginBean.getUserBean().setInstanceCode("INST1");
        loginBean.getUserBean().setSalesObjectAccess(true);
        loginBean.getUserBean().setUserRole("Admin");
        loginBean.getUserBean().setUserRoleId("ROLE123");

        // Mocking response values
        responseValues = new HashMap<>();
        responseValues.put("ibankStatus", "Active");
        responseValues.put("lastLogin", "2024-12-16");
        responseValues.put("creditLimit", "5000");
        responseValues.put("riskCode", "Low");
        responseValues.put("kycStatus", "Verified");
        responseValues.put("outstandingBalance", "10000");
        responseValues.put("currentDueDate", "2024-12-31");
        responseValues.put("approvedAmount", "15000");
        responseValues.put("currentInstallment", "2000");
        responseValues.put("tenure", "12");
        responseValues.put("accountType", "Savings");
        responseValues.put("openComplaintsCount", 2);
        responseValues.put("openSRCount", 1);
        responseValues.put("sensitiveCust", "Yes");

        SectionDataResponse sectionDataResponse = mock(SectionDataResponse.class);
        when(sectionDataResponse.getSections()).thenReturn(
                List.of(new SectionData().setKeyValGridDataMap(responseValues)));

        when(cemsSecDataReqAction.getSectionDataResponse(anyMap(), eq(loginBean)))
                .thenReturn(sectionDataResponse);
    }

    @Test
    public void testAddTransients() {
        // Act
        callActivityServiceImpl.addTransients(callActivity, loginBean);

        // Assert
        assertEquals("Active", callActivity.getIbankStatus());
        assertEquals("2024-12-16", callActivity.getLastLogin());
        assertEquals("5000", callActivity.getCreditLimit());
        assertEquals("Low", callActivity.getFraudRiskCode());
        assertEquals("Verified", callActivity.getKyc());
        assertEquals("10000", callActivity.getLoanBal());
        assertEquals("2024-12-31", callActivity.getEmiDate());
        assertEquals("15000", callActivity.getPrincipalLoanAmt());
        assertEquals("2000", callActivity.getEmiAmount());
        assertEquals("12", callActivity.getTenure());
        assertEquals("Savings", callActivity.getAccType());
        assertEquals("3", callActivity.getOpenSr()); // Complaints + SR
        assertEquals("Yes", callActivity.getSenstvClient());

        verify(logger, times(responseValues.size())).debug(anyString(), any(), any());
        verify(cemsSecDataReqAction, times(1)).getSectionDataResponse(anyMap(), eq(loginBean));
    }
}
