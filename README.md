@Test
public void testInvoke_SuccessfulResponse() throws Exception {
    // Mock the response
    ResponseEntity<SRComplaintResponseBody> responseEntity = new ResponseEntity<>(mockResponseBody, HttpStatus.OK);
    
    // Mock the expected method call with the correct arguments
    when(ivrSRResponseEntityService.getReponseEntityforSR(
        argThat(map -> map.get("sr-status-enquiry") != null 
            && ((Map<String, Object>) map.get("sr-status-enquiry")).get("customer-id").equals("0150000350F")),
        eq("summary"),
        eq("ServiceRequest"),
        eq("US"),
        anyString()
    )).thenReturn(responseEntity);

    // Call the method under test
    Object result = ivrServiceRequestDetails.invoke(reqParamMap);

    // Verify the result
    assertNotNull(result);
    assertTrue(result instanceof IVRSRHoldingWrapper);

    IVRSRHoldingWrapper wrapper = (IVRSRHoldingWrapper) result;
    assertEquals("US", wrapper.getCountryCode());
    assertEquals(30, wrapper.getOpenSRCount());

    // Verify mock interactions
    verify(ivrSRResponseEntityService, times(1)).getReponseEntityforSR(
        anyMap(), eq("summary"), eq("ServiceRequest"), eq("US"), anyString()
    );
}
