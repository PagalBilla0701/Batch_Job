@Test
public void testGetLMSEndPointeUrl() throws Exception {
    // Mock dependencies
    ParamRepository paramRepository = mock(ParamRepository.class);
    YourClass yourClass = new YourClass();

    // Inject the mock into your class (use reflection if required)
    java.lang.reflect.Field field = YourClass.class.getDeclaredField("paramRepository");
    field.setAccessible(true);
    field.set(yourClass, paramRepository);

    // Mocking the behavior of ParamRepository
    Param mockParam = new Param("URL17");
    mockParam.setKeys(new String[]{"paramKey"}); // Ensure this doesn't cause issues

    Param mockResult = mock(Param.class);
    when(mockResult.getData()).thenReturn(new String[]{
        "GET", "http://example.com", "/path", "/to", "/resource", null, null, null, null, null, null
    });
    when(paramRepository.getParam(any(Param.class))).thenReturn(mockResult);

    // Use reflection to access the private method
    Method method = YourClass.class.getDeclaredMethod("getLMSEndPointeUrl", String.class);
    method.setAccessible(true);

    // Invoke the private method
    @SuppressWarnings("unchecked")
    Map<String, String> result = (Map<String, String>) method.invoke(yourClass, "paramKey");

    // Assertions
    assertNotNull(result);
    assertEquals("GET", result.get("httpMethod"));
    assertEquals("http://example.com/path/to/resource", result.get("serviceUrl"));
}
