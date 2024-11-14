@InjectMocks
private IVRCtomResponseEntityService instance;

@Mock
private ParamRepository paramRepository;

@Mock
private CtomOAuthTokenGenerator ctomOAuthTokenGenerator;

@Mock
private List<HttpMessageConverter<?>> customMessageConverters;

@Mock
private RestTemplate restTemplate;

private Map<String, Object> ctomRequestBody;
private String idParam;
private String xParamKey1;
private String xParamKey2;
private String countryCodeForParam;

@Before
public void setup() {
    MockitoAnnotations.openMocks(this);

    // Initialize test data
    ctomRequestBody = new HashMap<>();
    ctomRequestBody.put("countryCode", "IN");
    idParam = "someId";
    xParamKey1 = "xKey1";
    xParamKey2 = "xKey2";
    countryCodeForParam = "US";

    instance = new IVRCtomResponseEntityService();
}

@Test
public void testGetResponseEntityForCTOM() throws Exception {
    // Arrange
    Map<String, String> mockParamData = new HashMap<>();
    mockParamData.put("service_url", "http://example.com/api");

    // Mock the behavior of getCTOMEndPointURL
    IVRCtomResponseEntityService spyInstance = spy(instance);
    doReturn(mockParamData).when(spyInstance).getCTOMEndPointURL(anyString(), anyString(), anyString(), anyString());

    // Mock the behavior of ctomOAuthTokenGenerator
    when(ctomOAuthTokenGenerator.getAccessToken(mockParamData)).thenReturn("dummyAccessToken");

    // Mock RestTemplate to simulate an HTTP POST request and return a successful response
    ResponseEntity<String> mockResponseEntity = mock(ResponseEntity.class);
    when(mockResponseEntity.getBody()).thenReturn("Mocked response body");
    when(restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(mockResponseEntity);

    // Act
    ResponseEntity<String> response = spyInstance.getReponseEntityforCTOM(
            ctomRequestBody, idParam, xParamKey1, xParamKey2, countryCodeForParam);

    // Assert
    assertNotNull(response);
    assertEquals("Mocked response body", response.getBody());

    // Verify interactions
    verify(ctomOAuthTokenGenerator, times(1)).getAccessToken(mockParamData);
    verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(String.class));
}

@Test
public void testGetCTOMEndPointURL() throws Exception {
    // Arrange
    String xParamkey1 = "testKey1";
    String xParamkey2 = "testKey2";
    String countryCode = "US";
    String idParam = "id123";

    String[] data = {"", "roleIdValue", "secretIdValue", "", "", "oAuthUrlValue", "serviceUrlValue"};

    Param param = new Param(idParam);
    param.setCountryCode(countryCode);

    Param results = mock(Param.class);
    when(results.getData()).thenReturn(data);
    when(paramRepository.getParam(any(Param.class))).thenReturn(results);

    // Access private method using reflection
    Method method = IVRCtomResponseEntityService.class.getDeclaredMethod("getCTOMEndPointURL", String.class, String.class, String.class, String.class);
    method.setAccessible(true);

    // Act
    Map<String, String> resultMap = (Map<String, String>) method.invoke(instance, xParamkey1, xParamkey2, countryCode, idParam);

    // Assert
    assertEquals("roleIdValue", resultMap.get("roleld"));
    assertEquals("secretIdValue", resultMap.get("secretId"));
    assertEquals("oAuthUrlValue", resultMap.get("oAuth_url"));
    assertEquals("serviceUrlValue", resultMap.get("service_url"));
}
