import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

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
    private CallActivityServiceImpl callActivityService;

    @Mock
    private Logger logger;

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

    private CallActivity callActivity;
    private LoginBean loginBean;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        callActivity = new CallActivity();
        loginBean = new LoginBean();

        // Initialize callActivity fields for testing purposes
        callActivity.setCustId("12345");
        callActivity.setOneFa("1FA_Example");
        callActivity.setTwoFa("2FA_Example");
        callActivity.setAvailableAuth("AUTH_A|AUTH_B");
        callActivity.setFailedAuthOne("AUTH_C");
        callActivity.setFailedAuthTwo("AUTH_D");
    }

    @Test
    public void testGetButtons_OneFaVerified() {
        callActivity.setOneFaVerifed(true);

        Map<String, String> buttons = callActivityService.getButtons(callActivity);

        assertNotNull(buttons);
        assertTrue(buttons.containsKey("twoPlusOne"));
        assertEquals("Success", buttons.get("twoPlusOne"));
    }

    @Test
    public void testGetButtons_NotOneFaVerified() {
        callActivity.setOneFaVerifed(false);

        Map<String, String> buttons = callActivityService.getButtons(callActivity);

        assertNotNull(buttons);
        assertTrue(buttons.containsKey("twoPlusOne"));
        assertEquals("Enable", buttons.get("twoPlusOne"));
    }

    @Test
    public void testAddAvailableAuthButtons() {
        CallButtons buttons = new CallButtons();

        callActivityService.addAvailableAuthButtons(callActivity, buttons);

        assertEquals("Enable", buttons.getButtons().get("AUTH_A"));
        assertEquals("Enable", buttons.getButtons().get("AUTH_B"));
    }

    @Test
    public void testSetFailureAuthButtons() {
        CallButtons buttons = new CallButtons();

        callActivityService.setFailureAuthButtons(callActivity, buttons);

        assertEquals("Failure", buttons.getButtons().get("AUTH_C"));
        assertEquals("Failure", buttons.getButtons().get("AUTH_D"));
    }

    @Test
    public void testGetAttachedData() {
        Map<String, Object> attachedData = callActivityService.getAttachedData(callActivity, "Inbound");

        assertNotNull(attachedData);
        assertEquals("12345", attachedData.get("relld"));
        assertEquals("1FA_Example", attachedData.get("1FA"));
        assertEquals("2FA_Example", attachedData.get("2FA"));
        assertEquals("Inbound", attachedData.get("Call Type"));
    }

    @Test
    public void testRenderGenesysNewCallInfo() {
        callActivity.setVerified(true);
        Map<String, Object> sectionDataReqMap = new HashMap<>();
        sectionDataReqMap.put("sectionId", "16015");

        SectionDataResponse mockResponse = new SectionDataResponse();
        when(cemsUiIntegrator.integrate(anyMap(), anyMap(), eq(loginBean))).thenReturn(mockResponse);

        SectionDataResponse response = callActivityService.renderGenesysNewCallInfo(callActivity, loginBean);

        assertNotNull(response);
        verify(cemsUiIntegrator, times(1)).integrate(anyMap(), anyMap(), eq(loginBean));
    }

    // Additional tests can be added to cover other methods if needed.
}
