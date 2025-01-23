@Test
public void testLmsDataProcess() throws Exception {
    // Mock RestTemplate
    RestTemplate restTemplate = mock(RestTemplate.class);
    MyService service = new MyService(restTemplate);

    Map<String, Object> request = new HashMap<>();
    Map<String, Object> payload = new HashMap<>();

    // Mock response
    when(restTemplate.exchange(
            anyString(), // URI
            any(),       // HttpMethod
            any(),       // HttpEntity
            eq(Map.class) // Response type
    )).thenReturn(ResponseEntity.ok(new HashMap<>()));

    // Call the service method
    Map<String, Object> result = service.lmsDataProcess(request, payload, "key");

    // Assertions
    assertNotNull(result);
}
