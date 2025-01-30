@Test
public void testManageLeadData_SuccessfulResponse() throws Exception {
    // Arrange
    Map<String, Object> request = new HashMap<>();
    request.put("headerData", "testHeader");

    int index = 12;

    // Mock the service URL
    String serviceUrl = "http://example.com/service";
    when(service.getSales2ServiceUrl(index)).thenReturn(serviceUrl); // Mocking dynamic URL

    // Mock the response entity
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put("key", "value");

    ResponseEntity<Map> responseEntity = mock(ResponseEntity.class);
    when(responseEntity.getStatusCodeValue()).thenReturn(200);
    when(responseEntity.getBody()).thenReturn(responseMap);

    // Mock RestTemplate behavior
    when(restTemplate.exchange(
            eq(new URI(serviceUrl)), // Ensure URI is correctly mocked
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
