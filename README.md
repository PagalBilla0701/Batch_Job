import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CallActivityServiceImplTest {

    @InjectMocks
    private CallActivityServiceImpl callActivityService;

    @Mock
    private SectionDataResponse sectionDataResponse;

    @Mock
    private CallActivity callActivity;

    @Mock
    private LoginBean loginBean;

    private static final String COUNTRY_CODE = "IN";

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        // Initialize mock CallActivity
        callActivity = new CallActivity();
        callActivity.setCustId("12345");
        callActivity.setOneFa("1FA_Example");
        callActivity.setTwoFa("2FA_Example");
        callActivity.setAvailableAuth("AUTH_A | AUTH_B");
        callActivity.setFailedAuthOne("AUTH_C");
        callActivity.setFailedAuthTwo("AUTH_D");
    }

    @Test
    public void testRenderVerified() {
        // Arrange
        when(callActivity.isGeneral()).thenReturn(true);
        SectionDataResponse mockResponse = new SectionDataResponse();

        // Spy on the service to mock renderCallInfo method
        CallActivityServiceImpl spyService = spy(callActivityService);
        doReturn(mockResponse).when(spyService).renderCallInfo(callActivity, true, COUNTRY_CODE, loginBean);

        // Act
        SectionDataResponse response = spyService.renderVerified(callActivity, COUNTRY_CODE, loginBean);

        // Assert
        assertNotNull(response);
        assertEquals(mockResponse, response);

        // Verify interactions
        verify(callActivity).isGeneral();
        verify(spyService).renderCallInfo(callActivity, true, COUNTRY_CODE, loginBean);
    }
}
