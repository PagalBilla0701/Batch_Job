package com.scb.cems.serviceImpl;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.scb.cems.central.beans.LoginBean;

@RunWith(MockitoJUnitRunner.class)
public class LoginServiceImplTest {

    @InjectMocks
    private LoginServiceImpl loginService;

    @Mock
    private ModelMap model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private SessionStatus sessionStatus;

    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImplTest.class);

    private LoginBean loginBean;

    @Before
    public void setUp() {
        loginBean = new LoginBean();
    }

    @Test
    public void testLandingPage_AllCases() {
        // 1. SME country test
        loginBean.setCountry("SME_US");
        loginService.landingPage(loginBean, "someError", model, redirectAttributes, sessionStatus);
        assert loginBean.getCountry().equals("US");

        // 2. Non-SME country test
        loginBean.setCountry("OTHER_UK");
        loginService.landingPage(loginBean, "someError", model, redirectAttributes, sessionStatus);
        assert loginBean.getCountry().equals("OTHER_UK");

        // 3. Null country test
        loginBean.setCountry(null);
        loginService.landingPage(loginBean, "someError", model, redirectAttributes, sessionStatus);
        assert loginBean.getCountry() == null;

        // 4. Empty country test
        loginBean.setCountry("");
        loginService.landingPage(loginBean, "someError", model, redirectAttributes, sessionStatus);
        assert loginBean.getCountry().equals("");

        // Verify logging
        verifyNoInteractions(model, redirectAttributes, sessionStatus);
    }
}
