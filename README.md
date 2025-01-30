@Test
public void testManageLeadData_SuccessfulResponse() throws Exception {
    // Arrange
    Map<String, Object> request = new HashMap<>();
    request.put("headerData", "testHeader");

    int index = 12;
    String serviceUrl = "http://example.com/service";

    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put("key", "value");

    // Mock the response entity
    ResponseEntity<Map> responseEntity = mock(ResponseEntity.class);
    when(responseEntity.getStatusCodeValue()).thenReturn(200);
    when(responseEntity.getBody()).thenReturn(responseMap);

    // Mock RestTemplate behavior
    when(restTemplate.exchange(
            any(URI.class),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(Map.class))
    ).thenReturn(responseEntity);

    // Act
    Map<String, Object> response = service.manageLeadData(request, index);

    // Assert
    assertNotNull(response);
    assertEquals("value", response.get("key"));
}
