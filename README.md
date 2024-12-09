@Test
public void testGetResponseEntityForSR_Success() throws Exception {
    // Arrange
    Map<String, Object> srRequestBody = new HashMap<>();
    srRequestBody.put("countryCode", "MY");

    Map<String, String> paramData = new HashMap<>();
    paramData.put("service_url", "http://mock-service-url.com");

    Param mockParam = Mockito.mock(Param.class);
    Mockito.when(mockParam.getKeys()).thenReturn(new String[]{"xParamKey1", "xParamKey2"});

    SRComplaintResponseBody mockResponse = new SRComplaintResponseBody();
    ResponseEntity<SRComplaintResponseBody> mockResponseEntity = new ResponseEntity<>(mockResponse, HttpStatus.OK);

    Mockito.when(paramRepository.getParam(Mockito.any(Param.class))).thenReturn(mockParam);
    Mockito.when(restTemplate.postForEntity(
            Mockito.anyString(),
            Mockito.any(HttpEntity.class),
            Mockito.eq(SRComplaintResponseBody.class)
    )).thenReturn(mockResponseEntity);

    // Act
    ResponseEntity<SRComplaintResponseBody> response = ivrService.getReponseEntityforSR(
            srRequestBody,
            "idParam",
            "xParamKey1",
            "xParamKey2",
            "MY"
    );

    // Assert
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    Mockito.verify(paramRepository).getParam(Mockito.any(Param.class));
    Mockito.verify(restTemplate).postForEntity(
            Mockito.eq("http://mock-service-url.com"),
            Mockito.any(HttpEntity.class),
            Mockito.eq(SRComplaintResponseBody.class)
    );
}
