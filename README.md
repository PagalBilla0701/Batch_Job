     @Test
public void testGetResponseEntityForSR_Success() throws Exception {
    // Arrange
    Param mockParam = Mockito.mock(Param.class);
    Mockito.when(mockParam.getData()).thenReturn(new String[]{"key1", "key2", "key3", "key4", "key5", "key6", "http://mock-service-url.com"});

    Mockito.when(paramRepository.getParam(Mockito.any(Param.class))).thenReturn(mockParam);

    ResponseEntity<SRComplaintResponseBody> mockResponseEntity =
        new ResponseEntity<>(new SRComplaintResponseBody(), HttpStatus.OK);

    Mockito.when(restTemplate.postForEntity(
        Mockito.eq("http://mock-service-url.com"),
        Mockito.any(HttpEntity.class),
        Mockito.eq(SRComplaintResponseBody.class)
    )).thenReturn(mockResponseEntity);

    // Act
    ResponseEntity<SRComplaintResponseBody> response = ivrService.getReponseEntityforSR(
        srRequestBodyMap, idParam, xParamKey1, xParamKey2, countryCodeForParam
    );

    // Assert
    assertNotNull("Response should not be null", response);
    assertEquals("Status code should be 200 OK", HttpStatus.OK, response.getStatusCode());
    assertNotNull("Response body should not be null", response.getBody());
}
