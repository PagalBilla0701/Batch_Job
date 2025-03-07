package com.scb.cems.serviceImpl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.concurrent.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scb.cems.central.beans.UserBean;
import com.scb.cems.data.assembler.exception.ScvSavRuntimeException;
import com.scb.cems.exceptions.Sales2ServiceRuntimeException;
import com.scb.cems.model.EventsEntityData;
import com.scb.coms.model.LeadsDataRequestJson.Header;
import com.scb.cems.service.S2SEventService;
import com.scb.core.codeparam.data.model.Param;
import com.scb.core.codeparam.repository.ParamRepository;

@RunWith(MockitoJUnitRunner.class)
public class S2SEventServiceImplTest {

    @InjectMocks
    private S2SEventServiceImpl s2SEventService;

    @Mock
    private ParamRepository paramRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /** Test Case for 1msDataProcess() */
    @Test
    public void testImsDataProcess_Success() throws Exception {
        // Sample data setup
        Map<String, Object> request = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        String paramKey = "EVENTEDIT";

        request.put("headerData", "testHeader");
        payload.put("sample", "data");

        Map<String, String> paramData = new HashMap<>();
        paramData.put("serviceUrl", "http://mockurl.com");
        paramData.put("httpMethod", "POST");

        when(paramRepository.getParam(any(Param.class))).thenReturn(new Param("P9992", new String[]{"S2S", "NS2S"}));
        when(restTemplate.exchange(any(URI.class), any(HttpMethod.class), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(new ResponseEntity<>(Collections.singletonMap("response", "success"), HttpStatus.OK));

        Map<String, Object> response = s2SEventService.imsDataProcess(request, payload, paramKey);
        
        assertNotNull(response);
        assertEquals("success", response.get("response"));
    }

    /** Test Case for getUserChannel() */
    @Test
    public void testGetUserChannel() {
        Param param = new Param("P9992", new String[]{"S2S", "NS2S"});
        when(paramRepository.getParam(any(Param.class))).thenReturn(param);

        String channel = s2SEventService.getUserChannel("S2S");
        assertEquals("S2S", channel);

        channel = s2SEventService.getUserChannel("NS2S");
        assertEquals("NS2S", channel);
    }

    /** Test Case for getLMSPageSize() */
    @Test
    public void testGetLMSPageSize() {
        Param param = new Param("P0054", new String[]{"10"});
        when(paramRepository.getParam(any(Param.class))).thenReturn(param);

        int pageSize = s2SEventService.getLMSPageSize(0);
        assertEquals(10, pageSize);
    }

    /** Test Case for massUpdate() */
    @Test
    public void testMassUpdate() throws Exception {
        UserBean userBean = new UserBean();
        userBean.setCountryCode("IN");

        List<EventsEntityData> requestPayload = Arrays.asList(new EventsEntityData(), new EventsEntityData());

        S2SEventServiceImpl spyService = spy(s2SEventService);
        doReturn(Collections.singletonMap("response", "success"))
                .when(spyService).imsDataProcess(anyMap(), anyMap(), anyString());

        Map<String, Object> response = spyService.massUpdate(userBean, requestPayload);
        assertNotNull(response);
        assertEquals("success", response.get("succstatus"));
    }

    /** Test Case for getHeaderString() */
    @Test
    public void testGetHeaderString() throws IOException {
        Map<String, String> headerData = new HashMap<>();
        headerData.put("key", "value");

        when(objectMapper.writeValueAsString(headerData)).thenReturn("{\"key\":\"value\"}");

        String headerString = s2SEventService.getHeaderString(headerData);
        assertEquals("{\"key\":\"value\"}", headerString);
    }

    /** Test Case for getLMSEndPointeUrl() */
    @Test
    public void testGetLMSEndPointeUrl() {
        Param param = new Param("URL17", new String[]{"POST", "http://mockurl.com"});
        when(paramRepository.getParam(any(Param.class))).thenReturn(param);

        Map<String, String> result = s2SEventService.getLMSEndPointeUrl("TEST_KEY");
        assertEquals("http://mockurl.com", result.get("serviceUrl"));
        assertEquals("POST", result.get("httpMethod"));
    }

    /** Test Case for strSubstitutor() */
    @Test
    public void testStrSubstitutor() throws Exception {
        String url = "http://mock.com/{param}";
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("param", "value");

        String result = s2SEventService.strSubstitutor(url, inputMap);
        assertEquals("http://mock.com/value", result);
    }

    /** Test Case for logJson() */
    @Test
    public void testLogJson() {
        Map<String, String> request = new HashMap<>();
        request.put("key", "value");

        Map<String, String> payload = new HashMap<>();
        payload.put("data", "test");

        try {
            s2SEventService.logJson(request, payload);
        } catch (Exception e) {
            fail("logJson should not throw an exception");
        }
    }

    /** Test Case for getHeaderData() */
    @Test
    public void testGetHeaderData() {
        UserBean userBean = new UserBean();
        userBean.setInstanceCode("CB_SME");
        userBean.setCountryCode("IN");
        userBean.setRowId("123");
        userBean.setPeoplewiseId("P001");
        userBean.setUserRole("Admin");

        Header header = s2SEventService.getHeaderData(userBean);
        assertNotNull(header);
        assertEquals("CEMSSME", header.getApplication());
        assertEquals("IN", header.getCountryCode());
        assertEquals("123", header.getUserRowId());
        assertEquals("P001", header.getUserId());
    }
}
