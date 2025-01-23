@Test
public void testLogJson() throws JsonProcessingException {
    // Arrange
    Map<String, String> request = Map.of("key", "value");
    Map<String, String> payload = Map.of("key", "value");
    
    when(mapper.writeValueAsString(request)).thenReturn("{\"key\":\"value\"}");
    when(mapper.writeValueAsString(payload)).thenReturn("{\"key\":\"value\"}");
    
    // Act
    service.logJson(request, payload);
    
    // Assert
    verify(mapper, times(1)).writeValueAsString(request);
    verify(mapper, times(1)).writeValueAsString(payload);
}
