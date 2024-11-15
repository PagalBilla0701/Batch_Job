@Test
public void testButtonClickInfo_Success() throws Exception {
    // Arrange
    String callRefNo = "12345";
    String type = "type";
    LoginBean login = new LoginBean();
    ModelMap model = new ModelMap();

    // Mock the behavior of callActivityService and other dependencies as needed
    Map<String, Object> formFields = new HashMap<>();
    formFields.put("RelationshipNo", "custId123");
    formFields.put("AccountNo", "accountNo123");
    formFields.put("callRefNo", callRefNo);
    formFields.put("type", type);
    formFields.put("MobileNo", "rmn123");
    formFields.put("identified", "true");

    // Mocking the callActivityService to return a response wrapped with formFields and success status
    when(callActivityService.makeResponseWrapper(formFields, true)).thenReturn(formFields);

    // Act
    Object response = callActivityController.buttonClickInfo(login, type, callRefNo, model);

    // Assert
    assertNotNull(response);
    assertTrue(response instanceof Map);
    assertEquals("custId123", ((Map<?, ?>) response).get("RelationshipNo"));
    assertEquals("accountNo123", ((Map<?, ?>) response).get("AccountNo"));
    assertEquals("12345", ((Map<?, ?>) response).get("callRefNo"));
    assertEquals("type", ((Map<?, ?>) response).get("type"));
    assertEquals("rmn123", ((Map<?, ?>) response).get("MobileNo"));
    assertEquals("true", ((Map<?, ?>) response).get("identified"));
}
