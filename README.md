package com.scb.cems.serviceImpl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.concurrent.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scb.cems.central.beans.UserBean;
import com.scb.cems.exceptions.Sales2ServiceRuntimeException;
import com.scb.cems.model.EventsEntityData;
import com.scb.coms.model.LeadsDataRequestJson.Header;
import com.scb.core.codeparam.data.model.Param;
import com.scb.core.codeparam.repository.ParamRepository;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class S2SEventServiceImplTest {

    @InjectMocks
    private S2SEventServiceImpl s2SEventService;

    @Mock
    private ParamRepository paramRepository;

    @Mock
    private RestTemplate restTemplate;

    private UserBean mockUserBean;
    private EventsEntityData mockEventEntity;
    private Map<String, Object> mockRequest;
    private Map<String, Object> mockPayload;

    @Before
    public void setUp() {
        mockUserBean = new UserBean();
        mockUserBean.setCountryCode("IN");
        mockUserBean.setInstanceCode("CB_SME");
        mockUserBean.setRowId("12345");
        mockUserBean.setPeoplewiseId("P123");
        mockUserBean.setUserRole("Admin");

        mockEventEntity = new EventsEntityData();
        mockRequest = new HashMap<>();
        mockPayload = new HashMap<>();

        s2SEventService = new S2SEventServiceImpl();
        s2SEventService.paramRepository = paramRepository;
    }

    @Test
    public void testImsDataProcess_Success() throws Exception {
        Map<String, String> mockParamData = new HashMap<>();
        mockParamData.put("serviceUrl", "http://mock.url");
        mockParamData.put("httpMethod", "POST");

        when(paramRepository.getParam(any())).thenReturn(new Param("URL17"));
        when(paramRepository.getParam(any()).getData()).thenReturn(new String[]{"POST", "http://mock.url"});

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(Collections.singletonMap("success", true), HttpStatus.OK);
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(Map.class))).thenReturn(responseEntity);

        Map<String, Object> response = s2SEventService.imsDataProcess(mockRequest, mockPayload, "EVENTEDIT");

        assertNotNull(response);
        assertTrue(response.containsKey("success"));
    }

    @Test(expected = Sales2ServiceRuntimeException.class)
    public void testImsDataProcess_Failure() throws Exception {
        when(paramRepository.getParam(any())).thenReturn(null);

        s2SEventService.imsDataProcess(mockRequest, mockPayload, "INVALID_KEY");
    }

    @Test
    public void testMassUpdate_Success() throws Exception {
        List<EventsEntityData> eventsList = Collections.singletonList(mockEventEntity);

        Map<String, Object> response = s2SEventService.massUpdate(mockUserBean, eventsList);

        assertNotNull(response);
    }

    @Test
    public void testGetUserChannel() {
        Param mockParam = new Param("P9992");
        when(paramRepository.getParam(any())).thenReturn(mockParam);
        when(paramRepository.getParam(any()).getData()).thenReturn(new String[]{"S2S_Channel", "NS2S_Channel"});

        String result = s2SEventService.getUserChannel("S2S");
        assertEquals("S2S_Channel", result);
    }

    @Test
    public void testGetLMSPageSize() {
        Param mockParam = new Param("P0054");
        when(paramRepository.getParam(any())).thenReturn(mockParam);
        when(paramRepository.getParam(any()).getData()).thenReturn(new String[]{"20"});

        int size = s2SEventService.getLMSPageSize(0);
        assertEquals(20, size);
    }

    @Test
    public void testGetHeaderString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String expectedJson = mapper.writeValueAsString(mockUserBean);

        String actualJson = s2SEventService.getHeaderString(mockUserBean);

        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testStrSubstitutor() throws Exception {
        String url = "http://mock.service/{id}";
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("id", "123");

        String result = s2SEventService.strSubstitutor(url, inputMap);
        assertEquals("http://mock.service/123", result);
    }

    @Test(expected = Exception.class)
    public void testStrSubstitutor_Exception() throws Exception {
        String url = "http://mock.service/{id}";
        Map<String, Object> inputMap = new HashMap<>();

        s2SEventService.strSubstitutor(url, inputMap);
    }

    @Test
    public void testLogJson() {
        try {
            S2SEventServiceImpl.logJson(mockRequest, mockPayload);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }
}
