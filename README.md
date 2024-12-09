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
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetResponseEntityForSR_Success() throws Exception {
        // Arrange
        Map<String, Object> srRequestBody = new HashMap<>();
        srRequestBody.put("countryCode", "MY");

        Map<String, String> paramData = new HashMap<>();
        paramData.put("service_url", "http://mock-service-url.com");

        Param mockParam = new Param("idParam");
        mockParam.setCountryCode("MY");
        mockParam.setKeys(new String[]{"xParamKey1", "xParamKey2"});

        SRComplaintResponseBody mockResponse = new SRComplaintResponseBody();
        ResponseEntity<SRComplaintResponseBody> mockResponseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);

        Mockito.when(paramRepository.getParam(Mockito.any(Param.class))).thenReturn(mockParam);
        Mockito.when(restTemplate.postForEntity(
                Mockito.anyString(),
                Mockito.any(HttpEntity.class),
                Mockito.eq(SRComplaintResponseBody.class)
        )).thenReturn(mockResponseEntity);

        // Act
        ResponseEntity<SRComplaintResponseBody> response = ivrService.getReponseEntityforSR(
                srRequestBody,
                "idParam",
                "xParamKey1",
                "xParamKey2",
                "MY"
        );

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Mockito.verify(paramRepository).getParam(Mockito.any(Param.class));
        Mockito.verify(restTemplate).postForEntity(
                Mockito.eq("http://mock-service-url.com"),
                Mockito.any(HttpEntity.class),
                Mockito.eq(SRComplaintResponseBody.class)
        );
    }

    @Test(expected = Exception.class)
    public void testGetResponseEntityForSR_Exception() throws Exception {
        // Arrange
        Map<String, Object> srRequestBody = new HashMap<>();
        srRequestBody.put("countryCode", "MY");

        Mockito.when(paramRepository.getParam(Mockito.any(Param.class))).thenThrow(new RuntimeException("Mock Exception"));

        // Act
        ivrService.getReponseEntityforSR(
                srRequestBody,
                "idParam",
                "xParamKey1",
                "xParamKey2",
                "MY"
        );

        // Assert: Exception is expected
    }
}
