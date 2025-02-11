package com.scb.cems.serviceImpl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.scb.core.codeparam.data.model.Param;
import com.scb.core.codeparam.repository.ParamRepository;

@RunWith(MockitoJUnitRunner.class)
public class LMSServiceImplTest {

    @InjectMocks
    private LMSServiceImpl lmsService;

    @Mock
    private ParamRepository paramRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private DefaultHttpClient httpClient;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLmsDataProcess_Success() throws Exception {
        Map<String, Object> request = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        request.put("headerData", new HashMap<>());

        Map<String, String> paramData = new HashMap<>();
        paramData.put("serviceUrl", "http://example.com");
        paramData.put("httpMethod", "POST");

        when(paramRepository.getParam(any(Param.class))).thenReturn(new Param());
        when(restTemplate.exchange(any(URI.class), eq(org.springframework.http.HttpMethod.POST), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(ResponseEntity.ok(new HashMap<>()));

        Map<String, Object> result = lmsService.lmsDataProcess(request, payload, "paramKey");

        assertNotNull(result);
    }

    @Test(expected = IOException.class)
    public void testLmsDataProcess_ThrowsIOException() throws Exception {
        Map<String, Object> request = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        doThrow(new IOException("IO error")).when(restTemplate)
                .exchange(any(URI.class), eq(org.springframework.http.HttpMethod.POST), any(HttpEntity.class), eq(Map.class));

        lmsService.lmsDataProcess(request, payload, "paramKey");
    }

    @Test
    public void testGetUserChannel_Success() {
        Param param = new Param();
        param.setKeys(new String[]{"S2S", "NS2S"});
        when(paramRepository.getParam(any(Param.class))).thenReturn(param);

        String channel = lmsService.getUserChannel("S2S");
        assertEquals("S2S", channel);
    }

    @Test
    public void testGetLMSPageSize_Success() {
        Param param = new Param();
        param.setKeys(new String[]{"15"});
        when(paramRepository.getParam(any(Param.class))).thenReturn(param);

        int pageSize = lmsService.getLMSPageSize(0);
        assertEquals(15, pageSize);
    }

    @Test
    public void testGetHeaderString() throws IOException {
        Map<String, String> headerData = new HashMap<>();
        headerData.put("key", "value");

        String headerString = LMSServiceImpl.getHeaderString(headerData);
        assertNotNull(headerString);
    }

    @Test
    public void testPrivateMethod_GetLMSEndPointeUrl() throws Exception {
        Method method = LMSServiceImpl.class.getDeclaredMethod("getLMSEndPointeUrl", String.class);
        method.setAccessible(true);

        Param param = new Param();
        param.setKeys(new String[]{"POST", "http://example.com"});
        when(paramRepository.getParam(any(Param.class))).thenReturn(param);

        Map<String, String> result = (Map<String, String>) method.invoke(lmsService, "paramKey");

        assertNotNull(result);
        assertEquals("POST", result.get("httpMethod"));
        assertEquals("http://example.com", result.get("serviceUrl"));
    }

    @Test
    public void testLogJson() {
        try {
            LMSServiceImpl.logJson(new HashMap<>(), new HashMap<>());
        } catch (Exception e) {
            fail("logJson method should not throw an exception.");
        }
    }

    @Test
    public void testStrSubstitutor() throws Exception {
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("key", "value");

        String result = lmsService.strSubstitutor("http://example.com/{key}", inputMap);
        assertEquals("http://example.com/value", result);
    }

    @Test
    public void testGetAttachmentFileContent() throws Exception {
        Map<String, Object> request = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        request.put("headerData", new HashMap<>());

        HttpPost httpPost = new HttpPost("http://example.com");
        when(httpClient.execute(any(HttpPost.class))).thenReturn(null);

        byte[] result = lmsService.getAttachmentFileContent(payload, request, "paramKey");

        assertNull(result);
    }
}
