@Test
public void testGetResponseEntityForCTOM() throws Exception {
    // Arrange
    Map<String, String> mockParamData = new HashMap<>();
    mockParamData.put("service_url", "http://example.com/api");

    IVRCtomResponseEntityService instance = new IVRCtomResponseEntityService();

    Method method = IVRCtomResponseEntityService.class.getDeclaredMethod(
        "getCTOMEndPointURL", String.class, String.class, String.class, String.class);
    method.setAccessible(true);

    String xParamKey1 = "xParamKey1";
    String xParamKey2 = "xParamKey2";
    String countryCodeForParam = "countryCode";
    String idParam = "idParam";
    
    // Act - Invoke the private method
    Map<String, String> resultMap = (Map<String, String>) method.invoke(
        instance, xParamKey1, xParamKey2, countryCodeForParam, idParam);

    // Mock the behavior of getCTOMEndPointURL to return mock data
    when(resultMap).thenReturn(mockParamData);

    // Mock the behavior of ctomOAuthTokenGenerator to return a dummy access token
    CtomOAuthTokenGenerator ctomOAuthTokenGenerator = mock(CtomOAuthTokenGenerator.class);
    when(ctomOAuthTokenGenerator.getAccessToken(mockParamData)).thenReturn("dummyAccessToken");

    // Mock RestTemplate to simulate an HTTP POST request and return a successful response
    ResponseEntity<String> mockResponseEntity = mock(ResponseEntity.class);
    when(mockResponseEntity.getBody()).thenReturn("Mocked response body");

    // Mock RestTemplate behavior
    RestTemplate restTemplate = mock(RestTemplate.class);
    when(restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(mockResponseEntity);

    // Act
    String ctomRequestBody = "ctomRequestBody";
    IVRCtomResponseEntityService spyInstance = spy(instance);
    doReturn(restTemplate).when(spyInstance).getRestTemplate();

    ResponseEntity<String> response = spyInstance.getResponseEntityForCTOM(
        ctomRequestBody, idParam, xParamKey1, xParamKey2, countryCodeForParam);

    // Assert
    assertNotNull(resultMap);
    assertNotNull(response);
    assertEquals("Mocked response body", response.getBody());

    // Verify interactions
    verify(ctomOAuthTokenGenerator, times(1)).getAccessToken(mockParamData);
    verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(String.class));
}
