@RunWith(MockitoJUnitRunner.class)
public class IVRSRResponseEntityServiceTest {

    @InjectMocks
    private IVRSRResponseEntityService ivrService; // The service to test

    @Mock
    private ParamRepository paramRepository; // Mock for ParamRepository

    @Mock
    private RestTemplate restTemplate; // Mock for RestTemplate

    @Mock
    private List<HttpMessageConverter<?>> customMessageConverters; // Mock for Message Converters

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize the mocks
    }

    @Test
    public void testGetResponseEntityForSR_Success() throws Exception {
        // Arrange
        Map<String, Object> srRequestBody = new HashMap<>();
        srRequestBody.put("countryCode", "MY");

        String idParam = "idParam";
        String xParamKey1 = "xParamKey1";
        String xParamKey2 = "xParamKey2";
        String countryCodeForParam = "MY";

        // Mocking getSREndPointURL behavior
        Param param = new Param(idParam);
        param.setCountryCode(countryCodeForParam);
        param.setKeys(new String[]{xParamKey1, xParamKey2});

        Mockito.when(paramRepository.getParam(Mockito.any(Param.class))).thenAnswer(invocation -> {
            Param paramMock = invocation.getArgument(0);
            if (paramMock.getId().equals(idParam)) {
                return paramMock;
            }
            return null;
        });

        // Mocking RestTemplate response
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
                srRequestBody,
                idParam,
                xParamKey1,
                xParamKey2,
                countryCodeForParam
        );

        // Assert
        assertNotNull("Response should not be null", response);
        assertEquals("Status code should be 200 OK", HttpStatus.OK, response.getStatusCode());
        assertNotNull("Response body should not be null", response.getBody());

        // Verifying the interactions
        Mockito.verify(paramRepository, Mockito.times(1)).getParam(Mockito.any(Param.class));
        Mockito.verify(restTemplate, Mockito.times(1)).postForEntity(
                Mockito.anyString(),
                Mockito.any(HttpEntity.class),
                Mockito.eq(SRComplaintResponseBody.class)
        );
    }
}
