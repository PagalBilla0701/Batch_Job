@Test
public void testButtonClickInfo_Success() throws Exception {
    // Mock input parameters
    String callRefNo = "SG20151125037392";
    String type = "ST";

    // Mock login bean
    LoginBean loginBean = new LoginBean();
    UserBean userBean = new UserBean();
    userBean.setCountryShortDesc("SG");
    loginBean.setUserBean(userBean);

    // Mock CallActivity object
    CallActivity mockCallActivity = new CallActivity();
    mockCallActivity.setCustId("custId123");
    mockCallActivity.setAccountNo("accountNo123");
    mockCallActivity.setRmn("rmn123");
    mockCallActivity.setOutboundCall("Y");
    mockCallActivity.setCustomerIdentified(true);

    // Mock dependencies
    when(callActivityAction.getCallActivityByRefNo("20151125037392", "SG")).thenReturn(mockCallActivity);

    Map<String, Object> expectedFormFields = new HashMap<>();
    expectedFormFields.put("RelationshipNo", "custId123");
    expectedFormFields.put("AccountNo", "accountNo123");
    expectedFormFields.put("callRefNo", callRefNo);
    expectedFormFields.put("type", type);
    expectedFormFields.put("MobileNo", "rmn123");
    expectedFormFields.put("identified", "true");

    when(callActivityService.makeResponseWrapper(expectedFormFields, true)).thenReturn(expectedFormFields);

    // Call the method under test
    ModelMap model = new ModelMap();
    Object response = controller.buttonClickInfo(loginBean, type, callRefNo, model);

    // Verify results
    assertNotNull(response);
    assertTrue(response instanceof Map);
    @SuppressWarnings("unchecked")
    Map<String, Object> responseMap = (Map<String, Object>) response;
    assertEquals("custId123", responseMap.get("RelationshipNo"));
    assertEquals("accountNo123", responseMap.get("AccountNo"));
    assertEquals(callRefNo, responseMap.get("callRefNo"));
    assertEquals(type, responseMap.get("type"));
    assertEquals("rmn123", responseMap.get("MobileNo"));
    assertEquals("true", responseMap.get("identified"));

    // Verify mock interactions
    verify(callActivityAction).getCallActivityByRefNo("20151125037392", "SG");
    verify(callActivityService).makeResponseWrapper(expectedFormFields, true);
}
