package com.scb.cems.serviceImpl;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scb.core.codeparam.data.model.Param;
import com.scb.core.codeparam.repository.ParamRepository;
import com.scb.cems.exceptions.Sales2ServiceRuntimeException;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class LMSServiceImplTest {

    @InjectMocks
    private LMSServiceImpl lmsService;

    @Mock
    private ParamRepository paramRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CloseableHttpClient httpClient;

    @Mock
    private HttpResponse httpResponse;

    @Mock
    private StatusLine statusLine;

    private Map<String, Object> request;
    private Map<String, Object> payload;

    @Before
    public void setUp() {
        request = new HashMap<>();
        payload = new HashMap<>();

        // Mock some default behavior for the repository
        Param param = new Param();
        param.setData(new String[]{"POST", "http://mock-url.com"});
        when(paramRepository.getParam(any())).thenReturn(param);
    }

    @Test
    public void testImsDataProcess_SuccessfulResponse() throws Exception {
        // Mock RestTemplate behavior
        ResponseEntity<Map> mockResponse = new ResponseEntity<>(new HashMap<>(), HttpStatus.OK);
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(mockResponse);

        Map<String, Object> result = lmsService.imsDataProcess(request, payload, "testParamKey");
        assertNotNull(result);
    }

    @Test(expected = Sales2ServiceRuntimeException.class)
    public void testImsDataProcess_InvalidResponseStatus() throws Exception {
        // Mock RestTemplate behavior for non-200 response
        ResponseEntity<Map> mockResponse = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(mockResponse);

        lmsService.imsDataProcess(request, payload, "testParamKey");
    }

    @Test
    public void testGetUserChannel_Success() {
        Param param = new Param();
        param.setData(new String[]{"Channel1", "Channel2"});
        when(paramRepository.getParam(any())).thenReturn(param);

        String result = lmsService.getUserChannel("S2S");
        assertEquals("Channel1", result);
    }

    @Test
    public void testGetLMSPageSize_DefaultValue() {
        when(paramRepository.getParam(any())).thenReturn(null);
        int pageSize = lmsService.getLMSPageSize(0);
        assertEquals(15, pageSize);
    }

    @Test
    public void testGetLMSPageSize_Success() {
        Param param = new Param();
        param.setData(new String[]{"20", "50"});
        when(paramRepository.getParam(any())).thenReturn(param);

        int pageSize = lmsService.getLMSPageSize(1);
        assertEquals(50, pageSize);
    }

    @Test
    public void testGetHeaderString_Success() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String result = LMSServiceImpl.getHeaderString(Collections.singletonMap("key", "value"));
        assertEquals(mapper.writeValueAsString(Collections.singletonMap("key", "value")), result);
    }

    @Test
    public void testLogJson_Success() {
        lmsService.logJson(Collections.singletonMap("key", "value"), Collections.singletonMap("payload", "data"));
        // Verify logging does not throw any exceptions
    }

    @Test
    public void testGetAttachmentFileContent_Success() throws Exception {
        when(httpClient.execute(any(HttpPost.class))).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(EntityUtils.toByteArray(any())).thenReturn("mockContent".getBytes());

        byte[] result = lmsService.getAttachmentFileContent(payload, request, "testParamKey");
        assertNotNull(result);
        assertEquals("mockContent", new String(result));
    }

    @Test(expected = Sales2ServiceRuntimeException.class)
    public void testGetAttachmentFileContent_ErrorResponse() throws Exception {
        when(httpClient.execute(any(HttpPost.class))).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        when(statusLine.getStatusCode()).thenReturn(400);

        lmsService.getAttachmentFileContent(payload, request, "testParamKey");
    }
}
