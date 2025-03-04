package com.scb.cems.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.scb.cems.central.beans.LoginBean;
import com.scb.cems.central.beans.UserBean;
import com.scb.cems.central.services.login.action.UserAction;
import com.scb.cems.central.services.login.model.UserProfile;
import com.scb.cems.data.assembler.service.internal.Login2FAEDMIService;
import com.scb.cems.workflow.authentication.context.UserAuthenticationRequest;
import com.scb.cems.workflow.authentication.context.UserAuthenticationResponse;
import com.scb.cems.workflow.processor.WorkflowEngine;
import com.scb.core.codeparam.repository.CodeRepository;
import com.scb.core.codeparam.repository.ParamRepository;

@ExtendWith(MockitoExtension.class)
class LoginServiceImplTest {

    @InjectMocks
    private LoginServiceImpl loginService;

    @Mock
    private WorkflowEngine authenticationWorkflow;

    @Mock
    private WorkflowEngine ssoAuthorizationWorkflow;

    @Mock
    private ParamRepository paramRepository;

    @Mock
    private CodeRepository codeRepository;

    @Mock
    private UserAction userAction;

    @Mock
    private Login2FAEDMIService login2FAEDMIService;

    private LoginBean loginBean;

    @BeforeEach
    void setUp() {
        loginBean = new LoginBean();
        loginBean.setUsername("testUser");
        loginBean.setPassword("testPass");
        loginBean.setCountry("IN");
        loginBean.setInstanceCode("SME");
    }

    @Test
    void testLandingPage_Success() {
        ModelMap model = new ModelMap();
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        UserBean userBean = new UserBean();
        userBean.setPeoplewiseId("P12345");

        UserProfile userProfile = new UserProfile();
        userProfile.setUserId("P12345");

        when(userAction.findUserByPeoplewiseId(any(), any(), any())).thenReturn(userProfile);

        String result = loginService.landingPage(loginBean, null, model, redirectAttributes, null);

        assertEquals("redirect:/central/dashboard.do", result);
    }

    @Test
    void testValidateOTP_Success() {
        when(login2FAEDMIService.validateOTP("testUser", "123456", "IN")).thenReturn(true);

        Map<String, String> result = loginService.validateOTP(loginBean, "123456");

        assertEquals("200", result.get("res-code"));
        assertEquals("Success", result.get("res-desc"));
    }

    @Test
    void testValidateOTP_Failure() {
        when(login2FAEDMIService.validateOTP("testUser", "123456", "IN")).thenReturn(false);

        Map<String, String> result = loginService.validateOTP(loginBean, "123456");

        assertEquals("400", result.get("res-code"));
        assertEquals("Failed", result.get("res-desc"));
    }

    @Test
    void testAuthenticationAndAuthorization_Success() throws Exception {
        ModelMap model = new ModelMap();
        UserAuthenticationResponse response = new UserAuthenticationResponse();
        response.setAuthorized(true);

        when(authenticationWorkflow.process(any(UserAuthenticationRequest.class)))
                .thenReturn(response);

        String result = loginService.authenticationAndAuthorization(loginBean, model);

        assertEquals("login-country-selection", result);
    }

    @Test
    void testAuthenticationAndAuthorization_Failure() throws Exception {
        ModelMap model = new ModelMap();
        UserAuthenticationResponse response = new UserAuthenticationResponse();
        response.setAuthorized(false);

        when(authenticationWorkflow.process(any(UserAuthenticationRequest.class)))
                .thenReturn(response);

        String result = loginService.authenticationAndAuthorization(loginBean, model);

        assertEquals("login", result);
    }

    @Test
    void testGetCountryURL_Success() {
        UserProfile userProfile = new UserProfile();
        userProfile.setCountryCode("IN");

        when(userAction.findUserByPeoplewiseId(any(), any(), any())).thenReturn(userProfile);

        Map<String, String> result = loginService.getCountryURL(loginBean, "SME");

        assertEquals("200", result.get("res-code"));
        assertEquals("User Found", result.get("res-desc"));
    }

    @Test
    void testGetCountryURL_UserNotFound() {
        when(userAction.findUserByPeoplewiseId(any(), any(), any())).thenReturn(null);

        Map<String, String> result = loginService.getCountryURL(loginBean, "SME");

        assertEquals("404", result.get("res-code"));
        assertEquals("User Not Found", result.get("res-desc"));
    }
}
