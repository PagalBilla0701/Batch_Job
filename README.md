import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.*;
import java.util.concurrent.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.scb.cems.central.beans.UserBean;
import com.scb.cems.model.EventsEntityData;
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
    private List<HttpMessageConverter<?>> customMessageConverters;

    @Before
    public void setUp() {
        s2SEventService = new S2SEventServiceImpl();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testImsDataProcess_success() throws Exception {
        // Prepare test data
        Map<String, Object> request = new HashMap<>();
        request.put("headerData", "testHeader");

        Map<String, Object> payLoad = new HashMap<>();
        String paramKey = "TEST_PARAM";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Map> mockResponseEntity = new ResponseEntity<>(Collections.singletonMap("result", "success"), HttpStatus.OK);
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.POST), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(mockResponseEntity);

        Map<String, Object> response = s2SEventService.imsDataProcess(request, payLoad, paramKey);

        assertNotNull(response);
        assertEquals("success", response.get("result"));
    }

    @Test
    public void testMassUpdate_success() throws Exception {
        UserBean userBean = new UserBean();
        userBean.setCountryCode("IN");

        List<EventsEntityData> requestPayload = new ArrayList<>();
        requestPayload.add(new EventsEntityData());

        ExecutorService executorService = Executors.newFixedThreadPool(requestPayload.size());
        when(paramRepository.getParam(any(Param.class))).thenReturn(new Param());

        Map<String, Object> response = s2SEventService.massUpdate(userBean, requestPayload);

        assertNotNull(response);
    }

    @Test
    public void testGetUserChannel_validType() {
        Param paramMock = new Param();
        paramMock.setData(new String[]{"S2S_CHANNEL", "NS2S_CHANNEL"});

        when(paramRepository.getParam(any(Param.class))).thenReturn(paramMock);

        String channel = s2SEventService.getUserChannel("S2S");
        assertEquals("S2S_CHANNEL", channel);

        channel = s2SEventService.getUserChannel("NS2S");
        assertEquals("NS2S_CHANNEL", channel);
    }

    @Test
    public void testGetLMSPageSize_valid() {
        Param paramMock = new Param();
        paramMock.setData(new String[]{"10", "20"});

        when(paramRepository.getParam(any(Param.class))).thenReturn(paramMock);

        int pageSize = s2SEventService.getLMSPageSize(0);
        assertEquals(10, pageSize);
    }

    @Test
    public void testGetLMSPageSize_default() {
        when(paramRepository.getParam(any(Param.class))).thenReturn(null);

        int pageSize = s2SEventService.getLMSPageSize(0);
        assertEquals(15, pageSize);
    }

    @Test
    public void testStrSubstitutor_reflection() throws Exception {
        Method method = S2SEventServiceImpl.class.getDeclaredMethod("strSubstitutor", String.class, Map.class);
        method.setAccessible(true);

        String url = "http://example.com?name=${name}";
        Map<String, Object> params = new HashMap<>();
        params.put("name", "John");

        String result = (String) method.invoke(s2SEventService, url, params);
        assertEquals("http://example.com?name=John", result);
    }

    @Test
    public void testGetLMSEndPointeUrl_reflection() throws Exception {
        Method method = S2SEventServiceImpl.class.getDeclaredMethod("getLMSEndPointeUrl", String.class);
        method.setAccessible(true);

        Param paramMock = new Param();
        paramMock.setData(new String[]{"POST", "http://example.com"});
        when(paramRepository.getParam(any(Param.class))).thenReturn(paramMock);

        @SuppressWarnings("unchecked")
        Map<String, String> result = (Map<String, String>) method.invoke(s2SEventService, "TEST_URL");

        assertNotNull(result);
        assertEquals("POST", result.get("httpMethod"));
        assertEquals("http://example.com", result.get("serviceUrl"));
    }
}
