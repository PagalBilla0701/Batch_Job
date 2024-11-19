@Test
public void testGetCTOMEndPointURL() throws Exception {
    String xParamKey1 = "testKey1";
    String xParamKey2 = "testKey2";
    String countryCode = "US";
    String idParam = "id123";

    String[] data = {"", "roleldValue", "secretIdValue", "", "", "oAuthUrlValue", "serviceUrlValue"};
    
    // Mock dependencies
    Param results = mock(Param.class);
    when(results.getData()).thenReturn(data);
    when(paramRepository.getParam(any(Param.class))).thenReturn(results);

    // Access private method using reflection
    Method method = IVRCtomResponseEntityService.class.getDeclaredMethod("getCTOMEndPointURL", String.class, String.class, String.class, String.class);
    method.setAccessible(true);

    // Act
    Map<String, String> resultMap = (Map<String, String>) method.invoke(instance, xParamKey1, xParamKey2, countryCode, idParam);

    // Assert
    assertEquals("roleldValue", resultMap.get("roleld"));
    assertEquals("secretIdValue", resultMap.get("secretId"));
    assertEquals("oAuthUrlValue", resultMap.get("oAuth_url"));
    assertEquals("serviceUrlValue", resultMap.get("service_url"));
}
