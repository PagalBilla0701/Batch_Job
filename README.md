@RunWith(MockitoJUnitRunner.class)
public class S2SOpportunityServiceImplTest {

    @InjectMocks
    private S2SOpportunityServiceImpl service;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ParamRepository paramRepository;

    @Mock
    private CemsUtil cemsUtil;

    @Mock
    private List<HttpMessageConverter<?>> customMessageConverters;

    @Mock
    private OpportunityPitchEntityDataRepository optyPitchRepo;

    @Mock
    private Logger logger;

    @Test
    public void testManageLeadDataSuccess() throws Exception {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("headerData", "testHeader");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(Collections.singletonMap("key", "value"), HttpStatus.OK);

        Mockito.when(restTemplate.exchange(
                Mockito.any(URI.class),
                Mockito.eq(HttpMethod.POST),
                Mockito.any(HttpEntity.class),
                Mockito.eq(Map.class)
        )).thenReturn(responseEntity);

        // Act
        Map<String, Object> result = service.manageLeadData(request, 1);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals("value", result.get("key"));
    }

    @Test(expected = Sales2ServiceRuntimeException.class)
    public void testManageLeadDataFailure() throws Exception {
        // Arrange
        Map<String, Object> request = new HashMap<>();
        request.put("headerData", "testHeader");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(Collections.emptyMap(), HttpStatus.BAD_REQUEST);

        Mockito.when(restTemplate.exchange(
                Mockito.any(URI.class),
                Mockito.eq(HttpMethod.POST),
                Mockito.any(HttpEntity.class),
                Mockito.eq(Map.class)
        )).thenReturn(responseEntity);

        // Act
        service.manageLeadData(request, 1);
    }

    @Test
    public void testGetSales2ServiceUrl() {
        // Arrange
        Param param = new Param("L9993");
        param.setData(Collections.singletonList("http://mock-url"));

        Mockito.when(paramRepository.getParam(Mockito.any(Param.class))).thenReturn(param);

        // Act
        String url = service.getSales2ServiceUrl(0);

        // Assert
        Assert.assertEquals("http://mock-url", url);
    }

    @Test
    public void testGetLMSPageSize() {
        // Arrange
        Param param = new Param("P0052");
        param.setData(Arrays.asList("10", "20", "30"));

        Mockito.when(paramRepository.getParam(Mockito.any(Param.class))).thenReturn(param);

        // Act
        Integer pageSize = service.getLMSPageSize(1);

        // Assert
        Assert.assertEquals((Integer) 20, pageSize);
    }

    @Test
    public void testGetUserChannel() {
        // Arrange
        Param param = new Param("P9992");
        param.setData(Arrays.asList("S2SChannel", "NS2SChannel"));

        Mockito.when(paramRepository.getParam(Mockito.any(Param.class))).thenReturn(param);

        // Act
        String channel = service.getUserChannel("S2S");

        // Assert
        Assert.assertEquals("S2SChannel", channel);
    }

    @Test
    public void testGetHeaderString() throws IOException {
        // Arrange
        Map<String, String> header = Collections.singletonMap("key", "value");

        // Act
        String headerString = S2SOpportunityServiceImpl.getHeaderString(header);

        // Assert
        Assert.assertEquals("{\"key\":\"value\"}", headerString);
    }

    @Test
    public void testStrSubstitutor() throws Exception {
        // Arrange
        String url = "http://mock-url?param1={value1}";
        Map<String, Object> inputMap = Collections.singletonMap("value1", "test");

        // Act
        String result = service.strSubstitutor(url, inputMap);

        // Assert
        Assert.assertEquals("http://mock-url?param1=test", result);
    }
}
