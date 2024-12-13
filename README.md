@RunWith(MockitoJUnitRunner.class)
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
        customMessageConverters = new ArrayList<>();
        customMessageConverters.add(new MappingJackson2HttpMessageConverter());
    }

    @Test
    public void testGetResponseEntityForSR_Success() throws Exception {
        // Arrange
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

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> srRequestBodyMap = objectMapper.convertValue(srRequestBody, Map.class);

        String idParam = "URL12";
        String xParamKey1 = "ServiceRequest";
        String xParamKey2 = "summary";
        String countryCodeForParam = "MY";

        Param mockParam = Mockito.mock(Param.class);
        Mockito.when(mockParam.getKeys()).thenReturn(new String[]{xParamKey1, xParamKey2});
        Mockito.when(paramRepository.getParam(Mockito.any(Param.class))).thenReturn(mockParam);

        SRComplaintResponseBody mockResponseBody = new SRComplaintResponseBody();
        ResponseEntity<SRComplaintResponseBody> mockResponseEntity =
                new ResponseEntity<>(mockResponseBody, HttpStatus.OK);

        Mockito.when(restTemplate.postForEntity(
                Mockito.anyString(),
                Mockito.any(HttpEntity.class),
                Mockito.eq(SRComplaintResponseBody.class)
        )).thenReturn(mockResponseEntity);

        // Act
        ResponseEntity<SRComplaintResponseBody> response = ivrService.getReponseEntityforSR(
                srRequestBodyMap, idParam, xParamKey1, xParamKey2, countryCodeForParam);

        // Assert
        assertNotNull("Response should not be null", response);
        assertEquals("Status code should be 200 OK", HttpStatus.OK, response.getStatusCode());
        assertNotNull("Response body should not be null", response.getBody());

        // Verify interactions
        Mockito.verify(paramRepository, Mockito.times(1)).getParam(Mockito.any(Param.class));
        Mockito.verify(restTemplate, Mockito.times(1)).postForEntity(Mockito.anyString(),
                Mockito.any(HttpEntity.class),
                Mockito.eq(SRComplaintResponseBody.class));
    }
}
