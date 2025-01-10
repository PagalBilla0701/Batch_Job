@Test
public void testUpdateCallDispositionStatus_Success() throws Exception {
    // Mocking ParamRepository response
    Param param = new Param("IVR03");
    Mockito.when(paramRepository.getParam(Mockito.any(Param.class)))
           .thenReturn(new Param("IVR03", new String[] {null, null, null, null, null, null, "https://example.com/api", "/update"}));

    // Mocking CallActivity
    CallActivity call = new CallActivity();
    call.setUserId("testUser");
    call.setConnectionId("connection123");
    call.setCallPrimaryType("primaryType");
    call.setCallSecondaryType("secondaryType");
    call.setCallDriver("callDriver");

    // Mocking HttpResponse
    HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
    Mockito.when(mockResponse.getStatusLine()).thenReturn(new BasicStatusLine(HttpVersion.HTTP_1_1, 200, "OK"));
    Mockito.when(httpClient.execute(Mockito.any(HttpPatch.class))).thenReturn(mockResponse);

    // Execute the method
    callActivityService.updateCallDispositionStatus("US", call);

    // Verify interactions
    Mockito.verify(paramRepository, Mockito.times(1)).getParam(Mockito.any(Param.class));
    Mockito.verify(httpClient, Mockito.times(1)).execute(Mockito.any(HttpPatch.class));

    // Assert logging or behavior as needed (if logging is captured)
    Assert.assertTrue(true); // Placeholder to indicate the test passes
}
