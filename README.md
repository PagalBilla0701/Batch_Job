import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class IVRSRResponseEntityServiceTest {

    @InjectMocks
    private IVRSRResponseEntityService ivrService;

    @Mock
    private ParamRepository paramRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private List<HttpMessageConverter<?>> customMessageConverters;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Mock custom message converters
        List<HttpMessageConverter<?>> mockConverters = new ArrayList<>();
        mockConverters.add(new MappingJackson2HttpMessageConverter());
        mockConverters.add(new StringHttpMessageConverter());

        when(customMessageConverters).thenReturn(mockConverters);
    }

    @Test
    public void testGetResponseEntityForSR_Success() throws Exception {
        // Arrange
        ObjectMapper objectMapper = new ObjectMapper();

        SRRequestBody srRequestBody = new SRRequestBody();
        SRStatusEnquiry srStatusEnquiry = new SRStatusEnquiry();
        srStatusEnquiry.setCustomerId("0150000350F");

        LocalDate currentDate = LocalDate.now();
        LocalDate previousDate = currentDate.minusDays(180);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        SRRequestBody.SRCreationDateRange srDateRange = new SRRequestBody.SRCreationDateRange();
        srDateRange.setFromDate(previousDate.format(formatter));
        srDateRange.setToDate(currentDate.format(formatter));
        srStatusEnquiry.setSrCreationDateRange(srDateRange);

        SRRequestBody.PageNavigation pageNavigation = new SRRequestBody.PageNavigation();
        pageNavigation.setPageNavigationFilter("Y");
        pageNavigation.setPageNo("1");
        pageNavigation.setPageSize("1");
        srStatusEnquiry.setPageNavigation(pageNavigation);

        srRequestBody.setSrStatusEnquiry(srStatusEnquiry);
        Map<String, Object> srRequestBodyMap = objectMapper.convertValue(srRequestBody, Map.class);

        String idParam = "URL12";
        String xParamKey1 = "ServiceRequest";
        String xParamKey2 = "summary";
        String countryCodeForParam = "MY";

        // Mocking private method using PowerMockito
        PowerMockito.spy(ivrService); // Allow spied object to invoke real methods
        Map<String, String> paramData = new HashMap<>();
        paramData.put("service_url", "http://mock-service-url.com");
        
        // Use PowerMockito to mock the private method
        PowerMockito.doReturn(paramData).when(ivrService, "getSREndPointURL", anyString(), anyString(), anyString(), anyString());

        // Mocking RestTemplate response
        SRComplaintResponseBody mockResponseBody = new SRComplaintResponseBody();
        ResponseEntity<SRComplaintResponseBody> mockResponseEntity = new ResponseEntity<>(mockResponseBody, HttpStatus.OK);
        when(restTemplate.postForEntity(
                anyString(),
                any(HttpEntity.class),
                eq(SRComplaintResponseBody.class)
        )).thenReturn(mockResponseEntity);

        // Act
        ResponseEntity<SRComplaintResponseBody> response = ivrService.getReponseEntityforSR(
                srRequestBodyMap,
                idParam,
                xParamKey1,
                xParamKey2,
                countryCodeForParam
        );

        // Assert
        assertNotNull("Response should not be null", response);
        assertEquals("Status code should be 200 OK", HttpStatus.OK, response.getStatusCode());
        assertNotNull("Response body should not be null", response.getBody());

        // Verify interactions
        verify(paramRepository, times(1)).getParam(any(Param.class));
        verify(restTemplate, times(1)).postForEntity(
                anyString(),
                any(HttpEntity.class),
                eq(SRComplaintResponseBody.class)
        );
    }
}
