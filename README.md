@Test
public void testUpdateCallDispositionStatus_Success() throws Exception {
    // Mocking ParamRepository response
    Param param = new Param("IVR03");
    Mockito.when(paramRepository.getParam(Mockito.any(Param.class)))
            .thenReturn(new Param("IVR03", new String[]{null, null, null, null, null, null, "https://example.com/api", "/update"}));

    // Mocking CallActivity
    CallActivity call = new CallActivity();
    call.setUserId("testUser");
    call.setConnectionId("connection123");
    call.setCallPrimaryType("primaryType");
    call.setCallSecondaryType("secondaryType");
    call.setCallDriver("callDriver");

    // Mocking CloseableHttpResponse
    CloseableHttpResponse mockResponse = Mockito.mock(CloseableHttpResponse.class);
    StatusLine statusLine = Mockito.mock(StatusLine.class);
    Mockito.when(statusLine.getStatusCode()).thenReturn(200);
    Mockito.when(mockResponse.getStatusLine()).thenReturn(statusLine);

    // Mocking HttpClient
    CloseableHttpClient httpClientMock = Mockito.mock(CloseableHttpClient.class);
    Mockito.when(httpClientMock.execute(Mockito.any(HttpPatch.class))).thenReturn(mockResponse);

    // Replace the actual HttpClient with the mock
    callActivityService.setHttpClient(httpClientMock);

    // Execute the method
    callActivityService.updateCallDispositionStatus("US", call);

    // Verify interactions
    Mockito.verify(paramRepository, Mockito.times(1)).getParam(Mockito.any(Param.class));
    Mockito.verify(httpClientMock, Mockito.times(1)).execute(Mockito.any(HttpPatch.class));

    // Assert behavior
    Assert.assertTrue(true); // Placeholder for additional assertions if required
}
