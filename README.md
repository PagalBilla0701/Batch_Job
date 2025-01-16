import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CallActivityServiceImplTest {

    @InjectMocks
    private CallActivityServiceImpl callActivityService;

    @Mock
    private CallActivityAction callActivityAction;

    @Mock
    private SectionDataResponse sectionDataResponse;

    private CallActivity callActivity;
    private LoginBean loginBean;

    private static final String COUNTRY_CODE = "IN";

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Initialize CallActivity
        callActivity = new CallActivity();
        callActivity.setCustId("12345");
        callActivity.setCustName("John Doe");
        callActivity.setProductType("Savings");
        callActivity.setAccountNo("987654321");
        callActivity.setAccountCurrency("USD");
        callActivity.setName("John Doe");
        callActivity.setGender("Male");
        callActivity.setStaffCategory("Staff");

        // Initialize LoginBean
        loginBean = new LoginBean();
    }

    @Test
    public void testWipeCallCustomerAccount() throws Exception {
        // Arrange
        when(callActivityAction.updateCallActivity(any(CallActivity.class))).thenReturn(null);
        SectionDataResponse mockResponse = new SectionDataResponse();
        CallActivityServiceImpl spyService = spy(callActivityService);
        doReturn(mockResponse).when(spyService).renderCallInfo(callActivity, true, COUNTRY_CODE, loginBean);
        when(callActivity.isGeneral()).thenReturn(true);

        // Act
        SectionDataResponse response = spyService.wipeCallCustomerAccount(callActivity, COUNTRY_CODE, loginBean);

        // Assert
        assertNotNull(response);
        assertEquals(mockResponse, response);

        // Verify that customer details were cleared
        assertNull(callActivity.getCustId());
        assertNull(callActivity.getCustName());
        assertNull(callActivity.getProductType());
        assertNull(callActivity.getAccountNo());
        assertNull(callActivity.getAccountCurrency());
        assertNull(callActivity.getName());
        assertNull(callActivity.getGender());
        assertNull(callActivity.getStaffCategory());

        // Verify method interactions
        verify(callActivityAction).updateCallActivity(callActivity);
        verify(spyService).renderCallInfo(callActivity, true, COUNTRY_CODE, loginBean);
        verify(callActivity).isGeneral();
    }
}
