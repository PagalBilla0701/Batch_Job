import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class IVRCtomResponseEntityServiceTest {

    @InjectMocks
    private IVRCtomResponseEntityService service;

    @Mock
    private ParamRepository paramRepository;

    @Mock
    private CtomOAuthTokenGenerator ctomOAuthTokenGenerator;

    @Mock
    private List<HttpMessageConverter<?>> customMessageConverters;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetReponseEntityforCTOM_Success() throws Exception {
        // Mock inputs
        Map<String, Object> ctomRequestBody = new HashMap<>();
        ctomRequestBody.put("countryCode", "US");
        
        String idParam = "testId";
        String xParamKey1 = "key1";
        String xParamKey2 = "key2";
        String countryCodeForParam = "US";

        Map<String, String> paramData = new HashMap<>();
        paramData.put("service_url", "http://mock-url.com");
        paramData.put("roleId", "testRole");
        paramData.put("secretId", "testSecret");

        when(service.getCTOMEndPointURL(xParamKey1, xParamKey2, countryCodeForParam, idParam)).thenReturn(paramData);
        when(ctomOAuthTokenGenerator.getAccessToken(paramData)).thenReturn("mockToken");

        // Mock headers
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("mockToken");

        // Mock response entity
        ResponseEntity<CtomComplaintResponseBody> mockResponse = ResponseEntity.ok(new CtomComplaintResponseBody());
        when(restTemplate.postForEntity(any(), any(), eq(CtomComplaintResponseBody.class))).thenReturn(mockResponse);

        // Execute the method
        ResponseEntity<CtomComplaintResponseBody> responseEntity = service.getReponseEntityforCTOM(ctomRequestBody, idParam, xParamKey1, xParamKey2, countryCodeForParam);

        // Verify
        assertNotNull(responseEntity);
        assertEquals(mockResponse, responseEntity);
        verify(restTemplate, times(1)).postForEntity(any(), any(), eq(CtomComplaintResponseBody.class));
    }

    @Test
    public void testGetCTOMEndPointURL_Success() {
        String idParam = "testId";
        String xParamKey1 = "key1";
        String xParamKey2 = "key2";
        String countryCode = "US";

        Param mockParam = new Param(idParam);
        mockParam.setCountryCode(countryCode);
        mockParam.setKeys(new String[]{xParamKey1, xParamKey2});
        
        Param results = new Param();
        results.setData(new String[]{"", "testRoleId", "testSecretId", "", "", "http://mock-oauth-url.com", "http://mock-service-url.com"});
        
        when(paramRepository.getParam(mockParam)).thenReturn(results);

        // Execute
        Map<String, String> dataMap = service.getCTOMEndPointURL(xParamKey1, xParamKey2, countryCode, idParam);

        // Verify
        assertNotNull(dataMap);
        assertEquals("testRoleId", dataMap.get("roleId"));
        assertEquals("testSecretId", dataMap.get("secretId"));
        assertEquals("http://mock-oauth-url.com", dataMap.get("oAuth_url"));
        assertEquals("http://mock-service-url.com", dataMap.get("service_url"));
    }

    @Test
    public void testStrSubstitutor_UrlSubstitution() throws Exception {
        String actualUrl = "http://mock-url.com?param1=$(param1)&param2=$(param2)";
        
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("param1", "value1");
        inputMap.put("param2", "value2");

        String expectedUrl = "http://mock-url.com?param1=value1&param2=value2";

        // Execute
        String result = service.strSubstitutor(actualUrl, inputMap);

        // Verify
        assertEquals(expectedUrl, result);
    }

    @Test
    public void testStrSubstitutor_MissingParams() throws Exception {
        String actualUrl = "http://mock-url.com?param1=$(param1)&param2=$(param2)";

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("param1", "value1");

        String expectedUrl = "http://mock-url.com?param1=value1&param2=";

        // Execute
        String result = service.strSubstitutor(actualUrl, inputMap);

        // Verify
        assertEquals(expectedUrl, result);
    }
}
