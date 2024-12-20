@RunWith(MockitoJUnitRunner.class)
public class CallActivityControllerTest {

    @Mock
    private ParamRepository paramRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CallActivityController controller;

    @Test
    public void testUpdatePreferredLanguage_ExceptionHandling() throws Exception {
        // Mock input
        String countryCode = "MY";
        String custId = "01460923085100";
        String userId = "1200085";

        // Mock Param response
        Param mockParam = new Param("IVR01");
        mockParam.setCountryCode(countryCode);
        mockParam.setData(new String[] {null, null, null, null, null, null, 
            "https://rdc-gbl-cems-test-cops-ivr-api.int-cpbb.ocp.dev.net/v1/cems/ivr/pref-lang", "?relId="});
        when(paramRepository.getParam(any(Param.class))).thenReturn(mockParam);

        // Mock API exception
        when(restTemplate.exchange(any(URI.class), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(Map.class)))
                .thenThrow(new RuntimeException("Mocked exception"));

        // Call the method
        String result = controller.updatePreferredLanguage(countryCode, custId, userId);

        // Verify that exception is handled gracefully
        assertEquals("Error occured in invoking API", result);

        // Verify method interactions
        verify(paramRepository, times(1)).getParam(any(Param.class));
        verify(restTemplate, times(1))
                .exchange(any(URI.class), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(Map.class));
    }
}
