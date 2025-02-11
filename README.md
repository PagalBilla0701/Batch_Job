import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.scb.cems.exceptions.Sales2ServiceRuntimeException;
import com.scb.core.codeparam.data.model.Param;
import com.scb.core.codeparam.repository.ParamRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class LMSServiceImplTest {

    @InjectMocks
    private LMSServiceImpl lmsService;

    @Mock
    private ParamRepository paramRepository;

    @Mock
    private RestTemplate restTemplate;

    private List<MappingJackson2HttpMessageConverter> customMessageConverters;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mocking customMessageConverters
        customMessageConverters = new ArrayList<>();
        customMessageConverters.add(new MappingJackson2HttpMessageConverter());
        lmsService.customMessageConverters = (List) customMessageConverters;
    }

    @Test
    public void testLmsDataProcess_Success() throws Exception {
        // Mock input data
        Map<String, Object> request = new HashMap<>();
        Map<String, Object> payLoad = new HashMap<>();
        request.put("headerData", "test-header");

        // Mock service URL from ParamRepository
        Param param = new Param("URL17");
        when(paramRepository.getParam(any(Param.class))).thenReturn(new Param(new String[]{"GET", "http://example.com"}));

        // Mock RestTemplate response
        HttpHeaders headers = new HttpHeaders();
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("key", "value");
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);

        when(restTemplate.exchange(
                any(),
                eq(org.springframework.http.HttpMethod.GET),
                any(),
                eq(Map.class))
        ).thenReturn(responseEntity);

        // Execute method
        Map<String, Object> response = lmsService.lmsDataProcess(request, payLoad, "paramKey");

        // Validate result
        assertNotNull(response);
        assertEquals("value", response.get("key"));
    }

    @Test(expected = Sales2ServiceRuntimeException.class)
    public void testLmsDataProcess_Failure() throws Exception {
        // Mock input data
        Map<String, Object> request = new HashMap<>();
        Map<String, Object> payLoad = new HashMap<>();

        // Mock failure scenario
        when(paramRepository.getParam(any(Param.class))).thenReturn(null);

        // Execute method
        lmsService.lmsDataProcess(request, payLoad, "paramKey");
    }
}
