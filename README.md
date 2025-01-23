public void testManageLeadData() throws Exception {
    RestTemplate restTemplate = mock(RestTemplate.class); // Mock the RestTemplate
    service = new S2SOpportunityServiceImpl(restTemplate);

    // Mocking
    Map<String, Object> request = new HashMap<>();
    request.put("headerData", "headerValue");

    String expectedUrl = "http://localhost/mock-api"; // Use a valid absolute URL for testing

    when(restTemplate.postForEntity(eq(expectedUrl), any(), eq(Map.class)))
        .thenReturn(ResponseEntity.ok(new HashMap<>())); // Mock response

    // Test
    Map<String, Object> result = service.manageLeadData(request, 1);

    // Assertions
    assertNotNull(result);
    verify(restTemplate, times(1)).postForEntity(eq(expectedUrl), any(), eq(Map.class));
}
