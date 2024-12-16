import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class CallActivityServiceImplTest {

    @InjectMocks
    private CallActivityServiceImpl callActivityService;

    @Mock
    private CemsSectionDataRegAction cemsSecDataReqAction;

    @Mock
    private LoginBean login;

    private CallActivity callActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Mocking CallActivity object
        callActivity = new CallActivity();
        callActivity.setCustId("123456");
        callActivity.setAccountNo("ACC001");

        // Mocking LoginBean and UserBean
        UserBean userBean = mock(UserBean.class);
        when(login.getUserBean()).thenReturn(userBean);
        when(userBean.getCountryCode()).thenReturn("IN");
        when(userBean.getUserLanguage()).thenReturn("EN");
        when(userBean.getUserId()).thenReturn(12345L);
        when(userBean.getPeoplewiseId()).thenReturn("PS123");
        when(userBean.getCountryShortDesc()).thenReturn("India");
        when(userBean.getInstanceCode()).thenReturn("INSTANCE_01");
        when(userBean.isSalesObjectAccess()).thenReturn(true);
        when(userBean.getUserRoleId()).thenReturn("ROLE123");
    }

    @Test
    public void testAddTransients() {
        // Mocking SectionDataResponse and Section
        SectionDataResponse sectionDataResponse = mock(SectionDataResponse.class);
        Section section = mock(Section.class);

        // Mocking the data returned by getKeyValGridDataMap
        Map<String, Object> values = new HashMap<>();
        values.put("ibankStatus", "Active");
        values.put("lastLogin", "2024-12-01");
        values.put("creditLimit", "100000");
        values.put("riskCode", "LOW");
        values.put("kycStatus", "Complete");
        values.put("outstandingBalance", "5000");
        values.put("currentDueDate", "2024-12-20");
        values.put("approvedAmount", "150000");
        values.put("currentInstallment", "1500");
        values.put("tenure", "36");
        values.put("accountType", "Savings");
        values.put("openComplaintsCount", 2);
        values.put("openSRCount", 3);
        values.put("sensitiveCust", "Yes");

        // Stubbing methods
        when(section.getKeyValGridDataMap()).thenReturn(values);
        when(sectionDataResponse.getSections()).thenReturn(Collections.singletonList(section));
        when(cemsSecDataReqAction.getSectionDataResponse(anyMap(), eq(login))).thenReturn(sectionDataResponse);

        // Calling the method under test
        callActivityService.addTransients(callActivity, login);

        // Verifying the CallActivity object
        assertEquals("Active", callActivity.getIbankStatus());
        assertEquals("2024-12-01", callActivity.getLastLogin());
        assertEquals("100000", callActivity.getCreditLimit());
        assertEquals("LOW", callActivity.getFraudRiskCode());
        assertEquals("Complete", callActivity.getKyc());
        assertEquals("5000", callActivity.getLoanBal());
        assertEquals("2024-12-20", callActivity.getEmiDate());
        assertEquals("150000", callActivity.getPrincipalLoanAmt());
        assertEquals("1500", callActivity.getEmiAmount());
        assertEquals("36", callActivity.getTenure());
        assertEquals("Savings", callActivity.getAccType());
        assertEquals("5", callActivity.getOpenSr()); // Sum of complaints and SR counts
        assertEquals("Yes", callActivity.getSenstvClient());

        // Verify mock interactions
        verify(cemsSecDataReqAction, times(1)).getSectionDataResponse(anyMap(), eq(login));
        verify(section, times(1)).getKeyValGridDataMap();
    }
}
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class CallActivityServiceImplTest {

    @InjectMocks
    private CallActivityServiceImpl callActivityService;

    @Mock
    private CemsSectionDataRegAction cemsSecDataReqAction;

    @Mock
    private LoginBean login;

    private CallActivity callActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        callActivity = new CallActivity();
        login = mock(LoginBean.class);

        // Mocking the login bean and user details
        UserBean userBean = mock(UserBean.class);
        when(login.getUserBean()).thenReturn(userBean);
        when(userBean.getCountryCode()).thenReturn("IN");
        when(userBean.getUserLanguage()).thenReturn("EN");
        when(userBean.getUserId()).thenReturn(12345L);
        when(userBean.getPeoplewiseId()).thenReturn("PS123");
        when(userBean.getCountryShortDesc()).thenReturn("India");
        when(userBean.getInstanceCode()).thenReturn("INSTANCE_01");
        when(userBean.isSalesObjectAccess()).thenReturn(true);
        when(userBean.getUserRoleId()).thenReturn("ROLE123");
    }

    @Test
    public void testAddTransients_success() {
        // Mocking the response from cemsSecDataReqAction
        SectionDataResponse sectionDataResponse = mock(SectionDataResponse.class);
        Section section = mock(Section.class);

        Map<String, Object> keyValGridDataMap = new HashMap<>();
        keyValGridDataMap.put("ibankStatus", "Active");
        keyValGridDataMap.put("lastLogin", "2024-12-01");
        keyValGridDataMap.put("openComplaintsCount", 2);
        keyValGridDataMap.put("openSRCount", 3);

        when(section.getKeyValGridDataMap()).thenReturn(keyValGridDataMap);
        when(sectionDataResponse.getSections()).thenReturn(java.util.Collections.singletonList(section));
        when(cemsSecDataReqAction.getSectionDataResponse(anyMap(), eq(login))).thenReturn(sectionDataResponse);

        // Call the method
        callActivityService.addTransients(callActivity, login);

        // Assertions
        assertEquals("Active", callActivity.getIbankStatus());
        assertEquals("2024-12-01", callActivity.getLastLogin());
        assertEquals("5", callActivity.getOpenSr()); // Sum of openComplaintsCount and openSRCount
    }

    @Test
    public void testAddTransients_noData() {
        // Mock an empty response
        SectionDataResponse sectionDataResponse = mock(SectionDataResponse.class);
        when(sectionDataResponse.getSections()).thenReturn(java.util.Collections.emptyList());
        when(cemsSecDataReqAction.getSectionDataResponse(anyMap(), eq(login))).thenReturn(sectionDataResponse);

        // Call the method
        callActivityService.addTransients(callActivity, login);

        // Assertions for null/empty values
        assertNull(callActivity.getIbankStatus());
        assertNull(callActivity.getLastLogin());
        assertNull(callActivity.getOpenSr());
    }
}
