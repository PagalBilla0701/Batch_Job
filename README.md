import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
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
