import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class IVRCtomResponseEntityServiceTest {

    @InjectMocks
    private IVRCtomResponseEntityService ivrCtomResponseEntityService;

    @Mock
    private ParamRepository paramRepository;

    @Mock
    private CtomOAuthTokenGenerator ctomOAuthTokenGenerator;

    @Mock
    private List<HttpMessageConverter<?>> customMessageConverters;

    private Map<String, Object> ctomRequestBody;
    private String idParam;
    private String xParamKey1;
    private String xParamKey2;
    private String countryCodeForParam;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Initialize test data
        ctomRequestBody = new HashMap<>();
        ctomRequestBody.put("countryCode", "IN");

        idParam = "someId";
        xParamKey1 = "xKey1";
        xParamKey2 = "xKey2";
        countryCodeForParam = "US";
    }

    @Test
    public void testGetReponseEntityforCTOM_Success() throws Exception {
        // Mock dependencies and setup test data
        Map<String, String> paramData = new HashMap<>();
        paramData.put("service_url", "http://mock-service-url.com");
        when(paramRepository.getParam(any())).thenReturn(new Param(idParam));
        when(ctomOAuthTokenGenerator.getAccessToken(any())).thenReturn("mock-access-token");

        // Mock RestTemplate response
        RestTemplate restTemplate = mock(RestTemplate.class);
        CtomComplaintResponseBody mockResponseBody = new CtomComplaintResponseBody();
        ResponseEntity<CtomComplaintResponseBody> mockResponseEntity = new ResponseEntity<>(mockResponseBody, HttpStatus.OK);
        when(restTemplate.postForEntity(any(URI.class), any(HttpEntity.class), eq(CtomComplaintResponseBody.class)))
            .thenReturn(mockResponseEntity);

        ResponseEntity<CtomComplaintResponseBody> responseEntity = ivrCtomResponseEntityService.getReponseEntityforCTOM(
                ctomRequestBody, idParam, xParamKey1, xParamKey2, countryCodeForParam);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testGetReponseEntityforCTOM_Failure() throws Exception {
        // Simulate an exception in the method
        when(paramRepository.getParam(any())).thenThrow(new RuntimeException("Database error"));

        try {
            ivrCtomResponseEntityService.getReponseEntityforCTOM(ctomRequestBody, idParam, xParamKey1, xParamKey2, countryCodeForParam);
            fail("Expected an exception to be thrown");
        } catch (Exception ex) {
            assertEquals("Database error", ex.getMessage());
        }
    }

    @Test
    public void testGetCTOMEndPointURL() {
        // Mock repository return data
        Param param = new Param(idParam);
        param.setCountryCode("IN");
        param.setKeys(new String[]{xParamKey1, xParamKey2});

        Param resultParam = new Param(idParam);
        resultParam.setData(new String[]{"", "role-id", "secret-id", "", "", "http://mock-oauth-url.com", "http://mock-service-url.com"});
        when(paramRepository.getParam(any())).thenReturn(resultParam);

        Map<String, String> dataMap = ivrCtomResponseEntityService.getCTOMEndPointURL(xParamKey1, xParamKey2, "IN", idParam);

        assertNotNull(dataMap);
        assertEquals("role-id", dataMap.get("roleId"));
        assertEquals("secret-id", dataMap.get("secretId"));
        assertEquals("http://mock-service-url.com", dataMap.get("service_url"));
    }

    @Test
    public void testStrSubstitutor_Success() throws Exception {
        String actualUrl = "http://example.com?param1=$(key1)&param2=$(key2)";
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("key1", "value1");
        inputMap.put("key2", "value2");

        String result = ivrCtomResponseEntityService.strSubstitutor(actualUrl, inputMap);

        assertEquals("http://example.com?param1=value1&param2=value2", result);
    }

    @Test
    public void testStrSubstitutor_MissingKey() throws Exception {
        String actualUrl = "http://example.com?param1=$(key1)&param2=$(key3)";
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("key1", "value1");

        String result = ivrCtomResponseEntityService.strSubstitutor(actualUrl, inputMap);

        assertEquals("http://example.com?param1=value1&param2=", result); // key3 is missing, so it should be blank
    }
}
