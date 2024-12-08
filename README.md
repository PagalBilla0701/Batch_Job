@RunWith(MockitoJUnitRunner.class)
public class IVRServiceRequestDetailsTest {

    @InjectMocks
    private IVRServiceRequestDetails ivrServiceRequestDetails;

    @Mock
    private IVRSRResponseEntityService ivrSRResponseEntityService;

    @Test
    public void testInvoke_SuccessfulResponse() {
        // Mock Response Entity
        SRComplaintResponseBody mockResponseBody = new SRComplaintResponseBody();
        SRComplaintResponseBody.Pageable pageable = new SRComplaintResponseBody.Pageable();
        pageable.setTotalElements("30");
        mockResponseBody.setPageable(pageable);

        ResponseEntity<SRComplaintResponseBody> responseEntity =
                new ResponseEntity<>(mockResponseBody, HttpStatus.OK);

        // Mock IVRSRResponseEntityService behavior
        when(ivrSRResponseEntityService.getReponseEntityforSR(
                eq("ServiceRequest"),
                eq("summary"),
                eq("US"),
                any(Map.class),
                eq("URL12")
        )).thenReturn(responseEntity);

        // Prepare input parameters
        Map<String, Object> reqParamMap = new HashMap<>();
        reqParamMap.put("customerID", "0150000350F");
        reqParamMap.put("countryCode", "US");

        // Call the method under test
        Object result = ivrServiceRequestDetails.invoke(reqParamMap);

        // Validate results
        assertNotNull(result);
        assertTrue(result instanceof IVRSRHoldingWrapper);

        IVRSRHoldingWrapper wrapper = (IVRSRHoldingWrapper) result;
        assertEquals("US", wrapper.getCountryCode());
        assertEquals(30, wrapper.getOpenSRCount());

        // Verify interactions with mocks
        verify(ivrSRResponseEntityService, times(1)).getReponseEntityforSR(
                eq("ServiceRequest"),
                eq("summary"),
                eq("US"),
                any(Map.class),
                eq("URL12")
        );
    }

    @Test
    public void testInvoke_NoResponse() {
        // Mock null Response Entity
        when(ivrSRResponseEntityService.getReponseEntityforSR(
                eq("ServiceRequest"),
                eq("summary"),
                eq("US"),
                any(Map.class),
                eq("URL12")
        )).thenReturn(null);

        // Prepare input parameters
        Map<String, Object> reqParamMap = new HashMap<>();
        reqParamMap.put("customerID", "0150000350F");
        reqParamMap.put("countryCode", "US");

        // Call the method under test
        Object result = ivrServiceRequestDetails.invoke(reqParamMap);

        // Validate results
        assertNotNull(result);
        assertTrue(result instanceof IVRSRHoldingWrapper);

        IVRSRHoldingWrapper wrapper = (IVRSRHoldingWrapper) result;
        assertEquals("US", wrapper.getCountryCode());
        assertEquals(0, wrapper.getOpenSRCount());

        // Verify interactions with mocks
        verify(ivrSRResponseEntityService, times(1)).getReponseEntityforSR(
                eq("ServiceRequest"),
                eq("summary"),
                eq("US"),
                any(Map.class),
                eq("URL12")
        );
    }

    @Test
    public void testInvoke_ExceptionThrown() {
        // Mock exception scenario
        when(ivrSRResponseEntityService.getReponseEntityforSR(
                eq("ServiceRequest"),
                eq("summary"),
                eq("US"),
                any(Map.class),
                eq("URL12")
        )).thenThrow(new RuntimeException("Service unavailable"));

        // Prepare input parameters
        Map<String, Object> reqParamMap = new HashMap<>();
        reqParamMap.put("customerID", "0150000350F");
        reqParamMap.put("countryCode", "US");

        // Call the method under test
        Object result = ivrServiceRequestDetails.invoke(reqParamMap);

        // Validate results
        assertNotNull(result);
        assertTrue(result instanceof IVRSRHoldingWrapper);

        IVRSRHoldingWrapper wrapper = (IVRSRHoldingWrapper) result;
        assertEquals("US", wrapper.getCountryCode());
        assertEquals(0, wrapper.getOpenSRCount());

        // Verify interactions with mocks
        verify(ivrSRResponseEntityService, times(1)).getReponseEntityforSR(
                eq("ServiceRequest"),
                eq("summary"),
                eq("US"),
                any(Map.class),
                eq("URL12")
        );
    }
}
