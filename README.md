@RunWith(MockitoJUnitRunner.class)
public class YourClassTest {

    @InjectMocks
    private YourClass yourClass; // Replace with your class name containing the method

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ParamRepository paramRepository;

    @Mock
    private Logger logger; // If using a logger in your class

    @Test
    public void testUpdatePreferredLanguage() throws Exception {
        // Arrange
        String countryCode = "US";
        String custId = "12345";
        String userId = "user123";

        String idParam = "IVR01";
        Param param = new Param(idParam);
        param.setCountryCode(countryCode);

        Param results = new Param();
        results.setData(new String[]{"", "", "", "", "", "", "http://test-api.com", "/delete"});
        Mockito.when(paramRepository.getParam(Mockito.any())).thenReturn(results);

        String expectedUrl = "http://test-api.com/delete";
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", "SUCCESS");

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(responseMap, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                Mockito.any(URI.class),
                Mockito.eq(HttpMethod.DELETE),
                Mockito.any(HttpEntity.class),
                Mockito.eq(Map.class))
        ).thenReturn(responseEntity);

        // Act
        String result = yourClass.updatePreferredLanguage(countryCode, custId, userId);

        // Assert
        Assert.assertEquals("SUCCESS", result);

        // Verify interactions
        Mockito.verify(paramRepository, Mockito.times(1)).getParam(Mockito.any());
        Mockito.verify(restTemplate, Mockito.times(1)).exchange(
                Mockito.any(URI.class),
                Mockito.eq(HttpMethod.DELETE),
                Mockito.any(HttpEntity.class),
                Mockito.eq(Map.class)
        );
    }
}
