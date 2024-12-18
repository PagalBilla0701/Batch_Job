import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;

@RunWith(MockitoJUnitRunner.class)
public class CallActivityControllerTest {

    @InjectMocks
    private CallActivityController callActivityController;

    @Mock
    private MenuAccessRepository menuAccessRepository;

    @Mock
    private ParamRepository paramRepository;

    private LoginBean login;
    private UserBean userBean;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);

        userBean = new UserBean();
        userBean.setCountryCode("SG");
        userBean.setInstanceCode("CB_SG");
        userBean.setUserRoleId("ROLE_ADMIN");

        login = new LoginBean();
        login.setUserBean(userBean);
    }

    @Test
    public void testGenesysSoftPhone_WhenSoftPhoneIsDisplayed() throws Exception {
        // Mock menu access repository response
        MenuItems menuItem = new MenuItems();
        menuItem.setName("callActivity");
        when(menuAccessRepository.getMainMenuAndStartMenu(
                eq("ROLE_ADMIN"),
                eq("CB_SG"),
                eq("CEMS"),
                eq("SG"),
                eq("en")))
            .thenReturn(List.of(menuItem));

        // Mock param repository response
        Param param = new Param("GEN01");
        String[] data = {"https://iframeurl.com", "FLOW123", "Sample Flow"};
        param.setData(data);
        when(paramRepository.getParam(any(Param.class))).thenReturn(param);

        // Act
        Map<String, Object> response = callActivityController.genesysSoftPhone(login);

        // Assert
        assertNotNull(response);
        assertEquals("Yes", response.get("isSoftPhoneDisplay"));
        assertEquals("https://iframeurl.com", response.get("iFrameUrl"));
        assertEquals("FLOW123", response.get("flowID"));
        assertEquals("Sample Flow", response.get("flowName"));

        verify(menuAccessRepository, times(1)).getMainMenuAndStartMenu(
                eq("ROLE_ADMIN"),
                eq("CB_SG"),
                eq("CEMS"),
                eq("SG"),
                eq("en"));
        verify(paramRepository, times(1)).getParam(any(Param.class));
    }

    @Test
    public void testGenesysSoftPhone_WhenSoftPhoneIsNotDisplayed() throws Exception {
        // Mock menu access repository response
        when(menuAccessRepository.getMainMenuAndStartMenu(
                eq("ROLE_ADMIN"),
                eq("CB_SG"),
                eq("CEMS"),
                eq("SG"),
                eq("en")))
            .thenReturn(List.of());

        // Act
        Map<String, Object> response = callActivityController.genesysSoftPhone(login);

        // Assert
        assertNotNull(response);
        assertEquals("No", response.get("isSoftPhoneDisplay"));
        assertNull(response.get("iFrameUrl"));
        assertNull(response.get("flowID"));
        assertNull(response.get("flowName"));

        verify(menuAccessRepository, times(1)).getMainMenuAndStartMenu(
                eq("ROLE_ADMIN"),
                eq("CB_SG"),
                eq("CEMS"),
                eq("SG"),
                eq("en"));
        verify(paramRepository, never()).getParam(any(Param.class));
    }
}
