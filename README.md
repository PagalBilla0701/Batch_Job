package com.scb.cems.serviceImpl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.*;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scb.cems.central.beans.UserBean;
import com.scb.cems.data.repository.UserRepository;
import com.scb.cems.exceptions.Sales2ServiceRuntimeException;
import com.scb.cems.model.TaskAndAppointmentData;
import com.scb.core.codeparam.data.model.Param;
import com.scb.core.codeparam.repository.ParamRepository;
import org.springframework.test.util.ReflectionTestUtils;

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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(service, "paramKey", "APPOINTMENTSAVE");
    }

    @Test
    public void testImsDataProcess_Success() throws Exception {
        // Mock headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Mock request payload
        Map<String, Object> request = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        request.put("headerData", new HashMap<>());

        // Mock service URL response
        Map<String, String> paramData = new HashMap<>();
        paramData.put("serviceUrl", "http://test-url");
        paramData.put("httpMethod", "POST");
        
        when(paramRepository.getParam(any(Param.class))).thenReturn(new Param("URL17"));

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(new HashMap<>(), HttpStatus.OK);
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(responseEntity);

        // Invoke method
        Map<String, Object> response = service.imsDataProcess(request, payload, "APPOINTMENTSAVE");

        assertNotNull(response);
    }

    @Test(expected = Sales2ServiceRuntimeException.class)
    public void testImsDataProcess_Failure() throws Exception {
        Map<String, Object> request = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        request.put("headerData", new HashMap<>());

        when(paramRepository.getParam(any(Param.class))).thenReturn(null);
        service.imsDataProcess(request, payload, "INVALID_KEY");
    }

    @Test
    public void testMassUpdate_Success() {
        UserBean userBean = new UserBean();
        userBean.setCountryCode("IN");

        TaskAndAppointmentData task = new TaskAndAppointmentData();
        task.setOwner("123-XYZ");
        task.setActionLastUpdatedBy("456-ABC");

        List<TaskAndAppointmentData> requestPayload = Collections.singletonList(task);

        when(userRepository.getNameForId("IN")).thenReturn(Collections.singletonMap("123", "John Doe"));

        Map<String, Object> response = service.massUpdate(userBean, requestPayload);
        assertNotNull(response);
    }

    @Test
    public void testGetUserChannel() {
        Param param = new Param("P9992");
        param.setData(new String[]{"Channel1", "Channel2"});

        when(paramRepository.getParam(any(Param.class))).thenReturn(param);

        String channel = service.getUserChannel("S25");
        assertEquals("Channel1", channel);
    }

    @Test
    public void testLogJson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String requestJson = mapper.writeValueAsString(Collections.singletonMap("key", "value"));

        service.logJson(Collections.singletonMap("key", "value"), Collections.singletonMap("data", "test"));
    }

    @Test
    public void testGetHeaderString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = mapper.writeValueAsString(Collections.singletonMap("header", "testHeader"));

        String result = service.getHeaderString(Collections.singletonMap("header", "testHeader"));
        assertEquals(jsonStr, result);
    }

    // **Testing Private Methods with Reflection**
    @Test
    public void testStrSubstitutor() throws Exception {
        Method method = TaskAndAppointmentServiceImpl.class.getDeclaredMethod("strSubstitutor", String.class, Map.class);
        method.setAccessible(true);

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("param", "value");

        String result = (String) method.invoke(service, "http://test-url/{param}", inputMap);
        assertEquals("http://test-url/value", result);
    }

    @Test
    public void testGetLMSEndPointeUrl() throws Exception {
        Param param = new Param("URL17");
        param.setData(new String[]{"POST", "http://test-url"});

        when(paramRepository.getParam(any(Param.class))).thenReturn(param);

        Method method = TaskAndAppointmentServiceImpl.class.getDeclaredMethod("getLMSEndPointeUrl", String.class);
        method.setAccessible(true);

        Map<String, String> result = (Map<String, String>) method.invoke(service, "APPOINTMENTSAVE");

        assertEquals("http://test-url", result.get("serviceUrl"));
        assertEquals("POST", result.get("httpMethod"));
    }
}
