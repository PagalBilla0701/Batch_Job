@Test
public void testStrSubstitutor_Success() throws Exception {
    // Given a URL with placeholders and an input map with corresponding values
    String actualUrl = "http://example.com?param1=$(key1)&param2=$(key2)";
    Map<String, Object> inputMap = new HashMap<>();
    inputMap.put("key1", "value1");
    inputMap.put("key2", "value2");

    // When the strSubstitutor method is called
    String result = ivrCtomResponseEntityService.strSubstitutor(actualUrl, inputMap);

    // Then the placeholders should be replaced with actual values from inputMap
    assertEquals("http://example.com?param1=value1&param2=value2", result);
}

@Test
public void testStrSubstitutor_MissingKey() throws Exception {
    // Given a URL with placeholders and an input map missing one key
    String actualUrl = "http://example.com?param1=$(key1)&param2=$(key3)";
    Map<String, Object> inputMap = new HashMap<>();
    inputMap.put("key1", "value1");

    // When the strSubstitutor method is called
    String result = ivrCtomResponseEntityService.strSubstitutor(actualUrl, inputMap);

    // Then the existing key should be replaced, and missing key should remain empty
    assertEquals("http://example.com?param1=value1&param2=", result); // key3 is missing, so it should be blank
}
