@Test(expected = Sales2ServiceRuntimeException.class)
public void testManageLeadData_InvalidResponse() throws Exception {
    // Arrange
    Map<String, Object> request = new HashMap<>();
    int index = 12;

    ResponseEntity<Map> responseEntity = mock(ResponseEntity.class);
    when(responseEntity.getStatusCodeValue()).thenReturn(500); // Simulate invalid status code

    when(restTemplate.exchange(
            any(URI.class),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(Map.class)))
        .thenReturn(responseEntity);

    // Act
    service.manageLeadData(request, index); // This should throw Sales2ServiceRuntimeException
}

@Test(expected = Sales2ServiceRuntimeException.class)
public void testManageLeadData_IOException() throws Exception {
    // Arrange
    Map<String, Object> request = new HashMap<>();
    int index = 12;

    // Simulate IOException in RestTemplate
    when(restTemplate.exchange(
            any(URI.class),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(Map.class)))
        .thenThrow(new IOException("Simulated IOException"));

    // Act
    service.manageLeadData(request, index); // This should throw Sales2ServiceRuntimeException
}

@Test(expected = Sales2ServiceRuntimeException.class)
public void testManageLeadData_GenericException() throws Exception {
    // Arrange
    Map<String, Object> request = new HashMap<>();
    int index = 12;

    // Simulate generic Exception in RestTemplate
    when(restTemplate.exchange(
            any(URI.class),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(Map.class)))
        .thenThrow(new Exception("Simulated Generic Exception"));

    // Act
    service.manageLeadData(request, index); // This should throw Sales2ServiceRuntimeException
}
