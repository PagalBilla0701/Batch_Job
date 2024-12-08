@RunWith(MockitoJUnitRunner.class)
public class IVRServiceRequestDetailsTest {

    @InjectMocks
    private IVRServiceRequestDetails ivrServiceRequestDetails;

    @Mock
    private IVRSRResponseEntityService ivrSRResponseEntityService;

    private Map<String, Object> reqParamMap;

    @Before
    public void setUp() {
        reqParamMap = new HashMap<>();
        reqParamMap.put("customerID", "0150000350F");
        reqParamMap.put("countryCode", "IN");
    }

    @Test
    public void testInvoke_success() {
        // Mock response
        SRComplaintResponseBody mockResponseBody = new SRComplaintResponseBody();
        Pageable mockPageable = new Pageable();
        mockPageable.setTotalElements("5");
        mockResponseBody.setPageable(mockPageable);

        ResponseEntity<SRComplaintResponseBody> mockResponseEntity =
                new ResponseEntity<>(mockResponseBody, HttpStatus.OK);

        when(ivrSRResponseEntityService.getReponseEntityforSR(
                eq("ServiceRequest"),
                eq("summary"),
                eq("IN"),
                any(),
                eq("URL12")))
            .thenReturn(mockResponseEntity);

        // Execute
        IVRSRHoldingWrapper result = (IVRSRHoldingWrapper) ivrServiceRequestDetails.invoke(reqParamMap);

        // Verify
        assertNotNull(result);
        assertEquals(5, result.getOpenSRCount());
        assertEquals("IN", result.getCountryCode());

        verify(ivrSRResponseEntityService, times(1))
            .getReponseEntityforSR(eq("ServiceRequest"), eq("summary"), eq("IN"), any(), eq("URL12"));
    }

    @Test
    public void testInvoke_noResponse() {
        when(ivrSRResponseEntityService.getReponseEntityforSR(
                eq("ServiceRequest"),
                eq("summary"),
                eq("IN"),
                any(),
                eq("URL12")))
            .thenReturn(null);

        // Execute
        IVRSRHoldingWrapper result = (IVRSRHoldingWrapper) ivrServiceRequestDetails.invoke(reqParamMap);

        // Verify
        assertNotNull(result);
        assertEquals(0, result.getOpenSRCount());
        assertEquals("IN", result.getCountryCode());

        verify(ivrSRResponseEntityService, times(1))
            .getReponseEntityforSR(eq("ServiceRequest"), eq("summary"), eq("IN"), any(), eq("URL12"));
    }

    @Test
    public void testInvoke_exceptionHandling() {
        when(ivrSRResponseEntityService.getReponseEntityforSR(
                eq("ServiceRequest"),
                eq("summary"),
                eq("IN"),
                any(),
                eq("URL12")))
            .thenThrow(new RuntimeException("Test Exception"));

        // Execute
        IVRSRHoldingWrapper result = (IVRSRHoldingWrapper) ivrServiceRequestDetails.invoke(reqParamMap);

        // Verify
        assertNotNull(result);
        assertEquals(0, result.getOpenSRCount());
        assertEquals("IN", result.getCountryCode());

        verify(ivrSRResponseEntityService, times(1))
            .getReponseEntityforSR(eq("ServiceRequest"), eq("summary"), eq("IN"), any(), eq("URL12"));
    }
}
