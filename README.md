   import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.scb.cems.central.beans.LoginBean;
import com.scb.cems.central.beans.UserBean;
import com.scb.cems.central.services.login.action.UserAction;
import com.scb.cems.workflow.authentication.context.UserAuthenticationRequest;
import com.scb.cems.workflow.authentication.context.UserAuthenticationResponse;
import com.scb.cems.workflow.processor.WorkflowEngine;

@RunWith(MockitoJUnitRunner.class)
public class LoginServiceImplTest {

    @InjectMocks
    private LoginServiceImpl loginService;

    @Mock
    private WorkflowEngine authenticationWorkflow;

    @Mock
    private WorkflowEngine ssoAuthorizationWorkflow;

    @Mock
    private UserAction userAction;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLandingPage_ValidUser() {
        LoginBean login = new LoginBean();
        login.setInstanceCode("SME");
        login.setCountry("SME_IN");

        ModelMap model = new ModelMap();
        UserBean mockUser = new UserBean();
        mockUser.setPeoplewiseId("12345");

        when(userAction.findUserByPeoplewiseId(anyString(), anyString(), anyString())).thenReturn(mockUser);

        String result = loginService.landingPage(login, null, model, redirectAttributes, null);
        assertEquals("redirect:/central/dashboard.do", result);
    }

    @Test
    public void testValidateOTP_Success() {
        LoginBean login = new LoginBean();
        login.setUsername("testUser");

        when(loginService.validateOTP(anyString(), anyString())).thenReturn(new HashMap<String, String>() {{
            put("res-code", "200");
            put("res-desc", "Success");
        }});

        Map<String, String> result = loginService.validateOTP(login, "123456");
        assertEquals("200", result.get("res-code"));
        assertEquals("Success", result.get("res-desc"));
    }

    @Test
    public void testAuthenticationAndAuthorization_Success() {
        LoginBean login = new LoginBean();
        login.setUsername("testUser");
        login.setPassword("password");

        UserAuthenticationResponse response = new UserAuthenticationResponse();
        response.setAuthorized(true);

        when(authenticationWorkflow.process(any(UserAuthenticationRequest.class))).thenReturn(response);

        ModelMap model = new ModelMap();
        String result = loginService.authenticationAndAuthorization(login, model);
        assertEquals("login-country-selection", result);
    }

    @Test
    public void testGetCountryURL_UserFound() {
        LoginBean login = new LoginBean();
        login.setUsername("testUser");
        login.setCountry("IN");

        UserBean mockUser = new UserBean();
        mockUser.setPeoplewiseId("12345");

        when(userAction.findUserByPeoplewiseId(anyString(), anyString(), anyString())).thenReturn(mockUser);

        Map<String, String> result = loginService.getCountryURL(login, "instanceCode");
        assertEquals("200", result.get("res-code"));
    }

    @Test
    public void testGetCountryURL_UserNotFound() {
        LoginBean login = new LoginBean();
        login.setUsername("testUser");
        login.setCountry("IN");

        when(userAction.findUserByPeoplewiseId(anyString(), anyString(), anyString())).thenReturn(null);

        Map<String, String> result = loginService.getCountryURL(login, "instanceCode");
        assertEquals("404", result.get("res-code"));
        assertEquals("User Not Found", result.get("res-desc"));
    }

    @Test
    public void testPrivateMethod_UsingReflection() throws Exception {
        Method method = LoginServiceImpl.class.getDeclaredMethod("getTimeZone", String.class);
        method.setAccessible(true);
        String result = (String) method.invoke(loginService, "UTC");
        assertNotNull(result);
    }
}
