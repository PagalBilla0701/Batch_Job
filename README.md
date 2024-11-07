import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class CallActivityServiceImplTest {

    @InjectMocks
    private CallActivityServiceImpl callActivityService;

    @Mock
    private CemsUiIntegrator cemsUiIntegrator;

    @Mock
    private CemsSectionDataReqAction cemsSecDataReqAction;

    @Mock
    private CallActivityAction callActivityAction;

    @Mock
    private RecentItemAction recentItemAction;

    @Mock
    private GridMetaDataAction gridMetaDataAction;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetButtons_WithOneFaVerified() {
        CallActivity callActivity = new CallActivity();
        callActivity.setOneFaVerified(true);
        callActivity.setOneFa("2+1");

        Map<String, String> buttons = callActivityService.getButtons(callActivity);

        assertEquals("Success", buttons.get("twoPlusOne"));
    }

    @Test
    public void testGetButtons_WithTwoFaVerified() {
        CallActivity callActivity = new CallActivity();
        callActivity.setOneFaVerified(false);
        callActivity.setTwoFaVerified(true);
        callActivity.setTwoFa("authCode");

        Map<String, String> buttons = callActivityService.getButtons(callActivity);

        assertEquals("Success", buttons.get("authCode"));
    }

    @Test
    public void testAddTransients() {
        CallActivity call = new CallActivity();
        LoginBean login = mock(LoginBean.class);
        UserBean userBean = new UserBean();
        userBean.setCountryCode("IN");
        userBean.setUserLanguage("EN");
        userBean.setUserId(123L);
        userBean.setPeoplewiseld("ABC123");

        when(login.getUserBean()).thenReturn(userBean);
        when(cemsSecDataReqAction.getSectionDataResponse(any(), eq(login)))
            .thenReturn(mock(SectionDataResponse.class));

        callActivityService.addTransients(call, login);

        assertEquals("IN", call.getCountryCode());
        assertNotNull(call.getCustId());
    }

    @Test
    public void testValueToStringOrEmpty_WithValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");

        String result = callActivityService.valueToStringOrEmpty(map, "key1");

        assertEquals("value1", result);
    }

    @Test
    public void testValueToStringOrEmpty_WithNoValue() {
        Map<String, Object> map = new HashMap<>();

        String result = callActivityService.valueToStringOrEmpty(map, "key1");

        assertEquals("", result);
    }

    @Test
    public void testGetAttachedData() {
        CallActivity call = new CallActivity();
        call.setCustId("12345");
        call.setOneFa("1FAValue");
        call.setTwoFa("2FAValue");

        Map<String, Object> attachedData = callActivityService.getAttachedData(call, "CallType");

        assertEquals("12345", attachedData.get("relld"));
        assertEquals("1FAValue", attachedData.get("1FA"));
        assertEquals("2FAValue", attachedData.get("2FA"));
    }

    @Test
    public void testRenderGenesysNewCallInfo_Verified() {
        CallActivity call = new CallActivity();
        call.setVerified(true);
        LoginBean login = mock(LoginBean.class);

        SectionDataResponse mockResponse = mock(SectionDataResponse.class);
        when(cemsUiIntegrator.integrate(anyMap(), anyMap(), eq(login))).thenReturn(mockResponse);

        SectionDataResponse response = callActivityService.renderGenesysNewCallInfo(call, login);

        assertNotNull(response);
    }

    @Test
    public void testRenderGenesysNewCallInfo_NotVerified() {
        CallActivity call = new CallActivity();
        call.setVerified(false);
        LoginBean login = mock(LoginBean.class);

        SectionDataResponse mockResponse = mock(SectionDataResponse.class);
        when(cemsUiIntegrator.integrate(anyMap(), anyMap(), eq(login))).thenReturn(mockResponse);

        SectionDataResponse response = callActivityService.renderGenesysNewCallInfo(call, login);

        assertNotNull(response);
    }
}
