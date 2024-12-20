@RunWith(MockitoJUnitRunner.class)
public class CallActivityControllerTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ParamRepository paramRepository;

    @InjectMocks
    private CallActivityController controller;

    @Test
    public void testUpdatePreferredLanguage_Success() throws Exception {
        // Mock input
        String countryCode = "MY";
        String custId = "01460923085100";
        String userId = "1200085";

        // Mock Param response
        Param mockParam = new Param("IVR01");
        mockParam.setCountryCode(countryCode);
        mockParam.setData(new String[] {null, null, null, null, null, null, "http://example.com", "/path"});
        when(paramRepository.getParam(any(Param.class))).thenReturn(mockParam);

        // Mock API response
        Map<String, Object> mockResponseBody = new HashMap<>();
        mockResponseBody.put("status", "Success");
        ResponseEntity<Map> mockResponse = new ResponseEntity<>(mockResponseBody, HttpStatus.OK);

        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(mockResponse);

        // Call the method
        String result = controller.updatePreferredLanguage(countryCode, custId, userId);

        // Verify
        assertEquals("Success", result);
        verify(restTemplate, times(1))
                .exchange(any(URI.class), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(Map.class));
    }
}
