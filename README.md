package com.scb.cems.serviceImpl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import com.scb.cems.central.beans.UserBean;
import com.scb.cems.data.repository.UserRepository;
import com.scb.cems.exceptions.Sales2ServiceRuntimeException;
import com.scb.cems.model.TaskAndAppointmentData;
import com.scb.core.codeparam.data.model.Param;
import com.scb.core.codeparam.repository.ParamRepository;

@RunWith(MockitoJUnitRunner.class)
public class TaskAndAppointmentServiceImplTest {

    @InjectMocks
    private TaskAndAppointmentServiceImpl service;

    @Mock
    private ParamRepository paramRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private HttpHeaders httpHeaders;

    private Map<String, Object> request;
    private Map<String, Object> payload;
    private String paramKey = "APPOINTMENTSAVE";

    @Before
    public void setUp() {
        request = new HashMap<>();
        payload = new HashMap<>();
    }

    /** Test case for `lmsDataProcess` method */
    @Test
    public void testLmsDataProcessSuccess() throws Exception {
        Map<String, String> paramData = new HashMap<>();
        paramData.put("serviceUrl", "http://mock.service.com");
        paramData.put("httpMethod", "POST");

        when(paramRepository.getParam(any())).thenReturn(mockParam());
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(new ResponseEntity<>(Collections.singletonMap("data", "success"), HttpStatus.OK));

        Map<String, Object> response = service.lmsDataProcess(request, payload, paramKey);

        assertNotNull(response);
        assertEquals("success", response.get("data"));
    }

    /** Test case for `lmsDataProcess` when an exception occurs */
    @Test(expected = Sales2ServiceRuntimeException.class)
    public void testLmsDataProcessFailure() throws Exception {
        when(paramRepository.getParam(any())).thenReturn(mockParam());
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(Map.class)))
                .thenThrow(new IOException("Mocked IOException"));

        service.lmsDataProcess(request, payload, paramKey);
    }

    /** Test `massUpdate` method */
    @Test
    public void testMassUpdate() {
        UserBean userBean = new UserBean();
        List<TaskAndAppointmentData> taskList = Arrays.asList(new TaskAndAppointmentData());

        Map<String, Object> response = service.massUpdate(userBean, taskList);
        assertNotNull(response);
    }

    /** Test `getUserChannel` method */
    @Test
    public void testGetUserChannel() {
        when(paramRepository.getParam(any())).thenReturn(mockParam());
        String channel = service.getUserChannel("S2S");
        assertEquals("POST", channel);
    }

    /** Test private method `getLMSEndPointeUrl` using reflection */
    @Test
    public void testGetLMSEndPointeUrl() throws Exception {
        Method method = TaskAndAppointmentServiceImpl.class.getDeclaredMethod("getLMSEndPointeUrl", String.class);
        method.setAccessible(true);

        Map<String, String> result = (Map<String, String>) method.invoke(service, "APPOINTMENTSAVE");

        assertNotNull(result);
        assertEquals("POST", result.get("httpMethod"));
    }

    /** Test private method `strSubstitutor` using reflection */
    @Test
    public void testStrSubstitutor() throws Exception {
        Method method = TaskAndAppointmentServiceImpl.class.getDeclaredMethod("strSubstitutor", String.class, Map.class);
        method.setAccessible(true);

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("param1", "value1");

        String result = (String) method.invoke(service, "http://mock.url/${param1}", inputMap);
        assertEquals("http://mock.url/value1", result);
    }

    /** Mock `Param` object */
    private Param mockParam() {
        Param param = new Param();
        param.setKeys(new String[]{"S2S"});
        param.setData(new String[]{"POST", "http://mock.service.com"});
        return param;
    }
}
